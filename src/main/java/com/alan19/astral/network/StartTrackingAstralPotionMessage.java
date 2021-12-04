package com.alan19.astral.network;

import com.alan19.astral.effects.AstralEffects;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Packet to send the Astral travel effect to mobs on the client
 * Not implemented yet
 */
public class StartTrackingAstralPotionMessage {
    private final int entityID;
    private final MobEffectInstance potionEffect;

    public StartTrackingAstralPotionMessage(int entityID, MobEffectInstance potionEffect) {
        this.entityID = entityID;
        this.potionEffect = potionEffect;
    }

    public static StartTrackingAstralPotionMessage decode(FriendlyByteBuf packetBuffer) {
        return new StartTrackingAstralPotionMessage(packetBuffer.readInt(), new MobEffectInstance(AstralEffects.ASTRAL_TRAVEL.get(), packetBuffer.readInt(), packetBuffer.readInt()));
    }

    public static void encode(StartTrackingAstralPotionMessage message, FriendlyByteBuf packetBuffer) {
        packetBuffer.writeInt(message.entityID);
        packetBuffer.writeInt(message.potionEffect.getDuration());
        packetBuffer.writeInt(message.potionEffect.getAmplifier());
    }

    public static void handle(StartTrackingAstralPotionMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            final Optional<Level> optionalWorld = LogicalSidedProvider.CLIENTWORLD.get(contextSupplier.get().getDirection().getReceptionSide());

            optionalWorld.ifPresent(world -> {
                        Entity clientEntity = world.getEntity(message.entityID);
                        if (clientEntity instanceof LivingEntity) {
                            LivingEntity clientLivingEntity = (LivingEntity) world.getEntity(message.entityID);
                            clientLivingEntity.addEffect(message.potionEffect);
                        }
                    }
            );
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
