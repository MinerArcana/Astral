package com.alan199921.astral.network;

import com.alan199921.astral.effects.AstralEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Packet to send the Astral travel effect to mobs on the client
 * Not implemented yet
 */
public class StartTrackingAstralPotionMessage {
    private final int entityID;
    private final EffectInstance potionEffect;

    public StartTrackingAstralPotionMessage(int entityID, EffectInstance potionEffect) {
        this.entityID = entityID;
        this.potionEffect = potionEffect;
        System.out.println("Applying astral travel");
    }

    public static StartTrackingAstralPotionMessage decode(PacketBuffer packetBuffer) {
        return new StartTrackingAstralPotionMessage(packetBuffer.readInt(), new EffectInstance(AstralEffects.astralTravelEffect, packetBuffer.readInt(), packetBuffer.readInt()));
    }

    public static void encode(StartTrackingAstralPotionMessage startTrackingAstralPotionMessage, PacketBuffer packetBuffer) {
        packetBuffer.writeInt(startTrackingAstralPotionMessage.entityID);
        packetBuffer.writeInt(startTrackingAstralPotionMessage.potionEffect.getDuration());
        packetBuffer.writeInt(startTrackingAstralPotionMessage.potionEffect.getAmplifier());
    }

    public static void handle(StartTrackingAstralPotionMessage startTrackingAstralPotionMessage, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            final Optional<World> optionalWorld = LogicalSidedProvider.CLIENTWORLD.get(contextSupplier.get().getDirection().getReceptionSide());

            optionalWorld.ifPresent(world -> {
                    Entity clientEntity = world.getEntityByID(startTrackingAstralPotionMessage.entityID);
                    assert clientEntity != null;
                    if (clientEntity.isLiving()) {
                        LivingEntity clientLivingEntity = (LivingEntity) world.getEntityByID(startTrackingAstralPotionMessage.entityID);
                        AstralEffects.astralTravelEffect.applyAttributesModifiersToEntity(clientLivingEntity, clientLivingEntity.getAttributes(), startTrackingAstralPotionMessage.potionEffect.getAmplifier());
                    }
                }
            );
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
