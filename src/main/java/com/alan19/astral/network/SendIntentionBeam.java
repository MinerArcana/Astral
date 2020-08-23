package com.alan19.astral.network;

import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.effects.AstralEffects;
import com.alan19.astral.entity.AstralEntities;
import com.alan19.astral.entity.projectile.IntentionBeam;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
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
            if (sender != null && sender.isPotionActive(AstralEffects.ASTRAL_TRAVEL.get())) {
                final Boolean isBeamActive = sender.getCapability(AstralAPI.beamTrackerCapability).map(tracker -> tracker.getIntentionBeam(sender.getServerWorld()).isPresent()).orElse(false);
                if (!isBeamActive){
                    final IntentionBeam beam = new IntentionBeam(0, 32, sender, sender.getServerWorld());
                    beam.setPosition(sender.getPosX(), sender.getPosYEye(), sender.getPosZ());
                    beam.shoot(sender.getLookVec().x, sender.getLookVec().y, sender.getLookVec().z, .25f, 1);
                    sender.getServerWorld().addEntity(beam);
                    sender.getCapability(AstralAPI.beamTrackerCapability).ifPresent(tracker -> tracker.setIntentionBeam(beam));
                }
            }
        });
    }
}
