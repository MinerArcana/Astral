package com.alan19.astral.network;

import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.effects.AstralEffects;
import com.alan19.astral.particle.AstralParticles;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Random;
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
            if (sender != null && sender.isPotionActive(AstralEffects.ASTRAL_TRAVEL.get())) {
                sender.getCapability(AstralAPI.beamTrackerCapability).ifPresent(tracker -> tracker.getIntentionBeam(sender.getServerWorld()).ifPresent(intentionBeam -> {
                    final BlockPos blockPos = new BlockPos(intentionBeam.getPosX(), intentionBeam.getPosY() - sender.getEyeHeight(), intentionBeam.getPosZ());
                    //TODO Add XP cost
                    sender.teleportKeepLoaded(intentionBeam.getPosX(), intentionBeam.getPosY() - sender.getEyeHeight(), intentionBeam.getPosZ());
                    intentionBeam.remove();
                    Random rand = sender.getRNG();
                    sender.getServerWorld().spawnParticle(AstralParticles.INTENTION_BEAM_PARTICLE.get(), sender.getPosX() + (rand.nextDouble() - rand.nextDouble()), sender.getPosY() + (rand.nextDouble() - rand.nextDouble()), sender.getPosZ() + (rand.nextDouble() - rand.nextDouble()), 7, 0, 0, 0, .1);
                }));
            }
        });
    }
}
