package com.minerarcana.astral.api.innerrealmteleporter;

import com.minerarcana.astral.api.AstralCapabilities;
import com.minerarcana.astral.world.feature.dimensions.AstralDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InnerRealmTeleporter implements IInnerRealmTeleporter {
    private Map<UUID, BlockPos> spawnLocations = new HashMap<>();

    @Override
    public void teleport(ServerPlayer player) {
        Level innerRealmWorld = player.getServer().getLevel(AstralDimensions.INNER_REALM);
        final BlockPos spawnLocation = spawnLocations.computeIfAbsent(player.getGameProfile().getId(), uuid -> {
            final BlockPos pos = new BlockPos(spawnLocations.size() * 256 + 8, player.getLevel().getSeaLevel() + 4, 8);
            AstralCapabilities.getChunkClaimTracker(innerRealmWorld).ifPresent(cap -> cap.claimChunk(player, new ChunkPos(pos)));
            return pos;
        });
        TeleportationTools.performTeleport(player, AstralDimensions.INNER_REALM, new BlockPos(spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ()), Direction.UP);

    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag spawnLocationTag = new CompoundTag();
        spawnLocations.forEach((key, value) -> spawnLocationTag.put(key.toString(), NbtUtils.writeBlockPos(value)));
        return spawnLocationTag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        Map<UUID, BlockPos> newSpawnMap = new HashMap<>();
        for (String id : nbt.getAllKeys()) {
            newSpawnMap.put(UUID.fromString(id), NbtUtils.readBlockPos(nbt.getCompound(id)));
        }
        this.spawnLocations = newSpawnMap;

    }
}
