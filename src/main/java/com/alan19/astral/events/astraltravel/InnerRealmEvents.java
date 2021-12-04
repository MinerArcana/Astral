package com.alan19.astral.events.astraltravel;

import com.alan19.astral.Astral;
import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.api.psychicinventory.PsychicInventoryInstance;
import com.alan19.astral.dimensions.AstralDimensions;
import com.alan19.astral.effects.AstralEffects;
import com.alan19.astral.tags.AstralTags;
import com.alan19.astral.util.Constants;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.stream.IntStream;

@Mod.EventBusSubscriber(modid = Astral.MOD_ID)
public class InnerRealmEvents {
    @SubscribeEvent
    public static void cancelInnerRealmEntry(EntityTravelToDimensionEvent event) {
        if (event.getDimension() == AstralDimensions.INNER_REALM) {
            if (event.getEntity() instanceof LivingEntity && !((LivingEntity) event.getEntity()).hasEffect(AstralEffects.ASTRAL_TRAVEL.get()) || !(event.getEntity() instanceof LivingEntity)) {
                event.setCanceled(true);
                if (event.getEntity() instanceof Player) {
                    event.getEntity().sendMessage(new TranslatableComponent(Constants.INVALID_WITHDRAWAL), event.getEntity().getUUID());
                }
            }
        }
    }

    /**
     * Drop all items without the astral pickup tag when the player leaves the inner realm
     *
     * @param event The EntityTravelToDimensionEvent that contains the dimension the entity to traveling to
     */
    @SubscribeEvent
    public static void dropNonAstralItems(EntityTravelToDimensionEvent event) {
        final Entity entity = event.getEntity();
        if (entity.getCommandSenderWorld().dimension() == AstralDimensions.INNER_REALM && entity instanceof ServerPlayer) {
            ServerPlayer playerEntity = (ServerPlayer) entity;
            AstralAPI.getOverworldPsychicInventory((ServerLevel) playerEntity.getCommandSenderWorld()).ifPresent(iPsychicInventory -> dropAllNonAstralItems(playerEntity, iPsychicInventory.getInventoryOfPlayer(playerEntity.getUUID()), (ServerLevel) playerEntity.getCommandSenderWorld()));
        }
    }

    private static void dropAllNonAstralItems(Player playerEntity, PsychicInventoryInstance inventoryOfPlayer, ServerLevel entityWorld) {
        IntStream.range(0, inventoryOfPlayer.getAstralMainInventory().getSlots())
                .filter(i -> !AstralTags.ASTRAL_PICKUP.contains(inventoryOfPlayer.getAstralMainInventory().getStackInSlot(i).getItem()))
                .forEach(i -> Block.popResource(entityWorld, playerEntity.blockPosition(), inventoryOfPlayer.getAstralMainInventory().extractItem(i, 64, false)));

        IntStream.range(0, inventoryOfPlayer.getAstralArmorInventory().getSlots())
                .filter(i -> !AstralTags.ASTRAL_PICKUP.contains(inventoryOfPlayer.getAstralArmorInventory().getStackInSlot(i).getItem()))
                .forEach(i -> Block.popResource(entityWorld, playerEntity.blockPosition(), inventoryOfPlayer.getAstralArmorInventory().extractItem(i, 64, false)));

        IntStream.range(0, inventoryOfPlayer.getAstralHandsInventory().getSlots())
                .filter(i -> !AstralTags.ASTRAL_PICKUP.contains(inventoryOfPlayer.getAstralHandsInventory().getStackInSlot(i).getItem()))
                .forEach(i -> Block.popResource(entityWorld, playerEntity.blockPosition(), inventoryOfPlayer.getAstralHandsInventory().extractItem(i, 64, false)));
    }
}
