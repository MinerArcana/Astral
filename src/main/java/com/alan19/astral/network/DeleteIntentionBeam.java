package com.alan19.astral.network;

import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.effects.AstralEffects;
import com.alan19.astral.particle.AstralParticles;
import com.alan19.astral.util.ExperienceHelper;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundEvents;
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
                sender.getCapability(AstralAPI.beamTrackerCapability).ifPresent(tracker -> tracker.getIntentionBeam(sender.getServerWorld()).ifPresent(intentionBeam -> {
                    final BlockPos destinationPos = new BlockPos(intentionBeam.getPosX(), intentionBeam.getPosY() - sender.getEyeHeight(), intentionBeam.getPosZ());
                    final double teleportDistance = Math.sqrt(new BlockPos(sender.getPosX(), sender.getPosYEye(), sender.getPosZ()).distanceSq(intentionBeam.getPosX(), intentionBeam.getPosY(), intentionBeam.getPosZ(), true));
                    final int xpCost = (int) Math.floor(5 + teleportDistance / 10);
                    //Teleport if teleport distance is greater than 1, and if sender
                    if (teleportDistance > 1 && ExperienceHelper.getPlayerXP(sender) >= xpCost && !sender.getServerWorld().getBlockState(destinationPos).hasOpaqueCollisionShape(sender.getServerWorld(), destinationPos) && !sender.getServerWorld().getBlockState(intentionBeam.getPosition()).hasOpaqueCollisionShape(sender.getServerWorld(), intentionBeam.getPosition())) {
                        sender.teleportKeepLoaded(intentionBeam.getPosX(), intentionBeam.getPosY() - sender.getEyeHeight(), intentionBeam.getPosZ());
                        ExperienceHelper.drainPlayerXP(sender, xpCost);
                        Random rand = sender.getRNG();
                        sender.getServerWorld().spawnParticle(AstralParticles.INTENTION_BEAM_PARTICLE.get(), sender.getPosX() + (rand.nextDouble() - rand.nextDouble()), sender.getPosY() + (rand.nextDouble() - rand.nextDouble()), sender.getPosZ() + (rand.nextDouble() - rand.nextDouble()), 7, 0, 0, 0, .1);
                        intentionBeam.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 1F, 1F);
                    }
                    intentionBeam.remove();
                }));
            }
        });
    }
}
