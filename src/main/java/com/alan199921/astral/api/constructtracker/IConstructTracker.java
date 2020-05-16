package com.alan199921.astral.api.constructtracker;

import com.alan199921.astral.mentalconstructs.MentalConstructType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;

public interface IConstructTracker extends INBTSerializable<CompoundNBT> {
    PlayerMentalConstructTracker getMentalConstructsForPlayer(PlayerEntity player);

    void resetConstructEffect(MentalConstructType mentalConstruct, World worldIn, BlockPos blockPos);

    void updateAllPlayers(MentalConstructType mentalConstructType, ServerWorld world, BlockPos blockPos, int level);
}
