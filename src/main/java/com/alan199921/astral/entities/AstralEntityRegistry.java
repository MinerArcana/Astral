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
    public static final EntityType<PhysicalBodyEntity> PHYSICAL_BODY_ENTITY;

    static {
        ResourceLocation physicalBodyResourceLocation = new ResourceLocation(Astral.MOD_ID, "physical_body");
        final EntityType<?> entityType = EntityType.Builder.create(PhysicalBodyEntity::new, EntityClassification.MISC).size(1.8F, .6F).build(physicalBodyResourceLocation.toString()).setRegistryName(physicalBodyResourceLocation);
        PHYSICAL_BODY_ENTITY = (EntityType<PhysicalBodyEntity>) entityType;
    }

    @SubscribeEvent
    public static void registerEntity(RegistryEvent.Register<EntityType<?>> event) {
        event.getRegistry().register(PHYSICAL_BODY_ENTITY);
    }
}
