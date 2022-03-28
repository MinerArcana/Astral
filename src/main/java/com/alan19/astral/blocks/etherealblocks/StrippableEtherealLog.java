package com.alan19.astral.blocks.etherealblocks;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class StrippableEtherealLog extends EtherealLog {
    private final Supplier<Block> strippedLog;

    public StrippableEtherealLog(Supplier<Block> block) {
        super();
        this.strippedLog = block;
    }

    @Override
    public InteractionResult use(@Nonnull BlockState state, @Nonnull Level worldIn, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand handIn, @Nonnull BlockHitResult hit) {
        if (player.getItemInHand(handIn).getItem() instanceof AxeItem) {
            worldIn.playSound(player, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            worldIn.setBlock(pos, strippedLog.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS)), 11);
            player.getItemInHand(handIn).hurtAndBreak(1, player, playerEntity -> playerEntity.broadcastBreakEvent(handIn));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
