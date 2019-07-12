package com.alan199921.astral.capabilities;

import com.alan199921.astral.dimensions.ModDimensions;
import com.alan199921.astral.dimensions.TeleportationTools;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;

import java.util.HashMap;
import java.util.UUID;

public class InnerRealmTeleporter implements IPocketDimTeleporter {
    private HashMap<UUID, BlockPos> spawnLocations = new HashMap<>();
    private int spawnCounter = 0;

    @Override
    public void setSpawn(BlockPos pos) {

    }

    @Override
    public void newPlayer(ServerPlayerEntity player) {
        int distanceBetweenBoxes = 256;
        spawnLocations.put(player.getUniqueID(), new BlockPos(spawnCounter * distanceBetweenBoxes, player.getServerWorld().getSeaLevel()+1, 0));
        spawnCounter++;
    }

    @Override
    public void teleport(ServerPlayerEntity player, DimensionType dimension) {
        TeleportationTools.changeDim(player, spawnLocations.get(player.getUniqueID()), DimensionType.byName(ModDimensions.INNER_REALM));
    }

    @Override
    public BlockPos getSpawn() {
        return null;
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
