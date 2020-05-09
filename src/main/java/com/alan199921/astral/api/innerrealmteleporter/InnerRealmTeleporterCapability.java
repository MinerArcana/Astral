package com.alan199921.astral.api.innerrealmteleporter;

import com.alan199921.astral.api.innerrealmchunkclaim.InnerRealmChunkClaimProvider;
import com.alan199921.astral.dimensions.AstralDimensions;
import com.alan199921.astral.dimensions.TeleportationTools;
import com.alan199921.astral.dimensions.innerrealm.InnerRealmUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.DimensionManager;

import java.util.HashMap;
import java.util.UUID;

import static com.alan199921.astral.dimensions.AstralDimensions.INNER_REALM;

public class InnerRealmTeleporterCapability implements IInnerRealmTeleporterCapability {
    private final InnerRealmUtils innerRealmUtils = new InnerRealmUtils();
    private HashMap<UUID, BlockPos> spawnLocations = new HashMap<>();

    /**
     * Generate a 14^3 box of ego membranes and claim the chunk in the chunk claim capability
     *
     * @param player The player who will claim the chunk
     */
    @Override
    public void prepareSpawnChunk(PlayerEntity player) {
        if (!player.getEntityWorld().isRemote()) {
            ServerWorld innerRealmWorld = player.getServer().getWorld(DimensionType.byName(INNER_REALM));
            final BlockPos playerSpawnPos = getSpawn(player);
            Chunk spawnChunk = innerRealmWorld.getChunkAt(playerSpawnPos);
            innerRealmWorld.getCapability(InnerRealmChunkClaimProvider.CHUNK_CLAIM_CAPABILITY).ifPresent(cap -> cap.addChunkToPlayerClaims(player, spawnChunk.getPos()));
            innerRealmUtils.generateInnerRealmChunk(innerRealmWorld, spawnChunk);
            innerRealmWorld.getChunk(playerSpawnPos);
        }
    }

    private void addPlayerToHashMap(PlayerEntity player) {
        int distanceBetweenBoxes = 256;
        BlockPos spawnLocation = new BlockPos(spawnLocations.size() * distanceBetweenBoxes + 8, player.getEntityWorld().getSeaLevel() + 4, 8);
        spawnLocations.put(player.getUniqueID(), spawnLocation);
    }

    /**
     * Teleports player into the inner realm if they are in any other dimension
     * If they are not already registered, register them in the HashMap
     * Then load the chunk they will spawn into and generate it if they have not registered yet
     * Finally teleport the player
     *
     * @param player The player to be teleported into the inner realm
     */
    @Override
    public void teleport(PlayerEntity player) {
        if (!player.getEntityWorld().isRemote()) {
            DimensionType innerRealm = DimensionManager.registerOrGetDimension(INNER_REALM, AstralDimensions.innerRealm, null, true);
            Dimension dimension = innerRealm.create(player.world);
            World innerRealmWorld = dimension.getWorld();
            boolean firstTime = false;
            if (!spawnLocations.containsKey(player.getUniqueID())) {
                addPlayerToHashMap(player);
                firstTime = true;
            }
            BlockPos playerSpawn = getSpawn(player);
            if (firstTime) {
                prepareSpawnChunk(player);
            }
            if (!innerRealmWorld.isRemote()) {
                TeleportationTools.changeDim((ServerPlayerEntity) player, new BlockPos(playerSpawn.getX(), playerSpawn.getY(), playerSpawn.getZ()), DimensionType.byName(INNER_REALM));
            }
        }
    }

    @Override
    public BlockPos getSpawn(PlayerEntity player) {
        UUID uniqueID = player.getUniqueID();
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

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        HashMap<UUID, BlockPos> spawnList = new HashMap<>();
        CompoundNBT spawnLocationsNbt = (CompoundNBT) nbt.get("spawnLocations");
        for (String id : spawnLocationsNbt.keySet()) {
            spawnList.put(UUID.fromString(id), NBTUtil.readBlockPos(spawnLocationsNbt.getCompound(id)));
        }
        setSpawnList(spawnList);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT spawnLocationTag = new CompoundNBT();
        for (UUID uuid : getSpawnList().keySet()) {
            spawnLocationTag.put(uuid.toString(), NBTUtil.writeBlockPos(getSpawnList().get(uuid)));
        }
        CompoundNBT spawnLocationsNBT = new CompoundNBT();
        spawnLocationsNBT.put("spawnLocations", spawnLocationTag);
        return spawnLocationsNBT;
    }
}
