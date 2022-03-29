package com.alan19.astral.network;

import com.alan19.astral.Astral;
import com.alan19.astral.api.sleepmanager.ISleepManager;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;


public class AstralNetwork {
    private static final ResourceLocation CHANNEL_NAME = new ResourceLocation(Astral.MOD_ID, "network");
    private static final String NETWORK_VERSION = new ResourceLocation(Astral.MOD_ID, "1").toString();

    public static SimpleChannel getNetworkChannel() {
        final SimpleChannel channel = NetworkRegistry.ChannelBuilder.named(CHANNEL_NAME)
                .clientAcceptedVersions(version -> true)
                .serverAcceptedVersions(version -> true)
                .networkProtocolVersion(() -> NETWORK_VERSION)
                .simpleChannel();

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

        channel.messageBuilder(SendOfferingBrazierFinished.class, 7)
                .decoder(SendOfferingBrazierFinished::decode)
                .encoder(SendOfferingBrazierFinished::encode)
                .consumer(SendOfferingBrazierFinished::handle)
                .add();

        channel.messageBuilder(SendOpenAstralInventory.class, 8)
                .decoder(SendOpenAstralInventory::decode)
                .encoder(SendOpenAstralInventory::encode)
                .consumer(SendOpenAstralInventory::handle)
                .add();

        channel.messageBuilder(SyncPacketGrabbedItem.class, 9)
                .decoder(SyncPacketGrabbedItem::decode)
                .encoder(SyncPacketGrabbedItem::encode)
                .consumer(SyncPacketGrabbedItem::handle)
                .add();

        channel.messageBuilder(SendIntentionBeam.class, 10)
                .decoder(SendIntentionBeam::decode)
                .encoder(SendIntentionBeam::encode)
                .consumer(SendIntentionBeam::handle)
                .add();

        channel.messageBuilder(DeleteIntentionBeam.class, 11)
                .decoder(DeleteIntentionBeam::decode)
                .encoder(DeleteIntentionBeam::encode)
                .consumer(DeleteIntentionBeam::handle)
                .add();
        return channel;
    }

    public static void sendAstralEffectStarting(MobEffectInstance effectInstance, Entity astralEntity) {
        Astral.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> astralEntity), new StartTrackingAstralPotionMessage(astralEntity.getId(), effectInstance));
    }

    public static void sendAstralEffectEnding(Entity astralEntity) {
        Astral.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> astralEntity), new StopTrackingAstralPotionMessage(astralEntity.getId()));
    }

    public static void sendUpdateInputMessage(boolean upNow, boolean downNow, boolean forwardsNow, boolean backwardsNow, boolean leftNow, boolean rightNow, boolean sprintNow) {
        Astral.INSTANCE.sendToServer(new UpdateInputMessage(upNow, downNow, forwardsNow, backwardsNow, leftNow, rightNow, sprintNow));
    }

    public static void sendClientAstralTravelStart(ServerPlayer playerEntity, ISleepManager sleepManager) {
        Astral.INSTANCE.send(PacketDistributor.PLAYER.with(() -> playerEntity), new SendAstralTravelStarting(playerEntity.getId(), sleepManager));
    }

    public static void sendClientAstralTravelEnd(ServerPlayer playerEntity) {
        Astral.INSTANCE.send(PacketDistributor.PLAYER.with(() -> playerEntity), new SendAstralTravelEnding(playerEntity.getId()));
    }

    public static void sendOfferingBrazierFinishParticles(BlockPos blockPos, LevelChunk chunk) {
        Astral.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), new SendOfferingBrazierFinished(blockPos));
    }

    public static void sendOpenAstralInventory() {
        Astral.INSTANCE.send(PacketDistributor.SERVER.noArg(), new SendOpenAstralInventory());
    }

    public static void syncPacketGrabbedItem(ServerPlayer player, ItemStack stack) {
        Astral.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new SyncPacketGrabbedItem(stack));
    }

    public static void sendIntentionBeam() {
        Astral.INSTANCE.sendToServer(new SendIntentionBeam());
    }

    public static void deleteIntentionBeam() {
        Astral.INSTANCE.sendToServer(new DeleteIntentionBeam());
    }
}
