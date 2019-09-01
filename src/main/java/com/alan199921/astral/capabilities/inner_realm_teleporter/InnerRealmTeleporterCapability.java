package com.alan199921.astral.capabilities.inner_realm_teleporter;

import com.alan199921.astral.dimensions.ModDimensions;
import com.alan199921.astral.dimensions.TeleportationTools;
import com.alan199921.astral.dimensions.innerrealm.InnerRealmUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.dimension.DimensionType;

import java.util.HashMap;
import java.util.UUID;

public class InnerRealmTeleporterCapability implements IInnerRealmTeleporterCapability {
    private final InnerRealmUtils innerRealmUtils = new InnerRealmUtils();
    private HashMap<UUID, BlockPos> spawnLocations = new HashMap<>();

    @Override
    public void newPlayer(ServerPlayerEntity player) {
        int distanceBetweenBoxes = 256;
        BlockPos spawnLocation = new BlockPos(spawnLocations.size() * distanceBetweenBoxes + 8, player.getServerWorld().getSeaLevel() + 1, 8);
        IChunk spawnChunk = player.server.getWorld(DimensionType.byName(ModDimensions.INNER_REALM)).getChunk(spawnLocation);
        innerRealmUtils.generateInnerRealmChunk(player.world, spawnChunk);
        spawnLocations.put(player.getUniqueID(), spawnLocation);
    }

    @Override
    public void teleport(ServerPlayerEntity player) {
        if (!spawnLocations.containsKey(player.getUniqueID())) {
            newPlayer(player);
        }
        TeleportationTools.changeDim(player, spawnLocations.get(player.getUniqueID()), DimensionType.byName(ModDimensions.INNER_REALM));
    }

    @Override
    public BlockPos getSpawn(UUID uniqueID) {
        return spawnLocations.get(uniqueID);
    }

    @Override
    public HashMap<UUID, BlockPos> getSpawnList() {
        return spawnLocations;
    }

    @Override
    public void setSpawnList(HashMap<UUID, BlockPos> uuidBlockPosHashMap) {
        spawnLocations = uuidBlockPosHashMap;
    }
}
