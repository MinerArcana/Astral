package com.alan19.astral.items;

import com.alan19.astral.Astral;
import com.alan19.astral.blocks.AstralBlocks;
import com.alan19.astral.items.tools.*;
import com.alan19.astral.util.Constants;
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
            return Constants.getAstronomicon();
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
    public static final RegistryObject<BlockNamedItem> REDBULB_ITEM = ITEMS.register("redbulb", () -> convertToBlockItem(REDBULB.get()));
    public static final RegistryObject<BlockNamedItem> CYANGRASS_ITEM = ITEMS.register("cyangrass", () -> convertToBlockItem(CYANGRASS.get()));
    public static final RegistryObject<BlockNamedItem> GENTLEGRASS_ITEM = ITEMS.register("gentlegrass", () -> convertToBlockItem(GENTLEGRASS.get()));
    public static final RegistryObject<BlockNamedItem> WILDWEED_ITEM = ITEMS.register("wildweed", () -> convertToBlockItem(AstralBlocks.WILDWEED.get()));
    public static final RegistryObject<BlockNamedItem> TALL_REDBULB_ITEM = ITEMS.register("tall_redbulb", () -> convertToBlockItem(TALL_REDBULB.get()));
    public static final RegistryObject<BlockNamedItem> TALL_CYANGRASS_ITEM = ITEMS.register("tall_cyangrass", () -> convertToBlockItem(TALL_CYANGRASS.get()));
    public static final RegistryObject<BlockNamedItem> TALL_GENTLEGRASS_ITEM = ITEMS.register("tall_gentlegrass", () -> convertToBlockItem(TALL_GENTLEGRASS.get()));
    public static final RegistryObject<BlockNamedItem> TALL_WILDWEED_ITEM = ITEMS.register("tall_wildweed", () -> convertToBlockItem(TALL_WILDWEED.get()));
    public static final RegistryObject<BlockNamedItem> BLUECAP_MUSHROOM_ITEM = ITEMS.register("bluecap_mushroom", () -> convertToBlockItem(BLUECAP_MUSHROOM.get()));
    public static final RegistryObject<BlockNamedItem> RUSTCAP_MUSHROOM_ITEM = ITEMS.register("rustcap_mushroom", () -> convertToBlockItem(RUSTCAP_MUSHROOM.get()));
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
    public static final RegistryObject<BlockNamedItem> INDEX_OF_KNOWLEDGE_ITEM = ITEMS.register("index_of_knowledge", () -> convertToBlockItem(INDEX_OF_KNOWLEDGE.get()));
    public static final RegistryObject<MetaphoricFlesh> METAPHORIC_FLESH = ITEMS.register("metaphoric_flesh", MetaphoricFlesh::new);
    public static final RegistryObject<SleeplessEye> SLEEPLESS_EYE = ITEMS.register("sleepless_eye", SleeplessEye::new);
    public static final RegistryObject<CrystalChitin> CRYSTAL_CHITIN = ITEMS.register("crystal_chitin", CrystalChitin::new);
    public static final RegistryObject<BlockNamedItem> METAPHORIC_STONE_ITEM = ITEMS.register("metaphoric_stone", () -> convertToBlockItem(METAPHORIC_STONE.get()));
    public static final RegistryObject<BlockNamedItem> BONE_SHEETS_ITEM = ITEMS.register("bone_sheets", () -> convertToBlockItem(METAPHORIC_BONE_BLOCK.get()));
    public static final RegistryObject<BlockNamedItem> METAPHORIC_FLESH_BLOCK_ITEM = ITEMS.register("metaphoric_flesh_block", () -> convertToBlockItem(METAPHORIC_FLESH_BLOCK.get()));
    public static final RegistryObject<BlockNamedItem> CRYSTAL_WEB_ITEM = ITEMS.register("crystal_web", () -> convertToBlockItem(CRYSTAL_WEB.get()));
    public static final RegistryObject<PhantomEdge> PHANTOM_EDGE = ITEMS.register("phantom_edge", PhantomEdge::new);
    public static final RegistryObject<PhantasmalSword> PHANTASMAL_SWORD = ITEMS.register("phantasmal_sword", PhantasmalSword::new);
    public static final RegistryObject<PhantasmalPickaxe> PHANTASMAL_PICKAXE = ITEMS.register("phantasmal_pickaxe", PhantasmalPickaxe::new);
    public static final RegistryObject<PhantasmalShovel> PHANTASMAL_SHOVEL = ITEMS.register("phantasmal_shovel", PhantasmalShovel::new);
    public static final RegistryObject<PhantasmalAxe> PHANTASMAL_AXE = ITEMS.register("phantasmal_axe", PhantasmalAxe::new);
    public static final RegistryObject<PhantasmalShears> PHANTASMAL_SHEARS = ITEMS.register("phantasmal_shears", PhantasmalShears::new);

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
