package com.alan19.astral.data.providers.loottables;

import com.alan19.astral.Astral;
import com.alan19.astral.effects.AstralEffects;
import com.alan19.astral.entity.AstralEntities;
import com.alan19.astral.items.AstralItems;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.MobEffectsPredicate;
import net.minecraft.data.loot.EntityLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import javax.annotation.Nonnull;
import java.util.List;

public class AstralEntityLootTables extends EntityLoot {

    public static final LootItemCondition.Builder KILLER_PLAYER_HAS_ASTRAL_TRAVEL = LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.KILLER_PLAYER, EntityPredicate.Builder.entity().effects(MobEffectsPredicate.effects().and(AstralEffects.ASTRAL_TRAVEL.get())));

    @Override
    protected void addTables() {
        add(AstralEntities.CRYSTAL_SPIDER.get(), new LootTable.Builder()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(AstralItems.DREAMCORD.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2)))
                                .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, 1)))))
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(AstralItems.CRYSTAL_CHITIN.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(-1, 1)))
                                .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, 1)))
                                .when(KILLER_PLAYER_HAS_ASTRAL_TRAVEL))));
        add(new ResourceLocation(Astral.MOD_ID, "inject/phantom"), new LootTable.Builder()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(AstralItems.PHANTOM_EDGE.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2)))
                                .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, 1)))
                                .when(KILLER_PLAYER_HAS_ASTRAL_TRAVEL)))
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(AstralItems.SLEEPLESS_EYE.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1)))
                                .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, 1)))
                                .when(KILLER_PLAYER_HAS_ASTRAL_TRAVEL)))
        );
    }

    @Override
    @Nonnull
    protected Iterable<EntityType<?>> getKnownEntities() {
        return List.of(AstralEntities.CRYSTAL_SPIDER.get());
    }
}
