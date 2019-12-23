package com.alan199921.astral.dimensions;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;

public class TeleportationTools {
    /**
     * Teleports a player to another dimension with the specified BlockPos
     *
     * @param player The player to be teleported
     * @param pos    The position the player is being teleported to
     * @param type   The DimensionType the player is being teleported to
     */
    public static void changeDim(ServerPlayerEntity player, BlockPos pos, DimensionType type) {
        player.changeDimension(type);
        player.teleport(player.getServerWorld(), pos.getX(), pos.getY(), pos.getZ(), player.getRotationYawHead(), player.rotationPitch);
    }
}
