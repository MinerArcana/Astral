package com.alan199921.astral.events;

import com.alan199921.astral.Astral;
import com.alan199921.astral.capabilities.bodylink.BodyLinkProvider;
import com.alan199921.astral.dimensions.AstralDimensions;
import com.alan199921.astral.effects.AstralEffects;
import com.alan199921.astral.entities.AstralEntityRegistry;
import com.alan199921.astral.entities.PhysicalBodyEntity;
import com.alan199921.astral.network.AstralNetwork;
import com.alan199921.astral.tags.AstralTags;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
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
import java.util.stream.IntStream;

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
     *
     * @param event The LivingAttackEvent supplying the target, source, and damage
     */
    @SubscribeEvent
    public static void replacePhysicalWithAstralDamage(LivingAttackEvent event) {
        if (event.getSource().getTrueSource() instanceof LivingEntity) {
            LivingEntity trueSource = (LivingEntity) event.getSource().getTrueSource();
            LivingEntity target = event.getEntityLiving();
            DamageSource damageType = event.getSource();
            boolean isAstralTravelActive = target.isPotionActive(AstralEffects.ASTRAL_TRAVEL);
            if (!isAstralTravelActive && IAstralDamage.isDamageAstral(damageType)) {
                event.setCanceled(true);
            }
            else if (trueSource.isPotionActive(AstralEffects.ASTRAL_TRAVEL) && !IAstralDamage.isDamageAstral(damageType)) {
                event.setCanceled(true);
                target.attackEntityFrom(new AstralEntityDamage(trueSource), trueSource.getActivePotionEffect(AstralEffects.ASTRAL_TRAVEL).getAmplifier() + 1.0F);
            }
            else if (isAstralTravelActive && !IAstralDamage.isDamageAstral(damageType)) {
                event.setCanceled(true);
            }
        }
        //Check for astral damage vs. non astral and vice versa
        else {
            if ((!(IAstralDamage.isDamageAstral(event.getSource()) || event.getSource().isMagicDamage() || event.getSource().isDamageAbsolute()) && event.getEntityLiving().isPotionActive(AstralEffects.ASTRAL_TRAVEL) || IAstralDamage.isDamageAstral(event.getSource()) && !event.getEntityLiving().isPotionActive(AstralEffects.ASTRAL_TRAVEL))) {
                event.setCanceled(true);
            }
        }
    }

    //Function for detecting if an Astral entity is interacting with a non astral entity
    private static boolean isAstralVsNonAstral(LivingEntity mobA, LivingEntity mobB) {
        if (mobA == null || mobB == null) {
            return false;
        }
        return mobA.isPotionActive(AstralEffects.ASTRAL_TRAVEL) && !mobB.isPotionActive(AstralEffects.ASTRAL_TRAVEL) || !mobA.isPotionActive(AstralEffects.ASTRAL_TRAVEL) && mobB.isPotionActive(AstralEffects.ASTRAL_TRAVEL);
    }

    @SubscribeEvent
    public static void renderAstralEntities(RenderLivingEvent event) {
        if (!Minecraft.getInstance().player.isPotionActive(AstralEffects.ASTRAL_TRAVEL) && event.getEntity().isPotionActive(AstralEffects.ASTRAL_TRAVEL)) {
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
        if (potionEffect.equals(AstralEffects.ASTRAL_TRAVEL) && entityLiving instanceof PlayerEntity) {
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
                    transferInventoryToPlayer(playerEntity, serverWorld, physicalBodyEntity);
                    resetPlayerStats(playerEntity, physicalBodyEntity);
                    physicalBodyEntity.onKillCommand();
                });
            }
        }
        if (potionEffect.equals(AstralEffects.ASTRAL_TRAVEL) && !entityLiving.getEntityWorld().isRemote()) {
            AstralNetwork.sendAstralEffectEnding(entityLiving);
        }
    }

    private static void transferInventoryToPlayer(PlayerEntity playerEntity, ServerWorld serverWorld, PhysicalBodyEntity physicalBodyEntity) {
        if (!playerEntity.getEntityWorld().isRemote()) {
            IntStream.range(0, physicalBodyEntity.getMainInventory().getSlots()).forEach(i -> {
                if (playerEntity.inventory.getStackInSlot(i) == ItemStack.EMPTY) {
                    playerEntity.inventory.setInventorySlotContents(i, physicalBodyEntity.getMainInventory().getStackInSlot(i));
                }
                else if (playerEntity.inventory.getFirstEmptyStack() != -1) {
                    playerEntity.inventory.addItemStackToInventory(physicalBodyEntity.getMainInventory().getStackInSlot(i));
                }
                else {
                    Block.spawnAsEntity(serverWorld, physicalBodyEntity.getPosition(), physicalBodyEntity.getMainInventory().getStackInSlot(i));
                }
                physicalBodyEntity.getMainInventory().setStackInSlot(i, ItemStack.EMPTY);
            });
            for (EquipmentSlotType slot : EquipmentSlotType.values()) {
                ItemStack physicalBodyArmorItemStack = physicalBodyEntity.getItemStackFromSlot(slot);
                if (!slot.equals(EquipmentSlotType.MAINHAND) && playerEntity.inventory.armorItemInSlot(slot.getIndex()) == ItemStack.EMPTY) {
                    playerEntity.setItemStackToSlot(slot, physicalBodyArmorItemStack);
                }
                else if (playerEntity.inventory.getFirstEmptyStack() != -1) {
                    playerEntity.inventory.addItemStackToInventory(physicalBodyArmorItemStack);
                }
                else {
                    Block.spawnAsEntity(serverWorld, physicalBodyEntity.getPosition(), physicalBodyArmorItemStack);
                }
                physicalBodyEntity.setItemStackToSlot(slot, ItemStack.EMPTY);
            }
        }
    }

    /**
     * When the player gets access to the Astral travel effect, give them the ability to fly, and transfer their inventory into the physical body mob
     *
     * @param event The event that contains information about the player and the effect applied
     */
    @SubscribeEvent
    public static void travelEffectActivate(PotionEvent.PotionAddedEvent event) {
        if (event.getPotionEffect().getPotion().equals(AstralEffects.ASTRAL_TRAVEL) && event.getEntityLiving() instanceof PlayerEntity && !event.getEntityLiving().isPotionActive(AstralEffects.ASTRAL_TRAVEL)) {
            //Give player flight
            PlayerEntity playerEntity = (PlayerEntity) event.getEntityLiving();
            if (!playerEntity.abilities.isCreativeMode) {
                playerEntity.abilities.allowFlying = true;
                playerEntity.noClip = true;
                playerEntity.abilities.setFlySpeed(.05F * (event.getPotionEffect().getAmplifier() + 1));
                playerEntity.sendPlayerAbilities();
            }
            if (!playerEntity.getEntityWorld().isRemote()) {
                PhysicalBodyEntity physicalBodyEntity = (PhysicalBodyEntity) AstralEntityRegistry.PHYSICAL_BODY_ENTITY.spawn(playerEntity.getEntityWorld(), ItemStack.EMPTY, playerEntity, playerEntity.getPosition(), SpawnReason.TRIGGERED, false, false);
                physicalBodyEntity.setGameProfile(playerEntity.getGameProfile());
                //Store player UUID to body entity and give it a name
                playerEntity.getCapability(BodyLinkProvider.BODY_LINK_CAPABILITY).ifPresent(cap -> {
                    cap.setLinkedBodyID(physicalBodyEntity);
                    cap.setDimensionID(playerEntity.dimension.getId());
                });
                physicalBodyEntity.setName(event.getEntity().getScoreboardName());

                //Insert main inventory to body and clear
                moveInventoryToMob(playerEntity, physicalBodyEntity);
                physicalBodyEntity.setHealth(playerEntity.getHealth());
                physicalBodyEntity.setHungerLevel(playerEntity.getFoodStats().getFoodLevel());
            }
        }
        if (event.getPotionEffect().getPotion().equals(AstralEffects.ASTRAL_TRAVEL) && !event.getEntityLiving().getEntityWorld().isRemote()) {
            AstralNetwork.sendAstralEffectStarting(event.getPotionEffect(), event.getEntity());
        }
    }

    private static void moveInventoryToMob(PlayerEntity playerEntity, PhysicalBodyEntity physicalBodyEntity) {
        int i = 0;
        for (ItemStack stack : playerEntity.inventory.mainInventory) {
            if (AstralTags.ASTRAL_PICKUP.contains(stack.getItem())) {
                i++;
            }
            else {
                physicalBodyEntity.getMainInventory().setStackInSlot(i++, stack);
                stack.setCount(0);
            }
        }
        //Insert armor and offhand to entity
        for (EquipmentSlotType slotType : EquipmentSlotType.values()) {
            if (!AstralTags.ASTRAL_PICKUP.contains(playerEntity.getItemStackFromSlot(slotType).getItem())) {
                physicalBodyEntity.setItemStackToSlot(slotType, playerEntity.getItemStackFromSlot(slotType));
                playerEntity.setItemStackToSlot(slotType, ItemStack.EMPTY);
            }
        }
    }

    @SubscribeEvent
    public static void astralBlockInteraction(PlayerInteractEvent.RightClickBlock event) {
        if (event.getPlayer().isPotionActive(AstralEffects.ASTRAL_TRAVEL) && !isEntityInInnerRealm(event.getPlayer())) {
            Block targetedBlock = event.getWorld().getBlockState(event.getPos()).getBlock();
            event.setCanceled(!AstralTags.ASTRAL_INTERACT.contains(targetedBlock));
        }
    }

    private static boolean isEntityInInnerRealm(Entity entity) {
        return entity.dimension == DimensionType.byName(AstralDimensions.INNER_REALM);
    }

    @SubscribeEvent
    public static void astralBreakBlock(BlockEvent.BreakEvent event) {
        //Placeholder properties
        if (!AstralTags.ASTRAL_INTERACT.contains(event.getState().getBlock()) && event.getPlayer().isPotionActive(AstralEffects.ASTRAL_TRAVEL) && !isEntityInInnerRealm(event.getPlayer())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void astralHarvestSpeed(PlayerEvent.BreakSpeed event) {
        if (!AstralTags.ASTRAL_INTERACT.contains(event.getState().getBlock()) && event.getPlayer().isPotionActive(AstralEffects.ASTRAL_TRAVEL) && !isEntityInInnerRealm(event.getPlayer())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void astralPlaceBlockEvent(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof LivingEntity && !AstralTags.ASTRAL_INTERACT.contains(event.getState().getBlock()) && ((LivingEntity) event.getEntity()).isPotionActive(AstralEffects.ASTRAL_TRAVEL) && !isEntityInInnerRealm(event.getEntity())) {
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

    @SubscribeEvent
    public static void astralPickupEvent(EntityItemPickupEvent event) {
        World world = event.getEntityLiving().world;
        if (!world.isRemote() && event.getEntityLiving().isPotionActive(AstralEffects.ASTRAL_TRAVEL) && !AstralTags.ASTRAL_PICKUP.contains(event.getItem().getItem().getItem())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void astralDeath(LivingDeathEvent event) {
        if (event.getEntityLiving().isPotionActive(AstralEffects.ASTRAL_TRAVEL) && event.getEntityLiving() instanceof PlayerEntity) {
            event.setCanceled(true);
            event.getEntityLiving().removeActivePotionEffect(AstralEffects.ASTRAL_TRAVEL);
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
            if (playerEntity.isPotionActive(AstralEffects.ASTRAL_TRAVEL)) {
                //Cancel rendering of hunger bar
                if (event.getType() == RenderGameOverlayEvent.ElementType.FOOD) {
                    event.setCanceled(true);
                }
                if (event.getType() == RenderGameOverlayEvent.ElementType.HEALTH) {
                    event.setCanceled(true);
                    AstralHealthBar.renderAstralHearts(minecraft, playerEntity);
                }
            }
        }
    }
}