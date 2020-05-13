package com.alan199921.astral.mentalconstructs;

import javafx.util.Pair;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.RegistryObject;

import java.util.HashMap;

public class PlayerMentalConstructTracker implements INBTSerializable<CompoundNBT> {
    private final HashMap<String, Pair<MentalConstruct, Integer>> mentalConstructs = new HashMap<>();

    public PlayerMentalConstructTracker() {
        for (RegistryObject<MentalConstructType> mentalConstruct : AstralMentalConstructs.MENTAL_CONSTRUCTS.getEntries()) {
            mentalConstructs.put(mentalConstruct.getId().getPath(), new Pair<>(mentalConstruct.get().create(), -1));
        }
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        mentalConstructs.forEach((key, value) -> {
            final CompoundNBT constructNBT = new CompoundNBT();
            constructNBT.put("constructInfo", value.getKey().serializeNBT());
            constructNBT.putInt("level", value.getValue());
            nbt.put(key, constructNBT);
        });
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        for (RegistryObject<MentalConstructType> mentalConstruct : AstralMentalConstructs.MENTAL_CONSTRUCTS.getEntries()) {
            final MentalConstruct createdMentalConstruct = mentalConstruct.get().create();
            CompoundNBT constructNbt = nbt.getCompound(mentalConstruct.getId().getPath());
            createdMentalConstruct.deserializeNBT(constructNbt.getCompound("constructInfo"));
            int level = constructNbt.getInt("level");
            mentalConstructs.put(mentalConstruct.getId().getPath(), new Pair<>(createdMentalConstruct, level));
        }
    }
}
