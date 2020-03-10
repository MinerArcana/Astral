package com.alan199921.astral.items;

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

    @SubscribeEvent
    public static void onItemRegistry(final RegistryEvent.Register<Item> event) {
        registerItem(event.getRegistry(), new Snowberry(), "snowberry");
        registerItem(event.getRegistry(), new Feverweed(), "feverweed");
        registerItem(event.getRegistry(), new IntrospectionMedicine(), "introspection_medicine");
        registerItem(event.getRegistry(), new EnlightenmentKey(), "enlightenment_key");
        registerItem(event.getRegistry(), new TravelingMedicine(), "traveling_medicine");
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
