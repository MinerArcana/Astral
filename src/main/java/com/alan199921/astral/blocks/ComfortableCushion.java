package com.alan199921.astral.blocks;

import com.alan199921.astral.api.AstralAPI;
import com.alan199921.astral.mentalconstructs.AstralMentalConstructs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class ComfortableCushion extends Block {
    public ComfortableCushion() {
        super(Properties.create(Material.WOOL));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn instanceof ServerWorld) {
            AstralAPI.getConstructTracker((ServerWorld) worldIn).ifPresent(tracker -> tracker.getMentalConstructsForPlayer(player).modifyConstructInfo(AstralMentalConstructs.GARDEN.get(), 0));
        }
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }
}
