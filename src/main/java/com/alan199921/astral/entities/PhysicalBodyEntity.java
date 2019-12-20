package com.alan199921.astral.entities;

import com.alan199921.astral.events.TravelingHandlers;
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
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.stream.IntStream;

public class PhysicalBodyEntity extends LivingEntity {
    private static final DataParameter<Boolean> faceDown = EntityDataManager.createKey(PhysicalBodyEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<CompoundNBT> gameProfile = EntityDataManager.createKey(PhysicalBodyEntity.class, DataSerializers.COMPOUND_NBT);
    private static final DataParameter<Float> hungerLevel = EntityDataManager.createKey(PhysicalBodyEntity.class, DataSerializers.FLOAT);
    private final ItemStackHandler mainInventory = new ItemStackHandler(42);
    private final ItemStackHandler inventoryHands = new ItemStackHandler(2);
    private final ItemStackHandler inventoryArmor = new ItemStackHandler(4);

    protected PhysicalBodyEntity(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
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

        dataManager.set(gameProfile, compound.getCompound("gameProfile"));
        dataManager.set(faceDown, compound.getBoolean("facedown"));
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        compound.put("mainInventoryTag", mainInventory.serializeNBT());
        compound.put("armorInventoryTag", inventoryArmor.serializeNBT());
        compound.put("handsInventoryTag", inventoryHands.serializeNBT());

        compound.put("gameProfile", dataManager.get(gameProfile));
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
        } else if (slotIn.getSlotType() == EquipmentSlotType.Group.ARMOR) {
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
        dataManager.register(gameProfile, NBTUtil.writeGameProfile(new CompoundNBT(), new GameProfile(null, "test")));
        // Approximately 5% of the storage mobs will be facedown
        dataManager.register(faceDown, Math.random() < .05);
        dataManager.register(hungerLevel, 20F);
    }

    @Override
    public void tick() {
        if (!world.isRemote()) {
            ServerWorld serverWorld = (ServerWorld) world;
            serverWorld.forceChunk(this.chunkCoordX, this.chunkCoordZ, true);
            if (getGameProfile() != null && isAlive() && world.getPlayerByUuid(getGameProfile().getId()) != null) {
                TravelingHandlers.setPlayerMaxHealthTo(world.getPlayerByUuid(getGameProfile().getId()), getHealth());
            }
        }
        super.tick();
    }

    public boolean isFaceDown() {
        return dataManager.get(faceDown);
    }

    public GameProfile getGameProfile() {
        return NBTUtil.readGameProfile(dataManager.get(gameProfile));
    }

    public void setGameProfile(GameProfile playerProfile) {
        dataManager.set(gameProfile, NBTUtil.writeGameProfile(new CompoundNBT(), playerProfile));
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
        if (!world.isRemote()) {
            PlayerEntity playerEntity = world.getPlayerByUuid(getGameProfile().getId());
            if (!cause.getDamageType().equals("outOfWorld")) {
                playerEntity.onKillCommand();
                dropInventory();
            }
        }
    }

}
