package com.alan19.astral.api.innerrealmteleporter;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.util.INBTSerializable;

public interface IInnerRealmTeleporter extends INBTSerializable<CompoundTag> {
    void teleport(ServerPlayer player);
}
