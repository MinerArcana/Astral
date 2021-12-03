package com.alan19.astral.network;

import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.effects.AstralEffects;
import com.alan19.astral.entity.projectile.IntentionBeam;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SendIntentionBeam {
    public static void encode(SendIntentionBeam sendIntentionBeam, PacketBuffer packetBuffer) {
        //No need to encode
    }

    public static SendIntentionBeam decode(PacketBuffer packetBuffer) {
        return new SendIntentionBeam();
    }

    public static void handle(SendIntentionBeam sendIntentionBeam, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            final ServerPlayerEntity sender = contextSupplier.get().getSender();
            if (sender != null && sender.hasEffect(AstralEffects.ASTRAL_TRAVEL.get())) {
                final Boolean isBeamActive = sender.getCapability(AstralAPI.beamTrackerCapability).map(tracker -> tracker.getIntentionBeam(sender.getLevel()).isPresent()).orElse(false);
                if (!isBeamActive){
                    final IntentionBeam beam = new IntentionBeam(0, 32, sender, sender.getLevel());
                    beam.setPos(sender.getX(), sender.getEyeY(), sender.getZ());
                    beam.shoot(sender.getLookAngle().x, sender.getLookAngle().y, sender.getLookAngle().z, .25f, 1);
                    sender.getLevel().addFreshEntity(beam);
                    sender.getCapability(AstralAPI.beamTrackerCapability).ifPresent(tracker -> tracker.setIntentionBeam(beam));
                }
            }
        });
    }
}
