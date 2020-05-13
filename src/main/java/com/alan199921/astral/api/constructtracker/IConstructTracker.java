package com.alan199921.astral.api.constructtracker;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IConstructTracker extends INBTSerializable<CompoundNBT> {
    PlayerMentalConstructTracker getMentalConstructsForPlayer(PlayerEntity player);
}
