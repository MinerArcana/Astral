package com.alan199921.astral.network;

import com.alan199921.astral.capabilities.innerrealmchunkclaim.ChunkClaimCapability;
import com.alan199921.astral.capabilities.innerrealmchunkclaim.InnerRealmChunkClaimCapability;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class SendClaimedChunkMessage {
    private final ChunkPos chunkPos;
    private final UUID uuid;

    public SendClaimedChunkMessage(ChunkPos chunkPos, UUID uuid) {
        this.chunkPos = chunkPos;
        this.uuid = uuid;
    }

    public static SendClaimedChunkMessage decode(PacketBuffer packetBuffer) {
        return new SendClaimedChunkMessage(new ChunkPos(packetBuffer.readInt(), packetBuffer.readInt()), packetBuffer.readUniqueId());
    }

    public static void encode(SendClaimedChunkMessage sendClaimedChunkMessage, PacketBuffer packetBuffer) {
        packetBuffer.writeInt(sendClaimedChunkMessage.chunkPos.x);
        packetBuffer.writeInt(sendClaimedChunkMessage.chunkPos.z);
        packetBuffer.writeUniqueId(sendClaimedChunkMessage.uuid);
    }

    public static void handle(SendClaimedChunkMessage sendClaimedChunkMessage, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            final Optional<World> optionalWorld = LogicalSidedProvider.CLIENTWORLD.get(contextSupplier.get().getDirection().getReceptionSide());

            optionalWorld.ifPresent(world ->
                    ChunkClaimCapability.getCapability(world).ifPresent(chunkPressure -> {
                        if (!(chunkPressure instanceof InnerRealmChunkClaimCapability)) return;

                        ((InnerRealmChunkClaimCapability) chunkPressure).handleChunkClaim(world.getPlayerByUuid(sendClaimedChunkMessage.uuid), world.getChunk(sendClaimedChunkMessage.chunkPos.asBlockPos()));
                    })
            );
        });

        contextSupplier.get().setPacketHandled(true);
    }
}
