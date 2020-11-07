package com.alan19.astral.entity;

import com.alan19.astral.Astral;
import com.alan19.astral.entity.crystalspider.CrystalSpiderEntity;
import com.alan19.astral.entity.physicalbody.PhysicalBodyEntity;
import com.alan19.astral.entity.projectile.CrystalWebProjectileEntity;
import com.alan19.astral.entity.projectile.IntentionBeam;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AstralEntities {
    public static final String PHYSICAL_BODY = "physical_body";
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Astral.MOD_ID);

    public static final RegistryObject<EntityType<PhysicalBodyEntity>> PHYSICAL_BODY_ENTITY = ENTITIES.register(PHYSICAL_BODY, AstralEntities::getPhysicalBody);
    public static final RegistryObject<EntityType<CrystalSpiderEntity>> CRYSTAL_SPIDER = ENTITIES.register("crystal_spider", AstralEntities::getCrystalSpider);
    public static final RegistryObject<EntityType<CrystalWebProjectileEntity>> CRYSTAL_WEB_PROJECTILE_ENTITY = ENTITIES.register("crystal_web_projectile", AstralEntities::getCrystalWeb);
    public static final RegistryObject<EntityType<IntentionBeam>> INTENTION_BEAM_ENTITY = ENTITIES.register("intention_beam", AstralEntities::getIntentionBeam);

    public static void register(IEventBus modBus) {
        ENTITIES.register(modBus);
    }

    private static EntityType<IntentionBeam> getIntentionBeam() {
        EntityType.Builder<IntentionBeam> intentionBeamBuilder = EntityType.Builder.create(IntentionBeam::new, EntityClassification.MISC);
        intentionBeamBuilder.size(1F, 1F);
        return intentionBeamBuilder.build("crystal_web_projectile");
    }

    private static EntityType<CrystalWebProjectileEntity> getCrystalWeb() {
        EntityType.Builder<CrystalWebProjectileEntity> crystalWebProjectileEntityBuilder = EntityType.Builder.create(CrystalWebProjectileEntity::new, EntityClassification.MISC);
        crystalWebProjectileEntityBuilder.size(1F, 1F);
        return crystalWebProjectileEntityBuilder.build("crystal_web_projectile");
    }

    private static EntityType<PhysicalBodyEntity> getPhysicalBody() {
        return EntityType.Builder.create(PhysicalBodyEntity::new, EntityClassification.MISC)
                .size(1.8F, .6F)
                .build(PHYSICAL_BODY);
    }

    private static EntityType<CrystalSpiderEntity> getCrystalSpider() {
        return EntityType.Builder.create(CrystalSpiderEntity::new, EntityClassification.MONSTER)
                .size(0.7F, 0.5F)
                .build("crystal_spider");
    }

    public static void addSpawns() {
        EntitySpawnPlacementRegistry.register(AstralEntities.CRYSTAL_SPIDER.get(), EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, IAstralBeing::canEtherealEntitySpawn);
        BiomeDictionary.getBiomes(BiomeDictionary.Type.OCEAN).forEach(biome -> biome.getSpawns(EntityClassification.MONSTER).add(new Biome.SpawnListEntry(AstralEntities.CRYSTAL_SPIDER.get(), 100, 4, 4)));
    }
}
