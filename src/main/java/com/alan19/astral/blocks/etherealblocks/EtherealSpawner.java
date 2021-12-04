package com.alan19.astral.blocks.etherealblocks;

import com.alan19.astral.blocks.tileentities.EtherealMobSpawnerTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.SpawnerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class EtherealSpawner extends SpawnerBlock implements Ethereal {
    public EtherealSpawner() {
        super(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(5.0F).sound(SoundType.METAL).noOcclusion());
    }

    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockGetter worldIn) {
        return new EtherealMobSpawnerTileEntity();
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

    @Nonnull
    @Override
    public PushReaction getPistonPushReaction(@Nonnull BlockState state) {
        return Ethereal.getPushReaction();
    }
}
