package com.minerarcana.astral.api.bodytracker;


import com.minerarcana.astral.api.AstralCapabilities;
import com.minerarcana.astral.api.innerrealmteleporter.TeleportationTools;
import com.minerarcana.astral.api.psychicinventory.InventoryType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BodyTracker implements IBodyTracker {
    public static final UUID HEALTH_ID = UUID.fromString("8bce997a-4c3a-11e6-beb8-9e71128cae77");
    private final Map<UUID, CompoundTag> bodyTrackerMap = new HashMap<>();

    /**
     * Removes the player's attribute modifiers (nullified gravity, max health penalty) given by Astral Travel
     * Sets their current HP to the HP of the body and set their hunger to the hunger they had before Astral Travel started
     *
     * @param playerEntity The player to reset
     * @param bodyNBT      The NBT of the body
     */
    public static void resetPlayerStats(Player playerEntity, CompoundTag bodyNBT) {
        final AttributeInstance maxHealthAttribute = playerEntity.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealthAttribute != null) {
            maxHealthAttribute.removeModifier(HEALTH_ID);
        }
        playerEntity.setHealth(bodyNBT.getFloat("Health"));
        playerEntity.getFoodData().setFoodLevel(bodyNBT.getInt("Hunger"));
    }

    /**
     * Removes the player's attribute modifiers (nullified gravity, max health penalty) given by Astral Travel
     * Since we are unable to get the body's information, which is the player's stats before Astral Travel, we assume the player had max HP and hunger before they used Astral Travel and set the attributes with those values.
     *
     * @param playerEntity The player to reset
     */
    public static void resetPlayerStats(Player playerEntity) {
        final AttributeInstance maxHealthAttribute = playerEntity.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealthAttribute != null) {
            maxHealthAttribute.removeModifier(HEALTH_ID);
        }
        playerEntity.setHealth(playerEntity.getMaxHealth());
        playerEntity.getFoodData().setFoodLevel(20);
    }

    @Override
    public Map<UUID, CompoundTag> getBodyTrackerMap() {
        return bodyTrackerMap;
    }

    /**
     * Update's the player's body's NBT using the specified NBT
     *
     * @param player The player to record
     */
    @Override
    public void setBodyNBT(Player player) {
        // TODO Refactor parameters to take a ServerPlayerEntity
        bodyTrackerMap.put(player.getUUID(), player.serializeNBT());
    }

    /**
     * Sets the maximum health of the player based on the body's current HP
     * (The player's max HP while they have Astral Travel is equal to their physical body's current HP)
     * Decreases the player's HP to their new max HP if their HP is above the new max HP
     *
     * @param playerEntity The player to update
     * @param newMaxHealth The new max health of the player
     */
    private void setPlayerMaxHealthTo(Player playerEntity, float newMaxHealth) {
        final AttributeInstance maxHPAttributeInstance = playerEntity.getAttribute(Attributes.MAX_HEALTH);
        if (maxHPAttributeInstance != null) {
            // New max HP is equal to the current HP of the body, and the modifier is calculated using the body's HP subtracted by the player's normal max HP, which would usually result in either 0 or a negative number
            // This calculation decreases the player's max HP to the body's current HP
            maxHPAttributeInstance.removeModifier(HEALTH_ID);
            float healthModifier = newMaxHealth - playerEntity.getMaxHealth();
            maxHPAttributeInstance.addTransientModifier(new AttributeModifier(HEALTH_ID, "physical body health", healthModifier, AttributeModifier.Operation.ADDITION));
            if (playerEntity.getHealth() > newMaxHealth) {
                playerEntity.setHealth(newMaxHealth);
            }
        }
    }

//    /**
//     * Attempts to find the a player's body using the body's NBT from the HashMap.
//     *
//     * @param world   The ServerWorld used to search for the entity. Could be any dimension since the dimension is stored in the NBT.
//     * @param bodyNBT The NBT of the body.
//     * @return Optional.empty() if the body cannot be found, an optional with the body if it is found
//     */
//    public Optional<PhysicalBodyEntity> findBody(ServerWorld world, CompoundTag bodyNBT) {
//        final ServerWorld bodyWorld = world.getServer().getWorld(RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation(bodyNBT.getString("Dimension"))));
//        final INBT id = bodyNBT.get("UUID");
//        // Add null checks to avoid NPEs if entity is not found
//        if (id != null && bodyWorld != null) {
//            return Optional.ofNullable((PhysicalBodyEntity) bodyWorld.getEntityByUuid(NBTUtil.readUniqueId(id)));
//        }
//        return Optional.empty();
//    }

    /**
     * Sets the player's max HP based on the body's information in the HashMap. If the body has 0 HP or less, kill the player.
     *
     * @param playerEntity The player to update
     */
    public void updatePlayer(Player playerEntity) {
        if (bodyTrackerMap.containsKey(playerEntity.getUUID())) {
            final float bodyHealth = bodyTrackerMap.get(playerEntity.getUUID()).getFloat("Health");
            if (bodyHealth <= 0) {
                playerEntity.kill();
            } else {
                setPlayerMaxHealthTo(playerEntity, bodyHealth);
            }
        }
    }

    /**
     * Merges a player who has Astral Travel with their body. This means the player is teleported back to their body, switches to using their regular inventory, and lose the modifiers provided by Astral Travel. If there is no record on their body, teleport them back to their spawn point.
     *
     * @param serverPlayerEntity The player to attempt to merge with their body
     */
    @Override
    public void mergePlayerWithBody(ServerPlayer serverPlayerEntity) {
        // Reset player motion
        serverPlayerEntity.setDeltaMovement(0, 0, 0);
//        serverPlayerEntity.isFallFlying() = false;
        serverPlayerEntity.fallDistance = 0;

        AstralCapabilities.getPsychicInventory(serverPlayerEntity.level).ifPresent(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(serverPlayerEntity.getUUID()).setInventoryType(InventoryType.PHYSICAL, serverPlayerEntity.getInventory()));

        // Teleport the player
        if (bodyTrackerMap.containsKey(serverPlayerEntity.getUUID())) {
            final CompoundTag bodyNBT = bodyTrackerMap.get(serverPlayerEntity.getUUID());
            final ListTag posNBT = bodyNBT.getList("Pos", Tag.TAG_DOUBLE);
            final BlockPos pos = new BlockPos(posNBT.getDouble(0), posNBT.getDouble(1), posNBT.getDouble(2));
            TeleportationTools.performTeleport(serverPlayerEntity, ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(bodyNBT.getString("Dimension"))), new BlockPos(pos.getX(), pos.getY(), pos.getZ()), Direction.UP);

            // Kill the body, reset player stats, and remove the body from the tracker
//            findBody(world, bodyNBT).ifPresent(LivingEntity::onKillCommand);
            resetPlayerStats(serverPlayerEntity, bodyNBT);
            bodyTrackerMap.remove(serverPlayerEntity.getUUID());
        }
        //If body is not found, teleport player to their spawn location (bed or world spawn)
        else {
            ResourceKey<Level> playerSpawnDimension = serverPlayerEntity.getRespawnDimension();

            //Teleport to bed
            if (serverPlayerEntity.getRespawnPosition() != null) {
                BlockPos bedPos = serverPlayerEntity.getRespawnPosition();
                TeleportationTools.performTeleport(serverPlayerEntity, playerSpawnDimension, bedPos, null);
//                serverPlayerEntity.sendMessage(new TranslationTextComponent(Constants.SLEEPWALKING_BED), serverPlayerEntity.getUUID());
            }
            //Teleport to spawn
            else {
                BlockPos serverSpawn = serverPlayerEntity.getServer().overworld().getSharedSpawnPos();
                TeleportationTools.performTeleport(serverPlayerEntity, playerSpawnDimension, serverSpawn, null);
//                serverPlayerEntity.sendMessage(new TranslationTextComponent(Constants.SLEEPWALKING_SPAWN), serverPlayerEntity.getUniqueID());
            }
            resetPlayerStats(serverPlayerEntity);
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        bodyTrackerMap.forEach((uuid, mobNBT) -> nbt.put(uuid.toString(), mobNBT));
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        nbt.getAllKeys().forEach(s -> bodyTrackerMap.put(UUID.fromString(s), nbt.getCompound(s)));
    }

}
