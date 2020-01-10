package com.alan199921.astral.dimensions;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SPlayEntityEffectPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;

public class TeleportationTools {
    /**
     * Teleports a player to another dimension with the specified BlockPos
     *
     * @param player The player to be teleported
     * @param pos    The position the player is being teleported to
     * @param type   The DimensionType the player is being teleported to
     */
    public static void changeDim(ServerPlayerEntity player, BlockPos pos, DimensionType type) {
        ServerWorld nextWorld = player.getServer().getWorld(type);
        nextWorld.getChunk(pos);    // make sure the chunk is loaded
        player.teleport(nextWorld, pos.getX(), pos.getY(), pos.getZ(), player.rotationYaw, player.rotationPitch);
        for (EffectInstance effectinstance : player.getActivePotionEffects()) {
            player.connection.sendPacket(new SPlayEntityEffectPacket(player.getEntityId(), effectinstance));
        }

    }
}
