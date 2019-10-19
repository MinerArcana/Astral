package com.alan199921.astral.network;

import com.alan199921.astral.capabilities.innerrealmchunkclaim.InnerRealmChunkClaimCapability;
import com.alan199921.astral.capabilities.innerrealmchunkclaim.InnerRealmChunkClaimProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

public class SendClaimedChunkMessage {
    private final CompoundNBT nbt;

    public SendClaimedChunkMessage(CompoundNBT nbt) {
        this.nbt = nbt;
    }

    public static SendClaimedChunkMessage decode(PacketBuffer packetBuffer) {
        return new SendClaimedChunkMessage(packetBuffer.readCompoundTag());
    }

    public static void encode(SendClaimedChunkMessage sendClaimedChunkMessage, PacketBuffer packetBuffer) {
        packetBuffer.writeCompoundTag(sendClaimedChunkMessage.nbt);
    }

    public static void handle(SendClaimedChunkMessage sendClaimedChunkMessage, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            final Optional<World> optionalWorld = LogicalSidedProvider.CLIENTWORLD.get(contextSupplier.get().getDirection().getReceptionSide());

            optionalWorld.ifPresent(world ->
                    world.getCapability(InnerRealmChunkClaimProvider.CHUNK_CLAIM_CAPABILITY).ifPresent(innerRealmChunkClaimCapability -> {
                        if (!(innerRealmChunkClaimCapability instanceof InnerRealmChunkClaimCapability)) return;
                        innerRealmChunkClaimCapability.deserializeNBT(sendClaimedChunkMessage.nbt);
                    })
            );
        });

        contextSupplier.get().setPacketHandled(true);
    }
}
