package com.alan19.astral.api.bodytracker;

import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.api.psychicinventory.InventoryType;
import com.alan19.astral.dimensions.TeleportationTools;
import com.alan19.astral.entity.physicalbody.PhysicalBodyEntity;
import com.alan19.astral.util.Constants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class BodyTracker implements IBodyTracker {
    private final Map<UUID, CompoundNBT> bodyTrackerMap = new HashMap<>();
    public static final UUID HEALTH_ID = UUID.fromString("8bce997a-4c3a-11e6-beb8-9e71128cae77");

    @Override
    public Map<UUID, CompoundNBT> getBodyTrackerMap() {
        return bodyTrackerMap;
    }

    /**
     * Update's the player's body's NBT using the specified NBT and then update the player's maximum HP, or kill them if the body drops below 0HP
     *
     * @param uuid  The UUID of the player
     * @param nbt   The NBT to update the HashMap with
     * @param world The ServerWorld to update the player with
     */
    @Override
    public void setBodyNBT(UUID uuid, CompoundNBT nbt, ServerWorld world) {
        bodyTrackerMap.put(uuid, nbt);
        final ServerPlayerEntity playerByUuid = (ServerPlayerEntity) world.getPlayerByUuid(uuid);
        if (playerByUuid != null) {
            updatePlayer(playerByUuid);
        }
    }

    /**
     * Sets the maximum health of the player based on the body's current HP
     * (The player's max HP while they have Astral Travel is equal to their physical body's current HP)
     * Decreases the player's HP to their new max HP if their HP is above the new max HP
     *
     * @param playerEntity The player to update
     * @param newMaxHealth The new max health of the player
     */
    private void setPlayerMaxHealthTo(PlayerEntity playerEntity, float newMaxHealth) {
        final ModifiableAttributeInstance maxHPAttributeInstance = playerEntity.getAttribute(Attributes.MAX_HEALTH);
        if (maxHPAttributeInstance != null) {
            // New max HP is equal to the current HP of the body, and the modifier is calculated using the body's HP subtracted by the player's normal max HP, which would usually result in either 0 or a negative number
            // This calculation decreases the player's max HP to the body's current HP
            maxHPAttributeInstance.removeModifier(HEALTH_ID);
            float healthModifier = newMaxHealth - playerEntity.getMaxHealth();
            maxHPAttributeInstance.applyPersistentModifier(new AttributeModifier(HEALTH_ID, "physical body health", healthModifier, AttributeModifier.Operation.ADDITION));
            if (playerEntity.getHealth() > newMaxHealth) {
                playerEntity.setHealth(newMaxHealth);
            }
        }
    }


    /**
     * Sets the player's max HP based on the body's information in the HashMap. If the body has 0 HP or less, kill the player.
     *
     * @param playerEntity The player to update
     */
    public void updatePlayer(ServerPlayerEntity playerEntity) {
        if (bodyTrackerMap.containsKey(playerEntity.getUniqueID())) {
            final float bodyHealth = bodyTrackerMap.get(playerEntity.getUniqueID()).getFloat("Health");
            if (bodyHealth <= 0) {
                playerEntity.onKillCommand();
            }
            else {
                setPlayerMaxHealthTo(playerEntity, bodyHealth);
            }
        }
    }

    /**
     * Merges a player who has Astral Travel with their body. This means the player is teleported back to their body, switches to using their regular inventory, and lose the modifiers provided by Astral Travel. If there is no record on their body, teleport them back to their spawn point.
     *
     * @param serverPlayerEntity The player to attempt to merge with their body
     * @param world              The ServerWorld object used to find the body and access the Psychic Inventory
     */
    @Override
    public void mergePlayerWithBody(ServerPlayerEntity serverPlayerEntity, ServerWorld world) {
        // Reset player motion
        serverPlayerEntity.setMotion(0, 0, 0);
        serverPlayerEntity.isAirBorne = false;
        serverPlayerEntity.fallDistance = 0;
        // Teleport the player
        if (bodyTrackerMap.containsKey(serverPlayerEntity.getUniqueID())) {
            final CompoundNBT bodyNBT = bodyTrackerMap.get(serverPlayerEntity.getUniqueID());
            final ListNBT posNBT = bodyNBT.getList("Pos", net.minecraftforge.common.util.Constants.NBT.TAG_DOUBLE);
            final BlockPos pos = new BlockPos(posNBT.getDouble(0), posNBT.getDouble(1), posNBT.getDouble(2));
            TeleportationTools.performTeleport(serverPlayerEntity, RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation(bodyNBT.getString("Dimension"))), new BlockPos(pos.getX(), pos.getY(), pos.getZ()), Direction.UP);

            // Get the inventory and transfer items
            AstralAPI.getOverworldPsychicInventory(world).ifPresent(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(serverPlayerEntity.getUniqueID()).setInventoryType(InventoryType.PHYSICAL, serverPlayerEntity.inventory));

            // Kill the body, reset player stats, and remove the body from the tracker
            findBody(world, bodyNBT).ifPresent(LivingEntity::onKillCommand);
            resetPlayerStats(serverPlayerEntity, bodyNBT);
            bodyTrackerMap.remove(serverPlayerEntity.getUniqueID());
        }
        //If body is not found, teleport player to their spawn location (bed or world spawn)
        else {
            RegistryKey<World> playerSpawnDimension = serverPlayerEntity.func_241141_L_();
            //Teleport to bed
            if (serverPlayerEntity.getBedPosition().isPresent()) {
                BlockPos bedPos = serverPlayerEntity.getBedPosition().get();
                TeleportationTools.performTeleport(serverPlayerEntity, playerSpawnDimension, bedPos, null);
                serverPlayerEntity.sendMessage(new TranslationTextComponent(Constants.SLEEPWALKING_BED), serverPlayerEntity.getUniqueID());
            }
            //Teleport to spawn
            else {
                BlockPos serverSpawn = serverPlayerEntity.getServerWorld().getSpawnPoint();
                TeleportationTools.performTeleport(serverPlayerEntity, playerSpawnDimension, serverSpawn, null);
                serverPlayerEntity.sendMessage(new TranslationTextComponent(Constants.SLEEPWALKING_SPAWN), serverPlayerEntity.getUniqueID());
            }
            resetPlayerStats(serverPlayerEntity);
        }
    }

    /**
     * Attempts to find the a player's body using the body's NBT from the HashMap.
     *
     * @param world   The ServerWorld used to search for the entity. Could be any dimension since the dimension is stored in the NBT.
     * @param bodyNBT The NBT of the body.
     * @return Optional.empty() if the body cannot be found, an optional with the body if it is found
     */
    public Optional<PhysicalBodyEntity> findBody(ServerWorld world, CompoundNBT bodyNBT) {
        final ServerWorld bodyWorld = world.getServer().getWorld(RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation(bodyNBT.getString("Dimension"))));
        final INBT id = bodyNBT.get("UUID");
        // Add null checks to avoid NPEs if entity is not found
        if (id != null && bodyWorld != null) {
            return Optional.ofNullable((PhysicalBodyEntity) bodyWorld.getEntityByUuid(NBTUtil.readUniqueId(id)));
        }
        return Optional.empty();
    }

    /**
     * Removes the player's attribute modifiers (nullified gravity, max health penalty) given by Astral Travel
     * Sets their current HP to the HP of the body and set their hunger to the hunger they had before Astral Travel started
     *
     * @param playerEntity The player to reset
     * @param bodyNBT      The NBT of the body
     */
    public static void resetPlayerStats(PlayerEntity playerEntity, CompoundNBT bodyNBT) {
        final ModifiableAttributeInstance maxHealthAttribute = playerEntity.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealthAttribute != null) {
            maxHealthAttribute.removeModifier(HEALTH_ID);
        }
        playerEntity.setHealth(bodyNBT.getFloat("Health"));
        playerEntity.getFoodStats().setFoodLevel(bodyNBT.getInt("Hunger"));
    }

    /**
     * Removes the player's attribute modifiers (nullified gravity, max health penalty) given by Astral Travel
     * Since we are unable to get the body's information, which is the player's stats before Astral Travel, we assume the player had max HP and hunger before they used Astral Travel and set the attributes with those values.
     *
     * @param playerEntity The player to reset
     */
    public static void resetPlayerStats(PlayerEntity playerEntity) {
        final ModifiableAttributeInstance maxHealthAttribute = playerEntity.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealthAttribute != null) {
            maxHealthAttribute.removeModifier(HEALTH_ID);
        }
        playerEntity.setHealth(playerEntity.getMaxHealth());
        playerEntity.getFoodStats().setFoodLevel(20);
    }


    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        bodyTrackerMap.forEach((uuid, mobNBT) -> nbt.put(uuid.toString(), mobNBT));
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        nbt.keySet().forEach(s -> bodyTrackerMap.put(UUID.fromString(s), nbt.getCompound(s)));
    }

}
