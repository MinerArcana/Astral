package com.alan19.astral.events.astraltravel;

import com.alan19.astral.Astral;
import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.api.psychicinventory.PsychicInventoryInstance;
import com.alan19.astral.dimensions.AstralDimensions;
import com.alan19.astral.effects.AstralEffects;
import com.alan19.astral.tags.AstralTags;
import com.alan19.astral.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.stream.IntStream;

@Mod.EventBusSubscriber(modid = Astral.MOD_ID)
public class InnerRealmEvents {
    @SubscribeEvent
    public static void cancelInnerRealmEntry(EntityTravelToDimensionEvent event) {
        if (event.getDimension().getRegistryName() != null && event.getDimension().getRegistryName().equals(AstralDimensions.INNER_REALM_RL)) {
            if (event.getEntity() instanceof LivingEntity && !((LivingEntity) event.getEntity()).isPotionActive(AstralEffects.ASTRAL_TRAVEL.get()) || !(event.getEntity() instanceof LivingEntity)) {
                event.setCanceled(true);
                if (event.getEntity() instanceof PlayerEntity) {
                    event.getEntity().sendMessage(new TranslationTextComponent(Constants.INVALID_WITHDRAWAL));
                }
            }
        }
    }

    @SubscribeEvent
    public static void dropNonAstralItems(EntityTravelToDimensionEvent event) {
        final Entity entity = event.getEntity();
        if (event.getEntity().world.dimension.getType().getRegistryName() != null && event.getEntity().world.dimension.getType().getRegistryName().equals(AstralDimensions.INNER_REALM_RL) && entity instanceof ServerPlayerEntity) {
            ServerPlayerEntity playerEntity = (ServerPlayerEntity) entity;
            AstralAPI.getOverworldPsychicInventory((ServerWorld) playerEntity.getEntityWorld()).ifPresent(iPsychicInventory -> dropAllNonAstralItems(playerEntity, iPsychicInventory.getInventoryOfPlayer(playerEntity.getUniqueID()), (ServerWorld) playerEntity.getEntityWorld()));
        }
    }

    private static void dropAllNonAstralItems(PlayerEntity playerEntity, PsychicInventoryInstance inventoryOfPlayer, ServerWorld entityWorld) {
        IntStream.range(0, inventoryOfPlayer.getAstralMainInventory().getSlots())
                .filter(i -> !AstralTags.ASTRAL_PICKUP.contains(inventoryOfPlayer.getAstralMainInventory().getStackInSlot(i).getItem()))
                .forEach(i -> Block.spawnAsEntity(entityWorld, playerEntity.getPosition(), inventoryOfPlayer.getAstralMainInventory().extractItem(i, 64, false)));

        IntStream.range(0, inventoryOfPlayer.getAstralArmorInventory().getSlots())
                .filter(i -> !AstralTags.ASTRAL_PICKUP.contains(inventoryOfPlayer.getAstralArmorInventory().getStackInSlot(i).getItem()))
                .forEach(i -> Block.spawnAsEntity(entityWorld, playerEntity.getPosition(), inventoryOfPlayer.getAstralArmorInventory().extractItem(i, 64, false)));

        IntStream.range(0, inventoryOfPlayer.getAstralHandsInventory().getSlots())
                .filter(i -> !AstralTags.ASTRAL_PICKUP.contains(inventoryOfPlayer.getAstralHandsInventory().getStackInSlot(i).getItem()))
                .forEach(i -> Block.spawnAsEntity(entityWorld, playerEntity.getPosition(), inventoryOfPlayer.getAstralHandsInventory().extractItem(i, 64, false)));
    }
}
