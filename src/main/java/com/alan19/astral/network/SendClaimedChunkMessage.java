package com.alan19.astral.network;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Message that causes the client to recreate the HashMap every time it is updated
 */
public class SendClaimedChunkMessage {
    // NBT tag with the HashMap information to be used in InnerRealmChunkClaim#deserializeNBT()
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

    /**
     * When the client gets the message, calls the deserialize function in the capability to sync with server
     *
     * @param sendClaimedChunkMessage The message that is being sent from the server
     * @param contextSupplier         The supplier that gives the client access to the world object
     */
    public static void handle(SendClaimedChunkMessage sendClaimedChunkMessage, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            final Optional<World> optionalWorld = LogicalSidedProvider.CLIENTWORLD.get(contextSupplier.get().getDirection().getReceptionSide());

//            optionalWorld.ifPresent(world ->
//                    world.getCapability(InnerRealmChunkClaimProvider.CHUNK_CLAIM_CAPABILITY).ifPresent(innerRealmChunkClaimCapability -> {
//                        if (!(innerRealmChunkClaimCapability instanceof InnerRealmChunkClaim))
//                            return;
//                        innerRealmChunkClaimCapability.deserializeNBT(sendClaimedChunkMessage.nbt);
//                    })
//            );
        });

        contextSupplier.get().setPacketHandled(true);
    }
}
