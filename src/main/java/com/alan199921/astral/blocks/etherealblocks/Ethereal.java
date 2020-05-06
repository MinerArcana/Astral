package com.alan199921.astral.blocks.etherealblocks;

import com.alan199921.astral.effects.AstralEffects;
import com.alan199921.astral.tags.AstralTags;
import net.minecraft.block.BlockRenderType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface Ethereal {

    int ETHEREAL_BLOCK_OPACITY = 0;

    /**
     * Render the block as invisible if the player does not have Astral Travel
     *
     * @param defaultReturn The BlockRenderType returned from the super function of the block
     * @return Invisible if player does not have Astral Travel, super if player does
     */
    static BlockRenderType getRenderType(BlockRenderType defaultReturn) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (player != null) {
            return player.isPotionActive(AstralEffects.ASTRAL_TRAVEL) ? defaultReturn : BlockRenderType.INVISIBLE;
        }
        return defaultReturn;
    }

    static boolean canEntityDestroy(Entity entity, boolean defaultReturn) {
        if (entity instanceof LivingEntity) {
            return ((LivingEntity) entity).isPotionActive(AstralEffects.ASTRAL_TRAVEL);
        }
        return defaultReturn;
    }

    /**
     * Allows Astral entities and items to not pass through Ethereal blocks
     *
     * @param context The context of the shape query
     * @return super (usually a regular shape) if the entity is Astral, empty if not
     */
    static VoxelShape getCollisionShape(ISelectionContext context, VoxelShape parentReturn) {
        if (context.getEntity() instanceof LivingEntity && ((LivingEntity) context.getEntity()).isPotionActive(AstralEffects.ASTRAL_TRAVEL) || context.getEntity() instanceof ItemEntity && AstralTags.ASTRAL_PICKUP.contains(((ItemEntity) context.getEntity()).getItem().getItem())) {
            return parentReturn;
        }
        return VoxelShapes.empty();
    }

    @OnlyIn(Dist.CLIENT)
    static VoxelShape getShape(VoxelShape parentShape) {
        ClientPlayerEntity clientPlayerEntity = Minecraft.getInstance().player;
        if (clientPlayerEntity != null && !clientPlayerEntity.isPotionActive(AstralEffects.ASTRAL_TRAVEL)) {
            return VoxelShapes.empty();
        }
        return parentShape;
    }

    static int getOpacity() {
        return ETHEREAL_BLOCK_OPACITY;
    }


}
