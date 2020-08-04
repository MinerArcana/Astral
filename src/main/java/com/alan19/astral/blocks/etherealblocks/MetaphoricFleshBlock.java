package com.alan19.astral.blocks.etherealblocks;

import com.alan19.astral.blocks.AstralBlocks;
import com.alan19.astral.dimensions.AstralDimensions;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class MetaphoricFleshBlock extends EtherealBlock implements Ethereal {
    public MetaphoricFleshBlock() {
        super(Block.Properties.create(Material.ORGANIC, MaterialColor.RED).hardnessAndResistance(2.0F).notSolid().tickRandomly());
    }

    @Override
    @ParametersAreNonnullByDefault
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        final ResourceLocation registryName = worldIn.getDimension().getType().getRegistryName();
        if (registryName != null && registryName.equals(AstralDimensions.INNER_REALM)){
            boolean didModify = false;
            final ImmutableList<BlockPos> adjacentBlockPos = getAdjacentBlockPos(pos);
            for (BlockPos blockPos : adjacentBlockPos) {
                final BlockState blockState = worldIn.getBlockState(blockPos);
                final Block block = blockState.getBlock();
                if (block == AstralBlocks.ETHER_DIRT.get() && EtherGrass.canGrassSpread(blockState, worldIn, blockPos)) {
                    worldIn.setBlockState(blockPos, AstralBlocks.ETHER_GRASS.get().getDefaultState());
                    didModify = true;
                }
                else if (block == AstralBlocks.ETHER_GRASS.get()){
                    IGrowable etherGrass = (IGrowable) block;
                    if (etherGrass.canUseBonemeal(worldIn, rand, pos, blockState)){
                        etherGrass.grow(worldIn, rand, blockPos, blockState);
                        didModify = true;
                    }
                }
            }
            if (didModify){
                worldIn.setBlockState(pos, Fluids.WATER.getDefaultState().getBlockState());
            }
        }
    }

    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        super.randomTick(state, worldIn, pos, random);
    }

    private ImmutableList<BlockPos> getAdjacentBlockPos(BlockPos pos) {
        final BlockPos immutablePos = pos.toImmutable();
        return ImmutableList.of(immutablePos.up(), immutablePos.down(), immutablePos.east(), immutablePos.west(), immutablePos.north(), immutablePos.south());
    }
}
