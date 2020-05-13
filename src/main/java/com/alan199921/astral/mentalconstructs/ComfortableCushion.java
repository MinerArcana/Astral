package com.alan199921.astral.mentalconstructs;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public class ComfortableCushion extends MentalConstruct {
    private float saturationSnapshot;
    private float saturationCounter;

    public ComfortableCushion(PlayerEntity playerEntity) {
        saturationSnapshot = playerEntity.getFoodStats().getSaturationLevel();
        saturationCounter = 0;
    }

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
        CompoundNBT nbt = new CompoundNBT();
        nbt.putFloat("saturationSnapshot", saturationSnapshot);
        nbt.putFloat("saturationCounter", saturationCounter);
        nbt.putInt("level", getConstructLevel());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        saturationSnapshot = nbt.getFloat("saturationSnapshot");
        saturationCounter = nbt.getFloat("saturationCounter");
        setConstructLevel(nbt.getInt("level"));
    }
}
