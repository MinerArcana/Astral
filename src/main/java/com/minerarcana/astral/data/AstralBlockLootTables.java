package com.minerarcana.astral.data;

import com.minerarcana.astral.blocks.AstralBlocks;
import com.minerarcana.astral.items.AstralItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

import static com.minerarcana.astral.blocks.AstralBlocks.FEVERWEED;

public class AstralBlockLootTables extends BlockLoot {
    @Override
    protected void addTables() {
        this.add(AstralBlocks.SNOWBERRY_BUSH.get(), new LootTable.Builder()
                .withPool(new LootPool.Builder()
                        .name("ripe")
                        .add(LootItem.lootTableItem(AstralItems.SNOWBERRIES.get()))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(AstralBlocks.SNOWBERRY_BUSH.get())
                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SweetBerryBushBlock.AGE, 3)))
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3)))
                        .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE, 1)))
                .withPool(new LootPool.Builder()
                        .name("unripe")
                        .add(LootItem.lootTableItem(AstralItems.SNOWBERRIES.get()))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(AstralBlocks.SNOWBERRY_BUSH.get())
                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SweetBerryBushBlock.AGE, 2)))
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                        .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE, 1)))
                .withPool(new LootPool.Builder()
                        .name("not_grown")
                        .add(LootItem.lootTableItem(AstralItems.SNOWBERRIES.get()))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(AstralBlocks.SNOWBERRY_BUSH.get())
                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SweetBerryBushBlock.AGE, 1)))
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1)))
                        .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE, 1)))
                .withPool(new LootPool.Builder()
                        .name("planted")
                        .add(LootItem.lootTableItem(AstralItems.SNOWBERRIES.get()))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(AstralBlocks.SNOWBERRY_BUSH.get())
                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SweetBerryBushBlock.AGE, 0)))
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1)))
                        .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE, 1)))
                .apply(ApplyExplosionDecay.explosionDecay())
        );
        add(FEVERWEED.get(), createSingleItemTable(AstralItems.FEVERWEED_ITEM.get()));
    }

    @Override
    @Nonnull
    protected Iterable<Block> getKnownBlocks() {
        return AstralBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList());
    }

}
