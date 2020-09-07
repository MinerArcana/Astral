package com.alan19.astral.api.intentiontracker;

import com.alan19.astral.entity.projectile.IntentionBeam;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;

import java.util.Optional;
import java.util.UUID;

public class BeamTracker implements IBeamTracker {
    private UUID beamEntityID;

    @Override
    public Optional<IntentionBeam> getIntentionBeam(ServerWorld world) {
        return Optional.ofNullable(beamEntityID != null && world.getEntityByUuid(beamEntityID) instanceof IntentionBeam ? (IntentionBeam)world.getEntityByUuid(beamEntityID) : null);
    }

    @Override
    public void setIntentionBeam(IntentionBeam beam) {
        beamEntityID = beam.getUniqueID();
    }

    @Override
    public void clearIntentionBeam() {
        beamEntityID = null;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compoundNBT = new CompoundNBT();
        if (beamEntityID != null){
            compoundNBT.putUniqueId("beamID", beamEntityID);
        }
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (nbt.contains("beamID")){
            beamEntityID = nbt.getUniqueId("beamID");
        }
    }
}
