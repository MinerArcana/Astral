package com.alan199921.astral.items;

import com.alan199921.astral.Astral;
import com.alan199921.astral.blocks.AstralBlocks;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstralItems {

    @ObjectHolder("astral:snowberry")
    public static final Item SNOWBERRY = null;

    @ObjectHolder("astral:feverweed")
    public static final Item FEVERWEED = null;

    @ObjectHolder("astral:introspection_medicine")
    public static final Item INTROSPECTION_MEDICINE = null;

    @ObjectHolder("astral:enlightenment_key")
    public static final Item ENLIGHTENMENT_KEY = null;

    @ObjectHolder("astral:traveling_medicine")
    public static final Item TRAVELING_MEDICINE = null;

    @ObjectHolder("astral:offering_brazier")
    public static final Item OFFERING_BRAZIER_ITEM = null;

    @ObjectHolder("astral:ether_dirt")
    public static final Item ETHER_DIRT_ITEM = null;

    @ObjectHolder("astral:ether_grass")
    public static final Item ETHER_GRASS_ITEM = null;

    @SubscribeEvent
    public static void onItemRegistry(final RegistryEvent.Register<Item> event) {
        registerItem(event.getRegistry(), new Snowberry(), "snowberry");
        registerItem(event.getRegistry(), new Feverweed(), "feverweed");
        registerItem(event.getRegistry(), new IntrospectionMedicine(), "introspection_medicine");
        registerItem(event.getRegistry(), new EnlightenmentKey(), "enlightenment_key");
        registerItem(event.getRegistry(), new TravelingMedicine(), "traveling_medicine");
        registerItem(event.getRegistry(), new BlockNamedItem(AstralBlocks.OFFERING_BRAZIER, new Item.Properties().group(Astral.setup.astralItems)), "offering_brazier");
        registerItem(event.getRegistry(), new BlockNamedItem(AstralBlocks.ETHER_DIRT, new Item.Properties().group(Astral.setup.astralItems)), "ether_dirt");
        registerItem(event.getRegistry(), new BlockNamedItem(AstralBlocks.ETHER_GRASS, new Item.Properties().group(Astral.setup.astralItems)), "ether_grass");
    }

    /**
     * Registers an item
     *
     * @param registry The event registry to register the item
     * @param item     An instance of an item
     * @param name     The registry name of the item
     */
    private static void registerItem(IForgeRegistry<Item> registry, Item item, String name) {
        item.setRegistryName(name);
        registry.register(item);
    }
}
