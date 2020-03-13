package com.alan199921.astral.api.bodylink;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;

import java.util.Objects;
import java.util.UUID;

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
    public int getDimensionID() {
        return dimensionID;
    }

    @Override
    public void setDimensionID(int dimensionID) {
        this.dimensionID = dimensionID;
    }
}
