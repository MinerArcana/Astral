package com.minerarcana.astral.api.innerrealmcubegen;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.common.util.INBTSerializable;

public interface IInnerRealmCubeGen extends INBTSerializable<CompoundTag> {
    boolean isChunkClaimedByPlayer(Player player, ChunkPos chunk);

    boolean claimChunk(Player player, ChunkPos chunk);
}
