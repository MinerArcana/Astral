package com.alan199921.astral.network;

import com.alan199921.astral.Astral;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class AstralNetwork {
    public static final ResourceLocation CHANNEL_NAME = new ResourceLocation(Astral.MOD_ID, "network");
    public static final String NETWORK_VERSION = new ResourceLocation(Astral.MOD_ID, "1").toString();

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

        channel.messageBuilder(UpdateClientAstralPotionMessage.class, 2)
                .decoder(UpdateClientAstralPotionMessage::decode)
                .encoder(UpdateClientAstralPotionMessage::encode)
                .consumer(UpdateClientAstralPotionMessage::handle)
                .add();

        return channel;
    }
}
