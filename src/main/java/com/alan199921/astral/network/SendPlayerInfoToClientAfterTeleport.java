package com.alan199921.astral.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SendPlayerInfoToClientAfterTeleport {
    private final int playerId;

    public SendPlayerInfoToClientAfterTeleport(int playerId) {
        this.playerId = playerId;
    }

    public static void encode(SendPlayerInfoToClientAfterTeleport sendPlayerInfoToClientAfterTeleport, PacketBuffer packetBuffer) {
        packetBuffer.writeInt(sendPlayerInfoToClientAfterTeleport.playerId);
    }

    public static SendPlayerInfoToClientAfterTeleport decode(PacketBuffer packetBuffer) {

        return new SendPlayerInfoToClientAfterTeleport(packetBuffer.readInt());
    }

    public static boolean handle(SendPlayerInfoToClientAfterTeleport sendPlayerInfoToClientAfterTeleport, Supplier<NetworkEvent.Context> contextSupplier) {
        return false;
    }
}
