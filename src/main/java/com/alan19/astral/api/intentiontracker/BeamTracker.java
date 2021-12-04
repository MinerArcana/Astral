package com.alan19.astral.api.intentiontracker;

import com.alan19.astral.entity.projectile.IntentionBeam;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;

import java.util.Optional;
import java.util.UUID;

public class BeamTracker implements IBeamTracker {
    private UUID beamEntityID;

    @Override
    public Optional<IntentionBeam> getIntentionBeam(ServerLevel world) {
        return Optional.ofNullable(beamEntityID != null && world.getEntity(beamEntityID) instanceof IntentionBeam ? (IntentionBeam)world.getEntity(beamEntityID) : null);
    }

    @Override
    public void setIntentionBeam(IntentionBeam beam) {
        beamEntityID = beam.getUUID();
    }

    @Override
    public void clearIntentionBeam() {
        beamEntityID = null;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundNBT = new CompoundTag();
        if (beamEntityID != null){
            compoundNBT.putUUID("beamID", beamEntityID);
        }
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (nbt.contains("beamID")){
            beamEntityID = nbt.getUUID("beamID");
        }
    }
}
