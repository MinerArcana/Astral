package com.alan199921.astral.api.constructtracker;

import com.alan199921.astral.mentalconstructs.AstralMentalConstructs;
import com.alan199921.astral.mentalconstructs.MentalConstruct;
import com.alan199921.astral.mentalconstructs.MentalConstructType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.RegistryObject;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;

public class PlayerMentalConstructTracker implements INBTSerializable<CompoundNBT> {
    private final HashMap<String, Pair<MentalConstruct, Integer>> mentalConstructs = new HashMap<>();

    public PlayerMentalConstructTracker() {
        for (RegistryObject<MentalConstructType> construct : AstralMentalConstructs.MENTAL_CONSTRUCTS.getEntries()) {
            mentalConstructs.put(construct.getId().getPath(), Pair.of(construct.get().create(), -1));
        }
    }

    public void modifyConstructInfo(MentalConstructType type, int level) {
        final String constructName = type.getRegistryName().getPath();
        final Pair<MentalConstruct, Integer> updatedConstruct = mentalConstructs.getOrDefault(constructName, Pair.of(type.create(), level));
        mentalConstructs.put(constructName, Pair.of(updatedConstruct.getKey(), level));
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
            mentalConstructs.put(mentalConstruct.getId().getPath(), Pair.of(createdMentalConstruct, level));
        }
    }

    public void performAllPassiveEffects(PlayerEntity playerEntity) {
        for (Pair<MentalConstruct, Integer> mentalConstructIntegerPair : mentalConstructs.values()) {
            if (mentalConstructIntegerPair.getKey().getEffectType() == MentalConstruct.EffectType.PASSIVE) {
                mentalConstructIntegerPair.getKey().performEffect(playerEntity, mentalConstructIntegerPair.getValue());
            }
        }
    }
}
