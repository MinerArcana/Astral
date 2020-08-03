package com.alan19.astral.api.bodylink;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Optional;

public interface IBodyLink extends INBTSerializable<CompoundNBT> {
    void setBodyInfo(BodyInfo bodyInfo);
    Optional<BodyInfo> getBodyInfo();
    void mergeBodies(PlayerEntity playerEntity, ServerWorld world);
    void updatePlayer(ServerPlayerEntity playerEntity, ServerWorld world);
}
