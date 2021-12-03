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
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class MetaphoricFleshBlock extends EtherealBlock implements Ethereal {
    public MetaphoricFleshBlock() {
        super(Block.Properties.of(Material.GRASS, MaterialColor.COLOR_RED).strength(2.0F).noOcclusion().randomTicks());
    }

    @Override
    @ParametersAreNonnullByDefault
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (worldIn.dimension() == AstralDimensions.INNER_REALM) {
            boolean didModify = false;
            final ImmutableList<BlockPos> adjacentBlockPos = getAdjacentBlockPos(pos);
            for (BlockPos blockPos : adjacentBlockPos) {
                final BlockState blockState = worldIn.getBlockState(blockPos);
                final Block block = blockState.getBlock();
                if (block == AstralBlocks.ETHER_DIRT.get() && EtherGrass.canGrassSpread(blockState, worldIn, blockPos)) {
                    worldIn.setBlockAndUpdate(blockPos, AstralBlocks.ETHER_GRASS.get().defaultBlockState());
                    didModify = true;
                }
                else if (block == AstralBlocks.ETHER_GRASS.get()) {
                    IGrowable etherGrass = (IGrowable) block;
                    if (etherGrass.isValidBonemealTarget(worldIn, blockPos, blockState, false)) {
                        etherGrass.performBonemeal(worldIn, rand, blockPos, blockState);
                        didModify = true;
                    }
                }
            }
            if (didModify) {
                worldIn.setBlockAndUpdate(pos, Fluids.WATER.defaultFluidState().createLegacyBlock());
            }
        }
    }

    private ImmutableList<BlockPos> getAdjacentBlockPos(BlockPos pos) {
        final BlockPos immutablePos = pos.immutable();
        return ImmutableList.of(immutablePos.above(), immutablePos.below(), immutablePos.east(), immutablePos.west(), immutablePos.north(), immutablePos.south());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    @ParametersAreNonnullByDefault
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        super.animateTick(stateIn, worldIn, pos, rand);
        if (worldIn.dimension() == AstralDimensions.INNER_REALM && canWork(pos, worldIn)) {
            for (int i = 0; i < 2; i++) {
                final double x = pos.getX() + rand.nextDouble();
                final double y = pos.getY() + rand.nextDouble();
                final double z = pos.getZ() + rand.nextDouble();
                worldIn.addParticle(ParticleTypes.DRIPPING_WATER, x, y, z, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    private boolean canWork(BlockPos pos, World worldIn) {
        return getAdjacentBlockPos(pos).stream().anyMatch(blockPos -> {
            final BlockState blockState = worldIn.getBlockState(blockPos);
            Block block = blockState.getBlock();
            if (block == AstralBlocks.ETHER_DIRT.get()){
                return true;
            }
            else if (block == AstralBlocks.ETHER_GRASS.get()){
                return ((EtherGrass)block).isValidBonemealTarget(worldIn, blockPos, blockState, true);
            }
            else {
                return false;
            }
        });
    }
}
