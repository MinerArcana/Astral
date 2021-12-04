package com.alan19.astral.api.constructtracker;

import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.mentalconstructs.MentalConstruct;
import com.alan19.astral.mentalconstructs.MentalConstructType;
import com.alan19.astral.util.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;

public class PlayerMentalConstructTracker implements INBTSerializable<CompoundTag> {
    //Map of mental construct ResourceLocation as String to Mental Construct info
    private final Map<ResourceLocation, MentalConstruct> mentalConstructs = new HashMap<>();

    public Map<ResourceLocation, MentalConstruct> getMentalConstructs() {
        return mentalConstructs;
    }

    public void modifyConstructInfo(BlockPos pos, ServerLevel world, MentalConstructType type, int level) {
        if (!mentalConstructs.containsKey(type.getRegistryName())) {
            mentalConstructs.put(type.getRegistryName(), type.create());
        }
        final MentalConstruct entry = mentalConstructs.get(type.getRegistryName());
        ResourceKey<Level> oldConstructWorld = entry.getDimensionKey();
        if (oldConstructWorld != null) {
            BlockPos oldPos = entry.getConstructPos();
            final BlockState blockState = world.getServer().getLevel(oldConstructWorld).getBlockState(oldPos);
            if (blockState.getProperties().contains(Constants.TRACKED_CONSTRUCT)) {
                blockState.setValue(Constants.TRACKED_CONSTRUCT, false);
            }
        }
        entry.setLevel(level);
        entry.setConstructPos(pos);
        entry.setDimensionKey(world);
        world.getBlockState(pos).setValue(Constants.TRACKED_CONSTRUCT, true);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        mentalConstructs.forEach((key, value) -> nbt.put(key.toString(), value.serializeNBT()));
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        for (String mentalConstructKey : nbt.getAllKeys()) {
            final ResourceLocation resourceLocation = new ResourceLocation(mentalConstructKey);
            if (AstralAPI.MENTAL_CONSTRUCT_TYPES.get().containsKey(resourceLocation)) {
                MentalConstruct construct = AstralAPI.MENTAL_CONSTRUCT_TYPES.get().getValue(resourceLocation).create();
                construct.deserializeNBT(nbt.getCompound(mentalConstructKey));
                mentalConstructs.put(resourceLocation, construct);
            }
        }
    }

    public void performAllPassiveEffects(Player playerEntity) {
        for (MentalConstruct mentalConstructIntegerPair : mentalConstructs.values()) {
            if (mentalConstructIntegerPair.getEffectType() == MentalConstruct.EffectType.PASSIVE) {
                mentalConstructIntegerPair.performEffect(playerEntity, mentalConstructIntegerPair.getLevel());
            }
        }
    }

    /**
     * Deletes a mental construct from the player's mental construct tracker. Called when the mental construct block is broken.
     *
     * @param mentalConstruct The mental construct to remove
     */
    public void removeMentalConstruct(MentalConstructType mentalConstruct) {
        mentalConstructs.remove(mentalConstruct.getRegistryName());
    }
}
