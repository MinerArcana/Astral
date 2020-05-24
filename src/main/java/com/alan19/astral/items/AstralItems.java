package com.alan19.astral.items;

import com.alan19.astral.Astral;
import com.alan19.astral.blocks.AstralBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.alan19.astral.blocks.AstralBlocks.*;

public class AstralItems {
    //Astral ItemGroup using  a Snowberry Bush as an icon
    public static final ItemGroup ASTRAL_ITEMS = new ItemGroup("astral") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(SNOWBERRY_BUSH.get());
        }
    };

    //Items
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Astral.MOD_ID);
    public static final RegistryObject<BlockNamedItem> ETHER_GRASS_ITEM = ITEMS.register("ether_grass", () -> convertToBlockItem(ETHER_GRASS.get()));
    public static final RegistryObject<BlockNamedItem> ETHER_DIRT_ITEM = ITEMS.register("ether_dirt", () -> convertToBlockItem(ETHER_DIRT.get()));
    public static final RegistryObject<BlockNamedItem> OFFERING_BRAZIER_ITEM = ITEMS.register("offering_brazier", () -> convertToBlockItem(OFFERING_BRAZIER.get()));
    public static final RegistryObject<IntrospectionMedicine> INTROSPECTION_MEDICINE = ITEMS.register("introspection_medicine", IntrospectionMedicine::new);
    public static final RegistryObject<EnlightenmentKey> ENLIGHTENMENT_KEY = ITEMS.register("enlightenment_key", EnlightenmentKey::new);
    public static final RegistryObject<TravelingMedicine> TRAVELING_MEDICINE = ITEMS.register("traveling_medicine", TravelingMedicine::new);
    public static final RegistryObject<Snowberry> SNOWBERRY = ITEMS.register("snowberry", Snowberry::new);
    public static final RegistryObject<FeverweedItem> FEVERWEED = ITEMS.register("feverweed", FeverweedItem::new);
    public static final RegistryObject<BlockNamedItem> ETHERIC_POWDER_ITEM = ITEMS.register("etheric_powder", () -> convertToBlockItem(ETHERIC_POWDER.get()));
    public static final RegistryObject<BlockNamedItem> ETHEREAL_LEAVES_ITEM = ITEMS.register("ethereal_leaves", () -> convertToBlockItem(ETHEREAL_LEAVES.get()));
    public static final RegistryObject<BlockNamedItem> ETHEREAL_WOOD_ITEM = ITEMS.register("ethereal_wood", () -> convertToBlockItem(ETHEREAL_WOOD.get()));
    public static final RegistryObject<BlockNamedItem> CYAN_CYST_ITEM = ITEMS.register("ethereal_fern", () -> convertToBlockItem(CYAN_CYST.get()));
    public static final RegistryObject<BlockNamedItem> LARGE_CYAN_CYST_ITEM = ITEMS.register("large_ethereal_fern", () -> convertToBlockItem(AstralBlocks.LARGE_CYAN_CYST.get()));
    public static final RegistryObject<BlockNamedItem> CYAN_SWARD_ITEM = ITEMS.register("ethereal_grass", () -> convertToBlockItem(AstralBlocks.CYAN_SWARD.get()));
    public static final RegistryObject<BlockNamedItem> TALL_CYAN_SWARD_ITEM = ITEMS.register("tall_ethereal_grass", () -> convertToBlockItem(TALL_CYAN_SWARD.get()));
    public static final RegistryObject<BlockNamedItem> CYAN_BELLEVINE_ITEM = ITEMS.register("cyan_bellevine", () -> convertToBlockItem(CYAN_BELLEVINE.get()));
    public static final RegistryObject<BlockNamedItem> CYAN_BLISTERWART_ITEM = ITEMS.register("cyan_blisterwat", () -> convertToBlockItem(CYAN_BLISTERWART.get()));
    public static final RegistryObject<BlockNamedItem> CYAN_KLORID_ITEM = ITEMS.register("cyan_klorid", () -> convertToBlockItem(CYAN_KLORID.get()));
    public static final RegistryObject<BlockNamedItem> CYAN_MORKEL_ITEM = ITEMS.register("cyan_morkel", () -> convertToBlockItem(CYAN_MORKEL.get()));
    public static final RegistryObject<BlockNamedItem> CYAN_PODS_ITEM = ITEMS.register("cyan_pods", () -> convertToBlockItem(CYAN_PODS.get()));
    public static final RegistryObject<Item> METAPHORIC_BONE = ITEMS.register("metaphoric_bone", () -> new Item(new Item.Properties().group(ASTRAL_ITEMS)));
    public static final RegistryObject<BlockNamedItem> ETHEREAL_PLANKS_ITEM = ITEMS.register("ethereal_planks", () -> convertToBlockItem(ETHEREAL_PLANKS.get()));
    public static final RegistryObject<BlockNamedItem> ETHEREAL_DOOR_ITEM = ITEMS.register("ethereal_door", () -> convertToBlockItem(ETHEREAL_DOOR.get()));
    public static final RegistryObject<BlockNamedItem> ETHEREAL_TRAPDOOR_ITEM = ITEMS.register("ethereal_trapdoor", () -> convertToBlockItem(ETHEREAL_TRAPDOOR.get()));
    public static final RegistryObject<BlockNamedItem> STRIPPED_ETHEREAL_LOG_ITEM = ITEMS.register("stripped_ethereal_log", () -> convertToBlockItem(STRIPPED_ETHEREAL_LOG.get()));
    public static final RegistryObject<BlockNamedItem> STRIPPED_ETHEREAL_WOOD_ITEM = ITEMS.register("stripped_ethereal_wood", () -> convertToBlockItem(STRIPPED_ETHEREAL_WOOD.get()));
    public static final RegistryObject<Item> DREAMCORD = ITEMS.register("dreamcord", () -> new Item(new Item.Properties().group(ASTRAL_ITEMS)));
    public static final RegistryObject<Item> DREAMWEAVE = ITEMS.register("dreamweave", () -> new Item(new Item.Properties().group(ASTRAL_ITEMS)));
    public static final RegistryObject<BlockNamedItem> COMFORTABLE_CUSHION_ITEM = ITEMS.register("comfortable_cushion", () -> convertToBlockItem(COMFORTABLE_CUSHION.get()));
    public static final RegistryObject<BlockNamedItem> ETHEREAL_SAPLING_ITEM = ITEMS.register("ethereal_sapling", () -> convertToBlockItem(ETHEREAL_SAPLING.get()));

    /**
     * Converts a block into a BlockNamedItem that belongs to the Astral tab
     *
     * @param block The block to be converted
     * @return A converted block
     */
    private static BlockNamedItem convertToBlockItem(Block block) {
        return new BlockNamedItem(block, new Item.Properties().group(ASTRAL_ITEMS));
    }

    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
    }

}
