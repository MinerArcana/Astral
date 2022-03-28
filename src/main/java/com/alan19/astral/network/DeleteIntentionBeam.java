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
            if (sender != null && sender.hasEffect(AstralEffects.ASTRAL_TRAVEL.get())) {
                sender.getCapability(AstralAPI.beamTrackerCapability).ifPresent(tracker -> tracker.getIntentionBeam(sender.getLevel()).ifPresent(intentionBeam -> {
                    final BlockPos destinationPos = new BlockPos(intentionBeam.getX(), intentionBeam.getY() - sender.getEyeHeight(), intentionBeam.getZ());
                    final double teleportDistance = Math.sqrt(new BlockPos(sender.getX(), sender.getEyeY(), sender.getZ()).distSqr(intentionBeam.getX(), intentionBeam.getY(), intentionBeam.getZ(), true));
                    final int xpCost = (int) Math.floor(5 + teleportDistance / 10);
                    //Teleport if teleport distance is greater than 1, and if sender
                    if (teleportDistance > 1 && ExperienceHelper.getPlayerXP(sender) >= xpCost && !sender.getLevel().getBlockState(destinationPos).isCollisionShapeFullBlock(sender.getLevel(), destinationPos) && !sender.getLevel().getBlockState(intentionBeam.blockPosition()).isCollisionShapeFullBlock(sender.getLevel(), intentionBeam.blockPosition())) {
                        sender.teleportToWithTicket(intentionBeam.getX(), intentionBeam.getY() - sender.getEyeHeight(), intentionBeam.getZ());
                        ExperienceHelper.drainPlayerXP(sender, xpCost);
                        Random rand = sender.getRandom();
                        sender.getLevel().sendParticles(AstralParticles.INTENTION_BEAM_PARTICLE.get(), sender.getX() + (rand.nextDouble() - rand.nextDouble()), sender.getY() + (rand.nextDouble() - rand.nextDouble()), sender.getZ() + (rand.nextDouble() - rand.nextDouble()), 7, 0, 0, 0, .1);
                        intentionBeam.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1F, 1F);
                    }
                    intentionBeam.remove();
                }));
            }
        });
    }
}
