package com.alan19.astral.data.providers.loottables;

import com.alan19.astral.Astral;
import com.alan19.astral.items.AstralItems;
import net.minecraft.data.loot.ChestLootTables;
import net.minecraft.loot.*;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.ResourceLocation;

import java.util.function.BiConsumer;

public class AstralChestLootTables extends ChestLootTables {
    @Override
    public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        consumer.accept(new ResourceLocation(Astral.MOD_ID, "inject/chests/shipwreck_supply"), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantRange.exactly(1))
                        .add(ItemLootEntry.lootTableItem(AstralItems.SNOWBERRY.get())
                                .setWeight(2)
                                .apply(SetCount.setCount(RandomValueRange.between(0, 4)))))
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantRange.exactly(1))
                        .add(ItemLootEntry.lootTableItem(AstralItems.FEVERWEED.get())
                                .setWeight(2)
                                .apply(SetCount.setCount(RandomValueRange.between(0, 4))))));
    }
}
