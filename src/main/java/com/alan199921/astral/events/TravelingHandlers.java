package com.alan199921.astral.events;

import com.alan199921.astral.Astral;
import com.alan199921.astral.api.AstralAPI;
import com.alan199921.astral.api.bodylink.BodyLinkProvider;
import com.alan199921.astral.api.innerrealmteleporter.InnerRealmTeleporterProvider;
import com.alan199921.astral.api.psychicinventory.IPsychicInventory;
import com.alan199921.astral.api.psychicinventory.InventoryType;
import com.alan199921.astral.api.sleepmanager.ISleepManager;
import com.alan199921.astral.api.sleepmanager.SleepManager;
import com.alan199921.astral.dimensions.AstralDimensions;
import com.alan199921.astral.dimensions.TeleportationTools;
import com.alan199921.astral.effects.AstralEffects;
import com.alan199921.astral.effects.AstralTravelEffect;
import com.alan199921.astral.entities.AstralEntityRegistry;
import com.alan199921.astral.entities.PhysicalBodyEntity;
import com.alan199921.astral.flight.FlightHandler;
import com.alan199921.astral.network.AstralNetwork;
import com.alan199921.astral.tags.AstralTags;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = Astral.MOD_ID)
public class TravelingHandlers {
    private static final UUID healthId = UUID.fromString("8bce997a-4c3a-11e6-beb8-9e71128cae77");
    private static final UUID astralGravity = UUID.fromString("c58e6f58-28e8-11ea-978f-2e728ce88125");

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

    @SubscribeEvent
    public static void cancelRegularMovement(InputUpdateEvent event) {
        if (event.getPlayer().isPotionActive(AstralEffects.ASTRAL_TRAVEL)) {
            final MovementInput movementInput = event.getMovementInput();
            movementInput.backKeyDown = false;
            movementInput.forwardKeyDown = false;
            movementInput.moveForward = 0;
            movementInput.rightKeyDown = false;
            movementInput.moveStrafe = 0;
        }
    }

    @SubscribeEvent
    public static void astralFlight(TickEvent.PlayerTickEvent event) {
        if (event.player.isPotionActive(AstralEffects.ASTRAL_TRAVEL)) {
            final LazyOptional<ISleepManager> iSleepManagerLazyOptional = event.player.getCapability(AstralAPI.sleepManagerCapability);
            if (iSleepManagerLazyOptional.isPresent()) {
                final ISleepManager sleepManager = iSleepManagerLazyOptional.orElse(new SleepManager());
                if (sleepManager.isEntityTraveling()) {
                    FlightHandler.handleAstralFlight(event.player);
                }
                else {
                    sleepManager.addSleep();
                    if (sleepManager.isEntityTraveling()) {
                        spawnPhysicalBody(event.player);
                        final Boolean goingToInnerRealm = iSleepManagerLazyOptional.map(ISleepManager::isGoingToInnerRealm).orElse(false);
                        if (Boolean.TRUE.equals(goingToInnerRealm)) {
                            event.player.getEntityWorld().getCapability(InnerRealmTeleporterProvider.TELEPORTER_CAPABILITY).ifPresent(cap -> cap.teleport(event.player));
                            iSleepManagerLazyOptional.ifPresent(iSleepManager -> iSleepManager.setGoingToInnerRealm(false));
                        }
                        else {
                            event.player.setMotion(0, 10, 0);
                            event.player.move(MoverType.SELF, new Vec3d(0, 1, 0));
                        }

                    }
                }
            }

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
            final ISleepManager sleepManager = livingEntity.getCapability(AstralAPI.sleepManagerCapability).orElse(new SleepManager());
            return livingEntity.isPotionActive(AstralEffects.ASTRAL_TRAVEL) && sleepManager.isEntityTraveling();
        }
        else {
            return livingEntity.isPotionActive(AstralEffects.ASTRAL_TRAVEL);
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
    public static void renderAstralEntities(RenderLivingEvent event) {
        if (!isAstralTravelActive(Minecraft.getInstance().player) && isAstralTravelActive(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void travelEffectExpire(PotionEvent.PotionExpiryEvent event) {
        if (event.getPotionEffect() != null && event.getPotionEffect().getPotion() instanceof AstralTravelEffect) {
            handleAstralEffectEnd(event.getEntityLiving());
        }
    }

    @SubscribeEvent
    public static void travelEffectRemove(PotionEvent.PotionRemoveEvent event) {
        if (event.getPotionEffect() != null && event.getPotionEffect().getPotion() instanceof AstralTravelEffect) {
            handleAstralEffectEnd(event.getEntityLiving());
        }
    }

    /**
     * When the Astral Travel potion effect ends, remove the player's flying abilities, teleport them to the body,
     * transfer the body's inventory into the player's inventory, and then kill it.
     *
     * @param entityLiving The entity that with the potion effect
     */
    private static void handleAstralEffectEnd(LivingEntity entityLiving) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) entityLiving;
            playerEntity.getAttribute(LivingEntity.ENTITY_GRAVITY).removeModifier(astralGravity);
            //Only run serverside
            if (!playerEntity.getEntityWorld().isRemote()) {
                //Get server versions of world and player
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) playerEntity;
                ServerWorld serverWorld = serverPlayerEntity.getServerWorld();

                //Retrieve the body entity object  from the capability
                playerEntity.getCapability(BodyLinkProvider.BODY_LINK_CAPABILITY).ifPresent(cap -> {
                    serverPlayerEntity.setMotion(0, 0, 0);
                    serverPlayerEntity.setVelocity(0, 0, 0);
                    serverPlayerEntity.isAirBorne = false;
                    //Teleport the player
                    if (cap.getLinkedEntity(serverWorld) instanceof PhysicalBodyEntity) {
                        PhysicalBodyEntity body = (PhysicalBodyEntity) cap.getLinkedEntity(serverWorld);
                        serverPlayerEntity.teleport(serverWorld.getServer().getWorld(DimensionType.getById(cap.getDimensionID())), body.lastTickPosX, body.lastTickPosY, body.lastTickPosZ, serverPlayerEntity.rotationYaw, serverPlayerEntity.rotationPitch);
                        //Get the inventory and transfer items
                        PhysicalBodyEntity physicalBodyEntity = (PhysicalBodyEntity) cap.getLinkedEntity(serverWorld);
                        AstralAPI.getOverworldPsychicInventory(serverWorld).ifPresent(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(playerEntity.getUniqueID()).setInventoryType(InventoryType.PHYSICAL, playerEntity.inventory));
                        resetPlayerStats(playerEntity, physicalBodyEntity);
                        physicalBodyEntity.onKillCommand();
                    }
                    //If body is not found, teleport player to their spawn location (bed or world spawn)
                    else {
                        teleportPlayerToSpawn(serverPlayerEntity);
                    }
                });
            }
        }
        if (!entityLiving.getEntityWorld().isRemote()) {
            AstralNetwork.sendAstralEffectEnding(entityLiving);
        }
    }

    private static void teleportPlayerToSpawn(ServerPlayerEntity serverPlayerEntity) {
        DimensionType playerSpawnDimension = serverPlayerEntity.getSpawnDimension();
        //Teleport to bed
        if (serverPlayerEntity.getBedPosition().isPresent()) {
            BlockPos bedPos = serverPlayerEntity.getBedPosition().get();
            TeleportationTools.changeDim(serverPlayerEntity, bedPos, playerSpawnDimension);
            serverPlayerEntity.sendMessage(TextComponentUtils.toTextComponent(() -> I18n.format("astral.chat_message.sleepwalking")));
        }
        //Teleport to spawn
        else {
            BlockPos serverSpawn = serverPlayerEntity.getServerWorld().getSpawnPoint();
            TeleportationTools.changeDim(serverPlayerEntity, serverSpawn, playerSpawnDimension);
            serverPlayerEntity.sendMessage(TextComponentUtils.toTextComponent(() -> I18n.format("astral.chat_message.sleepwalking.spawn")));
        }
        resetPlayerStats(serverPlayerEntity);
    }

    /**
     * When the player gets access to the Astral travel effect, give them the ability to fly, and transfer their inventory into the physical body mob
     *
     * @param event The event that contains information about the player and the effect applied
     */
    @SubscribeEvent
    public static void travelEffectActivate(PotionEvent.PotionAddedEvent event) {
        if (event.getPotionEffect().getPotion().equals(AstralEffects.ASTRAL_TRAVEL) && event.getEntityLiving() instanceof PlayerEntity && !event.getEntityLiving().isPotionActive(AstralEffects.ASTRAL_TRAVEL)) {
            PlayerEntity playerEntity = (PlayerEntity) event.getEntityLiving();
            playerEntity.getCapability(AstralAPI.sleepManagerCapability).orElse(new SleepManager()).resetSleep();
            if (!playerEntity.getEntityWorld().isRemote()) {
                playerEntity.getAttribute(LivingEntity.ENTITY_GRAVITY).applyModifier(new AttributeModifier(astralGravity, "disables gravity", -1, AttributeModifier.Operation.MULTIPLY_TOTAL).setSaved(true));
            }
        }
        if (event.getPotionEffect().getPotion().equals(AstralEffects.ASTRAL_TRAVEL) && !event.getEntityLiving().getEntityWorld().isRemote()) {
            AstralNetwork.sendAstralEffectStarting(event.getPotionEffect(), event.getEntity());
        }
    }

    public static void spawnPhysicalBody(PlayerEntity playerEntity) {
        PhysicalBodyEntity physicalBodyEntity = (PhysicalBodyEntity) AstralEntityRegistry.PHYSICAL_BODY_ENTITY.spawn(playerEntity.getEntityWorld(), ItemStack.EMPTY, playerEntity, playerEntity.getPosition(), SpawnReason.TRIGGERED, false, false);
        physicalBodyEntity.setGameProfile(playerEntity.getGameProfile());
        //Store player UUID to body entity and give it a name
        playerEntity.getCapability(BodyLinkProvider.BODY_LINK_CAPABILITY).ifPresent(cap -> {
            cap.setLinkedBodyID(physicalBodyEntity);
            cap.setDimensionID(playerEntity.dimension.getId());
        });
        physicalBodyEntity.setName(playerEntity.getScoreboardName());

        //Insert main inventory to body and clear
        if (!playerEntity.getEntityWorld().isRemote()) {

            final LazyOptional<IPsychicInventory> psychicInventory = AstralAPI.getOverworldPsychicInventory((ServerWorld) playerEntity.getEntityWorld());
            final Boolean goingToInnerRealm = playerEntity.getCapability(AstralAPI.sleepManagerCapability).map(ISleepManager::isGoingToInnerRealm).orElse(false);

            psychicInventory.ifPresent(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(playerEntity.getUniqueID()).setInventoryType(Boolean.TRUE.equals(goingToInnerRealm) ? InventoryType.INNER_REALM : InventoryType.ASTRAL, playerEntity.inventory));
        }
        physicalBodyEntity.setHealth(playerEntity.getHealth());
        physicalBodyEntity.setHungerLevel(playerEntity.getFoodStats().getFoodLevel());
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
    public static void startTrackingAstralEntity(PlayerEvent.StartTracking event) {
        if (event.getTarget().isLiving()) {
            LivingEntity livingTarget = event.getEntityLiving();
            if (livingTarget.isPotionActive(AstralEffects.ASTRAL_TRAVEL)) {
                AstralNetwork.sendAstralEffectStarting(livingTarget.getActivePotionEffect(AstralEffects.ASTRAL_TRAVEL), event.getEntity());
            }
        }
    }

    public static void setPlayerMaxHealthTo(PlayerEntity playerEntity, float newMaxHealth) {
        playerEntity.getAttribute(SharedMonsterAttributes.MAX_HEALTH).removeModifier(healthId);
        float healthModifier = newMaxHealth - playerEntity.getMaxHealth();
        playerEntity.getAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier(healthId, "physical body health", healthModifier, AttributeModifier.Operation.ADDITION));
        if (playerEntity.getHealth() > newMaxHealth) {
            playerEntity.setHealth(newMaxHealth);
        }
    }

    /**
     * Resets the player entity's stats to what the physical body has
     *
     * @param playerEntity       The player entity to recieve stats from the physical body
     * @param physicalBodyEntity The physical body storing player stats
     */
    public static void resetPlayerStats(PlayerEntity playerEntity, PhysicalBodyEntity physicalBodyEntity) {
        playerEntity.getAttribute(SharedMonsterAttributes.MAX_HEALTH).removeModifier(healthId);
        playerEntity.setHealth(physicalBodyEntity.getHealth());
        playerEntity.getFoodStats().setFoodLevel((int) physicalBodyEntity.getHungerLevel());
    }

    public static void resetPlayerStats(PlayerEntity playerEntity) {
        playerEntity.getAttribute(SharedMonsterAttributes.MAX_HEALTH).removeModifier(healthId);
    }

    @SubscribeEvent
    public static void astralPickupEvent(EntityItemPickupEvent event) {
        World world = event.getEntityLiving().world;
        if (!world.isRemote() && isAstralTravelActive(event.getEntityLiving()) && !AstralTags.ASTRAL_PICKUP.contains(event.getItem().getItem().getItem())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void astralDeath(LivingDeathEvent event) {
        if (isAstralTravelActive(event.getEntityLiving()) && event.getEntityLiving() instanceof PlayerEntity && !event.getEntityLiving().getEntityWorld().isRemote()) {
            event.getEntityLiving().removePotionEffect(AstralEffects.ASTRAL_TRAVEL);
            event.setCanceled(true);
        }
    }

    /**
     * Make hearts white and remove the hunger bar when the player has the Astral potion effect
     *
     * @param event The game overlay render event to be cancelled if Astral Travel is active and the event is rendering food or health
     */
    @SubscribeEvent(receiveCanceled = true)
    public static void astralHUDRendering(RenderGameOverlayEvent.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.getRenderViewEntity() instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) minecraft.getRenderViewEntity();
            if (isAstralTravelActive(playerEntity)) {
                //Cancel rendering of hunger bar
                if (event.getType() == RenderGameOverlayEvent.ElementType.FOOD) {
                    event.setCanceled(true);
                }
                if (event.getType() == RenderGameOverlayEvent.ElementType.HEALTH) {
                    event.setCanceled(true);
                    AstralRendering.renderAstralHearts(minecraft, playerEntity);
                }
            }
            playerEntity.getCapability(AstralAPI.sleepManagerCapability).ifPresent(iSleepManager -> {
                if (playerEntity.isPotionActive(AstralEffects.ASTRAL_TRAVEL) && !iSleepManager.isEntityTraveling()) {
                    AstralRendering.renderAstralScreenFade(iSleepManager.getSleep());
                }
            });
        }
    }

}