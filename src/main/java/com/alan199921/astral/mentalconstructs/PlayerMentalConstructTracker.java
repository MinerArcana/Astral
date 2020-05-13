package com.alan199921.astral.mentalconstructs;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;

public class PlayerMentalConstructTracker implements INBTSerializable<CompoundNBT> {
    private final HashMap<MentalConstruct, Integer> mentalConstructs = new HashMap<>();

    public PlayerMentalConstructTracker() {
        AstralMentalConstructs.MENTAL_CONSTRUCTS.getEntries().forEach(mentalConstruct -> mentalConstructs.put(mentalConstruct.get().create(), -1));
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        mentalConstructs.forEach((key, value) -> nbt.putInt(key.getRegistryName().getPath(), value));
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        AstralMentalConstructs.MENTAL_CONSTRUCTS.getEntries().forEach(mentalConstruct -> mentalConstructs.put(mentalConstruct.get().create(), nbt.getInt(mentalConstruct.getId().getPath())));
    }
}
