package com.alan199921.astral.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SendClaimedChunkMessage {
    public static SendClaimedChunkMessage decode(PacketBuffer packetBuffer) {
        return null;
    }

    public static void encode(SendClaimedChunkMessage sendClaimedChunkMessage, PacketBuffer packetBuffer) {

    }

    public static boolean handle(SendClaimedChunkMessage sendClaimedChunkMessage, Supplier<NetworkEvent.Context> contextSupplier) {
        return false;
    }
}
