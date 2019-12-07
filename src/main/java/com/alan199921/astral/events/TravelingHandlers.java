package com.alan199921.astral.events;

import com.alan199921.astral.Astral;
import com.alan199921.astral.capabilities.bodylink.BodyLinkProvider;
import com.alan199921.astral.effects.AstralEffects;
import com.alan199921.astral.entities.AstralEntityRegistry;
import com.alan199921.astral.entities.PhysicalBodyEntity;
import com.alan199921.astral.network.AstralNetwork;
import com.alan199921.astral.tags.AstralBlockTags;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Effect;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = Astral.MOD_ID)
public class TravelingHandlers {
    private static final UUID healthId = UUID.fromString("8bce997a-4c3a-11e6-beb8-9e71128cae77");

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
     * @param event The LivingAttackEvent supplying the target, source, and damage
     */
    @SubscribeEvent
    public static void replacePhysicalWithAstralDamage(LivingAttackEvent event){
        if (event.getSource().getTrueSource() != null && event.getSource().getTrueSource().isLiving()){
            LivingEntity trueSource = (LivingEntity) event.getSource().getTrueSource();
            LivingEntity target = event.getEntityLiving();
            String damageType = event.getSource().damageType;
            boolean isAstralTravelActive = target.isPotionActive(AstralEffects.ASTRAL_TRAVEL_EFFECT);
            if (!isAstralTravelActive && AstralDamage.isAstralDamage(damageType)) {
                event.setCanceled(true);
            } else if (trueSource.isPotionActive(AstralEffects.ASTRAL_TRAVEL_EFFECT) && !AstralDamage.isAstralDamage(damageType)) {
                event.setCanceled(true);
                target.attackEntityFrom(new AstralDamage(trueSource), trueSource.getActivePotionEffect(AstralEffects.ASTRAL_TRAVEL_EFFECT).getAmplifier() + 1.0F);
            } else if (isAstralTravelActive && !AstralDamage.isAstralDamage(damageType)) {
                event.setCanceled(true);
            }
        }
    }

    //Function for detecting if an Astral entity is interacting with a non astral entity
    private static boolean isAstralVsNonAstral(LivingEntity mobA, LivingEntity mobB) {
        if (mobA == null || mobB == null) {
            return false;
        }
        return mobA.isPotionActive(AstralEffects.ASTRAL_TRAVEL_EFFECT) && !mobB.isPotionActive(AstralEffects.ASTRAL_TRAVEL_EFFECT) || !mobA.isPotionActive(AstralEffects.ASTRAL_TRAVEL_EFFECT) && mobB.isPotionActive(AstralEffects.ASTRAL_TRAVEL_EFFECT);
    }

    @SubscribeEvent
    public static void renderAstralEntities(RenderLivingEvent event) {
        if (!Minecraft.getInstance().player.isPotionActive(AstralEffects.ASTRAL_TRAVEL_EFFECT) && event.getEntity().isPotionActive(AstralEffects.ASTRAL_TRAVEL_EFFECT)) {
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
        if (potionEffect.equals(AstralEffects.ASTRAL_TRAVEL_EFFECT) && entityLiving instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) entityLiving;
            //Revoke flight mode capabilities
            if (!playerEntity.abilities.isCreativeMode) {
                playerEntity.abilities.allowFlying = false;
                playerEntity.noClip = false;
                playerEntity.abilities.setFlySpeed(.05F);
                playerEntity.abilities.allowEdit = true;
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
                    transferInventoryToPlayer(playerEntity, serverWorld, physicalBodyEntity);
                    resetPlayerHealth(playerEntity, physicalBodyEntity);
                    physicalBodyEntity.onKillCommand();
                });
            }
        }
        if (potionEffect.equals(AstralEffects.ASTRAL_TRAVEL_EFFECT) && !entityLiving.getEntityWorld().isRemote()) {
            AstralNetwork.sendAstralEffectEnding(entityLiving);
        }
    }

    private static void transferInventoryToPlayer(PlayerEntity playerEntity, ServerWorld serverWorld, PhysicalBodyEntity physicalBodyEntity) {
        physicalBodyEntity.getMainInventory().forEach(item -> {
            if (playerEntity.inventory.getFirstEmptyStack() != -1) {
                playerEntity.addItemStackToInventory(item);
            } else {
                Block.spawnAsEntity(serverWorld, physicalBodyEntity.getPosition(), item);
            }
        });
        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            ItemStack physicalBodyArmorItemStack = physicalBodyEntity.getItemStackFromSlot(slot);
            if (!slot.equals(EquipmentSlotType.MAINHAND) && playerEntity.inventory.armorItemInSlot(slot.getIndex()).getItem() == Items.AIR) {
                playerEntity.setItemStackToSlot(slot, physicalBodyArmorItemStack);
            } else if (playerEntity.inventory.getFirstEmptyStack() != -1) {
                playerEntity.inventory.addItemStackToInventory(physicalBodyArmorItemStack);
            } else {
                Block.spawnAsEntity(serverWorld, physicalBodyEntity.getPosition(), physicalBodyArmorItemStack);
            }
            physicalBodyEntity.setItemStackToSlot(slot, ItemStack.EMPTY);
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
        if (event.getPotionEffect().getPotion().equals(AstralEffects.ASTRAL_TRAVEL_EFFECT) && event.getEntityLiving() instanceof PlayerEntity && !event.getEntityLiving().isPotionActive(AstralEffects.ASTRAL_TRAVEL_EFFECT)) {
            //Give player flight
            PlayerEntity p = (PlayerEntity) event.getEntityLiving();
            if (!p.abilities.isCreativeMode) {
                p.abilities.allowFlying = true;
                p.noClip = true;
                p.abilities.setFlySpeed(.05F * (event.getPotionEffect().getAmplifier() + 1));
                p.abilities.allowEdit = false;
                p.sendPlayerAbilities();
            }
            if (!p.getEntityWorld().isRemote()) {
                PhysicalBodyEntity physicalBodyEntity = (PhysicalBodyEntity) AstralEntityRegistry.PHYSICAL_BODY_ENTITY.spawn(p.getEntityWorld(), ItemStack.EMPTY, p, p.getPosition(), SpawnReason.TRIGGERED, false, false);
                physicalBodyEntity.setGameProfile(p.getGameProfile());
                //Store player UUID to body entity and give it a name
                p.getCapability(BodyLinkProvider.BODY_LINK_CAPABILITY).ifPresent(cap -> {
                    cap.setLinkedBodyID(physicalBodyEntity);
                    cap.setDimensionID(p.dimension.getId());
                });
                physicalBodyEntity.setName(event.getEntity().getScoreboardName());

                //Insert main inventory to body and clear
                moveInventoryToMob(event, physicalBodyEntity);
                physicalBodyEntity.setHealth(p.getHealth());
            }
        }
        if (event.getPotionEffect().getPotion().equals(AstralEffects.ASTRAL_TRAVEL_EFFECT) && !event.getEntityLiving().getEntityWorld().isRemote()) {
            AstralNetwork.sendAstralEffectStarting(event.getPotionEffect(), event.getEntity());
        }
    }

    private static void moveInventoryToMob(PotionEvent.PotionAddedEvent event, PhysicalBodyEntity physicalBodyEntity) {
        int i = 0;
        for (ItemStack stack : ((PlayerEntity) event.getEntityLiving()).inventory.mainInventory) {
            physicalBodyEntity.getMainInventory().set(i++, stack);
        }
        ((PlayerEntity) event.getEntityLiving()).inventory.mainInventory.clear();

        //Insert armor and offhand to entity
        for (EquipmentSlotType slotType : EquipmentSlotType.values()) {
            physicalBodyEntity.setItemStackToSlot(slotType, event.getEntityLiving().getItemStackFromSlot(slotType));
        }
        ((PlayerEntity) event.getEntityLiving()).inventory.armorInventory.clear();
        ((PlayerEntity) event.getEntityLiving()).inventory.offHandInventory.clear();
    }

    @SubscribeEvent
    public static void astralBlockInteraction(PlayerInteractEvent.RightClickBlock event) {
        if (event.getPlayer().isPotionActive(AstralEffects.ASTRAL_TRAVEL_EFFECT)) {
            Block targetedBlock = event.getWorld().getBlockState(event.getPos()).getBlock();
            event.setCanceled(!AstralBlockTags.ASTRAL_INTERACT.contains(targetedBlock));
        }
    }

    @SubscribeEvent
    public static void astralBreakBlock(PlayerEvent.BreakSpeed event) {
        //Placeholder properties
        if (!AstralBlockTags.ASTRAL_INTERACT.contains(event.getState().getBlock()) && event.getPlayer().isPotionActive(AstralEffects.ASTRAL_TRAVEL_EFFECT)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void startTrackingAstralEntity(PlayerEvent.StartTracking event) {
        if (event.getTarget().isLiving()) {
            LivingEntity livingTarget = event.getEntityLiving();
            if (livingTarget.isPotionActive(AstralEffects.ASTRAL_TRAVEL_EFFECT)) {
                AstralNetwork.sendAstralEffectStarting(livingTarget.getActivePotionEffect(AstralEffects.ASTRAL_TRAVEL_EFFECT), event.getEntity());
            }
        }
    }

    public static void setPlayerMaxHealthTo(PlayerEntity playerEntity, float newMaxHealth) {
        playerEntity.getAttribute(SharedMonsterAttributes.MAX_HEALTH).removeModifier(healthId);
        float healthModifier = newMaxHealth - playerEntity.getMaxHealth();
        playerEntity.getAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier(healthId, "physical body health", healthModifier, AttributeModifier.Operation.ADDITION));
    }

    public static void resetPlayerHealth(PlayerEntity playerEntity, PhysicalBodyEntity physicalBodyEntity) {
        playerEntity.getAttribute(SharedMonsterAttributes.MAX_HEALTH).removeModifier(healthId);
        playerEntity.setHealth(physicalBodyEntity.getHealth());
    }

    @SubscribeEvent
    public static void astralPickupEvent(EntityItemPickupEvent event) {
        World world = event.getEntityLiving().world;
        if (!world.isRemote() && event.getEntityLiving().isPotionActive(AstralEffects.ASTRAL_TRAVEL_EFFECT) && !AstralBlockTags.ASTRAL_PICKUP.contains(event.getItem().getItem().getItem())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void astralDeath(LivingDeathEvent event) {
        if (event.getEntityLiving().isPotionActive(AstralEffects.ASTRAL_TRAVEL_EFFECT) && event.getEntityLiving() instanceof PlayerEntity) {
            event.setCanceled(true);
            event.getEntityLiving().removeActivePotionEffect(AstralEffects.ASTRAL_TRAVEL_EFFECT);
        }
    }
}