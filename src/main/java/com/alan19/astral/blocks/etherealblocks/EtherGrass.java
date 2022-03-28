package com.alan19.astral.blocks.etherealblocks;

import com.alan19.astral.blocks.AstralBlocks;
import com.alan19.astral.tags.AstralTags;
import net.minecraft.block.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LayerLightEngine;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

public class EtherGrass extends GrassBlock implements Ethereal, BonemealableBlock {
    public EtherGrass() {
        super(Properties.of(Material.GRASS)
                .strength(.5f)
                .harvestTool(ToolType.SHOVEL)
                .sound(SoundType.GRASS)
                .randomTicks()
                .noOcclusion());
    }

    @Nonnull
    @Override
    public RenderShape getRenderShape(@Nonnull BlockState state) {
        return Ethereal.getRenderType(super.getRenderShape(state));
    }

    @Override
    public boolean canEntityDestroy(BlockState state, BlockGetter world, BlockPos pos, Entity entity) {
        return Ethereal.canEntityDestroy(entity, super.canEntityDestroy(state, world, pos, entity));
    }

    @Nonnull
    @Override
    public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return Ethereal.getCollisionShape(context, super.getCollisionShape(state, worldIn, pos, context));
    }

    @OnlyIn(Dist.CLIENT)
    @Nonnull
    @Override
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return Ethereal.getShape(super.getShape(state, worldIn, pos, context));
    }

    @Override
    public int getLightBlock(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos) {
        return Ethereal.getOpacity();
    }

    @Override
    public PushReaction getPistonPushReaction(@Nonnull BlockState state) {
        return Ethereal.getPushReaction();
    }

    private static boolean isTopBlockSnowOrBrightEnough(BlockState state, LevelReader worldReader, BlockPos pos) {
        BlockPos blockpos = pos.above();
        BlockState blockstate = worldReader.getBlockState(blockpos);
        if (blockstate.getBlock() == Blocks.SNOW && blockstate.getValue(SnowLayerBlock.LAYERS) == 1) {
            return true;
        }
        else {
            int i = LayerLightEngine.getLightBlockInto(worldReader, state, pos, blockstate, blockpos, Direction.UP, blockstate.getLightBlock(worldReader, blockpos));
            return i < worldReader.getMaxLightLevel();
        }
    }

    public static boolean canGrassSpread(BlockState state, LevelReader worldReader, BlockPos pos) {
        BlockPos blockpos = pos.above();
        return isTopBlockSnowOrBrightEnough(state, worldReader, pos) && !worldReader.getFluidState(blockpos).is(FluidTags.WATER);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random rand) {
        if (!isTopBlockSnowOrBrightEnough(state, worldIn, pos)) {
            // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
            if (worldIn.isAreaLoaded(pos, 3)) {
                worldIn.setBlockAndUpdate(pos, AstralBlocks.ETHER_DIRT.get().defaultBlockState());
            }
        }
        else if (worldIn.getMaxLocalRawBrightness(pos.above()) >= 9) {
            BlockState blockstate = this.defaultBlockState();

            IntStream.range(0, 4)
                    .mapToObj(i -> pos.offset(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1))
                    .filter(blockPos -> worldIn.getBlockState(blockPos).getBlock() == AstralBlocks.ETHER_DIRT.get() && canGrassSpread(blockstate, worldIn, blockPos))
                    .forEach(blockPos -> worldIn.setBlockAndUpdate(blockPos, blockstate));
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void performBonemeal(ServerLevel worldIn, Random rand, BlockPos pos, BlockState state) {
        final int numberOfPlants = rand.nextInt(8) + 8;
        final Collection<Block> ethericGrowths = AstralTags.ETHERIC_GROWTHS.getValues();
        //Attempt to spawn 8-16 Etheric Growths
        for (int i = 0; i < numberOfPlants; i++) {
            //Keep track of current and previous blockpos
            BlockPos grassPos = pos;
            BlockPos prevPos = grassPos;
            for (int j = 0; j < 128; j++) {
                //If the new pos is Ether Grass, keep going, else, revert to previous position
                if (worldIn.getBlockState(grassPos).getBlock() != AstralBlocks.ETHER_GRASS.get()) {
                    grassPos = prevPos;
                }
                else {
                    prevPos = grassPos;
                    final Optional<Block> blockOptional = ethericGrowths.stream().skip(rand.nextInt(ethericGrowths.size())).findFirst();
                    BlockPos upPos = grassPos.immutable().above();
                    if (blockOptional.isPresent() && shouldPlant(worldIn, rand, grassPos, blockOptional.get(), upPos)) {
                        setPlant(worldIn, blockOptional.get(), upPos);
                        break;
                    }
                    else {
                        grassPos = grassPos.offset(rand.nextInt(3) - 1, (rand.nextInt(3) - 1) * rand.nextInt(3) / 2, rand.nextInt(3) - 1);
                    }
                }
            }
        }
    }

    private void setPlant(ServerLevel worldIn, Block block, BlockPos upPos) {
        if (block instanceof TallEthericGrowth) {
            ((TallEthericGrowth) block).placeAt(worldIn, upPos, 2);
        }
        else {
            worldIn.setBlockAndUpdate(upPos, block.defaultBlockState());
        }
    }

    private boolean shouldPlant(ServerLevel worldIn, Random rand, BlockPos grassPos, Block blockOptional, BlockPos upPos) {
        return rand.nextInt(8) == 0 && worldIn.isEmptyBlock(upPos) && worldIn.getBlockState(grassPos).getBlock() == AstralBlocks.ETHER_GRASS.get() && blockOptional.defaultBlockState().canSurvive(worldIn, upPos);
    }
}
