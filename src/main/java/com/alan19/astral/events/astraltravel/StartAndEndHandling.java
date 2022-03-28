package com.alan19.astral.events.astraltravel;

import com.alan19.astral.Astral;
import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.api.psychicinventory.IPsychicInventory;
import com.alan19.astral.api.psychicinventory.InventoryType;
import com.alan19.astral.effects.AstralEffects;
import com.alan19.astral.effects.AstralTravelEffect;
import com.alan19.astral.entity.AstralEntities;
import com.alan19.astral.entity.physicalbody.PhysicalBodyEntity;
import com.alan19.astral.network.AstralNetwork;
import com.alan19.astral.util.Constants;
import net.minecraft.network.protocol.game.ClientboundRemoveMobEffectPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.alan19.astral.api.bodytracker.BodyTracker.HEALTH_ID;

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
        if (event.getPotionEffect().getEffect().equals(AstralEffects.ASTRAL_TRAVEL.get()) && event.getEntityLiving() instanceof Player && !event.getEntityLiving().hasEffect(AstralEffects.ASTRAL_TRAVEL.get())) {
            Player playerEntity = (Player) event.getEntityLiving();
            playerEntity.getEntityData().set(Entity.DATA_POSE, Pose.SLEEPING);
            if (!playerEntity.getCommandSenderWorld().isClientSide()) {
                //Only apply modifier if it does not exist
                if (!playerEntity.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).hasModifier(Constants.DISABLES_GRAVITY)) {
                    playerEntity.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).addPermanentModifier(Constants.DISABLES_GRAVITY);
                }
                //Reset the sleep manager
                playerEntity.getCapability(AstralAPI.sleepManagerCapability).ifPresent(sleepManager -> {
                    sleepManager.resetSleep();
                    if (playerEntity instanceof ServerPlayer && !playerEntity.level.isClientSide()) {
                        final ServerPlayer serverPlayerEntity = (ServerPlayer) playerEntity;
                        //Send Astral Travel is starting to client to display fade effect
                        AstralNetwork.sendClientAstralTravelStart(serverPlayerEntity, sleepManager);
                        serverPlayerEntity.connection.send(new ClientboundUpdateMobEffectPacket(serverPlayerEntity.getId(), event.getPotionEffect()));
                    }
                });
            }
            //Apply Astral Travel to entity on client
            if (event.getEntityLiving().level instanceof ServerLevel) {
                AstralNetwork.sendAstralEffectStarting(event.getPotionEffect(), event.getEntity());
            }
        }
    }

    /**
     * When the Astral Travel potion effect ends, remove the player's flying abilities, teleport them to the body,
     * transfer the body's inventory into the player's inventory, and then kill it.
     *
     * @param entityLiving The entity that with the potion effect
     */
    public static void astralTravelEnd(LivingEntity entityLiving) {
        if (entityLiving instanceof Player) {
            Player playerEntity = (Player) entityLiving;
            if (playerEntity.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).hasModifier(Constants.DISABLES_GRAVITY)) {
                playerEntity.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).removeModifier(Constants.DISABLES_GRAVITY);
            }
            //Only run serverside
            if (!playerEntity.getCommandSenderWorld().isClientSide()) {
                //Get server versions of world and player
                ServerPlayer serverPlayerEntity = (ServerPlayer) playerEntity;
                ServerLevel serverWorld = serverPlayerEntity.getLevel();
                AstralAPI.getBodyTracker(serverWorld).ifPresent(cap -> cap.mergePlayerWithBody(serverPlayerEntity, serverWorld));
                serverPlayerEntity.connection.send(new ClientboundRemoveMobEffectPacket(serverPlayerEntity.getId(), AstralEffects.ASTRAL_TRAVEL.get()));
                AstralNetwork.sendClientAstralTravelEnd(serverPlayerEntity);
            }
        }
        if (!entityLiving.getCommandSenderWorld().isClientSide()) {
            AstralNetwork.sendAstralEffectEnding(entityLiving);
        }
    }

    @SubscribeEvent
    public static void astralTravelExpire(PotionEvent.PotionExpiryEvent event) {
        if (event.getPotionEffect() != null && event.getPotionEffect().getEffect() instanceof AstralTravelEffect) {
            astralTravelEnd(event.getEntityLiving());
        }
    }

    @SubscribeEvent
    public static void astralTravelRemove(PotionEvent.PotionRemoveEvent event) {
        if (event.getPotionEffect() != null && event.getPotionEffect().getEffect() instanceof AstralTravelEffect) {
            astralTravelEnd(event.getEntityLiving());
        }
    }

    /**
     * Makes sure fade effect is not triggered when logging in
     *
     * @param event The player logged in event
     */
    @SubscribeEvent
    public static void sendCapsToPlayer(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getPlayer() instanceof ServerPlayer) {
            final ServerPlayer player = (ServerPlayer) event.getPlayer();
            AstralAPI.getSleepManager(event.getPlayer()).ifPresent(sleepManager -> AstralNetwork.sendClientAstralTravelStart(player, sleepManager));
        }
    }

    public static void spawnPhysicalBody(ServerPlayer playerEntity) {
        if (playerEntity != null) {
            PhysicalBodyEntity physicalBodyEntity = (PhysicalBodyEntity) AstralEntities.PHYSICAL_BODY_ENTITY.get().spawn(playerEntity.getLevel(), ItemStack.EMPTY, playerEntity, playerEntity.blockPosition(), MobSpawnType.TRIGGERED, false, false);
            if (physicalBodyEntity != null) {
                if (playerEntity.getMaxHealth() > physicalBodyEntity.getMaxHealth()) {
                    physicalBodyEntity.getAttribute(Attributes.MAX_HEALTH).removeModifier(HEALTH_ID);
                    float healthModifier = playerEntity.getMaxHealth() - physicalBodyEntity.getMaxHealth();
                    physicalBodyEntity.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier(HEALTH_ID, "physical body health", healthModifier, AttributeModifier.Operation.ADDITION));
                }

                physicalBodyEntity.setHealth(playerEntity.getHealth());
                physicalBodyEntity.setHungerLevel(playerEntity.getFoodData().getFoodLevel());

                //Store player UUID to body entity and give it a name
                physicalBodyEntity.setGameProfile(playerEntity.getGameProfile());
                physicalBodyEntity.setName(playerEntity.getScoreboardName());

                //Insert main inventory to body and clear
                if (!playerEntity.getCommandSenderWorld().isClientSide()) {

                    final LazyOptional<IPsychicInventory> psychicInventory = AstralAPI.getOverworldPsychicInventory((ServerLevel) playerEntity.getCommandSenderWorld());

                    psychicInventory.ifPresent(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(playerEntity.getUUID()).setInventoryType(InventoryType.ASTRAL, playerEntity.inventory));
                }
            }
        }
    }

    @SubscribeEvent
    public static void startTrackingAstralEntity(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof LivingEntity) {
            LivingEntity livingTarget = event.getEntityLiving();
            if (livingTarget.hasEffect(AstralEffects.ASTRAL_TRAVEL.get())) {
                AstralNetwork.sendAstralEffectStarting(livingTarget.getEffect(AstralEffects.ASTRAL_TRAVEL.get()), event.getEntity());
            }
        }
    }
}
