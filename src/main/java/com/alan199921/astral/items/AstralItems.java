package com.alan199921.astral.items;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstralItems {

    public static Item snowberry;
    public static Item feverweed;
    public static Item introspectionMedicine;
    public static Item enlightenmentKey;
    public static Item travellingMedicine;

    @SubscribeEvent
    public static void onItemRegistry(final RegistryEvent.Register<Item> event) {
        snowberry = registerItem(event.getRegistry(), new Snowberry(), "snowberry");
        feverweed = registerItem(event.getRegistry(), new Feverweed(), "feverweed");
        introspectionMedicine = registerItem(event.getRegistry(), new IntrospectionMedicine(), "introspection_medicine");
        enlightenmentKey = registerItem(event.getRegistry(), new EnlightenmentKey(), "enlightenment_key");
        travellingMedicine = registerItem(event.getRegistry(), new TravellingMedicine(), "travelling_medicine");
    }

    /**
     * Registers an item
     *
     * @param registry The event registry to register the item
     * @param item     An instance of an item
     * @param name     The registry name of the item
     * @return The item with a registry name
     */
    public static Item registerItem(IForgeRegistry<Item> registry, Item item, String name) {
        item.setRegistryName(name);
        registry.register(item);
        return item;
    }
}
