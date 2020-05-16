package com.alan19.astral.entities;

import com.alan19.astral.Astral;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AstralEntityRegistry {
    public static final String PHYSICAL_BODY = "physical_body";
    private static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, Astral.MOD_ID);
    public static final RegistryObject<EntityType<PhysicalBodyEntity>> PHYSICAL_BODY_ENTITY = ENTITIES.register(PHYSICAL_BODY, () -> EntityType.Builder
            .create(PhysicalBodyEntity::new, EntityClassification.MISC)
            .size(1.8F, .6F)
            .build(PHYSICAL_BODY));

    public static void register(IEventBus modBus) {
        ENTITIES.register(modBus);
    }
}
