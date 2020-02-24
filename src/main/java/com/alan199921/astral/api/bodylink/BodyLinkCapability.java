package com.alan199921.astral.api.bodylink;

import com.alan199921.astral.entities.PhysicalBodyEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Objects;
import java.util.UUID;
import java.util.stream.IntStream;

public class BodyLinkCapability implements IBodyLinkCapability {
    private UUID linkedBodyID = UUID.randomUUID();
    private int dimensionID;

    @Override
    public INBT serializeNBT() {
        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT.putInt("bodyDimension", dimensionID);
        compoundNBT.putUniqueId("uniqueID", linkedBodyID);
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CompoundNBT compoundNBT = (CompoundNBT) nbt;
        linkedBodyID = compoundNBT.getUniqueId("uniqueID");
        dimensionID = compoundNBT.getInt("bodyDimension");
    }

    @Override
    public Entity getLinkedEntity(ServerWorld world) {
        ServerWorld physicalBodyDimension = world.getServer().getWorld(Objects.requireNonNull(DimensionType.getById(dimensionID)));
        return physicalBodyDimension.getEntityByUuid(linkedBodyID);
    }

    @Override
    public void setLinkedBodyID(Entity entity) {
        linkedBodyID = entity.getUniqueID();
    }


    @Override
    public NonNullList<ItemStack> killEntity(ServerWorld world) {
        try {
            PhysicalBodyEntity physicalBodyEntity = (PhysicalBodyEntity) world.getServer().getWorld(Objects.requireNonNull(DimensionType.getById(dimensionID))).getEntityByUuid(linkedBodyID);
            physicalBodyEntity.onKillCommand();
            physicalBodyEntity.attackEntityFrom(DamageSource.OUT_OF_WORLD, 1000);
            ItemStackHandler handler = physicalBodyEntity.getMainInventory();
            NonNullList<ItemStack> itemStacks = NonNullList.withSize(handler.getSlots(), ItemStack.EMPTY);
            IntStream.range(0, handler.getSlots()).forEach(i -> itemStacks.set(i, handler.getStackInSlot(i)));
            return itemStacks;
        } catch (NullPointerException e) {
            System.out.println("Entity is already dead!");
        }
        return NonNullList.withSize(6 * 7, ItemStack.EMPTY);
    }

    @Override
    public int getDimensionID() {
        return dimensionID;
    }

    @Override
    public void setDimensionID(int dimensionID) {
        this.dimensionID = dimensionID;
    }
}