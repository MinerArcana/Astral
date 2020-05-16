package com.alan19.astral.api.bodylink;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.UUID;

public interface IBodyLinkCapability extends INBTSerializable<CompoundNBT> {
    void setInfo(UUID playerID, BodyInfo bodyInfo, ServerWorld world);

    BodyInfo getInfo(UUID playerID);

    void updatePlayer(UUID playerID, ServerWorld world);

    void handleMergeWithBody(UUID playerID, ServerWorld world);
}
