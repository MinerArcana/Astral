package com.alan19.astral.api.innerrealmteleporter;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IInnerRealmTeleporter extends INBTSerializable<CompoundNBT> {
    void teleport(ServerPlayerEntity player);
}
