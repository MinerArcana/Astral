package com.alan19.astral.network;

import com.alan19.astral.util.RenderingUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SendAstralTravelEnding {
    private final int entityId;

    public SendAstralTravelEnding(int entityId) {
        this.entityId = entityId;
    }

    public static SendAstralTravelEnding decode(FriendlyByteBuf packetBuffer) {
        return new SendAstralTravelEnding(packetBuffer.readInt());
    }

    public static void encode(SendAstralTravelEnding sendAstralTravelEnding, FriendlyByteBuf packetBuffer) {
        packetBuffer.writeInt(sendAstralTravelEnding.entityId);
    }

    public static void handle(SendAstralTravelEnding sendAstralTravelEnding, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> RenderingUtils.setReloadRenderers(true));
        contextSupplier.get().setPacketHandled(true);
    }
}
