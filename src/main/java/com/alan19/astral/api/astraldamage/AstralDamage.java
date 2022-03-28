package com.alan19.astral.api.astraldamage;

import net.minecraft.nbt.CompoundTag;

public class AstralDamage implements IAstralDamage {
    private int damage = 0;

    @Override
    public int getAstralDamage() {
        return damage;
    }

    @Override
    public void setAstralDamage(int newDamage) {
        damage = newDamage;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("damage", damage);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        damage = nbt.getInt("damage");
    }
}
