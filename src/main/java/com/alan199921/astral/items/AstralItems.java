package com.alan199921.astral.items;

import com.alan199921.astral.Astral;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstralItems {

    @ObjectHolder("astral:snowberry")
    public static final Item snowberry = null;

    @ObjectHolder("astral:feverweed")
    public static final Item feverweed = null;

    @ObjectHolder("astral:introspection_medicine")
    public static final Item introspectionMedicine = null;

    @ObjectHolder("astral:enlightenment_key")
    public static final Item enlightenmentKey = null;

    @ObjectHolder("astral:travelling_medicine")
    public static final Item travellingMedicine = null;

    @SubscribeEvent
    public static void onItemRegistry(final RegistryEvent.Register<Item> event) {
        registerItem(event.getRegistry(), new Snowberry(), "snowberry");
        registerItem(event.getRegistry(), new Feverweed(), "feverweed");
        registerItem(event.getRegistry(), new IntrospectionMedicine(), "introspection_medicine");
        registerItem(event.getRegistry(), new EnlightenmentKey(), "enlightenment_key");
        registerItem(event.getRegistry(), new TravellingMedicine(), "travelling_medicine");
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
