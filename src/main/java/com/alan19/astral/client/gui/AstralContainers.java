package com.alan19.astral.client.gui;

import com.alan19.astral.Astral;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AstralContainers {
    private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Astral.MOD_ID);

    public static final RegistryObject<MenuType<AstralInventoryContainer>> ASTRAL_INVENTORY_CONTAINER = CONTAINERS.register("astral_inventory", () -> IForgeMenuType.create((windowId, inv, data) -> new AstralInventoryContainer(windowId, inv)));

    public static void register(IEventBus modBus) {
        CONTAINERS.register(modBus);
    }
}
