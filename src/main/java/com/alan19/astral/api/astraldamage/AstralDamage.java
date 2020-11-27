package com.alan19.astral.api.astraldamage;

import net.minecraft.nbt.CompoundNBT;

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
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("damage", damage);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        damage = nbt.getInt("damage");
    }
}
