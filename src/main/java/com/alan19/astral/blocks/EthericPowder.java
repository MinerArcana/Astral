package com.alan19.astral.blocks;

import com.alan19.astral.effects.AstralEffects;
import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EthericPowder extends AbstractButtonBlock {
    protected EthericPowder() {
        super(false, Properties.create(Material.MISCELLANEOUS)
                .doesNotBlockMovement()
                .hardnessAndResistance(0));
    }

    @Nonnull
    @Override
    protected SoundEvent getSoundEvent(boolean buttonOn) {
        return buttonOn ? SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON : SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF;
    }

    @Nonnull
    @Override
    public ActionResultType onBlockActivated(@Nonnull BlockState blockState, @Nonnull World world, @Nonnull BlockPos blockPos, PlayerEntity playerEntity, @Nonnull Hand hand, @Nonnull BlockRayTraceResult blockRayTraceResult) {
        return playerEntity.isPotionActive(AstralEffects.ASTRAL_TRAVEL) ? super.onBlockActivated(blockState, world, blockPos, playerEntity, hand, blockRayTraceResult) : ActionResultType.FAIL;
    }
}
