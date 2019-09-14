package com.alan199921.astral.capabilities.bodylink;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;

public interface IBodyLinkCapability extends INBTSerializable {
    Entity getLinkedEntity(ServerWorld world);
    void setLinkedBodyID(Entity entity);
    NonNullList<ItemStack> killEntity(ServerWorld world);
}
