package com.alan199921.astral.api.bodylink;

import com.alan199921.astral.api.AstralAPI;
import com.alan199921.astral.api.psychicinventory.InventoryType;
import com.alan199921.astral.dimensions.TeleportationTools;
import com.alan199921.astral.entities.PhysicalBodyEntity;
import com.alan199921.astral.util.Constants;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Capability that handles the link between the physical body and the player
 * <p>
 * If the body dies before the player logs back in, kill the player when they log in.
 * Handles the teleportation of the player back to their body when Astral Travel ends.
 */
public class BodyLinkCapability implements IBodyLinkCapability {
    public static final UUID healthId = UUID.fromString("8bce997a-4c3a-11e6-beb8-9e71128cae77");
    private Map<UUID, BodyInfo> bodyInfoMap = new HashMap<>();

    /**
     * Resets the player entity's stats
     *
     * @param playerEntity       The player entity to recieve stats from the physical body
     * @param physicalBodyEntity The physical body storing player stats
     */
    public static void resetPlayerStats(PlayerEntity playerEntity, PhysicalBodyEntity physicalBodyEntity) {
        playerEntity.getAttribute(SharedMonsterAttributes.MAX_HEALTH).removeModifier(healthId);
        playerEntity.setHealth(physicalBodyEntity.getHealth());
        playerEntity.getFoodStats().setFoodLevel((int) physicalBodyEntity.getHungerLevel());
    }

    public static void resetPlayerStats(PlayerEntity playerEntity) {
        playerEntity.getAttribute(SharedMonsterAttributes.MAX_HEALTH).removeModifier(healthId);
    }

    public static void setPlayerMaxHealthTo(PlayerEntity playerEntity, float newMaxHealth) {
        playerEntity.getAttribute(SharedMonsterAttributes.MAX_HEALTH).removeModifier(healthId);
        float healthModifier = newMaxHealth - playerEntity.getMaxHealth();
        playerEntity.getAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier(healthId, "physical body health", healthModifier, AttributeModifier.Operation.ADDITION));
        if (playerEntity.getHealth() > newMaxHealth) {
            playerEntity.setHealth(newMaxHealth);
        }
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT mapNBT = new CompoundNBT();
        for (Map.Entry<UUID, BodyInfo> bodyInfoEntry : bodyInfoMap.entrySet()) {
            mapNBT.put(bodyInfoEntry.getKey().toString(), bodyInfoEntry.getValue().serializeNBT());
        }
        return mapNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        for (String uuid : nbt.keySet()) {
            final BodyInfo bodyInfo = new BodyInfo(nbt.getCompound(uuid));
            bodyInfoMap.put(UUID.fromString(uuid), bodyInfo);
        }
    }

    /**
     * Sets the linked body info in the map and update the player accordingly, if the are online
     *
     * @param playerID The player to be updated
     * @param bodyInfo The information to update the player with
     * @param world    The ServerWorld
     */
    @Override
    public void setInfo(UUID playerID, BodyInfo bodyInfo, ServerWorld world) {
        bodyInfoMap.put(playerID, bodyInfo);
        if (world.getPlayerByUuid(playerID) != null) {
            updatePlayer(playerID, world);
        }
    }

    @Override
    public BodyInfo getInfo(UUID playerID) {
        return bodyInfoMap.get(playerID);
    }

    @Override
    public void updatePlayer(UUID playerID, ServerWorld world) {
        final PlayerEntity player = world.getPlayerByUuid(playerID);
        if (player != null) {
            if (!bodyInfoMap.get(playerID).isAlive()) {
                player.onKillCommand();
            }
            else {
                setPlayerMaxHealthTo(player, bodyInfoMap.get(playerID).getHealth());
            }
        }

    }

    @Override
    public void handleMergeWithBody(UUID playerID, ServerWorld world) {
        PlayerEntity player = world.getPlayerByUuid(playerID);
        //Retrieve the body entity object  from the capability
        player.setMotion(0, 0, 0);
        player.setVelocity(0, 0, 0);
        player.isAirBorne = false;
        //Teleport the player
        if (bodyInfoMap.containsKey(playerID)) {
            final BodyInfo bodyInfo = bodyInfoMap.get(playerID);
            final BlockPos pos = bodyInfo.getPos();
            TeleportationTools.performTeleport(player, bodyInfo.getDimensionType(), new BlockPos(pos.getX(), pos.getY(), pos.getZ()), Direction.UP);
            //Get the inventory and transfer items
            AstralAPI.getOverworldPsychicInventory(world).ifPresent(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(playerID).setInventoryType(InventoryType.PHYSICAL, player.inventory));
            final Entity entity = world.getEntityByUuid(bodyInfo.getBodyId());
            if (entity instanceof PhysicalBodyEntity) {
                resetPlayerStats(player, (PhysicalBodyEntity) entity);
                entity.onKillCommand();
            }
        }
        //If body is not found, teleport player to their spawn location (bed or world spawn)
        else {
            if (player instanceof ServerPlayerEntity) {
                teleportPlayerToSpawn((ServerPlayerEntity) player);
            }
        }
        bodyInfoMap.remove(playerID);
    }

    private void teleportPlayerToSpawn(ServerPlayerEntity serverPlayerEntity) {
        DimensionType playerSpawnDimension = serverPlayerEntity.getSpawnDimension();
        //Teleport to bed
        if (serverPlayerEntity.getBedPosition().isPresent()) {
            BlockPos bedPos = serverPlayerEntity.getBedPosition().get();
            TeleportationTools.changeDim(serverPlayerEntity, bedPos, playerSpawnDimension);
            serverPlayerEntity.sendMessage(TextComponentUtils.toTextComponent(() -> I18n.format(Constants.SLEEPWALKING_BED)));
        }
        //Teleport to spawn
        else {
            BlockPos serverSpawn = serverPlayerEntity.getServerWorld().getSpawnPoint();
            TeleportationTools.changeDim(serverPlayerEntity, serverSpawn, playerSpawnDimension);
            serverPlayerEntity.sendMessage(TextComponentUtils.toTextComponent(() -> I18n.format(Constants.SLEEPWALKING_SPAWN)));
        }
        resetPlayerStats(serverPlayerEntity);
    }


}
