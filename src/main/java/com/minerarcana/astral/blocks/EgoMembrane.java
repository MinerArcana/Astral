package com.minerarcana.astral.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.NotNull;

public class EgoMembrane extends Block {
    public EgoMembrane() {
        super(BlockBehaviour.Properties.of(Material.CLAY)
                .strength(-1.0F, 3600000.0F)
                .noLootTable()
                .lightLevel(value -> 14)
                .isValidSpawn((pState, pLevel, pPos, pValue) -> false));
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull PushReaction getPistonPushReaction(@NotNull BlockState pState) {
        return PushReaction.IGNORE;
    }
}
