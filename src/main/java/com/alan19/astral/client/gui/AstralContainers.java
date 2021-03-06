package com.alan19.astral.client.gui;

import com.alan19.astral.Astral;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AstralContainers {
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Astral.MOD_ID);

    public static final RegistryObject<ContainerType<AstralInventoryContainer>> ASTRAL_INVENTORY_CONTAINER = CONTAINERS.register("astral_inventory", () -> IForgeContainerType.create((windowId, inv, data) -> new AstralInventoryContainer(windowId, inv)));

    public static void register(IEventBus modBus) {
        CONTAINERS.register(modBus);
    }
}
