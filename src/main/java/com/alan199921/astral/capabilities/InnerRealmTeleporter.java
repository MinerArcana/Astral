package com.alan199921.astral.capabilities;

import com.alan199921.astral.dimensions.ModDimensions;
import com.alan199921.astral.dimensions.TeleportationTools;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;

import java.util.HashMap;
import java.util.UUID;

public class InnerRealmTeleporter implements IInnerRealmTeleporter {
    private HashMap<UUID, BlockPos> spawnLocations = new HashMap<>();

    @Override
    public void newPlayer(ServerPlayerEntity player) {
        int distanceBetweenBoxes = 256;
        spawnLocations.put(player.getUniqueID(), new BlockPos(spawnLocations.size() * distanceBetweenBoxes, player.getServerWorld().getSeaLevel()+1, 0));
    }

    @Override
    public void teleport(ServerPlayerEntity player, DimensionType dimension) {
        if (!spawnLocations.containsKey(player.getUniqueID())){
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
