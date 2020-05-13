package com.alan199921.astral.mentalconstructs;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public class Garden extends MentalConstruct {
    private float saturationSnapshot;
    private float saturationCounter;

    @Override
    void performEffect(PlayerEntity player) {
        final float newSaturation = player.getFoodStats().getSaturationLevel();
        saturationCounter += (Math.max(0, saturationSnapshot - newSaturation));
        if (saturationCounter / (1 + getConstructLevel() * .1) >= 1) {
            player.getFoodStats().addStats(1, 0);
        }
        saturationSnapshot = newSaturation;

    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.putFloat("saturationSnapshot", saturationSnapshot);
        nbt.putFloat("saturationCounter", saturationCounter);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        saturationSnapshot = nbt.getFloat("saturationSnapshot");
        saturationCounter = nbt.getFloat("saturationCounter");
    }
}
