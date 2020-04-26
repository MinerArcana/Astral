package com.alan199921.astral.items;

import com.alan199921.astral.Astral;
import com.alan199921.astral.blocks.AstralBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AstralItems {
    //Items
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Astral.MOD_ID);
    public static final RegistryObject<BlockNamedItem> ETHER_GRASS_ITEM = ITEMS.register("ether_grass", () -> convertToBlockItem(AstralBlocks.ETHER_GRASS.get()));
    public static final RegistryObject<BlockNamedItem> ETHER_DIRT_ITEM = ITEMS.register("ether_dirt", () -> convertToBlockItem(AstralBlocks.ETHER_DIRT.get()));
    public static final RegistryObject<BlockNamedItem> OFFERING_BRAZIER_ITEM = ITEMS.register("offering_brazier", () -> convertToBlockItem(AstralBlocks.OFFERING_BRAZIER.get()));
    public static final RegistryObject<IntrospectionMedicine> INTROSPECTION_MEDICINE = ITEMS.register("introspection_medicine", IntrospectionMedicine::new);
    public static final RegistryObject<EnlightenmentKey> ENLIGHTENMENT_KEY = ITEMS.register("enlightenment_key", EnlightenmentKey::new);
    public static final RegistryObject<TravelingMedicine> TRAVELING_MEDICINE = ITEMS.register("traveling_medicine", TravelingMedicine::new);
    public static final RegistryObject<Snowberry> SNOWBERRY = ITEMS.register("snowberry", Snowberry::new);
    public static final RegistryObject<Feverweed> FEVERWEED = ITEMS.register("feverweed", Feverweed::new);
    public static final RegistryObject<BlockNamedItem> ETHERIC_POWDER_ITEM = ITEMS.register("etheric_powder", () -> convertToBlockItem(AstralBlocks.ETHERIC_POWDER.get()));
    public static final RegistryObject<BlockNamedItem> ETHEREAL_LEAVES_ITEM = ITEMS.register("ethereal_leaves", () -> convertToBlockItem(AstralBlocks.ETHEREAL_LEAVES.get()));
    public static final RegistryObject<BlockNamedItem> ETHEREAL_WOOD_ITEM = ITEMS.register("ethereal_wood", () -> convertToBlockItem(AstralBlocks.ETHEREAL_WOOD.get()));
    public static final RegistryObject<BlockNamedItem> ETHEREAL_FERN_ITEM = ITEMS.register("ethereal_fern", () -> convertToBlockItem(AstralBlocks.ETHEREAL_FERN.get()));
    public static final RegistryObject<BlockNamedItem> LARGE_ETHEREAL_FERN_ITEM = ITEMS.register("large_ethereal_fern", () -> convertToBlockItem(AstralBlocks.LARGE_ETHEREAL_FERN.get()));
    public static final RegistryObject<BlockNamedItem> ETHEREAL_GRASS_ITEM = ITEMS.register("ethereal_grass", () -> convertToBlockItem(AstralBlocks.ETHEREAL_GRASS.get()));
    public static final RegistryObject<BlockNamedItem> TALL_ETHEREAL_GRASS_ITEM = ITEMS.register("tall_ethereal_grass", () -> convertToBlockItem(AstralBlocks.TALL_ETHEREAL_GRASS.get()));

    /**
     * Converts a block into a BlockNamedItem that belongs to the Astral tab
     *
     * @param block The block to be converted
     * @return A converted block
     */
    private static BlockNamedItem convertToBlockItem(Block block) {
        return new BlockNamedItem(block, new Item.Properties().group(Astral.setup.astralItems));
    }

    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
    }

}
