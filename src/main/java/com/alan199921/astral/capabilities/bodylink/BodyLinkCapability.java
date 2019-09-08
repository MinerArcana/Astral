package com.alan199921.astral.capabilities.bodylink;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.DamageSource;
import net.minecraft.world.server.ServerWorld;

import java.util.Objects;
import java.util.UUID;

public class BodyLinkCapability implements IBodyLinkCapability {
    private UUID linkedBodyID = UUID.randomUUID();

    @Override
    public INBT serializeNBT() {
        return NBTUtil.writeUniqueId(linkedBodyID);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        linkedBodyID = NBTUtil.readUniqueId((CompoundNBT) nbt);
    }

    @Override
    public Entity getLinkedEntity(ServerWorld world) {
        return world.getEntityByUuid(linkedBodyID);
    }

    @Override
    public void setLinkedBodyID(Entity entity) {
        linkedBodyID = entity.getUniqueID();
    }

    @Override
    public void killEntity(ServerWorld world){
        try {
            Objects.requireNonNull(world.getEntityByUuid(linkedBodyID)).attackEntityFrom(DamageSource.OUT_OF_WORLD, 1000);
        }
        catch (NullPointerException e){
            System.out.println("Entity is already dead!");
        }
    }
}
