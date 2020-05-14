package com.alan199921.astral.api.constructtracker;

import com.alan199921.astral.blocks.MentalConstructController;
import com.alan199921.astral.mentalconstructs.AstralMentalConstructs;
import com.alan199921.astral.mentalconstructs.MentalConstruct;
import com.alan199921.astral.mentalconstructs.MentalConstructType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.RegistryObject;

import java.util.HashMap;
import java.util.Map;

public class PlayerMentalConstructTracker implements INBTSerializable<CompoundNBT> {
    private final Map<String, MentalConstructEntry> mentalConstructs = new HashMap<>();

    public PlayerMentalConstructTracker() {
        for (RegistryObject<MentalConstructType> construct : AstralMentalConstructs.MENTAL_CONSTRUCTS.getEntries()) {
            mentalConstructs.put(construct.getId().toString(), new MentalConstructEntry(construct.get().create()));
        }
    }

    public void modifyConstructInfo(BlockPos pos, ServerWorld world, MentalConstructType type, int level) {
        final MentalConstructEntry entry = mentalConstructs.get(type.getRegistryName().toString());
        if (entry.getConstructPos().isPresent() && entry.getConstructWorld().isPresent()) {
            DimensionType oldConstructWorld = DimensionType.byName(entry.getConstructWorld().get());
            if (oldConstructWorld != null) {
                BlockPos oldPos = entry.getConstructPos().get();
                world.getServer().getWorld(oldConstructWorld).getBlockState(oldPos).with(MentalConstruct.TRACKED_CONSTRUCT, false);
            }
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
        for (RegistryObject<MentalConstructType> mentalConstruct : AstralMentalConstructs.MENTAL_CONSTRUCTS.getEntries()) {
            final MentalConstruct createdMentalConstruct = mentalConstruct.get().create();
            MentalConstructEntry entry = new MentalConstructEntry(createdMentalConstruct);
            entry.deserializeNBT(nbt.getCompound(mentalConstruct.getId().toString()));
            mentalConstructs.put(mentalConstruct.getId().toString(), entry);
        }
    }

    public void performAllPassiveEffects(PlayerEntity playerEntity) {
        for (MentalConstructEntry mentalConstructIntegerPair : mentalConstructs.values()) {
            if (mentalConstructIntegerPair.getMentalConstruct().getEffectType() == MentalConstruct.EffectType.PASSIVE) {
                mentalConstructIntegerPair.getMentalConstruct().performEffect(playerEntity, mentalConstructIntegerPair.getLevel());
            }
        }
    }

    public void resetConstructEffect(MentalConstructType mentalConstruct, World world, BlockPos blockPos) {
        if (mentalConstructs.containsKey(mentalConstruct.getRegistryName().toString())) {
            final MentalConstructEntry entry = mentalConstructs.get(mentalConstruct.getRegistryName().toString());
            if (entry.getConstructPos().isPresent() && entry.getConstructWorld().isPresent() && entry.getConstructPos().get().equals(blockPos) && entry.getConstructWorld().get().toString().equals(world.getDimension().getType().getRegistryName().toString())) {
                if (entry.getConstructPos().isPresent() && entry.getConstructWorld().isPresent()) {
                    DimensionType oldDimension = DimensionType.byName(entry.getConstructWorld().get());
                    BlockPos oldPos = entry.getConstructPos().get();
                    final MinecraftServer server = world.getServer();
                    if (oldDimension != null && server != null && server.getWorld(oldDimension).getBlockState(oldPos).getBlock() instanceof MentalConstructController) {
                        server.getWorld(oldDimension).getBlockState(oldPos).with(MentalConstruct.TRACKED_CONSTRUCT, false);
                    }
                }
                entry.setLevel(-1);
                entry.setConstructPos(null);
                entry.setConstructWorld(null);
            }
        }
    }
}
