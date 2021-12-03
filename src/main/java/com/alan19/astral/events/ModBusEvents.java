package com.alan19.astral.events;

import com.alan19.astral.Astral;
import com.alan19.astral.entity.AstralEntities;
import com.alan19.astral.entity.AstralModifiers;
import com.alan19.astral.entity.crystalspider.CrystalSpiderEntity;
import com.alan19.astral.entity.physicalbody.PhysicalBodyEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Astral.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBusEvents {
    /**
     * Add the Astral Attack Damage attribute to entities that don't have it
     *
     * @param event The EntityAttributeModificationEvent that allows for attributes to be added to entities
     */
    @SubscribeEvent
    public static void attachAstralDamageAttribute(EntityAttributeModificationEvent event) {
        event.getTypes().stream()
                .filter(entityType -> !event.has(entityType, AstralModifiers.ASTRAL_ATTACK_DAMAGE.get()))
                .forEach(entityType -> event.add(entityType, AstralModifiers.ASTRAL_ATTACK_DAMAGE.get()));
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(AstralEntities.CRYSTAL_SPIDER.get(), CrystalSpiderEntity.registerAttributes().build());
        event.put(AstralEntities.PHYSICAL_BODY_ENTITY.get(), PhysicalBodyEntity.registerAttributes().build());
    }
}
