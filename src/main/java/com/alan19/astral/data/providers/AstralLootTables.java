package com.alan19.astral.data.providers;

import com.alan19.astral.data.providers.loottables.AstralBlockLootTables;
import com.alan19.astral.data.providers.loottables.AstralChestLootTables;
import com.alan19.astral.data.providers.loottables.AstralEntityLootTables;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class AstralLootTables extends LootTableProvider {
    public AstralLootTables(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    @Nonnull
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return ImmutableList.of(Pair.of(AstralBlockLootTables::new, LootContextParamSets.BLOCK), Pair.of(AstralEntityLootTables::new, LootContextParamSets.ENTITY), Pair.of(AstralChestLootTables::new, LootContextParamSets.CHEST));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, @Nonnull ValidationContext validationtracker) {
        map.forEach((name, table) -> LootTables.validate(validationtracker, name, table));
    }

}
