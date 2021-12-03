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

    public static final ILootCondition.IBuilder KILLER_PLAYER_HAS_ASTRAL_TRAVEL = EntityHasProperty.hasProperties(LootContext.EntityTarget.KILLER_PLAYER, EntityPredicate.Builder.entity().effects(MobEffectsPredicate.effects().and(AstralEffects.ASTRAL_TRAVEL.get())));

    @Override
    protected void addTables() {
        add(AstralEntities.CRYSTAL_SPIDER.get(), new LootTable.Builder()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantRange.exactly(1))
                        .add(ItemLootEntry.lootTableItem(AstralItems.DREAMCORD.get())
                                .apply(SetCount.setCount(RandomValueRange.between(0, 2)))
                                .apply(LootingEnchantBonus.lootingMultiplier(RandomValueRange.between(0, 1)))))
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantRange.exactly(1))
                        .add(ItemLootEntry.lootTableItem(AstralItems.CRYSTAL_CHITIN.get())
                                .apply(SetCount.setCount(RandomValueRange.between(-1, 1)))
                                .apply(LootingEnchantBonus.lootingMultiplier(RandomValueRange.between(0, 1)))
                                .when(KILLER_PLAYER_HAS_ASTRAL_TRAVEL))));
        add(new ResourceLocation(Astral.MOD_ID, "inject/phantom"), new LootTable.Builder()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantRange.exactly(1))
                        .add(ItemLootEntry.lootTableItem(AstralItems.PHANTOM_EDGE.get())
                                .apply(SetCount.setCount(RandomValueRange.between(0, 2)))
                                .apply(LootingEnchantBonus.lootingMultiplier(RandomValueRange.between(0, 1)))
                                .when(KILLER_PLAYER_HAS_ASTRAL_TRAVEL)))
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantRange.exactly(1))
                        .add(ItemLootEntry.lootTableItem(AstralItems.SLEEPLESS_EYE.get())
                                .apply(SetCount.setCount(RandomValueRange.between(0, 1)))
                                .apply(LootingEnchantBonus.lootingMultiplier(RandomValueRange.between(0, 1)))
                                .when(KILLER_PLAYER_HAS_ASTRAL_TRAVEL)))
        );
    }

    @Override
    @Nonnull
    protected Iterable<EntityType<?>> getKnownEntities() {
        return ImmutableList.of(AstralEntities.CRYSTAL_SPIDER.get());
    }
}
