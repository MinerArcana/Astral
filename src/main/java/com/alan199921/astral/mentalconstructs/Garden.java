package com.alan199921.astral.mentalconstructs;

import com.alan199921.astral.Astral;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Astral.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Garden extends MentalConstruct {
    private float saturationSnapshot = 0;
    private float saturationCounter = 0;

    @Override
    public void performEffect(PlayerEntity player, int level) {
        if (level > -1) {
            final float newSaturation = player.getFoodStats().getSaturationLevel();
            saturationCounter += (Math.max(0, saturationSnapshot - newSaturation));
            if (saturationCounter >= getConversionRatio(level) && player.getFoodStats().needFood()) {
                saturationCounter = saturationCounter - getConversionRatio(level);
                player.getFoodStats().addStats(1, 0);
            }
            saturationSnapshot = newSaturation;
        }
    }

    public static float getConversionRatio(int level) {
        return (float) (1 / (.25 * Math.log10(level + 1.5)) + .5);
    }

    @Override
    public EffectType getEffectType() {
        return EffectType.PASSIVE;
    }

    @Override
    public MentalConstructType getType() {
        return AstralMentalConstructs.GARDEN.get();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putFloat("saturationSnapshot", saturationSnapshot);
        nbt.putFloat("saturationCounter", saturationCounter);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        saturationSnapshot = nbt.getFloat("saturationSnapshot");
        saturationCounter = nbt.getFloat("saturationCounter");
    }
}
