package com.alan19.astral.events.astraltravel;

import com.alan19.astral.Astral;
import com.alan19.astral.api.AstralAPI;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = Astral.MOD_ID)
public class PhysicalBodyTracking {

    // Remove the body when the player logs out
    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        final PlayerEntity player = event.getPlayer();
        if (player instanceof ServerPlayerEntity) {
            final ServerWorld serverWorld = ((ServerPlayerEntity) player).getLevel();
            AstralAPI.getBodyTracker(serverWorld).ifPresent(cap -> {
                if (cap.getBodyTrackerMap().containsKey(player.getUUID())) {
                    final INBT uuidNBT = cap.getBodyTrackerMap().get(player.getUUID()).get("UUID");
                    if (uuidNBT != null) {
                        final UUID uuid = NBTUtil.loadUUID(uuidNBT);
                        final Entity entity = serverWorld.getEntity(uuid);
                        if (entity != null) {
                            entity.remove();
                        }
                    }
                }
            });
        }
    }

    // Spawn the body using the body's NBT when the player logs in
    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        final PlayerEntity player = event.getPlayer();
        if (player instanceof ServerPlayerEntity) {
            final ServerWorld serverWorld = ((ServerPlayerEntity) player).getLevel();
            AstralAPI.getBodyTracker(serverWorld).ifPresent(cap -> {
                if (cap.getBodyTrackerMap().containsKey(player.getUUID())) {
                    final CompoundNBT bodyNBT = cap.getBodyTrackerMap().get(player.getUUID());
                    final ServerWorld dimension = serverWorld.getServer().getLevel(RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(bodyNBT.getString("Dimension"))));
                    final INBT bodyUUID = bodyNBT.get("UUID");
                    if (dimension != null && bodyUUID != null && dimension.getEntity(NBTUtil.loadUUID(bodyUUID)) == null) {
                        EntityType.create(bodyNBT, dimension).ifPresent(serverWorld::addFreshEntity);
                    }
                }
            });
        }
    }
}
