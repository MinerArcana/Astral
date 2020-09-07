package com.alan19.astral.api.intentiontracker;

import com.alan19.astral.entity.projectile.IntentionBeam;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Optional;

public interface IBeamTracker extends INBTSerializable<CompoundNBT> {
    Optional<IntentionBeam> getIntentionBeam(ServerWorld world);
    void setIntentionBeam(IntentionBeam beam);
    void clearIntentionBeam();
}
