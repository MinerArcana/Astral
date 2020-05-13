package com.alan199921.astral.datagen.providers;

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

import static com.alan199921.astral.blocks.AstralBlocks.*;

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
        private final List<Block> etherealPlants = new ArrayList<>(Arrays.asList(LARGE_ETHEREAL_FERN.get(), ETHEREAL_FERN.get(), ETHEREAL_GRASS.get(), TALL_ETHEREAL_GRASS.get(), ETHEREAL_LEAVES.get()));

        @Override
        protected void addTables() {
            etherealPlants.forEach(this::registerShearsRecipe);
            this.registerLootTable(LARGE_ETHEREAL_FERN.get(), onlyWithShears(ETHEREAL_FERN.get()));
            this.registerLootTable(TALL_ETHEREAL_GRASS.get(), onlyWithShears(ETHEREAL_GRASS.get()));
            this.registerLootTable(ETHEREAL_PLANKS.get(), dropping(ETHEREAL_PLANKS.get()));
            this.registerLootTable(ETHEREAL_TRAPDOOR.get(), dropping(ETHEREAL_TRAPDOOR.get()));
            this.registerLootTable(ETHEREAL_DOOR.get(), dropping(ETHEREAL_DOOR.get()));
//            this.registerLootTable(AstralBlocks.LARGE_ETHEREAL_FERN.get(), droppingWithShears(AstralBlocks.LARGE_ETHEREAL_FERN.get(), withSurvivesExplosion(AstralBlocks.LARGE_ETHEREAL_FERN.get(), ItemLootEntry
//                    .builder(Items.WHEAT_SEEDS))
//                    .acceptCondition(BlockStateProperty
//                            .builder(AstralBlocks.LARGE_ETHEREAL_FERN.get())
//                            .fromProperties(StatePropertiesPredicate.Builder
//                                    .newBuilder()
//                                    .withProp(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)))
//                    .acceptCondition(RandomChance.builder(0.125F))));
        }

        @Override
        @Nonnull
        protected Iterable<Block> getKnownBlocks() {
            final List<Block> astralBlocks = this.etherealPlants;
            astralBlocks.addAll(Arrays.asList(ETHEREAL_PLANKS.get(), ETHEREAL_TRAPDOOR.get(), ETHEREAL_DOOR.get()));
            return astralBlocks;
        }

        private void registerShearsRecipe(Block block) {
            this.registerLootTable(block, onlyWithShears(block));
        }
    }
}
