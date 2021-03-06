package com.alan19.astral.dimensions;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SPlayEntityEffectPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

public class TeleportationTools {

    /**
     * Teleports a player to the specified BlockPos. Adapted from https://github.com/blay09/Waystones/blob/cb808cf558bb9fe257df6373e0aaa4ec41b214ec/src/main/java/net/blay09/mods/waystones/core/PlayerWaystoneManager.java#L193
     *
     * @param player    The player to be teleported
     * @param dimension The dimension to teleport to
     * @param dest      The BlockPos to be teleported to
     * @param direction The direction the player is facing
     */
    @ParametersAreNonnullByDefault
    public static void performTeleport(ServerPlayerEntity player, RegistryKey<World> dimension, BlockPos dest, @Nullable Direction direction) {
        ServerWorld destWorld = player.getServer().getWorld(dimension);
        player.teleport(destWorld, dest.getX() + .5, dest.getY() + .5, dest.getZ() + .5, (Optional.ofNullable(direction).map(Direction::getHorizontalAngle).orElseGet(() -> player.rotationYaw)), player.rotationPitch);
        // Resync some things that Vanilla is missing:
        player.getActivePotionEffects().forEach(effectInstance -> player.connection.sendPacket(new SPlayEntityEffectPacket(player.getEntityId(), effectInstance)));
        player.setExperienceLevel(player.experienceLevel);
    }
}