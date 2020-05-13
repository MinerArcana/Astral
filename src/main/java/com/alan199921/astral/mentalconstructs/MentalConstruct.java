package com.alan199921.astral.mentalconstructs;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class MentalConstruct extends ForgeRegistryEntry<MentalConstruct> implements INBTSerializable<CompoundNBT> {
    private int constructLevel;

    public int getConstructLevel() {
        return constructLevel;
    }

    public void setConstructLevel(int constructLevel) {
        this.constructLevel = constructLevel;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("level", constructLevel);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        constructLevel = nbt.getInt("level");
    }

    abstract void performEffect(PlayerEntity player);
}
