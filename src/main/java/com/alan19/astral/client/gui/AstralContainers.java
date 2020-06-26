package com.alan19.astral.client.gui;

import com.alan19.astral.Astral;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AstralContainers {
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = new DeferredRegister<>(ForgeRegistries.CONTAINERS, Astral.MOD_ID);

    public static final RegistryObject<ContainerType<AstralInventoryContainer>> ASTRAL_INVENTORY_CONTAINER = CONTAINERS.register("astral_inventory", () -> new AstralInventoryContainer());
}
