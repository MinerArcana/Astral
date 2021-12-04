package com.alan19.astral.mentalconstructs;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;

public abstract class MentalConstruct extends ForgeRegistryEntry<MentalConstruct> implements INBTSerializable<CompoundTag> {
    private ResourceKey<Level> dimensionKey;
    private BlockPos constructPos;
    private int level;

    public MentalConstruct() {
        level = -1;
    }

    public abstract void performEffect(Player player, int level);

    public abstract EffectType getEffectType();

    public abstract MentalConstructType getType();

    public ResourceKey<Level> getDimensionKey() {
        return dimensionKey;
    }

    public void setDimensionKey(@Nonnull Level dimensionKey) {
        this.dimensionKey = dimensionKey.dimension();
    }

    public BlockPos getConstructPos() {
        return constructPos;
    }

    public void setConstructPos(BlockPos constructPos) {
        this.constructPos = constructPos;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public CompoundTag serializeNBT() {
        final CompoundTag constructNBT = new CompoundTag();
        constructNBT.putInt("level", level);
        constructNBT.put("pos", NbtUtils.writeBlockPos(constructPos));
        constructNBT.putString("world", dimensionKey.toString());
        return constructNBT;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        dimensionKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(nbt.getString("world")));
        constructPos = NbtUtils.readBlockPos(nbt.getCompound("pos"));
        level = nbt.getInt("level");
    }

    public enum EffectType {
        PASSIVE, CONDITIONAL, ACTIVE
    }
}
