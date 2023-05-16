package com.minerarcana.astral.api.innerrealmteleporter;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityTeleportEvent.TeleportCommand;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumSet;

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
        ServerLevel pLevel = player.getServer().getLevel(dimension);
        double pX = dest.getX();
        double pY = dest.getY();
        double pZ = dest.getZ();
        TeleportCommand event = ForgeEventFactory.onEntityTeleportCommand(player, pX, pY, pZ);
        if (!event.isCanceled()) {
            pX = event.getTargetX();
            pY = event.getTargetY();
            pZ = event.getTargetZ();

            float pYaw = player.getYRot();
            float pPitch = player.getXRot();
            float f = Mth.wrapDegrees(pYaw);
            float f1 = Mth.wrapDegrees(pPitch);
            ChunkPos chunkpos = new ChunkPos(new BlockPos(pX, pY, pZ));

            pLevel.getChunkSource().addRegionTicket(TicketType.POST_TELEPORT, chunkpos, 1, player.getId());
            player.stopRiding();
            if (player.isSleeping()) {
                player.stopSleepInBed(true, true);
            }

            if (pLevel == player.level) {
                player.connection.teleport(pX, pY, pZ, f, f1, EnumSet.noneOf(ClientboundPlayerPositionPacket.RelativeArgument.class));
            } else {
                player.teleportTo(pLevel, pX, pY, pZ, f, f1);
            }

            player.setYHeadRot(f);

            if (!player.isFallFlying()) {
                player.setDeltaMovement(player.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
                player.setOnGround(true);
            }

        }

    }
}