package com.alan19.astral.blocks.etherealblocks;

import com.alan19.astral.effects.AstralEffects;
import com.alan19.astral.events.astraltravel.TravelEffects;
import com.alan19.astral.tags.AstralTags;
import com.alan19.astral.util.Constants;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.material.PushReaction;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

public interface Ethereal {

    /**
     * Render the block as invisible if the player does not have Astral Travel
     *
     * @param defaultReturn The BlockRenderType returned from the super function of the block
     * @return Invisible if player does not have Astral Travel, super if player does
     */
    static BlockRenderType getRenderType(BlockRenderType defaultReturn) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            return EtherealPlayerRenderHelper.getRenderTypeClient(defaultReturn);
        }
        return defaultReturn;
    }

    /**
     * Returns whether an entity could break the Ethereal block
     *
     * @param entity        The entity breaking the block
     * @param defaultReturn The boolean returned from the super function
     * @return If the entity can destroy the Ethereal block
     */
    static boolean canEntityDestroy(Entity entity, boolean defaultReturn) {
        if (entity instanceof LivingEntity) {
            return ((LivingEntity) entity).isPotionActive(AstralEffects.ASTRAL_TRAVEL.get());
        }
        return defaultReturn;
    }

    /**
     * Allows Astral entities and items to not pass through Ethereal blocks
     *
     * @param context The context of the shape query
     * @return usually a regular shape if the entity is Astral, empty if not
     */
    static VoxelShape getCollisionShape(ISelectionContext context, VoxelShape parentReturn) {
        final boolean isEntityAstral = context.getEntity() instanceof LivingEntity && TravelEffects.isEntityAstral((LivingEntity) context.getEntity());
        final boolean isEntityEtherealItem = context.getEntity() instanceof ItemEntity && AstralTags.ASTRAL_PICKUP.contains(((ItemEntity) context.getEntity()).getItem().getItem());
        return isEntityAstral || isEntityEtherealItem ? parentReturn : VoxelShapes.empty();
    }

    /**
     * Gets the VoxelShape of the block
     *
     * @param parentShape The default shape of a block
     * @return The solid shape if the player has Astral Travel, empty shape if the player does not
     */
    static VoxelShape getShape(ISelectionContext context, VoxelShape parentShape) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            return EtherealPlayerRenderHelper.getVoxelShapeClient(parentShape);
        }
        else {
            final boolean isEntityAstral = context.getEntity() instanceof LivingEntity && TravelEffects.isEntityAstral((LivingEntity) context.getEntity());
            final boolean isEntityEtherealItem = context.getEntity() instanceof ItemEntity && AstralTags.ASTRAL_PICKUP.contains(((ItemEntity) context.getEntity()).getItem().getItem());
            return isEntityAstral || isEntityEtherealItem ? parentShape : VoxelShapes.empty();
        }
    }

    /**
     * Returns the opacity of the block
     *
     * @return 0 as Ethereal blocks are transparent
     */
    static int getOpacity() {
        return Constants.ETHEREAL_BLOCK_OPACITY;
    }

    /**
     * Ethereal blocks ignore Pistons
     *
     * @return IGNORE
     */
    static PushReaction getPushReaction() {
        return PushReaction.IGNORE;
    }

    /**
     * Inner class to not load Minecraft on the server
     */
    class EtherealPlayerRenderHelper {
        static BlockRenderType getRenderTypeClient(BlockRenderType defaultRenderType) {
            return Minecraft.getInstance().player != null && Minecraft.getInstance().player.isPotionActive(AstralEffects.ASTRAL_TRAVEL.get()) ? defaultRenderType : BlockRenderType.INVISIBLE;
        }

        static VoxelShape getVoxelShapeClient(VoxelShape defaultShape) {
            return Minecraft.getInstance().player != null && Minecraft.getInstance().player.isPotionActive(AstralEffects.ASTRAL_TRAVEL.get()) ? defaultShape : VoxelShapes.empty();
        }
    }
}