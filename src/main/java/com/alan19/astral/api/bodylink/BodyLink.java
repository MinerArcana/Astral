package com.alan19.astral.api.bodylink;

import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.api.psychicinventory.InventoryType;
import com.alan19.astral.dimensions.TeleportationTools;
import com.alan19.astral.entity.physicalbody.PhysicalBodyEntity;
import com.alan19.astral.util.Constants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;

import java.util.Optional;
import java.util.UUID;

public class BodyLink implements IBodyLink {
    public static final UUID HEALTH_ID = UUID.fromString("8bce997a-4c3a-11e6-beb8-9e71128cae77");
    private BodyInfo bodyInfo;

    public static void setPlayerMaxHealthTo(PlayerEntity playerEntity, float newMaxHealth) {
        playerEntity.getAttribute(SharedMonsterAttributes.MAX_HEALTH).removeModifier(HEALTH_ID);
        float healthModifier = newMaxHealth - playerEntity.getMaxHealth();
        playerEntity.getAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier(HEALTH_ID, "physical body health", healthModifier, AttributeModifier.Operation.ADDITION));
        if (playerEntity.getHealth() > newMaxHealth) {
            playerEntity.setHealth(newMaxHealth);
        }
    }

    /**
     * Resets the player entity's stats
     *
     * @param playerEntity       The player entity to recieve stats from the physical body
     * @param physicalBodyEntity The physical body storing player stats
     */
    public static void resetPlayerStats(PlayerEntity playerEntity, PhysicalBodyEntity physicalBodyEntity) {
        playerEntity.getAttribute(SharedMonsterAttributes.MAX_HEALTH).removeModifier(HEALTH_ID);
        playerEntity.setHealth(physicalBodyEntity.getHealth());
        playerEntity.getFoodStats().setFoodLevel((int) physicalBodyEntity.getHungerLevel());
    }

    public static void resetPlayerStats(PlayerEntity playerEntity) {
        playerEntity.getAttribute(SharedMonsterAttributes.MAX_HEALTH).removeModifier(HEALTH_ID);
    }

    public Optional<PhysicalBodyEntity> getBody(ServerWorld world) {
        final ServerWorld bodyWorld = world.getServer().getWorld(bodyInfo.getDimensionType());
        final Chunk chunk = bodyWorld.getChunkAt(bodyInfo.getPos());
        chunk.setLoaded(true);
        return Optional.ofNullable((PhysicalBodyEntity) bodyWorld.getEntityByUuid(bodyInfo.getBodyId()));
    }

    @Override
    public void setBodyInfo(BodyInfo bodyInfo) {
        this.bodyInfo = bodyInfo;
    }

    @Override
    public Optional<BodyInfo> getBodyInfo() {
        return Optional.ofNullable(bodyInfo);
    }

    @Override
    public void mergeBodies(PlayerEntity playerEntity, ServerWorld world) {
        if (playerEntity instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) playerEntity;
            serverPlayerEntity.setMotion(0, 0, 0);
            serverPlayerEntity.isAirBorne = false;
            serverPlayerEntity.fallDistance = 0;
            //Teleport the player
            if (bodyInfo != null) {
                final BlockPos pos = bodyInfo.getPos();
                TeleportationTools.performTeleport(serverPlayerEntity, bodyInfo.getDimensionType(), new BlockPos(pos.getX(), pos.getY(), pos.getZ()), Direction.UP);
                //Get the inventory and transfer items
                AstralAPI.getOverworldPsychicInventory(world).ifPresent(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(serverPlayerEntity.getUniqueID()).setInventoryType(InventoryType.PHYSICAL, serverPlayerEntity.inventory));

                getBody(world).ifPresent(LivingEntity::onKillCommand);
            }
            //If body is not found, teleport player to their spawn location (bed or world spawn)
            else {
                teleportPlayerToSpawn(serverPlayerEntity);
            }
        }

    }

    private void teleportPlayerToSpawn(ServerPlayerEntity serverPlayerEntity) {
        DimensionType playerSpawnDimension = serverPlayerEntity.getSpawnDimension();
        //Teleport to bed
        if (serverPlayerEntity.getBedPosition().isPresent()) {
            BlockPos bedPos = serverPlayerEntity.getBedPosition().get();
            TeleportationTools.performTeleport(serverPlayerEntity, playerSpawnDimension, bedPos, null);
            serverPlayerEntity.sendMessage(new TranslationTextComponent(Constants.SLEEPWALKING_BED));
        }
        //Teleport to spawn
        else {
            BlockPos serverSpawn = serverPlayerEntity.getServerWorld().getSpawnPoint();
            TeleportationTools.performTeleport(serverPlayerEntity, playerSpawnDimension, serverSpawn, null);
            serverPlayerEntity.sendMessage(new TranslationTextComponent(Constants.SLEEPWALKING_SPAWN));
        }
        resetPlayerStats(serverPlayerEntity);
    }

    @Override
    public void updatePlayer(ServerPlayerEntity playerEntity, ServerWorld world) {
        if (bodyInfo != null) {
            if (!bodyInfo.isAlive()) {
                playerEntity.onKillCommand();
            }
            else {
                setPlayerMaxHealthTo(playerEntity, bodyInfo.getHealth());
            }
        }
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT.putBoolean("bodyInfoIsPresent", bodyInfo != null);
        if (bodyInfo != null){
            compoundNBT.put("bodyInfo", bodyInfo.serializeNBT());
        }
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (nbt.getBoolean("bodyInfoIsPresent")){
            bodyInfo = new BodyInfo(nbt.getCompound("bodyInfo"));
        }
    }
}
