package com.alan19.astral.data.providers.loottables;

import com.alan19.astral.items.AstralItems;
import com.alan19.astral.util.Constants;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.loot.*;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.storage.loot.ConstantIntValue;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.RandomValueBounds;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

import static com.alan19.astral.blocks.AstralBlocks.*;

public class AstralBlockLootTables extends BlockLoot {

    @Override
    protected void addTables() {
        add(REDBULB.get(), onlyWithSilkTouchOrShears(REDBULB.get()));
        add(CYANGRASS.get(), onlyWithSilkTouchOrShears(CYANGRASS.get()));
        add(GENTLEGRASS.get(), onlyWithSilkTouchOrShears(GENTLEGRASS.get()));
        add(WILDWEED.get(), onlyWithSilkTouchOrShears(WILDWEED.get()));
        add(TALL_REDBULB.get(), onlyWithSilkTouchOrShears(TALL_REDBULB.get()));
        add(TALL_CYANGRASS.get(), onlyWithSilkTouchOrShears(TALL_CYANGRASS.get()));
        add(TALL_GENTLEGRASS.get(), onlyWithSilkTouchOrShears(TALL_GENTLEGRASS.get()));
        add(TALL_WILDWEED.get(), onlyWithSilkTouchOrShears(TALL_WILDWEED.get()));
        add(BLUECAP_MUSHROOM.get(), createSingleItemTable(BLUECAP_MUSHROOM.get()));
        add(RUSTCAP_MUSHROOM.get(), createSingleItemTable(RUSTCAP_MUSHROOM.get()));
        add(ETHEREAL_PLANKS.get(), createSingleItemTable(ETHEREAL_PLANKS.get()));
        add(ETHEREAL_TRAPDOOR.get(), createSingleItemTable(ETHEREAL_TRAPDOOR.get()));
        add(ETHEREAL_DOOR.get(), createSingleItemTable(ETHEREAL_DOOR.get()));
        add(COMFORTABLE_CUSHION.get(), createSingleItemTable(COMFORTABLE_CUSHION.get()));
        add(ETHEREAL_SAPLING.get(), createSingleItemTable(ETHEREAL_SAPLING.get()));
        add(CRYSTAL_WEB.get(), droppingWithSilkTouchOrShearsTag(CRYSTAL_WEB.get(), LootItem.lootTableItem(AstralItems.DREAMCORD.get())));
        this.add(SNOWBERRY_BUSH.get(), new LootTable.Builder()
                .withPool(new LootPool.Builder()
                        .name("ripe")
                        .add(LootItem.lootTableItem(AstralItems.SNOWBERRY.get()))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(SNOWBERRY_BUSH.get())
                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SweetBerryBushBlock.AGE, 3)))
                        .apply(SetItemCountFunction.setCount(RandomValueBounds.between(2, 3)))
                        .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE, 1)))
                .withPool(new LootPool.Builder()
                        .name("unripe")
                        .add(LootItem.lootTableItem(AstralItems.SNOWBERRY.get()))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(SNOWBERRY_BUSH.get())
                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SweetBerryBushBlock.AGE, 2)))
                        .apply(SetItemCountFunction.setCount(RandomValueBounds.between(1, 2)))
                        .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE, 1)))
                .withPool(new LootPool.Builder()
                        .name("not_grown")
                        .add(LootItem.lootTableItem(AstralItems.SNOWBERRY.get()))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(SNOWBERRY_BUSH.get())
                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SweetBerryBushBlock.AGE, 1)))
                        .apply(SetItemCountFunction.setCount(RandomValueBounds.between(1, 1)))
                        .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE, 1)))
                .withPool(new LootPool.Builder()
                        .name("planted")
                        .add(LootItem.lootTableItem(AstralItems.SNOWBERRY.get()))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(SNOWBERRY_BUSH.get())
                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SweetBerryBushBlock.AGE, 0)))
                        .apply(SetItemCountFunction.setCount(RandomValueBounds.between(1, 1)))
                        .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE, 1)))
                .apply(ApplyExplosionDecay.explosionDecay())
        );
        add(FEVERWEED_BLOCK.get(), createSingleItemTable(FEVERWEED_BLOCK.get()));
        add(OFFERING_BRAZIER.get(), createSingleItemTable(OFFERING_BRAZIER.get()));
        add(ETHER_DIRT.get(), createSingleItemTable(ETHER_DIRT.get()));
        add(ETHER_GRASS.get(), createSingleItemTableWithSilkTouch(ETHER_GRASS.get(), ETHER_DIRT.get()));
        add(ETHEREAL_SAPLING.get(), createSingleItemTable(ETHEREAL_SAPLING.get()));
        add(ETHEREAL_DOOR.get(), createSingleItemTable(ETHEREAL_DOOR.get()));
        add(ETHEREAL_TRAPDOOR.get(), createSingleItemTable(ETHEREAL_TRAPDOOR.get()));
        add(ETHEREAL_LOG.get(), createSingleItemTable(ETHEREAL_LOG.get()));
        add(ETHEREAL_LEAVES.get(), new LootTable.Builder()
                .withPool(new LootPool.Builder()
                        .setRolls(ConstantIntValue.exactly(1))
                        .add(AlternativesEntry.alternatives(LootItem.lootTableItem(ETHEREAL_LEAVES.get())
                                        .when(Constants.SILK_TOUCH_OR_SHEARS),
                                LootItem.lootTableItem(AstralItems.ETHEREAL_SAPLING_ITEM.get())
                                        .when(ExplosionCondition.survivesExplosion())
                                        .when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, .05f, .0625f, 0.083333336f, 0.1f)))))
                .withPool(new LootPool.Builder()
                        .setRolls(ConstantIntValue.exactly(1))
                        .add(LootItem.lootTableItem(AstralItems.METAPHORIC_BONE.get())
                                .when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.02f, 0.022222223f, 0.025f, 0.033333335f, 0.1f))
                                .apply(SetItemCountFunction.setCount(RandomValueBounds.between(1, 2)))
                                .apply(ApplyExplosionDecay.explosionDecay()))
                        .add(LootItem.lootTableItem(AstralItems.METAPHORIC_FLESH.get())
                                .when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.02f, 0.022222223f, 0.025f, 0.033333335f, 0.1f))
                                .apply(SetItemCountFunction.setCount(RandomValueBounds.between(1, 2)))
                                .apply(ApplyExplosionDecay.explosionDecay()))
                        .when(InvertedLootItemCondition.invert(Constants.SILK_TOUCH_OR_SHEARS))));
        add(ETHERIC_POWDER.get(), createSingleItemTable(ETHERIC_POWDER.get()));
        add(STRIPPED_ETHEREAL_LOG.get(), createSingleItemTable(STRIPPED_ETHEREAL_LOG.get()));
        add(STRIPPED_ETHEREAL_WOOD.get(), createSingleItemTable(STRIPPED_ETHEREAL_WOOD.get()));
        add(ETHEREAL_WOOD.get(), createSingleItemTable(ETHEREAL_WOOD.get()));
        add(INDEX_OF_KNOWLEDGE.get(), createSingleItemTable(INDEX_OF_KNOWLEDGE.get()));
        add(METAPHORIC_STONE.get(), createSingleItemTableWithSilkTouch(METAPHORIC_STONE.get(), METAPHORIC_BONE_BLOCK.get()));
        add(METAPHORIC_BONE_BLOCK.get(), createSingleItemTable(METAPHORIC_BONE_BLOCK.get()));
        add(METAPHORIC_FLESH_BLOCK.get(), createSingleItemTable(METAPHORIC_FLESH_BLOCK.get()));
        add(ETHEREAL_SPAWNER.get(), noDrop());
    }

    @Override
    @Nonnull
    protected Iterable<Block> getKnownBlocks() {
        return BLOCKS.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList());
    }

    private LootTable.Builder onlyWithSilkTouchOrShears(Block block) {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantIntValue.exactly(1))
                        .when(Constants.SILK_TOUCH_OR_SHEARS)
                        .add(LootItem.lootTableItem(block)));
    }

    private LootTable.Builder droppingWithSilkTouchOrShearsTag(Block block, LootPoolEntryContainer.Builder<?> drops) {
        return createSelfDropDispatchTable(block, Constants.SILK_TOUCH_OR_SHEARS, drops);
    }
}
