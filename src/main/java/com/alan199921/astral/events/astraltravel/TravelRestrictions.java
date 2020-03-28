package com.alan199921.astral.events.astraltravel;

import com.alan199921.astral.Astral;
import com.alan199921.astral.api.AstralAPI;
import com.alan199921.astral.api.sleepmanager.ISleepManager;
import com.alan199921.astral.api.sleepmanager.SleepManager;
import com.alan199921.astral.dimensions.AstralDimensions;
import com.alan199921.astral.effects.AstralEffects;
import com.alan199921.astral.entities.PhysicalBodyEntity;
import com.alan199921.astral.events.AstralEntityDamage;
import com.alan199921.astral.events.IAstralDamage;
import com.alan199921.astral.tags.AstralTags;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Astral.MOD_ID)
public class TravelRestrictions {
    @SubscribeEvent
    public static void mobsTargetPhysicalBody(EntityJoinWorldEvent event) {
        if (!event.getWorld().isRemote() && event.getEntity() instanceof IMob && !AstralTags.NEUTRAL_MOBS.contains(event.getEntity().getType())) {
            MobEntity mobEntity = (MobEntity) event.getEntity();
            mobEntity.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(mobEntity, PhysicalBodyEntity.class, true));
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
        if (event.getSource().getTrueSource() instanceof LivingEntity) {
            LivingEntity trueSource = (LivingEntity) event.getSource().getTrueSource();
            LivingEntity target = event.getEntityLiving();
            DamageSource damageType = event.getSource();
            boolean isAstralTravelActiveOnTarget = isAstralTravelActive(target);
            boolean isAstralTravelActiveOnSource = isAstralTravelActive(trueSource);
            //Negate astral damage to non Astral entities
            if (!isAstralTravelActiveOnTarget && IAstralDamage.isDamageAstral(damageType)) {
                event.setCanceled(true);
            }
            //Convert non astral damage to astral damage for Astral entities
            else if (isAstralTravelActiveOnSource && !IAstralDamage.isDamageAstral(damageType)) {
                event.setCanceled(true);
                target.attackEntityFrom(new AstralEntityDamage(trueSource), trueSource.getActivePotionEffect(AstralEffects.ASTRAL_TRAVEL).getAmplifier() + 1.0F);
            }
            //Negate non-Astral damage to Astral entities
            else if (isAstralTravelActiveOnTarget && !(IAstralDamage.isDamageAstral(damageType))) {
                event.setCanceled(true);
            }
        }
        //Check for astral damage vs. non astral and vice versa
        else {
            if (!(IAstralDamage.canDamageTypeDamageAstral(event.getSource())) && isAstralTravelActive(event.getEntityLiving()) || IAstralDamage.isDamageAstral(event.getSource()) && !isAstralTravelActive(event.getEntityLiving())) {
                event.setCanceled(true);
            }
        }
    }

    //Function for detecting if an Astral entity is interacting with a non astral entity
    private static boolean isAstralVsNonAstral(LivingEntity mobA, LivingEntity mobB) {
        if (mobA == null || mobB == null) {
            return false;
        }
        return isAstralTravelActive(mobA) && !isAstralTravelActive(mobB) || !isAstralTravelActive(mobA) && isAstralTravelActive(mobB);
    }

    @SubscribeEvent
    public static void astralBlockInteraction(PlayerInteractEvent.RightClickBlock event) {
        if (isAstralTravelActive(event.getPlayer()) && AstralDimensions.isEntityNotInInnerRealm(event.getPlayer())) {
            Block targetedBlock = event.getWorld().getBlockState(event.getPos()).getBlock();
            event.setCanceled(!AstralTags.ASTRAL_INTERACT.contains(targetedBlock));
        }
    }

    @SubscribeEvent
    public static void astralBreakBlock(BlockEvent.BreakEvent event) {
        //Placeholder properties
        if (!AstralTags.ASTRAL_INTERACT.contains(event.getState().getBlock()) && isAstralTravelActive(event.getPlayer()) && AstralDimensions.isEntityNotInInnerRealm(event.getPlayer())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void astralHarvestSpeed(PlayerEvent.BreakSpeed event) {
        if (!AstralTags.ASTRAL_INTERACT.contains(event.getState().getBlock()) && isAstralTravelActive(event.getPlayer()) && AstralDimensions.isEntityNotInInnerRealm(event.getPlayer())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void astralPlaceBlockEvent(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof LivingEntity && !AstralTags.ASTRAL_INTERACT.contains(event.getState().getBlock()) && isAstralTravelActive((LivingEntity) event.getEntity()) && AstralDimensions.isEntityNotInInnerRealm(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void astralPickupEvent(EntityItemPickupEvent event) {
        World world = event.getEntityLiving().world;
        if (!world.isRemote() && AstralDimensions.isEntityNotInInnerRealm(event.getPlayer()) && isAstralTravelActive(event.getEntityLiving()) && !AstralTags.ASTRAL_PICKUP.contains(event.getItem().getItem().getItem())) {
            event.setCanceled(true);
        }
    }

    /**
     * Returns whether the living entity should be receiving the effects of Astral Travel
     * <p>
     * Entities receive the effects of Astral Travel if they have the Astral Travel potion effect and is not a player
     * or if they have charged up their sleep gauge from the sleep manager capability
     *
     * @param livingEntity The living entity to be checked
     * @return Whether the entity should be receving the benefits of Astral Travel
     */
    public static boolean isAstralTravelActive(LivingEntity livingEntity) {
        if (livingEntity.getCapability(AstralAPI.sleepManagerCapability).isPresent()) {
            final ISleepManager sleepManager = livingEntity.getCapability(AstralAPI.sleepManagerCapability).orElseGet(SleepManager::new);
            return livingEntity.isPotionActive(AstralEffects.ASTRAL_TRAVEL) && sleepManager.isEntityTraveling();
        }
        else {
            return livingEntity.isPotionActive(AstralEffects.ASTRAL_TRAVEL);
        }
    }

    @SubscribeEvent
    public static void astralDeath(LivingDeathEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity && !event.getEntityLiving().getEntityWorld().isRemote()) {
            PlayerEntity playerEntity = (PlayerEntity) event.getEntityLiving();
            ServerWorld serverWorld = (ServerWorld) event.getEntityLiving().getEntityWorld();
            if (isAstralTravelActive(playerEntity)) {
                AstralAPI.getOverworldPsychicInventory(serverWorld).map(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(playerEntity.getUniqueID())).ifPresent(psychicInventoryInstance -> {
                    final Boolean isPhysicalBodyAlive = AstralAPI.getBodyLinkCapability(serverWorld).map(iBodyLinkCapability -> iBodyLinkCapability.getInfo(playerEntity.getUniqueID()).isAlive()).orElseGet(() -> false);
                    event.getEntityLiving().removePotionEffect(AstralEffects.ASTRAL_TRAVEL);
                    if (isPhysicalBodyAlive) {
                        event.setCanceled(true);
                    }
                });
            }

        }
    }
}
