package com.alan19.astral.items;

import com.alan19.astral.Astral;
import com.alan19.astral.blocks.AstralBlocks;
import com.alan19.astral.items.tools.*;
import com.alan19.astral.util.Constants;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.alan19.astral.blocks.AstralBlocks.*;

public class AstralItems {
    //Astral ItemGroup using  a Snowberry Bush as an icon
    public static final CreativeModeTab ASTRAL_ITEMS = new CreativeModeTab("astral") {
        @Override
        public ItemStack makeIcon() {
            return Constants.getAstronomicon();
        }
    };

    //Items
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Astral.MOD_ID);
    public static final RegistryObject<ItemNameBlockItem> ETHER_GRASS_ITEM = ITEMS.register("ether_grass", () -> convertToBlockItem(ETHER_GRASS.get()));
    public static final RegistryObject<ItemNameBlockItem> ETHER_DIRT_ITEM = ITEMS.register("ether_dirt", () -> convertToBlockItem(ETHER_DIRT.get()));
    public static final RegistryObject<ItemNameBlockItem> OFFERING_BRAZIER_ITEM = ITEMS.register("offering_brazier", () -> convertToBlockItem(OFFERING_BRAZIER.get()));
    public static final RegistryObject<IntrospectionMedicine> INTROSPECTION_MEDICINE = ITEMS.register("introspection_medicine", IntrospectionMedicine::new);
    public static final RegistryObject<EnlightenmentKey> ENLIGHTENMENT_KEY = ITEMS.register("key_of_enlightenment", EnlightenmentKey::new);
    public static final RegistryObject<TravelingMedicine> TRAVELING_MEDICINE = ITEMS.register("traveling_medicine", TravelingMedicine::new);
    public static final RegistryObject<Snowberry> SNOWBERRY = ITEMS.register("snowberry", Snowberry::new);
    public static final RegistryObject<FeverweedItem> FEVERWEED = ITEMS.register("feverweed", FeverweedItem::new);
    public static final RegistryObject<ItemNameBlockItem> ETHERIC_POWDER_ITEM = ITEMS.register("etheric_powder", () -> convertToBlockItem(ETHERIC_POWDER.get()));
    public static final RegistryObject<ItemNameBlockItem> ETHEREAL_LEAVES_ITEM = ITEMS.register("ethereal_leaves", () -> convertToBlockItem(ETHEREAL_LEAVES.get()));
    public static final RegistryObject<ItemNameBlockItem> ETHEREAL_LOG_ITEM = ITEMS.register("ethereal_log", () -> convertToBlockItem(ETHEREAL_LOG.get()));
    public static final RegistryObject<ItemNameBlockItem> REDBULB_ITEM = ITEMS.register("redbulb", () -> convertToBlockItem(REDBULB.get()));
    public static final RegistryObject<ItemNameBlockItem> CYANGRASS_ITEM = ITEMS.register("cyangrass", () -> convertToBlockItem(CYANGRASS.get()));
    public static final RegistryObject<ItemNameBlockItem> GENTLEGRASS_ITEM = ITEMS.register("gentlegrass", () -> convertToBlockItem(GENTLEGRASS.get()));
    public static final RegistryObject<ItemNameBlockItem> WILDWEED_ITEM = ITEMS.register("wildweed", () -> convertToBlockItem(AstralBlocks.WILDWEED.get()));
    public static final RegistryObject<ItemNameBlockItem> TALL_REDBULB_ITEM = ITEMS.register("tall_redbulb", () -> convertToBlockItem(TALL_REDBULB.get()));
    public static final RegistryObject<ItemNameBlockItem> TALL_CYANGRASS_ITEM = ITEMS.register("tall_cyangrass", () -> convertToBlockItem(TALL_CYANGRASS.get()));
    public static final RegistryObject<ItemNameBlockItem> TALL_GENTLEGRASS_ITEM = ITEMS.register("tall_gentlegrass", () -> convertToBlockItem(TALL_GENTLEGRASS.get()));
    public static final RegistryObject<ItemNameBlockItem> TALL_WILDWEED_ITEM = ITEMS.register("tall_wildweed", () -> convertToBlockItem(TALL_WILDWEED.get()));
    public static final RegistryObject<ItemNameBlockItem> BLUECAP_MUSHROOM_ITEM = ITEMS.register("bluecap_mushroom", () -> convertToBlockItem(BLUECAP_MUSHROOM.get()));
    public static final RegistryObject<ItemNameBlockItem> RUSTCAP_MUSHROOM_ITEM = ITEMS.register("rustcap_mushroom", () -> convertToBlockItem(RUSTCAP_MUSHROOM.get()));
    public static final RegistryObject<Item> METAPHORIC_BONE = ITEMS.register("metaphoric_bone", () -> new Item(new Item.Properties().tab(ASTRAL_ITEMS)));
    public static final RegistryObject<ItemNameBlockItem> ETHEREAL_PLANKS_ITEM = ITEMS.register("ethereal_planks", () -> convertToBlockItem(ETHEREAL_PLANKS.get()));
    public static final RegistryObject<ItemNameBlockItem> ETHEREAL_WOOD_ITEM = ITEMS.register("ethereal_wood", () -> convertToBlockItem(ETHEREAL_WOOD.get()));
    public static final RegistryObject<ItemNameBlockItem> ETHEREAL_DOOR_ITEM = ITEMS.register("ethereal_door", () -> convertToBlockItem(ETHEREAL_DOOR.get()));
    public static final RegistryObject<ItemNameBlockItem> ETHEREAL_TRAPDOOR_ITEM = ITEMS.register("ethereal_trapdoor", () -> convertToBlockItem(ETHEREAL_TRAPDOOR.get()));
    public static final RegistryObject<ItemNameBlockItem> STRIPPED_ETHEREAL_LOG_ITEM = ITEMS.register("stripped_ethereal_log", () -> convertToBlockItem(STRIPPED_ETHEREAL_LOG.get()));
    public static final RegistryObject<ItemNameBlockItem> STRIPPED_ETHEREAL_WOOD_ITEM = ITEMS.register("stripped_ethereal_wood", () -> convertToBlockItem(STRIPPED_ETHEREAL_WOOD.get()));
    public static final RegistryObject<Item> DREAMCORD = ITEMS.register("dreamcord", () -> new Item(new Item.Properties().tab(ASTRAL_ITEMS)));
    public static final RegistryObject<Item> DREAMWEAVE = ITEMS.register("dreamweave", () -> new Item(new Item.Properties().tab(ASTRAL_ITEMS)));
    public static final RegistryObject<ItemNameBlockItem> COMFORTABLE_CUSHION_ITEM = ITEMS.register("comfortable_cushion", () -> convertToBlockItem(COMFORTABLE_CUSHION.get()));
    public static final RegistryObject<ItemNameBlockItem> ETHEREAL_SAPLING_ITEM = ITEMS.register("ethereal_sapling", () -> convertToBlockItem(ETHEREAL_SAPLING.get()));
    public static final RegistryObject<ItemNameBlockItem> INDEX_OF_KNOWLEDGE_ITEM = ITEMS.register("index_of_knowledge", () -> convertToBlockItem(INDEX_OF_KNOWLEDGE.get()));
    public static final RegistryObject<MetaphoricFlesh> METAPHORIC_FLESH = ITEMS.register("metaphoric_flesh", MetaphoricFlesh::new);
    public static final RegistryObject<SleeplessEye> SLEEPLESS_EYE = ITEMS.register("sleepless_eye", SleeplessEye::new);
    public static final RegistryObject<CrystalChitin> CRYSTAL_CHITIN = ITEMS.register("crystal_chitin", CrystalChitin::new);
    public static final RegistryObject<ItemNameBlockItem> METAPHORIC_STONE_ITEM = ITEMS.register("metaphoric_stone", () -> convertToBlockItem(METAPHORIC_STONE.get()));
    public static final RegistryObject<ItemNameBlockItem> METAPHORIC_BONE_BLOCK_ITEM = ITEMS.register("metaphoric_bone_block", () -> convertToBlockItem(METAPHORIC_BONE_BLOCK.get()));
    public static final RegistryObject<ItemNameBlockItem> METAPHORIC_FLESH_BLOCK_ITEM = ITEMS.register("metaphoric_flesh_block", () -> convertToBlockItem(METAPHORIC_FLESH_BLOCK.get()));
    public static final RegistryObject<ItemNameBlockItem> CRYSTAL_WEB_ITEM = ITEMS.register("crystal_web", () -> convertToBlockItem(CRYSTAL_WEB.get()));
    public static final RegistryObject<PhantomEdge> PHANTOM_EDGE = ITEMS.register("phantom_edge", PhantomEdge::new);
    public static final RegistryObject<PhantasmalSword> PHANTASMAL_SWORD = ITEMS.register("phantasmal_sword", PhantasmalSword::new);
    public static final RegistryObject<PhantasmalPickaxe> PHANTASMAL_PICKAXE = ITEMS.register("phantasmal_pickaxe", PhantasmalPickaxe::new);
    public static final RegistryObject<PhantasmalShovel> PHANTASMAL_SHOVEL = ITEMS.register("phantasmal_shovel", PhantasmalShovel::new);
    public static final RegistryObject<PhantasmalAxe> PHANTASMAL_AXE = ITEMS.register("phantasmal_axe", PhantasmalAxe::new);
    public static final RegistryObject<PhantasmalShears> PHANTASMAL_SHEARS = ITEMS.register("phantasmal_shears", PhantasmalShears::new);
    public static final RegistryObject<ItemNameBlockItem> ETHEREAL_SPAWNER_ITEM = ITEMS.register("ethereal_spawner", () -> convertToBlockItem(ETHEREAL_SPAWNER.get()));

    /**
     * Converts a block into a BlockTagKeyItem that belongs to the Astral tab
     *
     * @param block The block to be converted
     * @return A converted block
     */
    private static ItemNameBlockItem convertToBlockItem(Block block) {
        return new ItemNameBlockItem(block, new Item.Properties().tab(ASTRAL_ITEMS));
    }

    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
    }

}
