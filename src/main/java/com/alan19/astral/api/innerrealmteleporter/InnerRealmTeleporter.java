package com.alan19.astral.api.innerrealmteleporter;

import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.api.psychicinventory.PsychicInventoryInstance;
import com.alan19.astral.dimensions.AstralDimensions;
import com.alan19.astral.dimensions.TeleportationTools;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InnerRealmTeleporter implements IInnerRealmTeleporter {
    private Map<UUID, BlockPos> spawnLocations = new HashMap<>();

    /**
     * Teleports player into the inner realm if they are in any other dimension
     * If they are not already registered, register them in the HashMap
     * Then load the chunk they will spawn into and generate it if they have not registered yet
     * Finally teleport the player
     *
     * @param player The player to be teleported into the inner realm
     */
    @Override
    public void teleport(ServerPlayer player) {
        ServerLevel innerRealmWorld = player.getLevel().getServer().getLevel(AstralDimensions.INNER_REALM);
        final BlockPos spawnLocation = spawnLocations.computeIfAbsent(player.getUUID(), uuid -> {
            final BlockPos pos = new BlockPos(spawnLocations.size() * 256 + 8, player.getCommandSenderWorld().getSeaLevel() + 4, 8);
            AstralAPI.getChunkClaimTracker(innerRealmWorld).ifPresent(cap -> cap.claimChunk(player, new ChunkPos(pos)));
            return pos;
        });
        TeleportationTools.performTeleport(player, AstralDimensions.INNER_REALM, new BlockPos(spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ()), Direction.UP);
        AstralAPI.getOverworldPsychicInventory(innerRealmWorld).ifPresent(psychicInventory -> dropInnerRealmItems(player, psychicInventory.getInventoryOfPlayer(player.getUUID())));
    }

    /**
     * Drops all items being sent to the Inner Realm
     *
     * @param playerEntity      The player who was teleproted to the Inner Realm
     * @param inventoryOfPlayer The PsychicInventoryInstance of the player
     */
    private void dropInnerRealmItems(Player playerEntity, PsychicInventoryInstance inventoryOfPlayer) {
        Level entityWorld = playerEntity.level;
        int inventorySlots = inventoryOfPlayer.getInnerRealmMain().getSlots();
        for (int i = 0; i < inventorySlots; i++) {
            Block.popResource(entityWorld, playerEntity.blockPosition(), inventoryOfPlayer.getInnerRealmMain().extractItem(i, 64, false));
        }

        int armorSlots = inventoryOfPlayer.getInnerRealmArmor().getSlots();
        for (int i = 0; i < armorSlots; i++) {
            Block.popResource(entityWorld, playerEntity.blockPosition(), inventoryOfPlayer.getInnerRealmArmor().extractItem(i, 64, false));
        }

        int handSlots = inventoryOfPlayer.getInnerRealmHands().getSlots();
        for (int i = 0; i < handSlots; i++) {
            Block.popResource(entityWorld, playerEntity.blockPosition(), inventoryOfPlayer.getInnerRealmHands().extractItem(i, 64, false));
        }
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
