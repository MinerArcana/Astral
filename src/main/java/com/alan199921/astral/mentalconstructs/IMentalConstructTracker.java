package com.alan199921.astral.mentalconstructs;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IMentalConstructTracker extends INBTSerializable<CompoundNBT> {
    PlayerMentalConstructTracker getMentalConstructsForPlayer(PlayerEntity player);
}
