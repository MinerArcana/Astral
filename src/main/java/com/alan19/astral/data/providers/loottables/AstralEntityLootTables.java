package com.alan19.astral.data.providers.loottables;

import com.alan19.astral.Astral;
import com.alan19.astral.entity.AstralEntities;
import com.alan19.astral.items.AstralItems;
import com.google.common.collect.ImmutableList;
import net.minecraft.data.loot.EntityLootTables;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;
import net.minecraft.world.storage.loot.functions.LootingEnchantBonus;
import net.minecraft.world.storage.loot.functions.SetCount;

import javax.annotation.Nonnull;

public class AstralEntityLootTables extends EntityLootTables {
    @Override
    protected void addTables() {
        registerLootTable(AstralEntities.CRYSTAL_SPIDER.get(), new LootTable.Builder()
                .addLootPool(LootPool.builder()
                        .rolls(ConstantRange.of(1))
                        .addEntry(ItemLootEntry.builder(AstralItems.DREAMCORD.get())
                                .acceptFunction(SetCount.builder(RandomValueRange.of(0, 2)))
                                .acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0, 1)))))
                .addLootPool(LootPool.builder()
                        .rolls(ConstantRange.of(1))
                        .addEntry(ItemLootEntry.builder(AstralItems.CRYSTAL_CHITIN.get())
                                .acceptFunction(SetCount.builder(RandomValueRange.of(-1, 1)))
                                .acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0, 1)))
                                .acceptCondition(KilledByPlayer.builder()))));
        registerLootTable(new ResourceLocation(Astral.MOD_ID, "inject/phantom"), new LootTable.Builder()
                .addLootPool(LootPool.builder()
                        .rolls(ConstantRange.of(1))
                        .addEntry(ItemLootEntry.builder(AstralItems.PHANTOM_EDGE.get())
                                .acceptFunction(SetCount.builder(RandomValueRange.of(0, 2)))
                                .acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0, 1)))))
                .addLootPool(LootPool.builder()
                        .rolls(ConstantRange.of(1))
                        .addEntry(ItemLootEntry.builder(AstralItems.SLEEPLESS_EYE.get())
                                .acceptFunction(SetCount.builder(RandomValueRange.of(0, 1)))
                                .acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0, 1)))))
        );
    }

    @Override
    @Nonnull
    protected Iterable<EntityType<?>> getKnownEntities() {
        return ImmutableList.of(AstralEntities.CRYSTAL_SPIDER.get());
    }
}
