package com.alan19.astral.mentalconstructs;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;

public abstract class MentalConstruct extends ForgeRegistryEntry<MentalConstruct> implements INBTSerializable<CompoundNBT> {
    private ResourceLocation dimensionName;
    private BlockPos constructPos;
    private int level;

    public MentalConstruct() {
        level = -1;
    }

    public abstract void performEffect(PlayerEntity player, int level);

    public abstract EffectType getEffectType();

    public abstract MentalConstructType getType();

    public ResourceLocation getDimensionName() {
        return dimensionName;
    }

    public void setDimensionName(@Nonnull World dimensionName) {
        this.dimensionName = dimensionName.getDimension().getType().getRegistryName();
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
    public CompoundNBT serializeNBT() {
        final CompoundNBT constructNBT = new CompoundNBT();
        constructNBT.putInt("level", level);
        constructNBT.put("pos", NBTUtil.writeBlockPos(constructPos));
        constructNBT.putString("world", dimensionName.toString());
        return constructNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        dimensionName = new ResourceLocation(nbt.getString("world"));
        constructPos = NBTUtil.readBlockPos(nbt.getCompound("pos"));
        level = nbt.getInt("level");
    }

    public enum EffectType {
        PASSIVE, CONDITIONAL, ACTIVE
    }
}
