package com.alan199921.astral.capabilities.innerrealmteleporter;

import com.alan199921.astral.capabilities.innerrealmchunkclaim.InnerRealmChunkClaimProvider;
import com.alan199921.astral.dimensions.AstralDimensions;
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

    /**
     * Generate a 14^3 box of ego membranes and claim the chunk in the chunk claim capability
     *
     * @param player The player who will claim the chunk
     */
    @Override
    public void prepareSpawnChunk(PlayerEntity player) {
        World innerRealmWorld = player.getEntityWorld();
        if (!innerRealmWorld.dimension.getType().equals(DimensionType.byName(AstralDimensions.INNER_REALM))) {
            System.out.println("Player is not in the inner realm!");
        }
        IChunk spawnChunk = innerRealmWorld.getChunk(getSpawn(player));
        innerRealmWorld.getCapability(InnerRealmChunkClaimProvider.CHUNK_CLAIM_CAPABILITY).ifPresent(cap -> cap.addChunkToPlayerClaims(player, spawnChunk.getPos()));
        innerRealmUtils.generateInnerRealmChunk(player.world, spawnChunk);
        innerRealmWorld.getChunk(getSpawn(player));
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
            TeleportationTools.changeDim((ServerPlayerEntity) player, new BlockPos(1, 1000, 1), DimensionType.byName(AstralDimensions.INNER_REALM));
        }
        World innerRealmWorld = player.getEntityWorld();
        boolean firstTime = false;
        if (!spawnLocations.containsKey(player.getUniqueID())) {
            addPlayerToHashMap(player);
            firstTime = true;
        }
        BlockPos playerSpawn = getSpawn(player);
        innerRealmWorld.getChunk(playerSpawn);
        if (firstTime) {
            prepareSpawnChunk(player);
        }

        if (!innerRealmWorld.isRemote()) {
            ((ServerPlayerEntity) player).teleport((ServerWorld) innerRealmWorld, playerSpawn.getX(), playerSpawn.getY(), playerSpawn.getZ(), player.rotationYaw, player.rotationPitch);
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
    public void deserializeNBT(INBT nbt) {
        HashMap<UUID, BlockPos> spawnList = new HashMap<>();
        CompoundNBT compoundNBT = (CompoundNBT) nbt;
        CompoundNBT spawnLocations = (CompoundNBT) compoundNBT.get("spawnLocations");
        for (String id : spawnLocations.keySet()) {
            spawnList.put(UUID.fromString(id), NBTUtil.readBlockPos(spawnLocations.getCompound(id)));
        }
        setSpawnList(spawnList);
    }

    @Override
    public INBT serializeNBT() {
        CompoundNBT spawnLocationTag = new CompoundNBT();
        for (UUID uuid : getSpawnList().keySet()) {
            spawnLocationTag.put(uuid.toString(), NBTUtil.writeBlockPos(getSpawnList().get(uuid)));
        }
        CompoundNBT spawnLocationsNBT = new CompoundNBT();
        spawnLocationsNBT.put("spawnLocations", spawnLocationTag);
        return spawnLocationsNBT;
    }
}
