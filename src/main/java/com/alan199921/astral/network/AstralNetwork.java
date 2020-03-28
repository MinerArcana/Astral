package com.alan199921.astral.network;

import com.alan199921.astral.Astral;
import com.alan199921.astral.api.sleepmanager.ISleepManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
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

        channel.messageBuilder(UpdateInputMessage.class, 4)
                .decoder(UpdateInputMessage::decode)
                .encoder(UpdateInputMessage::encode)
                .consumer(UpdateInputMessage::handle)
                .add();

        channel.messageBuilder(SendAstralTravelStarting.class, 5)
                .decoder(SendAstralTravelStarting::decode)
                .encoder(SendAstralTravelStarting::encode)
                .consumer(SendAstralTravelStarting::handle)
                .add();

        channel.messageBuilder(SendAstralTravelEnding.class, 6)
                .decoder(SendAstralTravelEnding::decode)
                .encoder(SendAstralTravelEnding::encode)
                .consumer(SendAstralTravelEnding::handle)
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

    public static void sendUpdateInputMessage(boolean upNow, boolean downNow, boolean forwardsNow, boolean backwardsNow, boolean leftNow, boolean rightNow, boolean sprintNow) {
        Astral.INSTANCE.sendToServer(new UpdateInputMessage(upNow, downNow, forwardsNow, backwardsNow, leftNow, rightNow, sprintNow));
    }

    public static void sendClientAstralTravelStart(ServerPlayerEntity playerEntity, ISleepManager sleepManager) {
        Astral.INSTANCE.send(PacketDistributor.PLAYER.with(() -> playerEntity), new SendAstralTravelStarting(playerEntity.getEntityId(), sleepManager));
    }

    public static void sendClientAstralTravelEnd(ServerPlayerEntity playerEntity) {
        Astral.INSTANCE.send(PacketDistributor.PLAYER.with(() -> playerEntity), new SendAstralTravelEnding(playerEntity.getEntityId()));
    }
}
