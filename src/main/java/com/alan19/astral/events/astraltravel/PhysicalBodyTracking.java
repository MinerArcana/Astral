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

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        final PlayerEntity player = event.getPlayer();
        if (player instanceof ServerPlayerEntity) {
            final ServerWorld serverWorld = ((ServerPlayerEntity) player).getServerWorld();
            AstralAPI.getBodyTracker(serverWorld).ifPresent(cap -> {
                if (cap.getBodyTrackerMap().containsKey(player.getUniqueID())) {
                    final INBT uuidNBT = cap.getBodyTrackerMap().get(player.getUniqueID()).get("UUID");
                    if (uuidNBT != null) {
                        final UUID uuid = NBTUtil.readUniqueId(uuidNBT);
                        final Entity entity = serverWorld.getEntityByUuid(uuid);
                        if (entity != null) {
                            entity.remove();
                        }
                    }
                }
            });
        }
    }

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        final PlayerEntity player = event.getPlayer();
        if (player instanceof ServerPlayerEntity) {
            final ServerWorld serverWorld = ((ServerPlayerEntity) player).getServerWorld();
            AstralAPI.getBodyTracker(serverWorld).ifPresent(cap -> {
                if (cap.getBodyTrackerMap().containsKey(player.getUniqueID())) {
                    final CompoundNBT bodyNBT = cap.getBodyTrackerMap().get(player.getUniqueID());
                    final ServerWorld dimension = serverWorld.getServer().getWorld(RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation(bodyNBT.getString("Dimension"))));
                    if (dimension != null) {
                        EntityType.loadEntityUnchecked(bodyNBT, dimension).ifPresent(serverWorld::addEntity);
                    }
                }
            });
        }
    }
}
