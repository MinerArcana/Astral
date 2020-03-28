package com.alan199921.astral.events.astraltravel;

import com.alan199921.astral.Astral;
import com.alan199921.astral.api.AstralAPI;
import com.alan199921.astral.api.psychicinventory.IPsychicInventory;
import com.alan199921.astral.api.psychicinventory.InventoryType;
import com.alan199921.astral.api.sleepmanager.ISleepManager;
import com.alan199921.astral.effects.AstralEffects;
import com.alan199921.astral.effects.AstralTravelEffect;
import com.alan199921.astral.entities.AstralEntityRegistry;
import com.alan199921.astral.entities.PhysicalBodyEntity;
import com.alan199921.astral.network.AstralNetwork;
import com.alan199921.astral.util.Constants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPlayEntityEffectPacket;
import net.minecraft.network.play.server.SRemoveEntityEffectPacket;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Astral.MOD_ID)
public class StartAndEndHandling {
    /**
     * When the player gets access to the Astral travel effect, disable their gravity and tell the sleep manager that
     * they are falling asleep.
     * Notifies the client that an entity is getting Astral Travel
     *
     * @param event The event that contains information about the player and the effect applied
     */
    @SubscribeEvent
    public static void astralTravelAdded(PotionEvent.PotionAddedEvent event) {
        //Only players need the startup effects
        if (event.getPotionEffect().getPotion().equals(AstralEffects.ASTRAL_TRAVEL) && event.getEntityLiving() instanceof PlayerEntity && !event.getEntityLiving().isPotionActive(AstralEffects.ASTRAL_TRAVEL)) {
            PlayerEntity playerEntity = (PlayerEntity) event.getEntityLiving();
            if (!playerEntity.getEntityWorld().isRemote()) {
                //Only apply modifier if it does not exist
                if (!playerEntity.getAttribute(LivingEntity.ENTITY_GRAVITY).hasModifier(Constants.DISABLES_GRAVITY)) {
                    playerEntity.getAttribute(LivingEntity.ENTITY_GRAVITY).applyModifier(Constants.DISABLES_GRAVITY);
                }
                //Reset the sleep manager
                playerEntity.getCapability(AstralAPI.sleepManagerCapability).ifPresent(sleepManager -> {
                    sleepManager.resetSleep();
                    if (playerEntity instanceof ServerPlayerEntity && !playerEntity.world.isRemote()) {
                        final ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) playerEntity;
                        //Send Astral Travel is starting to client to display fade effect
                        AstralNetwork.sendClientAstralTravelStart(serverPlayerEntity, sleepManager);
                        serverPlayerEntity.connection.sendPacket(new SPlayEntityEffectPacket(serverPlayerEntity.getEntityId(), event.getPotionEffect()));
                    }
                });
            }
        }
        //Apply Astral Travel to entity on client
        if (event.getEntityLiving().world instanceof ServerWorld) {
            AstralNetwork.sendAstralEffectStarting(event.getPotionEffect(), event.getEntity());
        }
    }

    /**
     * When the Astral Travel potion effect ends, remove the player's flying abilities, teleport them to the body,
     * transfer the body's inventory into the player's inventory, and then kill it.
     *
     * @param entityLiving The entity that with the potion effect
     */
    private static void astralTravelEnd(LivingEntity entityLiving) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) entityLiving;
            if (playerEntity.getAttribute(LivingEntity.ENTITY_GRAVITY).hasModifier(Constants.DISABLES_GRAVITY)) {
                playerEntity.getAttribute(LivingEntity.ENTITY_GRAVITY).removeModifier(Constants.DISABLES_GRAVITY);
            }
            //Only run serverside
            if (!playerEntity.getEntityWorld().isRemote()) {
                //Get server versions of world and player
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) playerEntity;
                ServerWorld serverWorld = serverPlayerEntity.getServerWorld();
                AstralAPI.getBodyLinkCapability(serverWorld).ifPresent(iBodyLinkCapability -> iBodyLinkCapability.handleMergeWithBody(playerEntity.getUniqueID(), serverWorld));
                serverPlayerEntity.connection.sendPacket(new SRemoveEntityEffectPacket(serverPlayerEntity.getEntityId(), AstralEffects.ASTRAL_TRAVEL));
                AstralNetwork.sendClientAstralTravelEnd(serverPlayerEntity);
            }
        }
        if (!entityLiving.getEntityWorld().isRemote()) {
            AstralNetwork.sendAstralEffectEnding(entityLiving);
        }
    }

    @SubscribeEvent
    public static void astralTravelExpire(PotionEvent.PotionExpiryEvent event) {
        if (event.getPotionEffect() != null && event.getPotionEffect().getPotion() instanceof AstralTravelEffect) {
            astralTravelEnd(event.getEntityLiving());
        }
    }

    @SubscribeEvent
    public static void astralTravelRemove(PotionEvent.PotionRemoveEvent event) {
        if (event.getPotionEffect() != null && event.getPotionEffect().getPotion() instanceof AstralTravelEffect) {
            astralTravelEnd(event.getEntityLiving());
        }
    }

    /**
     * Makes sure fade effect is not triggered when logging in and updates player with the physical body's info
     *
     * @param event The player logged in event
     */
    @SubscribeEvent
    public static void sendCapsToPlayer(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getPlayer() instanceof ServerPlayerEntity) {
            final ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
            final ServerWorld serverWorld = player.getServerWorld();
            AstralAPI.getSleepManager(event.getPlayer()).ifPresent(sleepManager -> AstralNetwork.sendClientAstralTravelStart(player, sleepManager));
            if (player.isPotionActive(AstralEffects.ASTRAL_TRAVEL)) {
                AstralAPI.getBodyLinkCapability(serverWorld).ifPresent(iBodyLinkCapability -> iBodyLinkCapability.updatePlayer(player.getUniqueID(), serverWorld));
            }
        }
    }

    public static void spawnPhysicalBody(PlayerEntity playerEntity) {
        PhysicalBodyEntity physicalBodyEntity = (PhysicalBodyEntity) AstralEntityRegistry.PHYSICAL_BODY_ENTITY.spawn(playerEntity.getEntityWorld(), ItemStack.EMPTY, playerEntity, playerEntity.getPosition(), SpawnReason.TRIGGERED, false, false);
        if (physicalBodyEntity != null) {
            //Store player UUID to body entity and give it a name
            physicalBodyEntity.setGameProfile(playerEntity.getGameProfile());
            physicalBodyEntity.setName(playerEntity.getScoreboardName());

            //Insert main inventory to body and clear
            if (!playerEntity.getEntityWorld().isRemote()) {

                final LazyOptional<IPsychicInventory> psychicInventory = AstralAPI.getOverworldPsychicInventory((ServerWorld) playerEntity.getEntityWorld());
                final Boolean goingToInnerRealm = playerEntity.getCapability(AstralAPI.sleepManagerCapability).map(ISleepManager::isGoingToInnerRealm).orElseGet(() -> false);

                psychicInventory.ifPresent(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(playerEntity.getUniqueID()).setInventoryType(Boolean.TRUE.equals(goingToInnerRealm) ? InventoryType.INNER_REALM : InventoryType.ASTRAL, playerEntity.inventory));
            }
            physicalBodyEntity.setHealth(playerEntity.getHealth());
            physicalBodyEntity.setHungerLevel(playerEntity.getFoodStats().getFoodLevel());
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
}
