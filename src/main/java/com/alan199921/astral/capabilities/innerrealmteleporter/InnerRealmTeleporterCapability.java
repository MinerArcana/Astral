package com.alan199921.astral.capabilities.innerrealmteleporter;

import com.alan199921.astral.capabilities.innerrealmchunkclaim.InnerRealmChunkClaimProvider;
import com.alan199921.astral.dimensions.ModDimensions;
import com.alan199921.astral.dimensions.TeleportationTools;
import com.alan199921.astral.dimensions.innerrealm.InnerRealmUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;

import java.util.HashMap;
import java.util.UUID;

public class InnerRealmTeleporterCapability implements IInnerRealmTeleporterCapability {
    private final InnerRealmUtils innerRealmUtils = new InnerRealmUtils();
    private HashMap<UUID, BlockPos> spawnLocations = new HashMap<>();

    @Override
    public void newPlayer(PlayerEntity player) {
        BlockPos spawnLocation = addPlayerToHashMap(player);
        World innerRealmWorld = player.getEntityWorld();
        IChunk spawnChunk = innerRealmWorld.getChunk(spawnLocation);
        innerRealmWorld.getCapability(InnerRealmChunkClaimProvider.CHUNK_CLAIM_CAPABILITY).ifPresent(cap -> cap.addChunkToPlayerClaims(player, spawnChunk.getPos()));
        innerRealmUtils.generateInnerRealmChunk(player.world, spawnChunk);
    }

    private BlockPos addPlayerToHashMap(PlayerEntity player) {
        int distanceBetweenBoxes = 256;
        BlockPos spawnLocation = new BlockPos(spawnLocations.size() * distanceBetweenBoxes + 8, player.getEntityWorld().getSeaLevel() + 1, 8);
        spawnLocations.put(player.getUniqueID(), spawnLocation);
        return spawnLocation;
    }

    @Override
    public void teleport(PlayerEntity player) {
        ServerWorld innerRealmWorld = ((ServerPlayerEntity) player).server.getWorld(DimensionType.byName(ModDimensions.INNER_REALM));
        innerRealmWorld.getChunk(getSpawn(player));
        ((ServerPlayerEntity) player).teleport(innerRealmWorld, 0, 1000, 0, player.rotationYaw, player.rotationPitch);
        if (!spawnLocations.containsKey(player.getUniqueID())) {
            newPlayer(player);
        }
        BlockPos playerSpawn = getSpawn(player);
        if (!player.world.isRemote()){
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
            serverPlayerEntity.teleport(serverPlayerEntity.getServerWorld(), playerSpawn.getX(), playerSpawn.getY(), playerSpawn.getZ(), player.rotationYaw, player.rotationPitch);
        }
    }

    @Override
    public BlockPos getSpawn(PlayerEntity player) {
        UUID uniqueID = player.getUniqueID();
        if (spawnLocations.containsKey(uniqueID)){
            return spawnLocations.get(uniqueID);
        }
        else{
            return addPlayerToHashMap(player);
        }
    }

    @Override
    public HashMap<UUID, BlockPos> getSpawnList() {
        return spawnLocations;
    }

    @Override
    public void setSpawnList(HashMap<UUID, BlockPos> uuidBlockPosHashMap) {
        spawnLocations = uuidBlockPosHashMap;
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        HashMap<UUID, BlockPos> spawnList = new HashMap<>();
        CompoundNBT compoundNBT = (CompoundNBT) nbt;
        CompoundNBT spawnLocations = (CompoundNBT) compoundNBT.get("spawnLocations");
        assert spawnLocations != null;
        for (String id: spawnLocations.keySet()) {
            spawnList.put(UUID.fromString(id), NBTUtil.readBlockPos(spawnLocations.getCompound(id)));
        }
        setSpawnList(spawnList);
    }

    @Override
    public INBT serializeNBT() {
        CompoundNBT spawnLocationTag = new CompoundNBT();
        for (UUID uuid: getSpawnList().keySet()) {
            spawnLocationTag.put(uuid.toString(), NBTUtil.writeBlockPos(getSpawnList().get(uuid)));
        }
        CompoundNBT spawnLocationsNBT = new CompoundNBT();
        spawnLocationsNBT.put("spawnLocations", spawnLocationTag);
        return spawnLocationsNBT;
    }
}
