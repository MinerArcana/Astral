package com.alan19.astral.api.astraldamage;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IAstralDamage extends INBTSerializable<CompoundTag> {
    int getAstralDamage();

    void setAstralDamage(int newDamage);
}
