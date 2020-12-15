package com.alan19.astral.api.innerrealmchunkclaim;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.util.INBTSerializable;

public interface IInnerRealmChunkClaim extends INBTSerializable<CompoundNBT> {

    boolean isChunkClaimedByPlayer(ServerPlayerEntity player, ChunkPos chunk);

    boolean claimChunk(ServerPlayerEntity player, ChunkPos chunk);
}
