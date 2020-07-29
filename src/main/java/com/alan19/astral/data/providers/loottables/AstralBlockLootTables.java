package com.alan19.astral.data.providers.loottables;

import com.alan19.astral.items.AstralItems;
import com.alan19.astral.util.Constants;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.BlockStateProperty;
import net.minecraft.world.storage.loot.conditions.Inverted;
import net.minecraft.world.storage.loot.conditions.SurvivesExplosion;
import net.minecraft.world.storage.loot.conditions.TableBonus;
import net.minecraft.world.storage.loot.functions.ApplyBonus;
import net.minecraft.world.storage.loot.functions.ExplosionDecay;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

import static com.alan19.astral.blocks.AstralBlocks.*;


public class AstralBlockLootTables extends BlockLootTables {

    @Override
    protected void addTables() {
        registerLootTable(REDBULB.get(), onlyWithSilkTouchOrShears(REDBULB.get()));
        registerLootTable(CYANGRASS.get(), onlyWithSilkTouchOrShears(CYANGRASS.get()));
        registerLootTable(GENTLEGRASS.get(), onlyWithSilkTouchOrShears(GENTLEGRASS.get()));
        registerLootTable(WILDWEED.get(), onlyWithSilkTouchOrShears(WILDWEED.get()));
        registerLootTable(TALL_REDBULB.get(), onlyWithSilkTouchOrShears(TALL_REDBULB.get()));
        registerLootTable(TALL_CYANGRASS.get(), onlyWithSilkTouchOrShears(TALL_CYANGRASS.get()));
        registerLootTable(TALL_GENTLEGRASS.get(), onlyWithSilkTouchOrShears(TALL_GENTLEGRASS.get()));
        registerLootTable(TALL_WILDWEED.get(), onlyWithSilkTouchOrShears(TALL_WILDWEED.get()));
        registerLootTable(BLUECAP_MUSHROOM.get(), dropping(BLUECAP_MUSHROOM.get()));
        registerLootTable(RUSTCAP_MUSHROOM.get(), dropping(RUSTCAP_MUSHROOM.get()));
        registerLootTable(ETHEREAL_PLANKS.get(), dropping(ETHEREAL_PLANKS.get()));
        registerLootTable(ETHEREAL_TRAPDOOR.get(), dropping(ETHEREAL_TRAPDOOR.get()));
        registerLootTable(ETHEREAL_DOOR.get(), dropping(ETHEREAL_DOOR.get()));
        registerLootTable(COMFORTABLE_CUSHION.get(), dropping(COMFORTABLE_CUSHION.get()));
        registerLootTable(ETHEREAL_SAPLING.get(), dropping(ETHEREAL_SAPLING.get()));
        registerLootTable(CRYSTAL_WEB.get(), droppingWithSilkTouchOrShears(CRYSTAL_WEB.get(), ItemLootEntry.builder(AstralItems.DREAMCORD.get())));
        this.registerLootTable(SNOWBERRY_BUSH.get(), new LootTable.Builder()
                .addLootPool(new LootPool.Builder()
                        .name("ripe")
                        .addEntry(ItemLootEntry.builder(AstralItems.SNOWBERRY.get()))
                        .acceptCondition(BlockStateProperty.builder(SNOWBERRY_BUSH.get())
                                .fromProperties(StatePropertiesPredicate.Builder.newBuilder().withIntProp(SweetBerryBushBlock.AGE, 3)))
                        .acceptFunction(SetCount.builder(RandomValueRange.of(2, 3)))
                        .acceptFunction(ApplyBonus.uniformBonusCount(Enchantments.FORTUNE, 1)))
                .addLootPool(new LootPool.Builder()
                        .name("unripe")
                        .addEntry(ItemLootEntry.builder(AstralItems.SNOWBERRY.get()))
                        .acceptCondition(BlockStateProperty.builder(SNOWBERRY_BUSH.get())
                                .fromProperties(StatePropertiesPredicate.Builder.newBuilder().withIntProp(SweetBerryBushBlock.AGE, 2)))
                        .acceptFunction(SetCount.builder(RandomValueRange.of(1, 2)))
                        .acceptFunction(ApplyBonus.uniformBonusCount(Enchantments.FORTUNE, 1)))
                .addLootPool(new LootPool.Builder()
                        .name("not_grown")
                        .addEntry(ItemLootEntry.builder(AstralItems.SNOWBERRY.get()))
                        .acceptCondition(BlockStateProperty.builder(SNOWBERRY_BUSH.get())
                                .fromProperties(StatePropertiesPredicate.Builder.newBuilder().withIntProp(SweetBerryBushBlock.AGE, 1)))
                        .acceptFunction(SetCount.builder(RandomValueRange.of(1, 1)))
                        .acceptFunction(ApplyBonus.uniformBonusCount(Enchantments.FORTUNE, 1)))
                .addLootPool(new LootPool.Builder()
                        .name("planted")
                        .addEntry(ItemLootEntry.builder(AstralItems.SNOWBERRY.get()))
                        .acceptCondition(BlockStateProperty.builder(SNOWBERRY_BUSH.get())
                                .fromProperties(StatePropertiesPredicate.Builder.newBuilder().withIntProp(SweetBerryBushBlock.AGE, 0)))
                        .acceptFunction(SetCount.builder(RandomValueRange.of(1, 1)))
                        .acceptFunction(ApplyBonus.uniformBonusCount(Enchantments.FORTUNE, 1)))
                .acceptFunction(ExplosionDecay.builder())
        );
        registerLootTable(FEVERWEED_BLOCK.get(), dropping(FEVERWEED_BLOCK.get()));
        registerLootTable(OFFERING_BRAZIER.get(), dropping(OFFERING_BRAZIER.get()));
        registerLootTable(ETHER_DIRT.get(), dropping(ETHER_DIRT.get()));
        registerLootTable(ETHER_GRASS.get(), droppingWithSilkTouch(ETHER_GRASS.get(), ETHER_DIRT.get()));
        registerLootTable(ETHEREAL_SAPLING.get(), dropping(ETHEREAL_SAPLING.get()));
        registerLootTable(ETHEREAL_DOOR.get(), dropping(ETHEREAL_DOOR.get()));
        registerLootTable(ETHEREAL_TRAPDOOR.get(), dropping(ETHEREAL_TRAPDOOR.get()));
        registerLootTable(ETHEREAL_LOG.get(), dropping(ETHEREAL_LOG.get()));
        registerLootTable(ETHEREAL_LEAVES.get(), new LootTable.Builder()
                .addLootPool(new LootPool.Builder()
                        .rolls(ConstantRange.of(1))
                        .addEntry(AlternativesLootEntry.builder(ItemLootEntry.builder(ETHEREAL_LEAVES.get())
                                        .acceptCondition(Constants.SILK_TOUCH_OR_SHEARS),
                                ItemLootEntry.builder(AstralItems.ETHEREAL_SAPLING_ITEM.get())
                                        .acceptCondition(SurvivesExplosion.builder())
                                        .acceptCondition(TableBonus.builder(Enchantments.FORTUNE, .05f, .0625f, 0.083333336f, 0.1f)))))
                .addLootPool(new LootPool.Builder()
                        .rolls(ConstantRange.of(1))
                        .addEntry(ItemLootEntry.builder(AstralItems.METAPHORIC_BONE.get())
                                .acceptCondition(TableBonus.builder(Enchantments.FORTUNE, 0.02f, 0.022222223f, 0.025f, 0.033333335f, 0.1f))
                                .acceptFunction(SetCount.builder(RandomValueRange.of(1, 2)))
                                .acceptFunction(ExplosionDecay.builder()))
                        .addEntry(ItemLootEntry.builder(AstralItems.METAPHORIC_FLESH.get())
                                .acceptCondition(TableBonus.builder(Enchantments.FORTUNE, 0.02f, 0.022222223f, 0.025f, 0.033333335f, 0.1f))
                                .acceptFunction(SetCount.builder(RandomValueRange.of(1, 2)))
                                .acceptFunction(ExplosionDecay.builder()))
                        .acceptCondition(Inverted.builder(Constants.SILK_TOUCH_OR_SHEARS))));
        registerLootTable(ETHERIC_POWDER.get(), dropping(ETHERIC_POWDER.get()));
        registerLootTable(STRIPPED_ETHEREAL_LOG.get(), dropping(STRIPPED_ETHEREAL_LOG.get()));
        registerLootTable(STRIPPED_ETHEREAL_WOOD.get(), dropping(STRIPPED_ETHEREAL_WOOD.get()));
        registerLootTable(ETHEREAL_WOOD.get(), dropping(ETHEREAL_WOOD.get()));
        registerLootTable(INDEX_OF_KNOWLEDGE.get(), dropping(INDEX_OF_KNOWLEDGE.get()));
        registerLootTable(METAPHORIC_STONE.get(), droppingWithSilkTouch(METAPHORIC_STONE.get(), METAPHORIC_BONE_BLOCK.get()));
        registerLootTable(METAPHORIC_BONE_BLOCK.get(), dropping(METAPHORIC_BONE_BLOCK.get()));
        registerLootTable(METAPHORIC_FLESH_BLOCK.get(), dropping(METAPHORIC_FLESH_BLOCK.get()));
    }

    @Override
    @Nonnull
    protected Iterable<Block> getKnownBlocks() {
        return BLOCKS.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList());
    }

    private LootTable.Builder onlyWithSilkTouchOrShears(Block block) {
        return LootTable.builder()
                .addLootPool(LootPool.builder()
                        .rolls(ConstantRange.of(1))
                        .acceptCondition(Constants.SILK_TOUCH_OR_SHEARS)
                        .addEntry(ItemLootEntry.builder(block)));
    }
}
