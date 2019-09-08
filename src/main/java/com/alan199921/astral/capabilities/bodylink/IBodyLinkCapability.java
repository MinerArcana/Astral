package com.alan199921.astral.capabilities.bodylink;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;

public interface IBodyLinkCapability extends INBTSerializable {
    Entity getLinkedEntity(ServerWorld world);
    void setLinkedBodyID(Entity entity);
    void killEntity(ServerWorld world);
}
