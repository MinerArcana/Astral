package com.alan19.astral.entity.physicalbody;

import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.configs.AstralConfig;
import com.alan19.astral.effects.AstralEffects;
import com.alan19.astral.events.astraltravel.StartAndEndHandling;
import com.alan19.astral.serializing.AstralSerializers;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

public class PhysicalBodyEntity extends LivingEntity {
    private static final EntityDataAccessor<Optional<GameProfile>> gameProfile = SynchedEntityData.defineId(PhysicalBodyEntity.class, AstralSerializers.OPTIONAL_GAME_PROFILE);
    private static final EntityDataAccessor<Boolean> faceDown = SynchedEntityData.defineId(PhysicalBodyEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> hungerLevel = SynchedEntityData.defineId(PhysicalBodyEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<LazyOptional<ItemStackHandler>> armorInventory = SynchedEntityData.defineId(PhysicalBodyEntity.class, AstralSerializers.OPTIONAL_ITEMSTACK_HANDLER);
    private static final EntityDataAccessor<LazyOptional<ItemStackHandler>> handsInventory = SynchedEntityData.defineId(PhysicalBodyEntity.class, AstralSerializers.OPTIONAL_ITEMSTACK_HANDLER);

    public PhysicalBodyEntity(EntityType<? extends LivingEntity> type, Level world) {
        super(type, world);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return LivingEntity.createLivingAttributes();
    }

    private LazyOptional<ItemStackHandler> getArmor() {
        return entityData.get(armorInventory);
    }

    private LazyOptional<ItemStackHandler> getHands() {
        return entityData.get(handsInventory);
    }

    @Nonnull
    @Override
    public Iterable<ItemStack> getArmorSlots() {
        NonNullList<ItemStack> itemStackList = NonNullList.withSize(4, ItemStack.EMPTY);
        int bound = itemStackList.size();
        IntStream.range(0, bound).forEach(i -> itemStackList.set(i, getArmor().orElseGet(() -> new ItemStackHandler(4)).getStackInSlot(i)));
        return itemStackList;
    }

    @Nonnull
    @Override
    public ItemStack getItemBySlot(EquipmentSlot slotIn) {
        switch (slotIn.getType()) {
            case HAND:
                return getHands().map(itemStackHandler -> itemStackHandler.getStackInSlot(slotIn.getIndex())).orElse(ItemStack.EMPTY);
            case ARMOR:
                return getArmor().map(itemStackHandler -> itemStackHandler.getStackInSlot(slotIn.getIndex())).orElse(ItemStack.EMPTY);
            default:
                return ItemStack.EMPTY;
        }
    }

    @Override
    public void load(@Nonnull CompoundTag compound) {
        super.load(compound);

        entityData.set(gameProfile, !compound.getBoolean("gameProfileExists") ? Optional.empty() : Optional.ofNullable(NbtUtils.readGameProfile(compound.getCompound("gameProfile"))));
        entityData.set(faceDown, compound.getBoolean("facedown"));
        if (!level.isClientSide() && level instanceof ServerLevel && getGameProfile().isPresent()) {
            final UUID playerId = getGameProfile().get().getId();
            entityData.set(armorInventory, AstralAPI.getOverworldPsychicInventory((ServerLevel) level).lazyMap(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(playerId).getPhysicalArmor()));
            entityData.set(handsInventory, AstralAPI.getOverworldPsychicInventory((ServerLevel) level).lazyMap(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(playerId).getPhysicalHands()));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putBoolean("gameProfileExists", entityData.get(gameProfile).isPresent());
        if (getGameProfile().isPresent()) {
            compound.put("gameProfile", NbtUtils.writeGameProfile(new CompoundTag(), entityData.get(gameProfile).get()));
        }
        compound.putBoolean("faceDown", entityData.get(faceDown));
        super.addAdditionalSaveData(compound);
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        dropPhysicalInventory();
    }

    private void clearPhysicalInventory() {
        if (level instanceof ServerLevel && getGameProfile().isPresent()) {
            AstralAPI.getOverworldPsychicInventory((ServerLevel) level).map(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(getGameProfile().get().getId())).ifPresent(psychicInventoryInstance -> {
                for (int i = 0; i < psychicInventoryInstance.getPhysicalInventory().getSlots(); ++i) {
                    psychicInventoryInstance.getPhysicalInventory().setStackInSlot(i, ItemStack.EMPTY);
                }
                for (int i = 0; i < psychicInventoryInstance.getPhysicalArmor().getSlots(); ++i) {
                    psychicInventoryInstance.getPhysicalArmor().setStackInSlot(i, ItemStack.EMPTY);
                }
                for (int i = 0; i < psychicInventoryInstance.getPhysicalHands().getSlots(); ++i) {
                    psychicInventoryInstance.getPhysicalHands().setStackInSlot(i, ItemStack.EMPTY);
                }
            });
        }
    }


    private void dropPhysicalInventory() {
        if (level instanceof ServerLevel && getGameProfile().isPresent()) {
            AstralAPI.getOverworldPsychicInventory((ServerLevel) level).map(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(getGameProfile().get().getId())).ifPresent(psychicInventoryInstance -> {
                for (int i = 0; i < psychicInventoryInstance.getPhysicalInventory().getSlots(); i++) {
                    Block.popResource(level, blockPosition(), psychicInventoryInstance.getPhysicalInventory().getStackInSlot(i));
                    psychicInventoryInstance.getPhysicalInventory().setStackInSlot(i, ItemStack.EMPTY);
                }
                for (int i = 0; i < psychicInventoryInstance.getPhysicalArmor().getSlots(); i++) {
                    Block.popResource(level, blockPosition(), psychicInventoryInstance.getPhysicalArmor().getStackInSlot(i));
                    psychicInventoryInstance.getPhysicalArmor().setStackInSlot(i, ItemStack.EMPTY);
                }
                for (int i = 0; i < psychicInventoryInstance.getPhysicalHands().getSlots(); i++) {
                    Block.popResource(level, blockPosition(), psychicInventoryInstance.getPhysicalHands().getStackInSlot(i));
                    psychicInventoryInstance.getPhysicalHands().setStackInSlot(i, ItemStack.EMPTY);
                }
            });
        }
    }

    @Override
    public void setItemSlot(EquipmentSlot slotIn, @Nonnull ItemStack stack) {
        if (slotIn.getType() == EquipmentSlot.Type.HAND) {
            getHands().orElseGet(() -> new ItemStackHandler(2)).setStackInSlot(slotIn.getIndex(), stack);
        }
        else if (slotIn.getType() == EquipmentSlot.Type.ARMOR) {
            getArmor().orElseGet(() -> new ItemStackHandler(4)).setStackInSlot(slotIn.getIndex(), stack);
        }
    }

    @Nonnull
    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    public void setName(String name) {
        this.setCustomName(new TextComponent(name + "'s Body"));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(gameProfile, Optional.empty());
        // Approximately 5% of the storage mobs will be facedown
        entityData.define(faceDown, Math.random() < .05);
        entityData.define(hungerLevel, 20);
        entityData.define(armorInventory, LazyOptional.empty());
        entityData.define(handsInventory, LazyOptional.empty());
    }

    /**
     * Updates the player's max health and alive status every second. If player is not found, delete the body
     */
    @Override
    public void tick() {
        if (level instanceof ServerLevel) {
            ServerLevel serverWorld = (ServerLevel) level;
            serverWorld.setChunkForced(this.xChunk, this.zChunk, true);
            if (level.getGameTime() % AstralConfig.getTravelingSettings().syncInterval.get() == 0 && isAlive()) {
                setBodyLinkInfo(serverWorld);
                AstralAPI.getBodyTracker(serverWorld).ifPresent(tracker -> tracker.setBodyNBT(getUUID(), serializeNBT(), serverWorld));
            }
        }
        super.tick();
    }

    @Override
    public CompoundTag serializeNBT() {
        final CompoundTag compoundNBT = super.serializeNBT();
        compoundNBT.putInt("Hunger", entityData.get(hungerLevel));
        compoundNBT.putString("Dimension", getCommandSenderWorld().dimension().location().toString());
        return compoundNBT;
    }

    /**
     * Remove Astral Travel from target if body takes drowning damage. If player is in creative mode, nullify the damage.
     *
     * @param damageSrc    The damage source
     * @param damageAmount The damage amount
     */
    @Override
    protected void actuallyHurt(@Nonnull DamageSource damageSrc, float damageAmount) {
        if (getGameProfile().map(GameProfile::getId).map(uuid -> getLinkedPlayer()).map(Player::isCreative).orElse(false) && !damageSrc.isBypassInvul()) {
            return;
        }
        super.actuallyHurt(damageSrc, damageAmount);
        if (damageSrc.msgId.equals("drown") && level instanceof ServerLevel){
            getGameProfile().ifPresent(gp -> {
                final Entity entity = ((ServerLevel) level).getEntity(gp.getId());
                if (entity instanceof LivingEntity){
                    final LivingEntity livingEntity = (LivingEntity) entity;
                    livingEntity.removeEffectNoUpdate(AstralEffects.ASTRAL_TRAVEL.get());
                    StartAndEndHandling.astralTravelEnd(livingEntity);
                }
            });
        }
    }

    public void setBodyLinkInfo(ServerLevel serverWorld) {
        if (getGameProfile().isPresent()) {
            AstralAPI.getBodyTracker(serverWorld).ifPresent(cap -> cap.setBodyNBT(getGameProfile().get().getId(), serializeNBT(), serverWorld));
        }
    }

    public boolean isFaceDown() {
        return entityData.get(faceDown);
    }

    public Optional<GameProfile> getGameProfile() {
        // TODO Refactor to allow ID to be fetched without game profile
        return entityData.get(gameProfile);
    }

    public void setGameProfile(GameProfile playerProfile) {
        entityData.set(gameProfile, Optional.of(playerProfile));
        if (level instanceof ServerLevel && getGameProfile().isPresent()) {
            final UUID playerId = playerProfile.getId();
            Player playerEntity = level.getPlayerByUUID(playerId);
            final ServerLevel serverWorld = (ServerLevel) this.level;
            if (playerEntity != null) {
                AstralAPI.getBodyTracker(serverWorld).ifPresent(cap -> cap.setBodyNBT(getGameProfile().get().getId(), serializeNBT(), serverWorld));
            }
            entityData.set(armorInventory, AstralAPI.getOverworldPsychicInventory(serverWorld).lazyMap(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(playerId).getPhysicalArmor()));
            entityData.set(handsInventory, AstralAPI.getOverworldPsychicInventory(serverWorld).lazyMap(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(playerId).getPhysicalHands()));
        }
    }

    public void setHungerLevel(int hunger) {
        entityData.set(hungerLevel, hunger);
    }

    /**
     * Kill player if the cause of death is not falling out of the world
     *
     * @param cause The cause of death
     */
    @Override
    public void die(@Nonnull DamageSource cause) {
        if (level instanceof ServerLevel && getGameProfile().isPresent()) {
            Player playerEntity = getLinkedPlayer();
            //If body is killed, drop inventory and kill the player
            if (!cause.getMsgId().equals("outOfWorld") && playerEntity != null && getGameProfile().isPresent()) {
                super.die(cause);
                setBodyLinkInfo((ServerLevel) level);
            }
            else if (cause.getMsgId().equals("despawn")) {
                super.die(cause);
            }
            //If body despawns because Astral Travel ends, clear the inventory so nothing gets dropped while inventory gets transferred to player
            else {
                clearPhysicalInventory();
                entityData.set(armorInventory, LazyOptional.of(() -> new ItemStackHandler(4)));
                entityData.set(handsInventory, LazyOptional.of(() -> new ItemStackHandler(2)));
                super.die(cause);
            }
        }
    }

    @Nullable
    private ServerPlayer getLinkedPlayer() {
        if (getCommandSenderWorld() instanceof ServerLevel && getCommandSenderWorld().getServer() != null && getGameProfile().isPresent()) {
            return getCommandSenderWorld().getServer().getPlayerList().getPlayer(getGameProfile().get().getId());
        }
        else {
            return null;
        }
    }
}
