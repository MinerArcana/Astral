package com.alan19.astral.events.astraltravel;

import com.alan19.astral.Astral;
import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.api.sleepmanager.ISleepManager;
import com.alan19.astral.api.sleepmanager.SleepManager;
import com.alan19.astral.dimensions.AstralDimensions;
import com.alan19.astral.effects.AstralEffects;
import com.alan19.astral.entity.IAstralBeing;
import com.alan19.astral.entity.physicalbody.PhysicalBodyEntity;
import com.alan19.astral.events.IAstralDamage;
import com.alan19.astral.tags.AstralTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = Astral.MOD_ID)
public class TravelEffects {
    @SubscribeEvent
    public static void mobsTargetPhysicalBody(EntityJoinWorldEvent event) {
        if (!event.getWorld().isClientSide() && event.getEntity() instanceof Enemy && !AstralTags.NEUTRAL_MOBS.contains(event.getEntity().getType())) {
            Mob mobEntity = (Mob) event.getEntity();
            mobEntity.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(mobEntity, PhysicalBodyEntity.class, true));
        }
        if (!event.getWorld().isClientSide() && event.getEntity().getType().equals(EntityType.PHANTOM)) {
            Phantom phantomEntity = (Phantom) event.getEntity();
            phantomEntity.targetSelector.addGoal(1, new NearestAttackableTargetGoal<Player>(phantomEntity, Player.class, true) {
                @Override
                protected boolean canAttack(@Nullable LivingEntity potentialTarget, @Nonnull TargetingConditions targetPredicate) {
                    return potentialTarget != null && isEntityAstral(potentialTarget);
                }
            });
        }
    }

//    @SubscribeEvent
//    public static void spiritualMobAttributes(EntityJoinWorldEvent event) {
//        if (AstralTags.SPIRITUAL_BEINGS.contains(event.getEntity().getType())) {
//            LivingEntity livingEntity = (LivingEntity) event.getEntity();
//            final ModifiableAttributeInstance attribute = livingEntity.getAttribute(AstralModifiers.ASTRAL_ATTACK_DAMAGE.get());
//            if (attribute != null && !attribute.hasModifier(Constants.SPIRITUAL_MOB_MODIFER)) {
//                attribute.applyPersistentModifier(Constants.SPIRITUAL_MOB_MODIFER);
//            }
//        }
//    }

    /**
     * Cancel the targeting event if an Astral entity targets an non-Astral entity or vice versa, as long as neither of them have the spiritual entity tag
     *
     * @param event The LivingSetAttackTargetEvent
     */
    @SubscribeEvent
    public static void doNotTargetAstrals(LivingSetAttackTargetEvent event) {
        if (event.getEntityLiving() instanceof Mob && isAstralVsNonAstral(event.getTarget(), event.getEntityLiving()) && !(AstralTags.SPIRITUAL_BEINGS.contains(event.getTarget().getType()) || AstralTags.SPIRITUAL_BEINGS.contains(event.getEntityLiving().getType()))) {
            Mob mobEntity = (Mob) event.getEntityLiving();
            mobEntity.setTarget(null);
        }
    }

    /**
     * Non Astral entities do not take damage from Astral damage
     * Astral entities have their physical damage replaced with Astral damage
     * Drowning Astral entities lose Astral Travel
     *
     * @param event The LivingAttackEvent supplying the target, source, and damage
     */
    @SubscribeEvent
    public static void replacePhysicalWithAstralDamage(LivingAttackEvent event) {
        final LivingEntity entityLiving = event.getEntityLiving();
        if (handleAstralDrowning(event, entityLiving)) {
            return;
        }

        //Replace Astral entity's Physical Damage with Astral damage
        if (event.getSource().getEntity() instanceof LivingEntity && (isEntityAstral((LivingEntity) event.getSource().getEntity()) || AstralTags.SPIRITUAL_BEINGS.contains(event.getSource().getEntity().getType()) && isEntityAstral(event.getEntityLiving())) && !IAstralDamage.canDamageTypeDamageAstral(event.getSource())) {
            event.setCanceled(true);
            IAstralBeing.attackEntityAsMobWithAstralDamage((LivingEntity) event.getSource().getEntity(), event.getEntity());
        }
        //Cancel Astral Damage against non Astral entities and Physical Damage against Astral Entities
        final boolean isTargetNotSpiritualEntity = !(event.getEntityLiving() != null && AstralTags.SPIRITUAL_BEINGS.contains(event.getEntityLiving().getType()));
        final boolean isTargetAstral = isEntityAstral(entityLiving);
        if (isTargetNotSpiritualEntity && (isTargetAstral && !IAstralDamage.canDamageTypeDamageAstral(event.getSource()) || !isTargetAstral && IAstralDamage.isDamageAstral(event.getSource()))) {
            event.setCanceled(true);
        }

    }

    public static boolean handleAstralDrowning(LivingAttackEvent event, LivingEntity entityLiving) {
        if (event.getSource().getMsgId().equals("drown")) {
            if (entityLiving instanceof Player && entityLiving.hasEffect(AstralEffects.ASTRAL_TRAVEL.get())) {
                entityLiving.removeEffectNoUpdate(AstralEffects.ASTRAL_TRAVEL.get());
                StartAndEndHandling.astralTravelEnd(entityLiving);
            }
            return true;
        }
        return false;
    }

    //Function for detecting if an Astral entity is interacting with a non astral entity
    private static boolean isAstralVsNonAstral(LivingEntity mobA, LivingEntity mobB) {
        if (mobA == null || mobB == null) {
            return false;
        }
        return isEntityAstral(mobA) && !isEntityAstral(mobB) || !isEntityAstral(mobA) && isEntityAstral(mobB);
    }

    @SubscribeEvent
    public static void astralBlockInteraction(PlayerInteractEvent.RightClickBlock event) {
        if (isEntityAstral(event.getPlayer()) && AstralDimensions.isEntityNotInInnerRealm(event.getPlayer())) {
            Block targetedBlock = event.getWorld().getBlockState(event.getPos()).getBlock();
            if (!AstralTags.ASTRAL_INTERACT.contains(targetedBlock)) {
                event.setUseBlock(Event.Result.DENY);
            }
            else {
                event.setUseBlock(Event.Result.DEFAULT);
            }
        }
    }

    @SubscribeEvent
    public static void astralBreakBlock(BlockEvent.BreakEvent event) {
        //Placeholder properties
        if (!AstralTags.ASTRAL_INTERACT.contains(event.getState().getBlock()) && isEntityAstral(event.getPlayer()) && AstralDimensions.isEntityNotInInnerRealm(event.getPlayer())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void astralHarvestSpeed(PlayerEvent.BreakSpeed event) {
        if (!AstralTags.ASTRAL_INTERACT.contains(event.getState().getBlock()) && isEntityAstral(event.getPlayer()) && AstralDimensions.isEntityNotInInnerRealm(event.getPlayer())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void astralPlaceBlockEvent(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof LivingEntity && !AstralTags.ASTRAL_INTERACT.contains(event.getState().getBlock()) && isEntityAstral((LivingEntity) event.getEntity()) && AstralDimensions.isEntityNotInInnerRealm(event.getEntity())) {
            event.setCanceled(true);
        }
        if (event.getEntity() instanceof LivingEntity && AstralTags.ASTRAL_INTERACT.contains(event.getState().getBlock()) && !isEntityAstral((LivingEntity) event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void astralPickupEvent(EntityItemPickupEvent event) {
        Level world = event.getEntityLiving().level;
        if (!world.isClientSide() && AstralDimensions.isEntityNotInInnerRealm(event.getPlayer()) && isEntityAstral(event.getEntityLiving()) && !AstralTags.ASTRAL_PICKUP.contains(event.getItem().getItem().getItem())) {
            event.setCanceled(true);
        }
    }

    /**
     * Returns whether the living entity should be receiving the effects of Astral Travel
     * <p>
     * Entities receive the effects of Astral Travel if they have the Astral Travel potion effect and is not a player,
     * or if they have charged up their sleep gauge from the sleep manager capability, or if they are part of the Astral Entities tag
     *
     * @param livingEntity The living entity to be checked
     * @return Whether the entity should be receving the benefits of Astral Travel
     */
    public static boolean isEntityAstral(LivingEntity livingEntity) {
        if (livingEntity instanceof Player && livingEntity.getCapability(AstralAPI.sleepManagerCapability).isPresent()) {
            final ISleepManager sleepManager = livingEntity.getCapability(AstralAPI.sleepManagerCapability).orElseGet(SleepManager::new);
            return livingEntity.hasEffect(AstralEffects.ASTRAL_TRAVEL.get()) && sleepManager.isEntityTraveling();
        }
        else {
            return livingEntity.hasEffect(AstralEffects.ASTRAL_TRAVEL.get()) || AstralTags.ETHEREAL_BEINGS.contains(livingEntity.getType());
        }
    }

    @SubscribeEvent
    public static void astralDeath(LivingDeathEvent event) {
        if (event.getEntityLiving() instanceof Player && !event.getEntityLiving().getCommandSenderWorld().isClientSide()) {
            Player playerEntity = (Player) event.getEntityLiving();
            ServerLevel serverWorld = (ServerLevel) event.getEntityLiving().getCommandSenderWorld();
            if (isEntityAstral(playerEntity)) {
                AstralAPI.getOverworldPsychicInventory(serverWorld).map(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(playerEntity.getUUID())).ifPresent(psychicInventoryInstance -> {
                    event.getEntityLiving().removeEffect(AstralEffects.ASTRAL_TRAVEL.get());
                    event.setCanceled(true);
                });
            }
        }
    }

    @SubscribeEvent
    public static void astralFalling(LivingFallEvent event) {
        if (event.getEntityLiving().hasEffect(AstralEffects.ASTRAL_TRAVEL.get())) {
            event.setCanceled(true);
        }
    }
}
