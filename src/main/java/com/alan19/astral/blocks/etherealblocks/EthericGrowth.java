package com.alan19.astral.blocks.etherealblocks;

import com.alan19.astral.blocks.AstralBlocks;
import com.alan19.astral.tags.AstralTags;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;
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
    public boolean isValidBonemealTarget(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
        return this == AstralBlocks.GENTLEGRASS.get() || this == AstralBlocks.WILDWEED.get() || this == AstralBlocks.CYANGRASS.get() || this == AstralBlocks.REDBULB.get();
    }

    @Override
    @ParametersAreNonnullByDefault
    public void performBonemeal(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state) {
        if (worldIn.isEmptyBlock(pos.above())){
            if (this == AstralBlocks.GENTLEGRASS.get()){
                AstralBlocks.TALL_GENTLEGRASS.get().placeAt(worldIn, pos, 2);
            }
            else if (this == AstralBlocks.WILDWEED.get()){
                AstralBlocks.TALL_WILDWEED.get().placeAt(worldIn, pos, 2);
            }
            else if (this == AstralBlocks.CYANGRASS.get()){
                AstralBlocks.TALL_CYANGRASS.get().placeAt(worldIn, pos, 2);
            }
            else if (this == AstralBlocks.REDBULB.get()){
                AstralBlocks.TALL_REDBULB.get().placeAt(worldIn, pos, 2);
            }
        }
    }

    @Nonnull
    @Override
    public BlockRenderType getRenderShape(@Nonnull BlockState state) {
        return Ethereal.getRenderType(super.getRenderShape(state));
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
    public int getLightBlock(@Nonnull BlockState state, @Nonnull IBlockReader worldIn, @Nonnull BlockPos pos) {
        return Ethereal.getOpacity();
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, @Nonnull IBlockReader worldIn, @Nonnull BlockPos pos) {
        return AstralTags.ETHEREAL_VEGETATION_PLANTABLE_ON.contains(state.getBlock());
    }
}
