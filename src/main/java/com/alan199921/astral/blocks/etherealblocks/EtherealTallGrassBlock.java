package com.alan199921.astral.blocks.etherealblocks;

import com.alan199921.astral.blocks.AstralBlocks;
import com.alan199921.astral.effects.AstralEffects;
import com.alan199921.astral.tags.AstralTags;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Random;

public class EtherealTallGrassBlock extends TallGrassBlock {
    public EtherealTallGrassBlock() {
        super(Block.Properties.create(Material.TALL_PLANTS).doesNotBlockMovement().hardnessAndResistance(0F).sound(SoundType.PLANT));
    }

    @Override
    protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return state.getBlock() == AstralBlocks.ETHER_DIRT.get() || state.getBlock() == AstralBlocks.ETHER_GRASS.get() || super.isValidGround(state, worldIn, pos);
    }

    @Override
    public void grow(@Nonnull ServerWorld worldIn, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        EtherealDoublePlantBlock etherealDoublePlantBlock = this == AstralBlocks.ETHEREAL_FERN.get() ? AstralBlocks.LARGE_ETHEREAL_FERN.get() : AstralBlocks.TALL_ETHEREAL_GRASS.get();
        if (etherealDoublePlantBlock.getDefaultState().isValidPosition(worldIn, pos) && worldIn.isAirBlock(pos.up())) {
            etherealDoublePlantBlock.placeAt(worldIn, pos, 2);
        }

    }

    /**
     * Render the block as invisible if the player does not have Astral Travel
     *
     * @param state The block state
     * @return Invisible if player does not have Astral Travel, super if player does
     */
    @Nonnull
    @Override
    public BlockRenderType getRenderType(@Nonnull BlockState state) {
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

    /**
     * Allows Astral entities and items to not pass through Ethereal blocks
     *
     * @param state   The blockstate of the ethereal block
     * @param worldIn The world
     * @param pos     The blockpos
     * @param context The context of the shape query
     * @return super (usually a regular shape) if the entity is Astral, empty if not
     */
    @Nonnull
    @Override
    public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull IBlockReader worldIn, @Nonnull BlockPos pos, ISelectionContext context) {
        if (context.getEntity() instanceof LivingEntity && ((LivingEntity) context.getEntity()).isPotionActive(AstralEffects.ASTRAL_TRAVEL) || context.getEntity() instanceof ItemEntity && AstralTags.ASTRAL_PICKUP.contains(((ItemEntity) context.getEntity()).getItem().getItem())) {
            return super.getCollisionShape(state, worldIn, pos, context);
        }
        return VoxelShapes.empty();
    }

    @OnlyIn(Dist.CLIENT)
    @Nonnull
    @Override
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull IBlockReader worldIn, @Nonnull BlockPos pos, @Nonnull ISelectionContext context) {
        ClientPlayerEntity clientPlayerEntity = Minecraft.getInstance().player;
        if (clientPlayerEntity != null && !clientPlayerEntity.isPotionActive(AstralEffects.ASTRAL_TRAVEL)) {
            return VoxelShapes.empty();
        }
        return super.getShape(state, worldIn, pos, context);
    }

    @Override
    public int getOpacity(@Nonnull BlockState state, @Nonnull IBlockReader worldIn, @Nonnull BlockPos pos) {
        return 0;
    }

}
