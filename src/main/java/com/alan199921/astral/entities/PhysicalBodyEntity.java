package com.alan199921.astral.entities;

import com.alan199921.astral.events.TravellingHandlers;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
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
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class PhysicalBodyEntity extends LivingEntity implements IItemHandler {
    private final NonNullList<ItemStack> mainInventory = NonNullList.withSize(6 * 7, ItemStack.EMPTY);
    private final NonNullList<ItemStack> inventoryHands = NonNullList.withSize(2, ItemStack.EMPTY);
    private final NonNullList<ItemStack> inventoryArmor = NonNullList.withSize(4, ItemStack.EMPTY);
    private static final DataParameter<Boolean> faceDown = EntityDataManager.createKey(PhysicalBodyEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<CompoundNBT> gameProfile = EntityDataManager.createKey(PhysicalBodyEntity.class, DataSerializers.COMPOUND_NBT);

    protected PhysicalBodyEntity(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return inventoryArmor;
    }

    public NonNullList<ItemStack> getInventoryHands() {
        return inventoryHands;
    }

    @Override
    public ItemStack getItemStackFromSlot(EquipmentSlotType slotIn) {
        switch (slotIn.getSlotType()) {
            case HAND:
                return this.inventoryHands.get(slotIn.getIndex());
            case ARMOR:
                return this.inventoryArmor.get(slotIn.getIndex());
            default:
                return ItemStack.EMPTY;
        }
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        ListNBT mainInventoryTag = compound.getList("mainInventoryTag", 10);
        for (int i = 0; i < mainInventoryTag.size(); i++) {
            mainInventory.set(i, ItemStack.read(mainInventoryTag.getCompound(i)));
        }

        ListNBT armorInventoryTag = compound.getList("armorInventoryTag", 10);
        for (int i = 0; i < armorInventoryTag.size(); i++) {
            inventoryArmor.set(i, ItemStack.read(armorInventoryTag.getCompound(i)));
        }

        ListNBT handsInventoryTag = compound.getList("handsInventoryTag", 10);
        for (int i = 0; i < handsInventoryTag.size(); i++) {
            inventoryHands.set(i, ItemStack.read(handsInventoryTag.getCompound(i)));
        }

        dataManager.set(gameProfile, compound.getCompound("gameProfile"));
        dataManager.set(faceDown, compound.getBoolean("facedown"));
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        ListNBT mainInventoryTag = new ListNBT();
        for (int i = 0; i < mainInventory.size(); i++) {
            CompoundNBT slotNBT = new CompoundNBT();
            mainInventory.get(i).write(slotNBT);
            mainInventoryTag.add(i, slotNBT);
        }
        compound.put("mainInventoryTag", mainInventoryTag);

        ListNBT armorInventoryTag = new ListNBT();
        for (int i = 0; i < inventoryArmor.size(); i++) {
            CompoundNBT slotNBT = new CompoundNBT();
            inventoryArmor.get(i).write(slotNBT);
            armorInventoryTag.add(i, slotNBT);
        }
        compound.put("armorInventoryTag", armorInventoryTag);

        ListNBT handsInventoryTag = new ListNBT();
        for (int i = 0; i < inventoryHands.size(); i++) {
            CompoundNBT slotNBT = new CompoundNBT();
            inventoryHands.get(i).write(slotNBT);
            handsInventoryTag.add(i, slotNBT);
        }
        compound.put("handsInventoryTag", handsInventoryTag);
        compound.put("gameProfile", dataManager.get(gameProfile));
        compound.putBoolean("faceDown", dataManager.get(faceDown));

        super.writeAdditional(compound);
    }

    @Override
    protected void dropInventory() {
        super.dropInventory();
    }

    @Override
    public void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack) {
        if (slotIn.getSlotType() == EquipmentSlotType.Group.HAND) {
            this.inventoryHands.set(slotIn.getIndex(), stack);
        } else if (slotIn.getSlotType() == EquipmentSlotType.Group.ARMOR) {
            this.inventoryArmor.set(slotIn.getIndex(), stack);
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
    public int getSlots() {
        return mainInventory.size();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return mainInventory.get(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return mainInventory.set(slot, stack);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return mainInventory.remove(slot).split(amount);
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return true;
    }

    public NonNullList<ItemStack> getInventory() {
        return mainInventory;
    }

    @Override
    protected void registerData() {
        super.registerData();
        dataManager.register(gameProfile, NBTUtil.writeGameProfile(new CompoundNBT(), new GameProfile(null, "test")));
        // Approximately 5% of the storage mobs will be facedown
        dataManager.register(faceDown, Math.random() < .05);
    }

    @Override
    public void tick() {
        if (!world.isRemote() && isServerWorld()) {
            ServerWorld serverWorld = (ServerWorld) world;
            serverWorld.forceChunk(this.chunkCoordX, this.chunkCoordZ, false);
            if (getGameProfile() != null && isAlive() && world.getPlayerByUuid(getGameProfile().getId()) != null) {
                TravellingHandlers.setPlayerMaxHealthTo(world.getPlayerByUuid(getGameProfile().getId()), getHealth());
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

    /**
     * Kill player if the cause of death is not falling out of the world
     *
     * @param cause
     */
    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        if (!world.isRemote()) {
            PlayerEntity playerEntity = world.getPlayerByUuid(getGameProfile().getId());
            if (!cause.getDamageType().equals("outOfWorld")) {
                playerEntity.onKillCommand();
            }
        }
    }

}
