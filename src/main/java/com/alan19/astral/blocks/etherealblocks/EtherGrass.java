package com.alan19.astral.blocks.etherealblocks;

import com.alan19.astral.blocks.AstralBlocks;
import com.alan19.astral.tags.AstralTags;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.lighting.LightEngine;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class EtherGrass extends GrassBlock implements Ethereal, IGrowable {
    public EtherGrass() {
        super(Properties.create(Material.ORGANIC)
                .hardnessAndResistance(.5f)
                .harvestTool(ToolType.SHOVEL)
                .sound(SoundType.PLANT)
                .notSolid());
    }

    @Nonnull
    @Override
    public BlockRenderType getRenderType(@Nonnull BlockState state) {
        return Ethereal.getRenderType(super.getRenderType(state));
    }

    @Override
    public boolean canEntityDestroy(BlockState state, IBlockReader world, BlockPos pos, Entity entity) {
        return Ethereal.canEntityDestroy(entity, super.canEntityDestroy(state, world, pos, entity));
    }

    @Nonnull
    @Override
    public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull IBlockReader worldIn, @Nonnull BlockPos pos, @Nonnull ISelectionContext context) {
        return Ethereal.getCollisionShape(context, super.getCollisionShape(state, worldIn, pos, context));
    }

    @OnlyIn(Dist.CLIENT)
    @Nonnull
    @Override
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull IBlockReader worldIn, @Nonnull BlockPos pos, @Nonnull ISelectionContext context) {
        return Ethereal.getShape(super.getShape(state, worldIn, pos, context));
    }

    @Override
    public int getOpacity(@Nonnull BlockState state, @Nonnull IBlockReader worldIn, @Nonnull BlockPos pos) {
        return Ethereal.getOpacity();
    }

    @Override
    public PushReaction getPushReaction(@Nonnull BlockState state) {
        return Ethereal.getPushReaction();
    }

    private static boolean isTopBlockSnowOrBrightEnough(BlockState state, IWorldReader worldReader, BlockPos pos) {
        BlockPos blockpos = pos.up();
        BlockState blockstate = worldReader.getBlockState(blockpos);
        if (blockstate.getBlock() == Blocks.SNOW && blockstate.get(SnowBlock.LAYERS) == 1) {
            return true;
        }
        else {
            int i = LightEngine.func_215613_a(worldReader, state, pos, blockstate, blockpos, Direction.UP, blockstate.getOpacity(worldReader, blockpos));
            return i < worldReader.getMaxLightLevel();
        }
    }

    private static boolean canGrassSpread(BlockState state, IWorldReader worldReader, BlockPos pos) {
        BlockPos blockpos = pos.up();
        return isTopBlockSnowOrBrightEnough(state, worldReader, pos) && !worldReader.getFluidState(blockpos).isTagged(FluidTags.WATER);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (!isTopBlockSnowOrBrightEnough(state, worldIn, pos)) {
            // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
            if (worldIn.isAreaLoaded(pos, 3)) {
                worldIn.setBlockState(pos, AstralBlocks.ETHER_DIRT.get().getDefaultState());
            }
        }
        else if (worldIn.getLight(pos.up()) >= 9) {
            BlockState blockstate = this.getDefaultState();

            IntStream.range(0, 4)
                    .mapToObj(i -> pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1))
                    .filter(blockPos -> worldIn.getBlockState(blockPos).getBlock() == AstralBlocks.ETHER_DIRT.get() && canGrassSpread(blockstate, worldIn, blockPos))
                    .forEach(blockPos -> worldIn.setBlockState(blockPos, blockstate));
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void grow(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state) {
        final Stream<BlockPos> allInBox = BlockPos.getAllInBox(pos.toImmutable().add(-3, -3, -3), pos.toImmutable().add(3, 3, 3));
        final Collection<Block> ethericGrowths = AstralTags.ETHERIC_GROWTHS.getAllElements();
        allInBox.filter(blockPos -> worldIn.isAirBlock(blockPos.up()))
                .map(blockPos -> Pair.of(blockPos, ethericGrowths.stream().skip(rand.nextInt(ethericGrowths.size())).findFirst()))
                .filter(posOptionalPair -> posOptionalPair.getRight().isPresent() && posOptionalPair.getRight().get().getDefaultState().isValidPosition(worldIn, posOptionalPair.getLeft()))
                .forEach(posOptionalPair -> worldIn.setBlockState(posOptionalPair.getLeft(), posOptionalPair.getRight().get().getDefaultState()));
    }
}
