package com.alan19.astral.blocks.etherealblocks;

import com.alan19.astral.blocks.AstralBlocks;
import com.alan19.astral.tags.AstralTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class EthericGrowth extends TallGrassBlock implements Ethereal {
    public EthericGrowth() {
        super(Block.Properties.of(Material.REPLACEABLE_PLANT).noCollission().strength(0F).sound(SoundType.GRASS));
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean isValidBonemealTarget(BlockGetter worldIn, BlockPos pos, BlockState state, boolean isClient) {
        return this == AstralBlocks.GENTLEGRASS.get() || this == AstralBlocks.WILDWEED.get() || this == AstralBlocks.CYANGRASS.get() || this == AstralBlocks.REDBULB.get();
    }

    @Override
    @ParametersAreNonnullByDefault
    public void performBonemeal(ServerLevel worldIn, Random rand, BlockPos pos, BlockState state) {
        if (worldIn.isEmptyBlock(pos.above())) {
            if (this == AstralBlocks.GENTLEGRASS.get()) {
                DoublePlantBlock.placeAt(worldIn, AstralBlocks.TALL_GENTLEGRASS.get().defaultBlockState(), pos, 2);
            }
            else if (this == AstralBlocks.WILDWEED.get()) {
                DoublePlantBlock.placeAt(worldIn, AstralBlocks.TALL_WILDWEED.get().defaultBlockState(), pos, 2);
            }
            else if (this == AstralBlocks.CYANGRASS.get()) {
                DoublePlantBlock.placeAt(worldIn, AstralBlocks.CYANGRASS.get().defaultBlockState(), pos, 2);
            }
            else if (this == AstralBlocks.REDBULB.get()) {
                DoublePlantBlock.placeAt(worldIn, AstralBlocks.TALL_REDBULB.get().defaultBlockState(), pos, 2);
            }
        }
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
    protected boolean mayPlaceOn(BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos) {
        return state.is(AstralTags.ETHEREAL_VEGETATION_PLANTABLE_ON);
    }
}
