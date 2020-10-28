package com.alan19.astral.events.astraltravel;

import com.alan19.astral.Astral;
import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.api.bodytracker.BodyInfo;
import com.alan19.astral.api.psychicinventory.IPsychicInventory;
import com.alan19.astral.api.psychicinventory.InventoryType;
import com.alan19.astral.effects.AstralEffects;
import com.alan19.astral.effects.AstralTravelEffect;
import com.alan19.astral.entity.AstralEntities;
import com.alan19.astral.entity.physicalbody.PhysicalBodyEntity;
import com.alan19.astral.network.AstralNetwork;
import com.alan19.astral.util.Constants;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.play.server.SPlayEntityEffectPacket;
import net.minecraft.network.play.server.SRemoveEntityEffectPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.alan19.astral.api.bodytracker.BodyTracker.HEALTH_ID;
import static net.minecraftforge.common.util.Constants.NBT;

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
        if (event.getPotionEffect().getPotion().equals(AstralEffects.ASTRAL_TRAVEL.get()) && event.getEntityLiving() instanceof PlayerEntity && !event.getEntityLiving().isPotionActive(AstralEffects.ASTRAL_TRAVEL.get())) {
            PlayerEntity playerEntity = (PlayerEntity) event.getEntityLiving();
            playerEntity.getDataManager().set(Entity.POSE, Pose.SLEEPING);
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
            //Apply Astral Travel to entity on client
            if (event.getEntityLiving().world instanceof ServerWorld) {
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
                AstralAPI.getBodyTracker(serverWorld).ifPresent(bodyLink -> bodyLink.mergeBodies(playerEntity, serverWorld));
                serverPlayerEntity.connection.sendPacket(new SRemoveEntityEffectPacket(serverPlayerEntity.getEntityId(), AstralEffects.ASTRAL_TRAVEL.get()));
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
            if (player.isPotionActive(AstralEffects.ASTRAL_TRAVEL.get())) {
                AstralAPI.getBodyTracker(serverWorld).ifPresent(tracker -> {
                    tracker.updatePlayer(player, serverWorld);
                    final BodyInfo info = tracker.getBodyTrackerMap().get(player.getUniqueID());
                    // TODO Investigate why this doens't work in the Inner Realm
                    if (info != null && !info.getBodyId().isPresent()){
                        spawnPhysicalBody(player, info);
                    }
                });
            }
        }
    }

    private static void spawnPhysicalBody(ServerPlayerEntity player, BodyInfo info) {
        PhysicalBodyEntity body = new PhysicalBodyEntity(player.getServer().getWorld(info.getDimensionType()), player.getGameProfile());
        body.setPosition(info.getPos().getX(), info.getPos().getY(), info.getPos().getZ());
        body.setHealth(info.getHealth());
        player.getServerWorld().addEntity(body);
    }

    public static void spawnPhysicalBody(PlayerEntity playerEntity) {
        PhysicalBodyEntity physicalBodyEntity = (PhysicalBodyEntity) AstralEntities.PHYSICAL_BODY_ENTITY.get().spawn(playerEntity.getEntityWorld(), ItemStack.EMPTY, playerEntity, playerEntity.getPosition(), SpawnReason.TRIGGERED, false, false);
        if (physicalBodyEntity != null) {
            if (playerEntity.getMaxHealth() > physicalBodyEntity.getMaxHealth()){
                physicalBodyEntity.getAttribute(SharedMonsterAttributes.MAX_HEALTH).removeModifier(HEALTH_ID);
                float healthModifier = playerEntity.getMaxHealth() - physicalBodyEntity.getMaxHealth();
                physicalBodyEntity.getAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier(HEALTH_ID, "physical body health", healthModifier, AttributeModifier.Operation.ADDITION));
            }

            physicalBodyEntity.setHealth(playerEntity.getHealth());
            physicalBodyEntity.setHungerLevel(playerEntity.getFoodStats().getFoodLevel());

            //Store player UUID to body entity and give it a name
            physicalBodyEntity.setGameProfile(playerEntity.getGameProfile());
            physicalBodyEntity.setName(playerEntity.getScoreboardName());

            //Insert main inventory to body and clear
            if (!playerEntity.getEntityWorld().isRemote()) {

                final LazyOptional<IPsychicInventory> psychicInventory = AstralAPI.getOverworldPsychicInventory((ServerWorld) playerEntity.getEntityWorld());

                psychicInventory.ifPresent(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(playerEntity.getUniqueID()).setInventoryType(InventoryType.ASTRAL, playerEntity.inventory));
            }

            if (physicalBodyEntity.getEntityWorld() instanceof ServerWorld) {
                AstralAPI.getBodyTracker((ServerWorld) physicalBodyEntity.getEntityWorld()).ifPresent(tracker -> tracker.setBodyInfo(new BodyInfo(physicalBodyEntity.getHealth(), physicalBodyEntity.getPosition(), physicalBodyEntity.isAlive(), physicalBodyEntity.dimension, physicalBodyEntity.getUniqueID()), playerEntity));
            }
        }
    }

    public static void  spawnPhysicalBody(PlayerEntity playerEntity, BodyInfo bodyInfo, CompoundNBT nbt) {
        nbt.putFloat("Health", bodyInfo.getHealth());
        final ListNBT pos = nbt.getList("Pos", NBT.TAG_DOUBLE);
        final PhysicalBodyEntity spawn = AstralEntities.PHYSICAL_BODY_ENTITY.get().spawn(playerEntity.getServer().getWorld(bodyInfo.getDimensionType()), nbt, null, playerEntity, new BlockPos(pos.getDouble(0), pos.getDouble(1), pos.getDouble(2)), SpawnReason.TRIGGERED, false, false);
        spawn.deserializeNBT(nbt);
    }

    @SubscribeEvent
    public static void startTrackingAstralEntity(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof LivingEntity) {
            LivingEntity livingTarget = event.getEntityLiving();
            if (livingTarget.isPotionActive(AstralEffects.ASTRAL_TRAVEL.get())) {
                AstralNetwork.sendAstralEffectStarting(livingTarget.getActivePotionEffect(AstralEffects.ASTRAL_TRAVEL.get()), event.getEntity());
            }
        }
    }
}
