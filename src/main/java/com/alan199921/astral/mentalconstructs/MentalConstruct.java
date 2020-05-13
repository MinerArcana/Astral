package com.alan199921.astral.mentalconstructs;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class MentalConstruct extends ForgeRegistryEntry<MentalConstruct> implements INBTSerializable<CompoundNBT> {
    abstract void performEffect(PlayerEntity player, int level);
}
