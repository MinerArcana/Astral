package com.alan199921.astral.capabilities;

import com.alan199921.astral.dimensions.ModDimensions;
import com.alan199921.astral.dimensions.TeleportationTools;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.WorldCapabilityData;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.UUID;

public class InnerRealmCapability implements Capability.IStorage<WorldCapabilityData> {
    private HashMap<UUID, BlockPos> spawnLocations = new HashMap<>();
    private int spawnCounter = 0;

    @Nullable
    @Override
    public INBT writeNBT(Capability<WorldCapabilityData> capability, WorldCapabilityData instance, Direction side) {
        return null;
    }

    @Override
    public void readNBT(Capability<WorldCapabilityData> capability, WorldCapabilityData instance, Direction side, INBT nbt) {

    }

    public void withdrawal(ServerPlayerEntity player){
        if (!spawnLocations.containsKey(player.getUniqueID())){
            int distanceBetweenBoxes = 256;
            spawnLocations.put(player.getUniqueID(), new BlockPos(spawnCounter * distanceBetweenBoxes, player.getServerWorld().getSeaLevel()+1, 0));
            spawnCounter++;
        }
        TeleportationTools.changeDim(player, spawnLocations.get(player.getUniqueID()), DimensionType.byName(ModDimensions.INNER_REALM));
    }
}
