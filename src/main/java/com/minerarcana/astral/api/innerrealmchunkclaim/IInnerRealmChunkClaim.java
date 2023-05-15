package com.minerarcana.astral.api.innerrealmchunkclaim;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.common.util.INBTSerializable;

public interface IInnerRealmChunkClaim extends INBTSerializable<CompoundTag> {

    boolean isChunkClaimedByPlayer(Player player, ChunkPos chunk);

    boolean claimChunk(ServerPlayer player, ChunkPos chunk);
}
