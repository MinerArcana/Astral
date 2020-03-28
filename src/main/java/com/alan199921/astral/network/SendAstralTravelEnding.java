package com.alan199921.astral.network;

import com.alan199921.astral.util.RenderingUtils;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SendAstralTravelEnding {
    private int entityId;

    public SendAstralTravelEnding(int entityId) {
        this.entityId = entityId;
    }

    public static SendAstralTravelEnding decode(PacketBuffer packetBuffer) {
        return new SendAstralTravelEnding(packetBuffer.readInt());
    }

    public static void encode(SendAstralTravelEnding sendAstralTravelEnding, PacketBuffer packetBuffer) {
        packetBuffer.writeInt(sendAstralTravelEnding.entityId);
    }

    public static void handle(SendAstralTravelEnding sendAstralTravelEnding, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> RenderingUtils.setReloadRenderers(true));
        contextSupplier.get().setPacketHandled(true);
    }
}
