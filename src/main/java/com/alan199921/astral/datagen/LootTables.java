package com.alan199921.astral.datagen;

import com.alan199921.astral.blocks.AstralBlocks;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LootTables extends LootTableProvider {
    public LootTables(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    @Nonnull
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
        return ImmutableList.of(Pair.of(Blocks::new, LootParameterSets.BLOCK));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, @Nonnull ValidationTracker validationtracker) {
        map.forEach((name, table) -> LootTableManager.func_227508_a_(validationtracker, name, table));
    }

    private static class Blocks extends BlockLootTables {
        private final List<Block> etherealPlants = new ArrayList<>(Arrays.asList(AstralBlocks.LARGE_ETHEREAL_FERN.get(), AstralBlocks.ETHEREAL_FERN.get(), AstralBlocks.ETHEREAL_GRASS.get(), AstralBlocks.TALL_ETHEREAL_GRASS.get(), AstralBlocks.ETHEREAL_LEAVES.get()));

        @Override
        protected void addTables() {
            etherealPlants.forEach(this::registerShearsRecipe);
        }

        @Override
        @Nonnull
        protected Iterable<Block> getKnownBlocks() {
            return etherealPlants;
        }

        private void registerShearsRecipe(Block block) {
            this.registerLootTable(block, droppingWithShears(block, ItemLootEntry.builder(block)));
        }
    }
}
