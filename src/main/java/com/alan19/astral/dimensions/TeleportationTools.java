package com.alan19.astral.dimensions;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

public class TeleportationTools {

    /**
     * Teleports a player to the specified BlockPos. Adapted from <a href="https://github.com/blay09/Waystones/blob/cb808cf558bb9fe257df6373e0aaa4ec41b214ec/src/main/java/net/blay09/mods/waystones/core/PlayerWaystoneManager.java#L193">https://github.com/blay09/Waystones/blob/cb808cf558bb9fe257df6373e0aaa4ec41b214ec/src/main/java/net/blay09/mods/waystones/core/PlayerWaystoneManager.java#L193</a>
     *
     * @param player    The player to be teleported
     * @param dimension The dimension to teleport to
     * @param dest      The BlockPos to be teleported to
     * @param direction The direction the player is facing
     */
    @ParametersAreNonnullByDefault
    public static void performTeleport(ServerPlayer player, ResourceKey<Level> dimension, BlockPos dest, @Nullable Direction direction) {
        ServerLevel destWorld = player.getServer().getLevel(dimension);
        player.teleportTo(destWorld, dest.getX() + .5, dest.getY() + .5, dest.getZ() + .5, (Optional.ofNullable(direction).map(Direction::toYRot).orElseGet(player::getYRot)), player.getXRot());
        // Resync some things that Vanilla is missing:
        player.getActiveEffects().forEach(effectInstance -> player.connection.send(new ClientboundUpdateMobEffectPacket(player.getId(), effectInstance)));
        player.setExperienceLevels(player.experienceLevel);
    }
}