package com.alan199921.astral.items;

import com.alan199921.astral.Astral;
import com.alan199921.astral.blocks.EgoMembrane;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstralItems {

    public static Item snowberry;
    public static Item feverweed;
    public static Item introspectionMedicine;
    public static Item enlightenmentKey;
    public static Item travellingMedicine;

    @SubscribeEvent
    public static void onItemRegistry(final RegistryEvent.Register<Item> event) {
        snowberry = registerItem(new Snowberry(), "snowberry");
        feverweed = registerItem(new Feverweed(), "feverweed");
        introspectionMedicine = registerItem(new IntrospectionMedicine(), "introspection_medicine");
        enlightenmentKey = registerItem(new EnlightenmentKey(), "enlightenment_key");
        travellingMedicine = registerItem(new TravellingMedicine(), "travelling_medicine");
    }

    /**
     * Registers an item
     * @param item An instance of an item
     * @param name The registry name of the item
     * @return The item with a registry name
     */
    public static Item registerItem(Item item, String name)
    {
        item.setRegistryName(name);
        ForgeRegistries.ITEMS.register(item);
        return item;
    }
}
