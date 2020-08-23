package com.alan19.astral.network;

import com.alan19.astral.api.AstralAPI;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class DeleteIntentionBeam {

    public static DeleteIntentionBeam decode(PacketBuffer packetBuffer) {
        return new DeleteIntentionBeam();
    }

    public static void encode(DeleteIntentionBeam deleteIntentionBeam, PacketBuffer packetBuffer) {
        // No need to encode
    }

    public static void handle(DeleteIntentionBeam deleteIntentionBeam, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            final ServerPlayerEntity sender = contextSupplier.get().getSender();
            if (sender != null) {
                sender.getCapability(AstralAPI.beamTrackerCapability).ifPresent(tracker -> tracker.getIntentionBeam(sender.getServerWorld()).ifPresent(Entity::remove));
            }
        });
    }
}
