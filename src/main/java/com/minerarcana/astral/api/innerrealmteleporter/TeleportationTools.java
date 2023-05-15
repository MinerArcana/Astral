package com.minerarcana.astral.api.innerrealmteleporter;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class TeleportationTools {

    /**
     * Teleports a player to the specified BlockPos. Adapted from https://github.com/TwelveIterationMods/Waystones/blob/ec3350febe836ed781b19dd3e0e00c787e5b558e/shared/src/main/java/net/blay09/mods/waystones/core/PlayerWaystoneManager.java#L277
     *
     * @param player    The player to be teleported
     * @param dimension The dimension to teleport to
     * @param dest      The BlockPos to be teleported to
     * @param direction The direction the player is facing
     */
    @ParametersAreNonnullByDefault
    public static void performTeleport(ServerPlayer player, ResourceKey<Level> dimension, BlockPos dest, @Nullable Direction direction) {
        ServerLevel destWorld = player.getServer().getLevel(dimension);
        player.teleportTo(destWorld, dest.getX() + .5, dest.getY() + .5, dest.getZ() + .5, player.getYHeadRot(), player.getXRot());
        // Resync some things that Vanilla is missing:
        // TODO Sync potion effects
        player.connection.send(new ClientboundSetExperiencePacket(player.experienceProgress, player.totalExperience, player.experienceLevel));

    }
}