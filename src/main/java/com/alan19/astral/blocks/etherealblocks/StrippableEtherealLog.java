package com.alan19.astral.blocks.etherealblocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class StrippableEtherealLog extends EtherealLog {
    private final Supplier<Block> strippedLog;

    public StrippableEtherealLog(Supplier<Block> block) {
        super();
        this.strippedLog = block;
    }

    @Override
    public ActionResultType onBlockActivated(@Nonnull BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand handIn, @Nonnull BlockRayTraceResult hit) {
        if (player.getHeldItem(handIn).getItem() instanceof AxeItem) {
            worldIn.playSound(player, pos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
            worldIn.setBlockState(pos, strippedLog.get().getDefaultState().with(RotatedPillarBlock.AXIS, state.get(RotatedPillarBlock.AXIS)), 11);
            player.getHeldItem(handIn).damageItem(1, player, playerEntity -> playerEntity.sendBreakAnimation(handIn));
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }
}
