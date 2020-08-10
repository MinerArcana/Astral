package com.alan19.astral.data.providers.loottables;

import com.alan19.astral.Astral;
import com.alan19.astral.items.AstralItems;
import net.minecraft.data.loot.ChestLootTables;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.functions.SetCount;

import java.util.function.BiConsumer;

public class AstralChestLootTables extends ChestLootTables {
    @Override
    public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        consumer.accept(new ResourceLocation(Astral.MOD_ID, "inject/chests/shipwreck_supply"), LootTable.builder()
                .addLootPool(LootPool.builder()
                        .rolls(ConstantRange.of(1))
                        .addEntry(ItemLootEntry.builder(AstralItems.SNOWBERRY.get())
                                .weight(2)
                                .acceptFunction(SetCount.builder(RandomValueRange.of(0, 4)))))
                .addLootPool(LootPool.builder()
                        .rolls(ConstantRange.of(1))
                        .addEntry(ItemLootEntry.builder(AstralItems.FEVERWEED.get())
                                .weight(2)
                                .acceptFunction(SetCount.builder(RandomValueRange.of(0, 4))))));
    }
}
