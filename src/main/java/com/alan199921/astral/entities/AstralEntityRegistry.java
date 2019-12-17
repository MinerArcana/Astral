package com.alan199921.astral.entities;

import com.alan199921.astral.Astral;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Astral.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstralEntityRegistry {
    public static final EntityType<?> PHYSICAL_BODY_ENTITY;

    static {
        ResourceLocation physical_body_resourceLocation = new ResourceLocation(Astral.MOD_ID, "physical_body");
        PHYSICAL_BODY_ENTITY = EntityType.Builder.create(PhysicalBodyEntity::new, EntityClassification.MISC).size(2, 1).build(physical_body_resourceLocation.toString()).setRegistryName(physical_body_resourceLocation);
    }

    @SubscribeEvent
    public static void registerEntity(RegistryEvent.Register<EntityType<?>> event) {
        event.getRegistry().register(PHYSICAL_BODY_ENTITY);
    }
}
