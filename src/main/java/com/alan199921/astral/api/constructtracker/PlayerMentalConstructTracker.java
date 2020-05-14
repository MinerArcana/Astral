package com.alan199921.astral.api.constructtracker;

import com.alan199921.astral.api.AstralAPI;
import com.alan199921.astral.mentalconstructs.MentalConstruct;
import com.alan199921.astral.mentalconstructs.MentalConstructType;
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
    private final Map<String, MentalConstructEntry> mentalConstructs = new HashMap<>();

    public PlayerMentalConstructTracker() {
        for (Map.Entry<ResourceLocation, MentalConstructType> construct : AstralAPI.MENTAL_CONSTRUCT_TYPES.get().getEntries()) {
            mentalConstructs.put(construct.getKey().toString(), new MentalConstructEntry(construct.getValue().create()));
        }
    }

    public Map<String, MentalConstructEntry> getMentalConstructs() {
        return mentalConstructs;
    }

    public void modifyConstructInfo(BlockPos pos, ServerWorld world, MentalConstructType type, int level) {
        if (!mentalConstructs.containsKey(type.getRegistryName().toString())) {
            mentalConstructs.put(type.getRegistryName().toString(), new MentalConstructEntry(type.create()));
        }
        final MentalConstructEntry entry = mentalConstructs.get(type.getRegistryName().toString());
        DimensionType oldConstructWorld = DimensionType.byName(entry.getConstructWorld());
        if (oldConstructWorld != null) {
            BlockPos oldPos = entry.getConstructPos();
            world.getServer().getWorld(oldConstructWorld).getBlockState(oldPos).with(MentalConstruct.TRACKED_CONSTRUCT, false);
        }
        entry.setLevel(level);
        entry.setConstructPos(pos);
        entry.setConstructWorld(world);
        world.getBlockState(pos).with(MentalConstruct.TRACKED_CONSTRUCT, true);
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
                final MentalConstructEntry entry = new MentalConstructEntry(construct);
                entry.deserializeNBT(nbt.getCompound(mentalConstructKey));
                mentalConstructs.put(mentalConstructKey, entry);
            }
        }
    }

    public void performAllPassiveEffects(PlayerEntity playerEntity) {
        for (MentalConstructEntry mentalConstructIntegerPair : mentalConstructs.values()) {
            if (mentalConstructIntegerPair.getMentalConstruct().getEffectType() == MentalConstruct.EffectType.PASSIVE) {
                mentalConstructIntegerPair.getMentalConstruct().performEffect(playerEntity, mentalConstructIntegerPair.getLevel());
            }
        }
    }

    /**
     * Deletes a mental construct from the player's mental construct tracker. Called when the mental construct block is broken.
     *
     * @param mentalConstruct The mental construct to remove
     * @return The removed mental construct entry
     */
    public MentalConstructEntry removeMentalConstruct(MentalConstructType mentalConstruct) {
        return mentalConstructs.remove(mentalConstruct.getRegistryName().toString());
    }
}
