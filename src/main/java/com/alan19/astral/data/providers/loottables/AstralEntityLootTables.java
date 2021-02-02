package com.alan19.astral.data.providers.loottables;

import com.alan19.astral.Astral;
import com.alan19.astral.effects.AstralEffects;
import com.alan19.astral.entity.AstralEntities;
import com.alan19.astral.items.AstralItems;
import com.google.common.collect.ImmutableList;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.MobEffectsPredicate;
import net.minecraft.data.loot.EntityLootTables;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.EntityHasProperty;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.functions.LootingEnchantBonus;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class AstralEntityLootTables extends EntityLootTables {

    public static final ILootCondition.IBuilder KILLER_PLAYER_HAS_ASTRAL_TRAVEL = EntityHasProperty.builder(LootContext.EntityTarget.KILLER_PLAYER, EntityPredicate.Builder.create().effects(MobEffectsPredicate.any().addEffect(AstralEffects.ASTRAL_TRAVEL.get())));

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
                                .acceptCondition(KILLER_PLAYER_HAS_ASTRAL_TRAVEL))));
        registerLootTable(new ResourceLocation(Astral.MOD_ID, "inject/phantom"), new LootTable.Builder()
                .addLootPool(LootPool.builder()
                        .rolls(ConstantRange.of(1))
                        .addEntry(ItemLootEntry.builder(AstralItems.PHANTOM_EDGE.get())
                                .acceptFunction(SetCount.builder(RandomValueRange.of(0, 2)))
                                .acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0, 1)))
                                .acceptCondition(KILLER_PLAYER_HAS_ASTRAL_TRAVEL)))
                .addLootPool(LootPool.builder()
                        .rolls(ConstantRange.of(1))
                        .addEntry(ItemLootEntry.builder(AstralItems.SLEEPLESS_EYE.get())
                                .acceptFunction(SetCount.builder(RandomValueRange.of(0, 1)))
                                .acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0, 1)))
                                .acceptCondition(KILLER_PLAYER_HAS_ASTRAL_TRAVEL)))
        );
    }

    @Override
    @Nonnull
    protected Iterable<EntityType<?>> getKnownEntities() {
        return ImmutableList.of(AstralEntities.CRYSTAL_SPIDER.get());
    }
}
