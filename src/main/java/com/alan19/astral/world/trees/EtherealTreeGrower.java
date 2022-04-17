package com.alan19.astral.world.trees;

import com.alan19.astral.world.AstralConfiguredFeatures;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Random;

public class EtherealTreeGrower extends AbstractTreeGrower {
    @Nullable
    @Override
    protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(@Nonnull Random random, boolean b) {
        return AstralConfiguredFeatures.CONFIGURED_ETHEREAL_TREE;
    }
}
