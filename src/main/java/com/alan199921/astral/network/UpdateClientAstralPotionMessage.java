package com.alan199921.astral.network;

import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Packet to send the Astral travel effect to mobs on the client
 * Not implemented yet
 */
public class UpdateClientAstralPotionMessage {
    public UpdateClientAstralPotionMessage(LivingEntity entityLiving, EffectInstance potionEffect) {
        System.out.println("Applying astral travel");
    }

    public static UpdateClientAstralPotionMessage decode(PacketBuffer packetBuffer) {
        return null;
    }

    public static void encode(UpdateClientAstralPotionMessage updateClientAstralPotionMessage, PacketBuffer packetBuffer) {

    }

    public static boolean handle(UpdateClientAstralPotionMessage updateClientAstralPotionMessage, Supplier<NetworkEvent.Context> contextSupplier) {
        return false;
    }
}
