package com.alan19.astral.entities;

import com.alan19.astral.Astral;
import com.alan19.astral.entities.physicalbody.PhysicalBodyEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AstralEntities {
    public static final String PHYSICAL_BODY = "physical_body";
    private static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, Astral.MOD_ID);

    public static final RegistryObject<EntityType<PhysicalBodyEntity>> PHYSICAL_BODY_ENTITY = ENTITIES.register(PHYSICAL_BODY, AstralEntities::getPhysicalBody);
    public static final RegistryObject<EntityType<CrystalSpider>> CRYSTAL_SPIDER = ENTITIES.register("crystal_spider", AstralEntities::getCrystalSpider);

    public static void register(IEventBus modBus) {
        ENTITIES.register(modBus);
    }

    private static EntityType<PhysicalBodyEntity> getPhysicalBody() {
        return EntityType.Builder.create(PhysicalBodyEntity::new, EntityClassification.MISC)
                .size(1.8F, .6F)
                .build(PHYSICAL_BODY);
    }

    private static EntityType<CrystalSpider> getCrystalSpider() {
        return EntityType.Builder.create(CrystalSpider::new, EntityClassification.MONSTER)
                .size(0.7F, 0.5F)
                .build("crystal_spider");
    }
}
