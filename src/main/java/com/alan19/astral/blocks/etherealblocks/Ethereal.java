package com.alan19.astral.blocks.etherealblocks;

import com.alan19.astral.effects.AstralEffects;
import com.alan19.astral.tags.AstralTags;
import com.alan19.astral.util.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface Ethereal {

    /**
     * Render the block as invisible if the player does not have Astral Travel
     *
     * @param defaultReturn The BlockRenderType returned from the super function of the block
     * @return Invisible if player does not have Astral Travel, super if player does
     */
    static RenderShape getRenderType(RenderShape defaultReturn) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            return player.hasEffect(AstralEffects.ASTRAL_TRAVEL.get()) ? defaultReturn : RenderShape.INVISIBLE;
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
            return ((LivingEntity) entity).hasEffect(AstralEffects.ASTRAL_TRAVEL.get());
        }
        return defaultReturn;
    }

    /**
     * Allows Astral entities and items to not pass through Ethereal blocks
     *
     * @param context The context of the shape query
     * @return usually a regular shape if the entity is Astral, empty if not
     */
    // TODO Switch to mixins?
    static VoxelShape getCollisionShape(CollisionContext context, VoxelShape parentReturn) {
        return parentReturn;
    }

    /**
     * Gets the VoxelShape of the block
     *
     * @param parentShape The default shape of a block
     * @return The solid shape if the player has Astral Travel, empty shape if the player does not
     */
    @OnlyIn(Dist.CLIENT)
    static VoxelShape getShape(VoxelShape parentShape) {
        LocalPlayer clientPlayerEntity = Minecraft.getInstance().player;
        if (clientPlayerEntity != null && !clientPlayerEntity.hasEffect(AstralEffects.ASTRAL_TRAVEL.get())) {
            return Shapes.empty();
        }
        return parentShape;
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
}