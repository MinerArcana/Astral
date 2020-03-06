package com.alan199921.astral.entities;

import com.alan199921.astral.api.AstralAPI;
import com.alan199921.astral.events.TravelingHandlers;
import com.alan199921.astral.serializing.AstralSerializers;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
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
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

public class PhysicalBodyEntity extends LivingEntity {
    private static final DataParameter<Optional<GameProfile>> gameProfile = EntityDataManager.createKey(PhysicalBodyEntity.class, AstralSerializers.OPTIONAL_GAME_PROFILE);
    private static final DataParameter<Boolean> faceDown = EntityDataManager.createKey(PhysicalBodyEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Float> hungerLevel = EntityDataManager.createKey(PhysicalBodyEntity.class, DataSerializers.FLOAT);

    private final ItemStackHandler mainInventory = new ItemStackHandler(42);
    private final ItemStackHandler inventoryHands = new ItemStackHandler(2);
    private final ItemStackHandler inventoryArmor = new ItemStackHandler(4);

    private LazyOptional<IItemHandler> inventory;
    private LazyOptional<IItemHandler> armorInventory;
    private LazyOptional<IItemHandler> handsInventory;


    protected PhysicalBodyEntity(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    private void handleInvalid() {
        if (inventory.isPresent()) {
            inventory.invalidate();
        }
    }


    public ItemStackHandler getMainInventory() {
        return mainInventory;
    }

    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        NonNullList<ItemStack> itemStackList = NonNullList.withSize(inventoryArmor.getSlots(), ItemStack.EMPTY);
        IntStream.range(0, inventoryArmor.getSlots()).forEach(i -> itemStackList.set(i, inventoryArmor.getStackInSlot(i)));
        return itemStackList;
    }

    @Override
    public ItemStack getItemStackFromSlot(EquipmentSlotType slotIn) {
        switch (slotIn.getSlotType()) {
            case HAND:
                return this.inventoryHands.getStackInSlot(slotIn.getIndex());
            case ARMOR:
                return this.inventoryArmor.getStackInSlot(slotIn.getIndex());
            default:
                return ItemStack.EMPTY;
        }
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        mainInventory.deserializeNBT(compound.getCompound("mainInventoryTag"));
        inventoryArmor.deserializeNBT(compound.getCompound("armorInventoryTag"));
        inventoryHands.deserializeNBT(compound.getCompound("handsInventoryTag"));

        dataManager.set(gameProfile, !compound.getBoolean("gameProfileExists") ? Optional.empty() : Optional.of(NBTUtil.readGameProfile(compound.getCompound("gameProfile"))));
        dataManager.set(faceDown, compound.getBoolean("facedown"));
        if (!world.isRemote() && world instanceof ServerWorld && getGameProfile().isPresent()) {
            final UUID playerId = getGameProfile().get().getId();
            inventory = AstralAPI.getOverworldPsychicInventory((ServerWorld) world).map(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(playerId).getPhysicalInventory());
            armorInventory = AstralAPI.getOverworldPsychicInventory((ServerWorld) world).map(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(playerId).getPhysicalArmor());
            handsInventory = AstralAPI.getOverworldPsychicInventory((ServerWorld) world).map(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(playerId).getPhysicalHands());
        }
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        compound.put("mainInventoryTag", mainInventory.serializeNBT());
        compound.put("armorInventoryTag", inventoryArmor.serializeNBT());
        compound.put("handsInventoryTag", inventoryHands.serializeNBT());

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
        IntStream.range(0, mainInventory.getSlots()).forEach(i -> Block.spawnAsEntity(world, getPosition(), mainInventory.getStackInSlot(i)));
        getArmorInventoryList().forEach(item -> Block.spawnAsEntity(world, getPosition(), item));
    }

    @Override
    public void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack) {
        if (slotIn.getSlotType() == EquipmentSlotType.Group.HAND) {
            this.inventoryHands.setStackInSlot(slotIn.getIndex(), stack);
        }
        else if (slotIn.getSlotType() == EquipmentSlotType.Group.ARMOR) {
            this.inventoryArmor.setStackInSlot(slotIn.getIndex(), stack);
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
    }

    @Override
    public void tick() {
        if (!world.isRemote() && world instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) world;
            serverWorld.forceChunk(this.chunkCoordX, this.chunkCoordZ, true);
            if (getGameProfile().isPresent() && isAlive() && world.getPlayerByUuid(getGameProfile().get().getId()) != null) {
                TravelingHandlers.setPlayerMaxHealthTo(world.getPlayerByUuid(getGameProfile().get().getId()), getHealth());
            }
        }
        super.tick();
    }

    public boolean isFaceDown() {
        return dataManager.get(faceDown);
    }

    public Optional<GameProfile> getGameProfile() {
        return dataManager.get(gameProfile);
    }

    public void setGameProfile(GameProfile playerProfile) {
        dataManager.set(gameProfile, Optional.of(playerProfile));
        if (!world.isRemote() && world instanceof ServerWorld && getGameProfile().isPresent()) {
            final UUID playerId = getGameProfile().get().getId();
            inventory = AstralAPI.getOverworldPsychicInventory((ServerWorld) world).map(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(playerId).getPhysicalInventory());
            armorInventory = AstralAPI.getOverworldPsychicInventory((ServerWorld) world).map(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(playerId).getPhysicalArmor());
            handsInventory = AstralAPI.getOverworldPsychicInventory((ServerWorld) world).map(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(playerId).getPhysicalHands());
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
        super.onDeath(cause);
        if (!world.isRemote() && getGameProfile().isPresent()) {
            PlayerEntity playerEntity = world.getPlayerByUuid(getGameProfile().get().getId());
            if (!cause.getDamageType().equals("outOfWorld") && playerEntity != null) {
                playerEntity.onKillCommand();
                dropInventory();
            }
        }
    }
}
