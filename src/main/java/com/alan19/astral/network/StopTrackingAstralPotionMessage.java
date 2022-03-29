package com.alan19.astral.network;

import com.alan19.astral.effects.AstralEffects;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

public class StopTrackingAstralPotionMessage {
    private final int entityID;

    public StopTrackingAstralPotionMessage(int entityID) {
        this.entityID = entityID;
    }

    public static StopTrackingAstralPotionMessage decode(FriendlyByteBuf packetBuffer) {
        return new StopTrackingAstralPotionMessage(packetBuffer.readInt());
    }

    public static void encode(StopTrackingAstralPotionMessage startTrackingAstralPotionMessage, FriendlyByteBuf packetBuffer) {
        packetBuffer.writeInt(startTrackingAstralPotionMessage.entityID);
    }

    public static void handle(StopTrackingAstralPotionMessage startTrackingAstralPotionMessage, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            final Optional<Level> optionalWorld = LogicalSidedProvider.CLIENTWORLD.get(contextSupplier.get().getDirection().getReceptionSide());

            optionalWorld.ifPresent(world -> {
                        Entity clientEntity = world.getEntity(startTrackingAstralPotionMessage.entityID);
                        if (clientEntity instanceof LivingEntity) {
                            LivingEntity clientLivingEntity = (LivingEntity) world.getEntity(startTrackingAstralPotionMessage.entityID);
                            clientLivingEntity.removeEffectNoUpdate(AstralEffects.ASTRAL_TRAVEL.get());
                        }
                    }
            );
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
