package com.alan199921.astral.network;

import com.alan199921.astral.Astral;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;


public class AstralNetwork {
    private static final ResourceLocation CHANNEL_NAME = new ResourceLocation(Astral.MOD_ID, "network");
    private static final String NETWORK_VERSION = new ResourceLocation(Astral.MOD_ID, "1").toString();

    public static SimpleChannel getNetworkChannel() {
        final SimpleChannel channel = NetworkRegistry.ChannelBuilder.named(CHANNEL_NAME)
                .clientAcceptedVersions(version -> true)
                .serverAcceptedVersions(version -> true)
                .networkProtocolVersion(() -> NETWORK_VERSION)
                .simpleChannel();

        channel.messageBuilder(SendClaimedChunkMessage.class, 1)
                .decoder(SendClaimedChunkMessage::decode)
                .encoder(SendClaimedChunkMessage::encode)
                .consumer(SendClaimedChunkMessage::handle)
                .add();

        channel.messageBuilder(StartTrackingAstralPotionMessage.class, 2)
                .decoder(StartTrackingAstralPotionMessage::decode)
                .encoder(StartTrackingAstralPotionMessage::encode)
                .consumer(StartTrackingAstralPotionMessage::handle)
                .add();
        channel.messageBuilder(StopTrackingAstralPotionMessage.class, 3)
                .decoder(StopTrackingAstralPotionMessage::decode)
                .encoder(StopTrackingAstralPotionMessage::encode)
                .consumer(StopTrackingAstralPotionMessage::handle)
                .add();

        channel.messageBuilder(SendPlayerInfoToClientAfterTeleport.class, 4)
                .decoder(SendPlayerInfoToClientAfterTeleport::decode)
                .encoder(SendPlayerInfoToClientAfterTeleport::encode)
                .consumer(SendPlayerInfoToClientAfterTeleport::handle)
                .add();
        return channel;
    }

    public static void sendClaimedChunksToPlayers(CompoundNBT claimedChunksMapNBT) {
        Astral.INSTANCE.send(PacketDistributor.ALL.noArg(), new SendClaimedChunkMessage(claimedChunksMapNBT));
    }

    public static void sendAstralEffectStarting(EffectInstance effectInstance, Entity astralEntity) {
        Astral.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> astralEntity), new StartTrackingAstralPotionMessage(astralEntity.getEntityId(), effectInstance));
    }

    public static void sendAstralEffectEnding(Entity astralEntity) {
        Astral.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> astralEntity), new StopTrackingAstralPotionMessage(astralEntity.getEntityId()));
    }
}
