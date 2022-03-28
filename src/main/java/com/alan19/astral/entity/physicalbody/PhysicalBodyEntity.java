package com.alan19.astral.entity.physicalbody;

import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.configs.AstralConfig;
import com.alan19.astral.effects.AstralEffects;
import com.alan19.astral.events.astraltravel.StartAndEndHandling;
import com.alan19.astral.serializing.AstralSerializers;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.HandSide;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

public class PhysicalBodyEntity extends LivingEntity {
    private static final DataParameter<Optional<GameProfile>> gameProfile = EntityDataManager.defineId(PhysicalBodyEntity.class, AstralSerializers.OPTIONAL_GAME_PROFILE);
    private static final DataParameter<Boolean> faceDown = EntityDataManager.defineId(PhysicalBodyEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> hungerLevel = EntityDataManager.defineId(PhysicalBodyEntity.class, DataSerializers.INT);
    private static final DataParameter<LazyOptional<ItemStackHandler>> armorInventory = EntityDataManager.defineId(PhysicalBodyEntity.class, AstralSerializers.OPTIONAL_ITEMSTACK_HANDLER);
    private static final DataParameter<LazyOptional<ItemStackHandler>> handsInventory = EntityDataManager.defineId(PhysicalBodyEntity.class, AstralSerializers.OPTIONAL_ITEMSTACK_HANDLER);

    public PhysicalBodyEntity(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
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
    public ItemStack getItemBySlot(EquipmentSlotType slotIn) {
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
    public void load(@Nonnull CompoundNBT compound) {
        super.load(compound);

        entityData.set(gameProfile, !compound.getBoolean("gameProfileExists") ? Optional.empty() : Optional.ofNullable(NBTUtil.readGameProfile(compound.getCompound("gameProfile"))));
        entityData.set(faceDown, compound.getBoolean("facedown"));
        if (!level.isClientSide() && level instanceof ServerWorld && getGameProfile().isPresent()) {
            final UUID playerId = getGameProfile().get().getId();
            entityData.set(armorInventory, AstralAPI.getOverworldPsychicInventory((ServerWorld) level).lazyMap(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(playerId).getPhysicalArmor()));
            entityData.set(handsInventory, AstralAPI.getOverworldPsychicInventory((ServerWorld) level).lazyMap(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(playerId).getPhysicalHands()));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        compound.putBoolean("gameProfileExists", entityData.get(gameProfile).isPresent());
        if (getGameProfile().isPresent()) {
            compound.put("gameProfile", NBTUtil.writeGameProfile(new CompoundNBT(), entityData.get(gameProfile).get()));
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
        if (level instanceof ServerWorld && getGameProfile().isPresent()) {
            AstralAPI.getOverworldPsychicInventory((ServerWorld) level).map(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(getGameProfile().get().getId())).ifPresent(psychicInventoryInstance -> {
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
        if (level instanceof ServerWorld && getGameProfile().isPresent()) {
            AstralAPI.getOverworldPsychicInventory((ServerWorld) level).map(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(getGameProfile().get().getId())).ifPresent(psychicInventoryInstance -> {
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
    public void setItemSlot(EquipmentSlotType slotIn, @Nonnull ItemStack stack) {
        if (slotIn.getType() == EquipmentSlotType.Group.HAND) {
            getHands().orElseGet(() -> new ItemStackHandler(2)).setStackInSlot(slotIn.getIndex(), stack);
        }
        else if (slotIn.getType() == EquipmentSlotType.Group.ARMOR) {
            getArmor().orElseGet(() -> new ItemStackHandler(4)).setStackInSlot(slotIn.getIndex(), stack);
        }
    }

    @Nonnull
    @Override
    public HandSide getMainArm() {
        return HandSide.RIGHT;
    }

    public void setName(String name) {
        this.setCustomName(new StringTextComponent(name + "'s Body"));
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
        if (level instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) level;
            serverWorld.setChunkForced(this.xChunk, this.zChunk, true);
            if (level.getGameTime() % AstralConfig.getTravelingSettings().syncInterval.get() == 0 && isAlive()) {
                setBodyLinkInfo(serverWorld);
                AstralAPI.getBodyTracker(serverWorld).ifPresent(tracker -> tracker.setBodyNBT(getUUID(), serializeNBT(), serverWorld));
            }
        }
        super.tick();
    }

    @Override
    public CompoundNBT serializeNBT() {
        final CompoundNBT compoundNBT = super.serializeNBT();
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
        if (getGameProfile().map(GameProfile::getId).map(uuid -> getLinkedPlayer()).map(PlayerEntity::isCreative).orElse(false) && !damageSrc.isBypassInvul()) {
            return;
        }
        super.actuallyHurt(damageSrc, damageAmount);
        if (damageSrc.msgId.equals("drown") && level instanceof ServerWorld) {
            getGameProfile().ifPresent(gp -> {
                final Entity entity = ((ServerWorld) level).getEntity(gp.getId());
                if (entity instanceof LivingEntity) {
                    final LivingEntity livingEntity = (LivingEntity) entity;
                    livingEntity.removeEffectNoUpdate(AstralEffects.ASTRAL_TRAVEL.get());
                    StartAndEndHandling.astralTravelEnd(livingEntity);
                }
            });
        }
    }

    public void setBodyLinkInfo(ServerWorld serverWorld) {
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
        if (level instanceof ServerWorld && getGameProfile().isPresent()) {
            final UUID playerId = playerProfile.getId();
            PlayerEntity playerEntity = level.getPlayerByUUID(playerId);
            final ServerWorld serverWorld = (ServerWorld) this.level;
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
        if (level instanceof ServerWorld && getGameProfile().isPresent()) {
            PlayerEntity playerEntity = getLinkedPlayer();
            //If body is killed, drop inventory and kill the player
            if (!cause.getMsgId().equals("outOfWorld") && playerEntity != null && getGameProfile().isPresent()) {
                super.die(cause);
                setBodyLinkInfo((ServerWorld) level);
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
    private ServerPlayerEntity getLinkedPlayer() {
        if (getCommandSenderWorld() instanceof ServerWorld && getCommandSenderWorld().getServer() != null && getGameProfile().isPresent()) {
            return getCommandSenderWorld().getServer().getPlayerList().getPlayer(getGameProfile().get().getId());
        }
        else {
            return null;
        }
    }
}
