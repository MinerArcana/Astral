package com.alan199921.astral.events;

import com.alan199921.astral.Astral;
import com.alan199921.astral.blocks.AstralMeridian;
import com.alan199921.astral.capabilities.bodylink.BodyLinkProvider;
import com.alan199921.astral.effects.AstralEffects;
import com.alan199921.astral.entities.PhysicalBodyEntity;
import com.alan199921.astral.entities.PhysicalBodyRegistry;
import com.alan199921.astral.network.AstralNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = Astral.MOD_ID)
public class TravellingHandlers {
    @SubscribeEvent
    public static void doNotTargetAstrals(LivingSetAttackTargetEvent event) {
        if (event.getEntityLiving() instanceof MobEntity && isAstralVsNonAstral(event.getTarget(), event.getEntityLiving())) {
            MobEntity mobEntity = (MobEntity) event.getEntityLiving();
            mobEntity.setAttackTarget(null);
        }
    }

    /**
     * An Astral entity only takes damage form other Astral Entities or Magic/Astral damage. An Astral entity can only damage non-astral entities
     *
     * @param event The LivingAttackEvent
     */
    @SubscribeEvent
    public static void nullifyAstralDamage(LivingAttackEvent event) {
        boolean isDamageTypeNotAstral = !event.getSource().getDamageType().equals("astral");
        boolean isDamageSourceNotMagic = !event.getSource().isMagicDamage();
        if (isDamageSourceNotMagic && isDamageTypeNotAstral && isAstralVsNonAstral((LivingEntity) event.getSource().getTrueSource(), event.getEntityLiving())){
            event.setCanceled(true);
        }
    }

    //Function for detecting if an Astral entity is interacting with a non astral entity
    private static boolean isAstralVsNonAstral(LivingEntity mobA, LivingEntity mobB) {
        if (mobA == null || mobB == null) {
            return false;
        }
        return mobA.isPotionActive(AstralEffects.astralTravelEffect) && !mobB.isPotionActive(AstralEffects.astralTravelEffect) || !mobA.isPotionActive(AstralEffects.astralTravelEffect) && mobB.isPotionActive(AstralEffects.astralTravelEffect);
    }

    @SubscribeEvent
    public static void renderAstralEntities(RenderLivingEvent event) {
        if (!Minecraft.getInstance().player.isPotionActive(AstralEffects.astralTravelEffect) && event.getEntity().isPotionActive(AstralEffects.astralTravelEffect)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void travelEffectExpire(PotionEvent.PotionExpiryEvent event) {
        handleAstralEffectEnd(event.getPotionEffect().getPotion(), event.getEntityLiving());
    }

    @SubscribeEvent
    public static void travelEffectRemove(PotionEvent.PotionRemoveEvent event) {
        handleAstralEffectEnd(event.getPotionEffect().getPotion(), event.getEntityLiving());
    }

    /**
     * When the Astral Travel potion effect ends, remove the player's flying abilities, teleport them to the body,
     * transfer the body's inventory into the player's inventory, and then kill it.
     *
     * @param potionEffect The potion effect that is ending
     * @param entityLiving The entity that with the potion effect
     */
    private static void handleAstralEffectEnd(Effect potionEffect, LivingEntity entityLiving) {
        if (potionEffect.equals(AstralEffects.astralTravelEffect) && entityLiving instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) entityLiving;
            //Revoke flight mode capabilities
            if (!playerEntity.abilities.isCreativeMode) {
                playerEntity.abilities.allowFlying = false;
                playerEntity.noClip = false;
                playerEntity.abilities.setFlySpeed(.05F);
                playerEntity.sendPlayerAbilities();
            }
            //Only run serverside
            if (!playerEntity.getEntityWorld().isRemote()) {
                //Get server versions of world and player
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) playerEntity;
                ServerWorld serverWorld = serverPlayerEntity.getServerWorld();

                //Retrieve the body entity object  from the capability
                playerEntity.getCapability(BodyLinkProvider.BODY_LINK_CAPABILITY).ifPresent(cap -> {
                    PhysicalBodyEntity body = (PhysicalBodyEntity) cap.getLinkedEntity(serverWorld);

                    //Teleport the player
                    serverPlayerEntity.teleport(serverWorld.getServer().getWorld(DimensionType.getById(cap.getDimensionID())), body.lastTickPosX, body.lastTickPosY, body.lastTickPosZ, serverPlayerEntity.rotationYaw, serverPlayerEntity.rotationPitch);

                    //Get the inventory and transfer items
                    PhysicalBodyEntity physicalBodyEntity = (PhysicalBodyEntity) cap.getLinkedEntity(serverWorld);
                    physicalBodyEntity.getInventory().forEach(playerEntity::addItemStackToInventory);
                    for (EquipmentSlotType slot : EquipmentSlotType.values()) {
                        if (!slot.equals(EquipmentSlotType.MAINHAND)) {
                            playerEntity.setItemStackToSlot(slot, physicalBodyEntity.getItemStackFromSlot(slot));
                        }
                    }
                    physicalBodyEntity.onKillCommand();
                });
            }
        }
        if (potionEffect.equals(AstralEffects.astralTravelEffect) && !entityLiving.getEntityWorld().isRemote()){
            AstralNetwork.sendAstralEffectEnding(entityLiving);
        }
    }

    /**
     * When the player gets access to the Astral travel effect, give them the ability to fly, and transfer their
     * inventory into the physical body mob
     *
     * @param event The event that contains information about the player and the effect applied
     */
    @SubscribeEvent
    public static void travelEffectActivate(PotionEvent.PotionAddedEvent event) {
        if (event.getPotionEffect().getPotion().equals(AstralEffects.astralTravelEffect) && event.getEntityLiving() instanceof PlayerEntity && !event.getEntityLiving().isPotionActive(AstralEffects.astralTravelEffect)) {
            //Give player flight
            PlayerEntity p = (PlayerEntity) event.getEntityLiving();
            if (!p.abilities.isCreativeMode) {
                p.abilities.allowFlying = true;
                p.noClip = true;
                p.abilities.setFlySpeed(.05F * (event.getPotionEffect().getAmplifier() + 1));
                p.sendPlayerAbilities();
            }
            if (!p.getEntityWorld().isRemote()) {
                PhysicalBodyEntity physicalBodyEntity = (PhysicalBodyEntity) PhysicalBodyRegistry.PHYSICAL_BODY_ENTITY.spawn(p.getEntityWorld(), ItemStack.EMPTY, p, p.getPosition(), SpawnReason.TRIGGERED, false, false);
                //Store player UUID to body entity and give it a name
                p.getCapability(BodyLinkProvider.BODY_LINK_CAPABILITY).ifPresent(cap -> {
                    cap.setLinkedBodyID(physicalBodyEntity);
                    cap.setDimensionID(p.dimension.getId());
                });
                physicalBodyEntity.setName(event.getEntity().getScoreboardName());

                //Insert main inventory to body and clear
                int i = 0;
                for (ItemStack stack : ((PlayerEntity) event.getEntityLiving()).inventory.mainInventory) {
                    physicalBodyEntity.insertItem(i++, stack, false);
                }
                ((PlayerEntity) event.getEntityLiving()).inventory.mainInventory.clear();

                //Insert armor and offhand to entity
                for (EquipmentSlotType slotType : EquipmentSlotType.values()) {
                    physicalBodyEntity.setItemStackToSlot(slotType, event.getEntityLiving().getItemStackFromSlot(slotType));
                }
                ((PlayerEntity) event.getEntityLiving()).inventory.armorInventory.clear();
                ((PlayerEntity) event.getEntityLiving()).inventory.offHandInventory.clear();
            }
        }
        if (event.getPotionEffect().getPotion().equals(AstralEffects.astralTravelEffect) && !event.getEntityLiving().getEntityWorld().isRemote()){
            AstralNetwork.sendAstralEffectStarting(event.getPotionEffect(), event.getEntity());
        }
    }

    @SubscribeEvent
    public static void astralBreakBlock(PlayerEvent.BreakSpeed event) {
        //Placeholder properties
        if (!event.getState().getProperties().contains(AstralMeridian.ASTRAL_BLOCK) && event.getPlayer().isPotionActive(AstralEffects.astralTravelEffect)) {
            event.setNewSpeed(0f);
        }
    }

    @SubscribeEvent
    public static void startTrackingAstralEntity(PlayerEvent.StartTracking event) {
        if (event.getTarget().isLiving()) {
            LivingEntity livingTarget = event.getEntityLiving();
            if (livingTarget.isPotionActive(AstralEffects.astralTravelEffect)) {
                AstralNetwork.sendAstralEffectStarting(livingTarget.getActivePotionEffect(AstralEffects.astralTravelEffect), event.getEntity());
            }
        }
    }
}