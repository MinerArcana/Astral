package com.minerarcana.astral.events;

import com.minerarcana.astral.Astral;
import com.minerarcana.astral.api.AstralCapabilities;
import com.minerarcana.astral.effect.AstralEffects;
import com.minerarcana.astral.effect.AstralTravelEffect;
import com.minerarcana.astral.tags.AstralTags;
import com.minerarcana.astral.world.feature.dimensions.AstralDimensions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Astral.MOD_ID)
public class AstralTravelEffects {
    /**
     * Astral entities don't take fall damage
     *
     * @param event A LivingFallEvent to cancel
     */
    @SubscribeEvent
    public static void negateAstralFallDamage(LivingFallEvent event) {
        if (event.getEntity().hasEffect(AstralEffects.ASTRAL_TRAVEL.get())) {
            event.setCanceled(true);
        }
    }

    /**
     * Cancel the targeting event if an Astral entity targets a non-Astral entity or vice versa, as long as it's not a spiritual mob
     *
     * @param event The LivingSetAttackTargetEvent
     */
    @SubscribeEvent
    public static void doNotTargetAstrals(LivingChangeTargetEvent event) {
        boolean cancelled = event.getNewTarget() != null && isAstralVsNonAstral(event.getEntity(), event.getNewTarget()) && !event.getEntity().getType().is(AstralTags.SPIRITUAL_MOBS);
        event.setCanceled(cancelled);
    }

    /**
     * Helper function for detecting if an Astral entity is interacting with a non astral entity
     *
     * @param mobA A mob
     * @param mobB Another mob
     * @return If one mob has Astral Travel but not both
     */
    private static boolean isAstralVsNonAstral(LivingEntity mobA, LivingEntity mobB) {
        return isEntityAstral(mobA) ^ isEntityAstral(mobB);
    }

    /**
     * Returns whether the living entity should be receiving the effects of Astral Travel
     * <p>
     * Entities receive the effects of Astral Travel if they have the Astral Travel potion effect and is not a player,
     * or if they have the Astral Entities tag
     *
     * @param livingEntity The living entity to be checked
     * @return Whether the entity should be receiving the benefits of Astral Travel
     */
    public static boolean isEntityAstral(LivingEntity livingEntity) {
        return livingEntity.hasEffect(AstralEffects.ASTRAL_TRAVEL.get()) || livingEntity.getType().is(AstralTags.ETHEREAL_MOBS);
    }

    /**
     * Only allow block harvesting when not in Inner Realm
     *
     * @param event the break speed event
     */
    @SubscribeEvent
    public static void astralHarvestSpeed(PlayerEvent.BreakSpeed event) {
        boolean cancelled = !event.getState().is(AstralTags.ASTRAL_INTERACTABLE) && isEntityAstral(event.getEntity()) && AstralDimensions.isEntityNotInInnerRealm(event.getEntity());
        event.setCanceled(cancelled);
    }

    @SubscribeEvent
    public static void astralPickupEvent(EntityItemPickupEvent event) {
        boolean cancelled = AstralDimensions.isEntityNotInInnerRealm(event.getEntity()) && isEntityAstral(event.getEntity()) && !event.getItem().getItem().is(AstralTags.ASTRAL_CAN_PICKUP);
        event.setCanceled(cancelled);
    }

    /**
     * When the player gets access to the Astral travel effect, disable their gravity and tell the sleep manager that
     * they are falling asleep.
     * Notifies the client that an entity is getting Astral Travel
     *
     * @param event The event that contains information about the player and the effect applied
     */
    @SubscribeEvent
    public static void astralTravelAdded(MobEffectEvent.Added event) {
        //Only players need the startup effects
        if (event.getEffectInstance().getEffect().equals(AstralEffects.ASTRAL_TRAVEL.get()) && event.getEntity() instanceof Player && !event.getEntity().hasEffect(AstralEffects.ASTRAL_TRAVEL.get())) {
            AstralCapabilities.getBodyTracker(event.getEntity().getLevel()).ifPresent(bodyTracker -> bodyTracker.setBodyNBT((Player) event.getEntity()));
        }
    }

    /**
     * When the Astral Travel potion effect ends, remove the player's flying abilities, teleport them to the body,
     * transfer the body's inventory into the player's inventory, and then kill it.
     *
     * @param entityLiving The entity that with the potion effect
     */
    public static void astralTravelEnd(ServerPlayer entityLiving) {
        AstralCapabilities.getBodyTracker(entityLiving.getLevel()).ifPresent(bodyTracker -> bodyTracker.mergePlayerWithBody(entityLiving));
    }

    @SubscribeEvent
    public static void astralTravelExpire(MobEffectEvent.Expired event) {
        if (event.getEffectInstance() != null && event.getEffectInstance().getEffect() instanceof AstralTravelEffect && event.getEntity() instanceof ServerPlayer serverPlayer) {
            astralTravelEnd(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void astralTravelRemove(MobEffectEvent.Expired event) {
        if (event.getEffectInstance() != null && event.getEffectInstance().getEffect() instanceof AstralTravelEffect && event.getEntity() instanceof ServerPlayer serverPlayer) {
            astralTravelEnd(serverPlayer);
        }
    }
}
