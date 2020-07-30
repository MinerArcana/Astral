package com.alan19.astral.entity.physicalbody;

import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.api.bodylink.BodyInfo;
import com.alan19.astral.api.bodylink.IBodyLink;
import com.alan19.astral.configs.AstralConfig;
import com.alan19.astral.effects.AstralEffects;
import com.alan19.astral.entity.AstralEntities;
import com.alan19.astral.events.astraltravel.StartAndEndHandling;
import com.alan19.astral.events.astraltravel.TravelEffects;
import com.alan19.astral.serializing.AstralSerializers;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
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
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

public class PhysicalBodyEntity extends LivingEntity {
    private static final DataParameter<Optional<GameProfile>> gameProfile = EntityDataManager.createKey(PhysicalBodyEntity.class, AstralSerializers.OPTIONAL_GAME_PROFILE);
    private static final DataParameter<Boolean> faceDown = EntityDataManager.createKey(PhysicalBodyEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Float> hungerLevel = EntityDataManager.createKey(PhysicalBodyEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<LazyOptional<ItemStackHandler>> armorInventory = EntityDataManager.createKey(PhysicalBodyEntity.class, AstralSerializers.OPTIONAL_ITEMSTACK_HANDLER);
    private static final DataParameter<LazyOptional<ItemStackHandler>> handsInventory = EntityDataManager.createKey(PhysicalBodyEntity.class, AstralSerializers.OPTIONAL_ITEMSTACK_HANDLER);

    public PhysicalBodyEntity(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    private LazyOptional<ItemStackHandler> getArmor() {
        return dataManager.get(armorInventory);
    }

    private LazyOptional<ItemStackHandler> getHands() {
        return dataManager.get(handsInventory);
    }

    @Nonnull
    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        NonNullList<ItemStack> itemStackList = NonNullList.withSize(4, ItemStack.EMPTY);
        int bound = itemStackList.size();
        IntStream.range(0, bound).forEach(i -> itemStackList.set(i, getArmor().orElseGet(() -> new ItemStackHandler(4)).getStackInSlot(i)));
        return itemStackList;
    }

    @Nonnull
    @Override
    public ItemStack getItemStackFromSlot(EquipmentSlotType slotIn) {
        switch (slotIn.getSlotType()) {
            case HAND:
                return getHands().map(itemStackHandler -> itemStackHandler.getStackInSlot(slotIn.getIndex())).orElseGet(() -> ItemStack.EMPTY);
            case ARMOR:
                return getArmor().map(itemStackHandler -> itemStackHandler.getStackInSlot(slotIn.getIndex())).orElseGet(() -> ItemStack.EMPTY);
            default:
                return ItemStack.EMPTY;
        }
    }

    @Override
    public void read(@Nonnull CompoundNBT compound) {
        super.read(compound);

        dataManager.set(gameProfile, !compound.getBoolean("gameProfileExists") ? Optional.empty() : Optional.ofNullable(NBTUtil.readGameProfile(compound.getCompound("gameProfile"))));
        dataManager.set(faceDown, compound.getBoolean("facedown"));
        if (!world.isRemote() && world instanceof ServerWorld && getGameProfile().isPresent()) {
            final UUID playerId = getGameProfile().get().getId();
            dataManager.set(armorInventory, AstralAPI.getOverworldPsychicInventory((ServerWorld) world).map(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(playerId).getPhysicalArmor()));
            dataManager.set(handsInventory, AstralAPI.getOverworldPsychicInventory((ServerWorld) world).map(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(playerId).getPhysicalHands()));
        }
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        compound.putBoolean("gameProfileExists", dataManager.get(gameProfile).isPresent());
        if (getGameProfile().isPresent()) {
            compound.put("gameProfile", NBTUtil.writeGameProfile(new CompoundNBT(), dataManager.get(gameProfile).get()));
        }
        compound.putBoolean("faceDown", dataManager.get(faceDown));
        super.writeAdditional(compound);
    }

    @Override
    protected void dropInventory() {
        super.dropInventory();
        dropPhysicalInventory();
    }

    private void clearPhysicalInventory() {
        if (world instanceof ServerWorld && getGameProfile().isPresent()) {
            AstralAPI.getOverworldPsychicInventory((ServerWorld) world).map(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(getGameProfile().get().getId())).ifPresent(psychicInventoryInstance -> {
                for (int i = 0; i < psychicInventoryInstance.getPhysicalInventory().getSlots(); i++) {
                    psychicInventoryInstance.getPhysicalInventory().setStackInSlot(i, ItemStack.EMPTY);
                }
                for (int i = 0; i < psychicInventoryInstance.getPhysicalArmor().getSlots(); i++) {
                    psychicInventoryInstance.getPhysicalArmor().setStackInSlot(i, ItemStack.EMPTY);
                }
                for (int i = 0; i < psychicInventoryInstance.getPhysicalHands().getSlots(); i++) {
                    psychicInventoryInstance.getPhysicalHands().setStackInSlot(i, ItemStack.EMPTY);
                }
            });
        }
    }


    private void dropPhysicalInventory() {
        if (world instanceof ServerWorld && getGameProfile().isPresent()) {
            AstralAPI.getOverworldPsychicInventory((ServerWorld) world).map(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(getGameProfile().get().getId())).ifPresent(psychicInventoryInstance -> {
                for (int i = 0; i < psychicInventoryInstance.getPhysicalInventory().getSlots(); i++) {
                    Block.spawnAsEntity(world, getPosition(), psychicInventoryInstance.getPhysicalInventory().getStackInSlot(i));
                    psychicInventoryInstance.getPhysicalInventory().setStackInSlot(i, ItemStack.EMPTY);
                }
                for (int i = 0; i < psychicInventoryInstance.getPhysicalArmor().getSlots(); i++) {
                    Block.spawnAsEntity(world, getPosition(), psychicInventoryInstance.getPhysicalArmor().getStackInSlot(i));
                    psychicInventoryInstance.getPhysicalArmor().setStackInSlot(i, ItemStack.EMPTY);
                }
                for (int i = 0; i < psychicInventoryInstance.getPhysicalHands().getSlots(); i++) {
                    Block.spawnAsEntity(world, getPosition(), psychicInventoryInstance.getPhysicalHands().getStackInSlot(i));
                    psychicInventoryInstance.getPhysicalHands().setStackInSlot(i, ItemStack.EMPTY);
                }
            });
        }
    }

    @Override
    public void setItemStackToSlot(EquipmentSlotType slotIn, @Nonnull ItemStack stack) {
        if (slotIn.getSlotType() == EquipmentSlotType.Group.HAND) {
            getHands().orElseGet(() -> new ItemStackHandler(2)).setStackInSlot(slotIn.getIndex(), stack);
        }
        else if (slotIn.getSlotType() == EquipmentSlotType.Group.ARMOR) {
            getArmor().orElseGet(() -> new ItemStackHandler(4)).setStackInSlot(slotIn.getIndex(), stack);
        }
    }

    @Nonnull
    @Override
    public HandSide getPrimaryHand() {
        return HandSide.RIGHT;
    }

    public void setName(String name) {
        this.setCustomName(new StringTextComponent(name + "'s Body"));
    }

    @Override
    protected void registerData() {
        super.registerData();
        dataManager.register(gameProfile, Optional.empty());
        // Approximately 5% of the storage mobs will be facedown
        dataManager.register(faceDown, Math.random() < .05);
        dataManager.register(hungerLevel, 20F);
        dataManager.register(armorInventory, LazyOptional.empty());
        dataManager.register(handsInventory, LazyOptional.empty());
    }

    /**
     * Updates the player's max health and alive status every second. If player is not found, delete the body
     */
    @Override
    public void tick() {
        if (world instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) world;
            serverWorld.forceChunk(this.chunkCoordX, this.chunkCoordZ, true);
            if (!getGameProfile().map(GameProfile::getId).map(serverWorld::getPlayerByUuid).isPresent()){
                this.attackEntityFrom(new DamageSource("despawn"), Float.MAX_VALUE);
                AstralAPI.getBodyTracker(serverWorld).ifPresent(tracker -> tracker.getBodyTrackerMap().put(getUniqueID(), serializeNBT()));
            }
            if (world.getGameTime() % AstralConfig.getTravelingSettings().getSyncInterval() == 0 && isAlive()) {
                setBodyLinkInfo(serverWorld);
                AstralAPI.getBodyTracker(serverWorld).ifPresent(tracker -> tracker.getBodyTrackerMap().put(getUniqueID(), serializeNBT()));
            }
        }
        super.tick();
    }

    /**
     * Remove Astral Travel from target if body takes drowning damage
     * @param damageSrc The damage source
     * @param damageAmount The damage amount
     */
    @Override
    protected void damageEntity(@Nonnull DamageSource damageSrc, float damageAmount) {
        super.damageEntity(damageSrc, damageAmount);
        if (damageSrc.damageType.equals("drown") && world instanceof ServerWorld){
            getGameProfile().ifPresent(gp -> {
                final Entity entity = ((ServerWorld) world).getEntityByUuid(gp.getId());
                if (entity instanceof LivingEntity){
                    final LivingEntity livingEntity = (LivingEntity) entity;
                    livingEntity.removeActivePotionEffect(AstralEffects.ASTRAL_TRAVEL.get());
                    StartAndEndHandling.astralTravelEnd(livingEntity);
                }
            });
        }
    }

    public void setBodyLinkInfo(ServerWorld serverWorld) {
        if (getGameProfile().isPresent()) {
            final PlayerEntity player = serverWorld.getPlayerByUuid(getGameProfile().get().getId());
            if (player != null){
                player.getCapability(AstralAPI.bodyLinkCapability).ifPresent(bodyLink -> syncPlayerInformation(serverWorld, (ServerPlayerEntity) player, bodyLink));
            }
        }
    }

    private void syncPlayerInformation(ServerWorld serverWorld, ServerPlayerEntity player, IBodyLink bodyLink) {
        bodyLink.setBodyInfo(new BodyInfo(getHealth(), getPosition(), isAlive(), dimension, getUniqueID()));
        bodyLink.updatePlayer(player, serverWorld);
    }

    public boolean isFaceDown() {
        return dataManager.get(faceDown);
    }

    public Optional<GameProfile> getGameProfile() {
        return dataManager.get(gameProfile);
    }

    public void setGameProfile(GameProfile playerProfile) {
        dataManager.set(gameProfile, Optional.of(playerProfile));
        if (world instanceof ServerWorld && getGameProfile().isPresent()) {
            final UUID playerId = playerProfile.getId();
            PlayerEntity playerEntity = world.getPlayerByUuid(playerId);
            if (playerEntity != null) {
                playerEntity.getCapability(AstralAPI.bodyLinkCapability).ifPresent(bodyLink -> bodyLink.setBodyInfo(new BodyInfo(getHealth(), getPosition(), isAlive(), dimension, getUniqueID())));
            }
            dataManager.set(armorInventory, AstralAPI.getOverworldPsychicInventory((ServerWorld) world).map(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(playerId).getPhysicalArmor()));
            dataManager.set(handsInventory, AstralAPI.getOverworldPsychicInventory((ServerWorld) world).map(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(playerId).getPhysicalHands()));
        }
    }

    public float getHungerLevel() {
        return dataManager.get(hungerLevel);
    }

    public void setHungerLevel(float hunger) {
        dataManager.set(hungerLevel, hunger);
    }

    /**
     * Kill player if the cause of death is not falling out of the world
     *
     * @param cause The cause of death
     */
    @Override
    public void onDeath(@Nonnull DamageSource cause) {
        if (!world.isRemote() && getGameProfile().isPresent()) {
            PlayerEntity playerEntity = world.getPlayerByUuid(getGameProfile().get().getId());
            //If body is killed, drop inventory and kill the player
            if (!cause.getDamageType().equals("outOfWorld") && playerEntity != null && getGameProfile().isPresent()) {
                super.onDeath(cause);
                setBodyLinkInfo((ServerWorld) world);
            }
            else if (cause.getDamageType().equals("despawn")){
                super.onDeath(cause);
            }
            //If body despawns because Astral Travel ends, clear the inventory so nothing gets dropped while inventory gets transferred to player
            else {
                clearPhysicalInventory();
                dataManager.set(armorInventory, LazyOptional.of(() -> new ItemStackHandler(4)));
                dataManager.set(handsInventory, LazyOptional.of(() -> new ItemStackHandler(2)));
                super.onDeath(cause);
            }
        }
    }
}
