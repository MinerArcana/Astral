package com.alan199921.astral.blocks.etherealblocks;

import com.alan199921.astral.effects.AstralEffects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nonnull;

public class EtherealBlock extends Block {
    public EtherealBlock(Properties properties) {
        super(properties.notSolid());
    }

    /**
     * Render the block as invisible if the player does not have Astral Travel
     *
     * @param state The block state
     * @return Invisible if player does not have Astral Travel, super if player does
     */
    @Nonnull
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (player != null) {
            return player.isPotionActive(AstralEffects.ASTRAL_TRAVEL) ? super.getRenderType(state) : BlockRenderType.INVISIBLE;
        }
        return super.getRenderType(state);
    }

    @Override
    public boolean canEntityDestroy(BlockState state, IBlockReader world, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity) {
            return ((LivingEntity) entity).isPotionActive(AstralEffects.ASTRAL_TRAVEL);
        }
        return super.canEntityDestroy(state, world, pos, entity);
    }

    @Nonnull
    @Override
    public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull IBlockReader worldIn, @Nonnull BlockPos pos, ISelectionContext context) {
        if (context.getEntity() instanceof LivingEntity && ((LivingEntity) context.getEntity()).isPotionActive(AstralEffects.ASTRAL_TRAVEL)) {
            return super.getCollisionShape(state, worldIn, pos, context);
        }
        return VoxelShapes.empty();
    }

    @Nonnull
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        ClientPlayerEntity clientPlayerEntity = Minecraft.getInstance().player;
        if (clientPlayerEntity != null && !clientPlayerEntity.isPotionActive(AstralEffects.ASTRAL_TRAVEL)) {
            return VoxelShapes.empty();
        }
        return super.getShape(state, worldIn, pos, context);
    }

    @Override
    public int getOpacity(BlockState state, @Nonnull IBlockReader worldIn, @Nonnull BlockPos pos) {
        return 0;
    }
}
