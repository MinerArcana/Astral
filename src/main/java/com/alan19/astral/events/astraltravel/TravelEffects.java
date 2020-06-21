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
import com.alan19.astral.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityEvent;
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

@Mod.EventBusSubscriber(modid = Astral.MOD_ID)
public class TravelEffects {
    @SubscribeEvent
    public static void mobsTargetPhysicalBody(EntityJoinWorldEvent event) {
        if (!event.getWorld().isRemote() && event.getEntity() instanceof IMob && !AstralTags.NEUTRAL_MOBS.contains(event.getEntity().getType())) {
            MobEntity mobEntity = (MobEntity) event.getEntity();
            mobEntity.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(mobEntity, PhysicalBodyEntity.class, true));
        }
    }

    @SubscribeEvent
    public static void registerAstralDamageAttribute(EntityEvent.EntityConstructing event) {
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) event.getEntity();
            if (livingEntity.getAttribute(Constants.ASTRAL_ATTACK_DAMAGE) == null) {
                ((LivingEntity) event.getEntity()).getAttributes().registerAttribute(Constants.ASTRAL_ATTACK_DAMAGE);
            }
        }
    }

    @SubscribeEvent
    public static void doNotTargetAstrals(LivingSetAttackTargetEvent event) {
        if (event.getEntityLiving() instanceof MobEntity && isAstralVsNonAstral(event.getTarget(), event.getEntityLiving())) {
            MobEntity mobEntity = (MobEntity) event.getEntityLiving();
            mobEntity.setAttackTarget(null);
        }
    }

    /**
     * Non Astral entities do not take damage from Astral damage
     * Astral entities have their physical damage replaced with Astral damage
     *
     * @param event The LivingAttackEvent supplying the target, source, and damage
     */
    @SubscribeEvent
    public static void replacePhysicalWithAstralDamage(LivingAttackEvent event) {
        //Replace Astral entity's Physical Damage with Astral damage
        if (event.getSource().getTrueSource() instanceof LivingEntity && isEntityAstral((LivingEntity) event.getSource().getTrueSource()) && !IAstralDamage.canDamageTypeDamageAstral(event.getSource())) {
            event.setCanceled(true);
            IAstralBeing.attackEntityAsMobWithAstralDamage((LivingEntity) event.getSource().getTrueSource(), event.getEntity());
        }
        //Cancel Magic Damage against non Astral entities and Physical Damage against Astral Entities
        if (isEntityAstral(event.getEntityLiving()) && !IAstralDamage.canDamageTypeDamageAstral(event.getSource()) || !isEntityAstral(event.getEntityLiving()) && IAstralDamage.isDamageAstral(event.getSource())) {
            event.setCanceled(true);
        }
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
        World world = event.getEntityLiving().world;
        if (!world.isRemote() && AstralDimensions.isEntityNotInInnerRealm(event.getPlayer()) && isEntityAstral(event.getEntityLiving()) && !AstralTags.ASTRAL_PICKUP.contains(event.getItem().getItem().getItem())) {
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
        if (livingEntity instanceof PlayerEntity && livingEntity.getCapability(AstralAPI.sleepManagerCapability).isPresent()) {
            final ISleepManager sleepManager = livingEntity.getCapability(AstralAPI.sleepManagerCapability).orElseGet(SleepManager::new);
            return livingEntity.isPotionActive(AstralEffects.ASTRAL_TRAVEL.get()) && sleepManager.isEntityTraveling();
        }
        else {
            return livingEntity.isPotionActive(AstralEffects.ASTRAL_TRAVEL.get()) || AstralTags.ETHEREAL_BEINGS.contains(livingEntity.getType());
        }
    }

    @SubscribeEvent
    public static void astralDeath(LivingDeathEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity && !event.getEntityLiving().getEntityWorld().isRemote()) {
            PlayerEntity playerEntity = (PlayerEntity) event.getEntityLiving();
            ServerWorld serverWorld = (ServerWorld) event.getEntityLiving().getEntityWorld();
            if (isEntityAstral(playerEntity)) {
                AstralAPI.getOverworldPsychicInventory(serverWorld).map(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(playerEntity.getUniqueID())).ifPresent(psychicInventoryInstance -> {
                    final Boolean isPhysicalBodyAlive = AstralAPI.getBodyLinkCapability(serverWorld).map(iBodyLinkCapability -> iBodyLinkCapability.getInfo(playerEntity.getUniqueID()).isAlive()).orElseGet(() -> false);
                    event.getEntityLiving().removePotionEffect(AstralEffects.ASTRAL_TRAVEL.get());
                    if (isPhysicalBodyAlive) {
                        event.setCanceled(true);
                    }
                });
            }

        }
    }

    @SubscribeEvent
    public static void astralFalling(LivingFallEvent event) {
        if (event.getEntityLiving().isPotionActive(AstralEffects.ASTRAL_TRAVEL.get())) {
            event.setCanceled(true);
        }
    }
}
