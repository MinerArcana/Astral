package com.minerarcana.astral.events;

import com.minerarcana.astral.Astral;
import com.minerarcana.astral.entity.AstralAttributes;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Astral.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AttachAttributeEvents {
    /**
     * Add the Astral Attack Damage attribute to entities that don't have it
     *
     * @param event The EntityAttributeModificationEvent that allows for attributes to be added to entities
     */
    @SubscribeEvent
    public static void attachAstralDamageAttribute(EntityAttributeModificationEvent event) {
        event.getTypes().stream()
                .filter(entityType -> !event.has(entityType, AstralAttributes.ASTRAL_ATTACK_DAMAGE.get()))
                .forEach(entityType -> event.add(entityType, AstralAttributes.ASTRAL_ATTACK_DAMAGE.get()));
    }
}
