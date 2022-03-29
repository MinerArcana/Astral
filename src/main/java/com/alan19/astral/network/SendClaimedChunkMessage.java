package com.alan19.astral.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Message that causes the client to recreate the HashMap every time it is updated
 */
public class SendClaimedChunkMessage {
    // NBT tag with the HashMap information to be used in InnerRealmChunkClaim#deserializeNBT()
    private final CompoundTag nbt;

    public SendClaimedChunkMessage(CompoundTag nbt) {
        this.nbt = nbt;
    }

    public static SendClaimedChunkMessage decode(FriendlyByteBuf packetBuffer) {
        return new SendClaimedChunkMessage(packetBuffer.readNbt());
    }

    public static void encode(SendClaimedChunkMessage sendClaimedChunkMessage, FriendlyByteBuf packetBuffer) {
        packetBuffer.writeNbt(sendClaimedChunkMessage.nbt);
    }

    /**
     * When the client gets the message, calls the deserialize function in the capability to sync with server
     *
     * @param sendClaimedChunkMessage The message that is being sent from the server
     * @param contextSupplier         The supplier that gives the client access to the world object
     */
    public static void handle(SendClaimedChunkMessage sendClaimedChunkMessage, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            final Optional<Level> optionalWorld = LogicalSidedProvider.CLIENTWORLD.get(contextSupplier.get().getDirection().getReceptionSide());

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
