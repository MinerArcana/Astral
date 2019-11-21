package com.alan199921.astral.entities;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.HandSide;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

public class PhysicalBodyEntity extends LivingEntity implements IItemHandler {
    private final NonNullList<ItemStack> mainInventory = NonNullList.withSize(6 * 7, ItemStack.EMPTY);
    private final NonNullList<ItemStack> inventoryHands = NonNullList.withSize(2, ItemStack.EMPTY);
    private final NonNullList<ItemStack> inventoryArmor = NonNullList.withSize(4, ItemStack.EMPTY);
    private static final DataParameter<Optional<UUID>> playerUUID = EntityDataManager.createKey(PhysicalBodyEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    private static final DataParameter<Boolean> faceDown = EntityDataManager.createKey(PhysicalBodyEntity.class, DataSerializers.BOOLEAN);
    private GameProfile gameProfile;

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
        for (int i = 0; i < mainInventoryTag.size(); i++){
            mainInventory.set(i, ItemStack.read(mainInventoryTag.getCompound(i)));
        }

        ListNBT armorInventoryTag = compound.getList("armorInventoryTag", 10);
        for (int i = 0; i < armorInventoryTag.size(); i++){
            inventoryArmor.set(i, ItemStack.read(armorInventoryTag.getCompound(i)));
        }

        ListNBT handsInventoryTag = compound.getList("handsInventoryTag", 10);
        for (int i = 0; i < handsInventoryTag.size(); i++){
            inventoryHands.set(i, ItemStack.read(handsInventoryTag.getCompound(i)));
        }

        dataManager.set(playerUUID, Optional.of(compound.getUniqueId("playerUUID")));
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        ListNBT mainInventoryTag = new ListNBT();
        for (int i = 0; i < mainInventory.size(); i++){
            CompoundNBT slotNBT = new CompoundNBT();
            mainInventory.get(i).write(slotNBT);
            mainInventoryTag.add(i, slotNBT);
        }
        compound.put("mainInventoryTag", mainInventoryTag);

        ListNBT armorInventoryTag = new ListNBT();
        for (int i = 0; i < inventoryArmor.size(); i++){
            CompoundNBT slotNBT = new CompoundNBT();
            inventoryArmor.get(i).write(slotNBT);
            armorInventoryTag.add(i, slotNBT);
        }
        compound.put("armorInventoryTag", armorInventoryTag);

        ListNBT handsInventoryTag = new ListNBT();
        for (int i = 0; i < inventoryHands.size(); i++){
            CompoundNBT slotNBT = new CompoundNBT();
            inventoryHands.get(i).write(slotNBT);
            handsInventoryTag.add(i, slotNBT);
        }
        compound.put("handsInventoryTag", handsInventoryTag);
        compound.putUniqueId("playerUUID", dataManager.get(playerUUID).get());

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
        dataManager.register(playerUUID, Optional.empty());
        // Approximately 5% of the storage mobs will be facedown
        dataManager.register(faceDown, Math.random() < .05);
    }

    public UUID getPlayerUUID() {
        return dataManager.get(playerUUID).get();
    }

    public void setPlayerUUID(UUID uuid) {
        dataManager.set(playerUUID, Optional.of(uuid));
    }

    @Override
    public void tick() {
        if (!world.isRemote() && isServerWorld()) {
            ServerWorld serverWorld = (ServerWorld) world;
            serverWorld.forceChunk(this.chunkCoordX, this.chunkCoordZ, false);
        }
        super.tick();
    }

    public boolean isFaceDown(){
        return dataManager.get(faceDown);
    }

    public GameProfile getGameProfile(){
        if (gameProfile == null){
            gameProfile = Minecraft.getInstance().getSessionService().fillProfileProperties(new GameProfile(getPlayerUUID(), null), false);
        }
        return gameProfile;
    }
}
