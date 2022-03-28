package com.alan19.astral.api.intentiontracker;

import com.alan19.astral.entity.projectile.IntentionBeam;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Optional;

public interface IBeamTracker extends INBTSerializable<CompoundTag> {
    Optional<IntentionBeam> getIntentionBeam(ServerLevel world);

    void setIntentionBeam(IntentionBeam beam);

    void clearIntentionBeam();
}
