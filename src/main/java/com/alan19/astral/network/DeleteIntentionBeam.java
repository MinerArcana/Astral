package com.alan19.astral.network;

import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.effects.AstralEffects;
import com.alan19.astral.entity.projectile.IntentionBeam;
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

    /**
     * Teleports the player and deletes the intention beam when the mouse button is released.
     *
     * Teleportation only happens if the beam is at least 1 block from the player, and the teleport costs (5 + blocks traveled / 10) XP
     * @param deleteIntentionBeam The instance of the DeleteIntentionBeam packet
     * @param contextSupplier The context
     */
    public static void handle(DeleteIntentionBeam deleteIntentionBeam, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            final ServerPlayerEntity sender = contextSupplier.get().getSender();
            //Player is only teleported if they have Astral Travel
            if (sender != null && sender.isPotionActive(AstralEffects.ASTRAL_TRAVEL.get())) {
                sender.getCapability(AstralAPI.beamTrackerCapability).ifPresent(tracker -> tracker.getIntentionBeam(sender.getServerWorld()).ifPresent(intentionBeam -> IntentionBeam.teleportPlayer(sender, intentionBeam)));
            }
        });
    }

}
