package com.alan19.astral.api.astraldamage;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IAstralDamage extends INBTSerializable<CompoundNBT> {
    int getAstralDamage();

    void setAstralDamage(int newDamage);
}
