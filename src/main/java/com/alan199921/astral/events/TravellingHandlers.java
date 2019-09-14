package com.alan199921.astral.events;

import com.alan199921.astral.Astral;
import com.alan199921.astral.blocks.AstralMeridian;
import com.alan199921.astral.capabilities.bodylink.BodyLinkProvider;
import com.alan199921.astral.effects.ModEffects;
import com.alan199921.astral.entities.PhysicalBodyEntity;
import com.alan199921.astral.entities.PhysicalBodyRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.FMLPlayMessages;

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

    @SubscribeEvent
    public static void nullifyAstralDamage(LivingAttackEvent event) {
        if (event.getSource().getTrueSource() != null && !event.getSource().getTrueSource().isLiving() && !event.getSource().getDamageType().equals("astral") && event.getEntityLiving().isPotionActive(ModEffects.astralEffect)) {
            event.setCanceled(true);
        }
        if (event.getSource().getTrueSource() != null && event.getSource().getTrueSource().isLiving()) {
            LivingEntity livingSource = (LivingEntity) event.getSource().getImmediateSource();

            if (isAstralVsNonAstral(livingSource, event.getEntityLiving())) {
                event.setCanceled(true);
            }
        }
    }

    //Function for detecting if an Astral entity is interacting with a non astral entity
    private static boolean isAstralVsNonAstral(LivingEntity mobA, LivingEntity mobB) {
        if (mobA == null || mobB == null) {
            return false;
        }
        return mobA.isPotionActive(ModEffects.astralEffect) && !mobB.isPotionActive(ModEffects.astralEffect) || !mobA.isPotionActive(ModEffects.astralEffect) && mobB.isPotionActive(ModEffects.astralEffect);
    }

    @SubscribeEvent
    public static void renderAstralEntities(RenderLivingEvent event) {
        if (!Minecraft.getInstance().player.isPotionActive(ModEffects.astralEffect) && event.getEntity().isPotionActive(ModEffects.astralEffect)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void travelEffectExpire(PotionEvent.PotionRemoveEvent event) {
        if (event.getPotionEffect().getPotion().equals(ModEffects.astralEffect) && event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity p = (PlayerEntity) event.getEntityLiving();
            if (!p.abilities.isCreativeMode){
                p.abilities.allowFlying = false;
                p.noClip = false;
                p.abilities.setFlySpeed(.05F);
                p.sendPlayerAbilities();
            }
            if (!p.getEntityWorld().isRemote()){
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) p;
                ServerWorld serverWorld = serverPlayerEntity.getServerWorld();
                p.getCapability(BodyLinkProvider.BODY_LINK_CAPABILITY).ifPresent(cap -> cap.killEntity(serverWorld));
            }
        }
    }

    @SubscribeEvent
    public static void travelEffectActivate(PotionEvent.PotionAddedEvent event) {
        if (event.getPotionEffect().getPotion().equals(ModEffects.astralEffect) && event.getEntityLiving() instanceof PlayerEntity && !event.getEntityLiving().isPotionActive(ModEffects.astralEffect)) {
            PlayerEntity p = (PlayerEntity) event.getEntityLiving();
            if (!p.abilities.isCreativeMode) {
                p.abilities.allowFlying = true;
                p.noClip = true;
                p.abilities.setFlySpeed(.05F * (event.getPotionEffect().getAmplifier() + 1));
                p.sendPlayerAbilities();
            }
            if (!p.getEntityWorld().isRemote()){
                PhysicalBodyEntity physicalBodyEntity = (PhysicalBodyEntity) PhysicalBodyRegistry.PHYSICAL_BODY_ENTITY.spawn(p.getEntityWorld(), p.inventory.getItemStack(), p, p.getPosition(), SpawnReason.TRIGGERED, false, false);
                UUID entityID = physicalBodyEntity.getUniqueID();
                p.getCapability(BodyLinkProvider.BODY_LINK_CAPABILITY).ifPresent(cap -> cap.setLinkedBodyID(physicalBodyEntity));
                physicalBodyEntity.setName(event.getEntity().getScoreboardName());
            }
        }
    }

    @SubscribeEvent
    public static void astralBreakBlock(PlayerEvent.BreakSpeed event){
        //Placeholder properties
        if (!event.getState().getProperties().contains(AstralMeridian.ASTRAL_BLOCK) && event.getPlayer().isPotionActive(ModEffects.astralEffect)){
            event.setNewSpeed(0f);
        }
    }
}