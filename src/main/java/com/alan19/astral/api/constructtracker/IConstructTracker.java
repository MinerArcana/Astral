package com.alan19.astral.api.constructtracker;

import com.alan19.astral.mentalconstructs.MentalConstructType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.INBTSerializable;

public interface IConstructTracker extends INBTSerializable<CompoundTag> {
    PlayerMentalConstructTracker getMentalConstructsForPlayer(Player player);

    void resetConstructEffect(MentalConstructType mentalConstruct, Level worldIn, BlockPos blockPos);

    void updateAllPlayers(MentalConstructType mentalConstructType, ServerLevel world, BlockPos blockPos, int level);
}
