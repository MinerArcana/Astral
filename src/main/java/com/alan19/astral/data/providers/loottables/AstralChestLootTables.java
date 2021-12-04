package com.alan19.astral.data.providers.loottables;

import com.alan19.astral.Astral;
import com.alan19.astral.items.AstralItems;
import net.minecraft.data.loot.ChestLoot;
import net.minecraft.loot.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.ConstantIntValue;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.RandomValueBounds;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;

import java.util.function.BiConsumer;

public class AstralChestLootTables extends ChestLoot {
    @Override
    public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        consumer.accept(new ResourceLocation(Astral.MOD_ID, "inject/chests/shipwreck_supply"), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantIntValue.exactly(1))
                        .add(LootItem.lootTableItem(AstralItems.SNOWBERRY.get())
                                .setWeight(2)
                                .apply(SetItemCountFunction.setCount(RandomValueBounds.between(0, 4)))))
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantIntValue.exactly(1))
                        .add(LootItem.lootTableItem(AstralItems.FEVERWEED.get())
                                .setWeight(2)
                                .apply(SetItemCountFunction.setCount(RandomValueBounds.between(0, 4))))));
    }
}
