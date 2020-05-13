package com.alan199921.astral.mentalconstructs;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;

public interface IMentalConstructTracker extends INBTSerializable<CompoundNBT> {
    List<MentalConstruct> getMentalConstructsForPlayer(PlayerEntity player);
}
