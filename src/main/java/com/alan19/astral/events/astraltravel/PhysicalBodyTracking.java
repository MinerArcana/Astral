package com.alan19.astral.events.astraltravel;

import com.alan19.astral.Astral;
import com.alan19.astral.api.AstralAPI;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = Astral.MOD_ID)
public class PhysicalBodyTracking {

    // Remove the body when the player logs out
    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        final Player player = event.getPlayer();
        if (player instanceof ServerPlayer) {
            final ServerLevel serverWorld = ((ServerPlayer) player).getLevel();
            AstralAPI.getBodyTracker(serverWorld).ifPresent(cap -> {
                if (cap.getBodyTrackerMap().containsKey(player.getUUID())) {
                    final Tag uuidNBT = cap.getBodyTrackerMap().get(player.getUUID()).get("UUID");
                    if (uuidNBT != null) {
                        final UUID uuid = NbtUtils.loadUUID(uuidNBT);
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
        final Player player = event.getPlayer();
        if (player instanceof ServerPlayer) {
            final ServerLevel serverWorld = ((ServerPlayer) player).getLevel();
            AstralAPI.getBodyTracker(serverWorld).ifPresent(cap -> {
                if (cap.getBodyTrackerMap().containsKey(player.getUUID())) {
                    final CompoundTag bodyNBT = cap.getBodyTrackerMap().get(player.getUUID());
                    final ServerLevel dimension = serverWorld.getServer().getLevel(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(bodyNBT.getString("Dimension"))));
                    final Tag bodyUUID = bodyNBT.get("UUID");
                    if (dimension != null && bodyUUID != null && dimension.getEntity(NbtUtils.loadUUID(bodyUUID)) == null) {
                        EntityType.create(bodyNBT, dimension).ifPresent(serverWorld::addFreshEntity);
                    }
                }
            });
        }
    }
}
