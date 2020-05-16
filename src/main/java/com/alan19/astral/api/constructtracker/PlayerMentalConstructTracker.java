package com.alan19.astral.api.constructtracker;

import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.mentalconstructs.MentalConstruct;
import com.alan19.astral.mentalconstructs.MentalConstructType;
import com.alan19.astral.util.Constants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;

public class PlayerMentalConstructTracker implements INBTSerializable<CompoundNBT> {
    private final Map<String, MentalConstruct> mentalConstructs = new HashMap<>();

    public Map<String, MentalConstruct> getMentalConstructs() {
        return mentalConstructs;
    }

    public void modifyConstructInfo(BlockPos pos, ServerWorld world, MentalConstructType type, int level) {
        if (!mentalConstructs.containsKey(type.getRegistryName().toString())) {
            mentalConstructs.put(type.getRegistryName().toString(), type.create());
        }
        final MentalConstruct entry = mentalConstructs.get(type.getRegistryName().toString());
        DimensionType oldConstructWorld = DimensionType.byName(entry.getDimensionName());
        if (oldConstructWorld != null) {
            BlockPos oldPos = entry.getConstructPos();
            world.getServer().getWorld(oldConstructWorld).getBlockState(oldPos).with(Constants.TRACKED_CONSTRUCT, false);
        }
        entry.setLevel(level);
        entry.setConstructPos(pos);
        entry.setDimensionName(world);
        world.getBlockState(pos).with(Constants.TRACKED_CONSTRUCT, true);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        mentalConstructs.forEach((key, value) -> nbt.put(key, value.serializeNBT()));
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        for (String mentalConstructKey : nbt.keySet()) {
            final ResourceLocation resourceLocation = new ResourceLocation(mentalConstructKey);
            if (AstralAPI.MENTAL_CONSTRUCT_TYPES.get().containsKey(resourceLocation)) {
                MentalConstruct construct = AstralAPI.MENTAL_CONSTRUCT_TYPES.get().getValue(resourceLocation).create();
                construct.deserializeNBT(nbt.getCompound(mentalConstructKey));
                mentalConstructs.put(mentalConstructKey, construct);
            }
        }
    }

    public void performAllPassiveEffects(PlayerEntity playerEntity) {
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
        mentalConstructs.remove(mentalConstruct.getRegistryName().toString());
    }
}