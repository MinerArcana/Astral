package com.alan199921.astral.network;

import com.alan199921.astral.effects.AstralEffects;
import com.alan199921.astral.events.TravellingHandlers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateClientAstralPotionMessage {
    public UpdateClientAstralPotionMessage(LivingEntity entityLiving, EffectInstance potionEffect) {
        System.out.println("Applying astral travel");
        entityLiving.addPotionEffect(potionEffect);
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
