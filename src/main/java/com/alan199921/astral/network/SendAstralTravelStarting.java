package com.alan199921.astral.network;

import com.alan199921.astral.api.AstralAPI;
import com.alan199921.astral.api.sleepmanager.ISleepManager;
import com.alan199921.astral.api.sleepmanager.SleepManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

public class SendAstralTravelStarting {
    private final int id;
    private final ISleepManager sleepManager;

    public SendAstralTravelStarting(int id, ISleepManager sleepManager) {
        this.id = id;
        this.sleepManager = sleepManager;
    }

    public static SendAstralTravelStarting decode(PacketBuffer packetBuffer) {
        int playerId = packetBuffer.readInt();
        SleepManager sleepManager = new SleepManager();
        sleepManager.setSleep(packetBuffer.readInt());
        sleepManager.setGoingToInnerRealm(packetBuffer.readBoolean());
        return new SendAstralTravelStarting(playerId, sleepManager);
    }

    public static void encode(SendAstralTravelStarting sendAstralTravelStarting, PacketBuffer packetBuffer) {
        packetBuffer.writeInt(sendAstralTravelStarting.id);
        packetBuffer.writeInt(sendAstralTravelStarting.sleepManager.getSleep());
        packetBuffer.writeBoolean(sendAstralTravelStarting.sleepManager.isGoingToInnerRealm());
    }

    public static void handle(SendAstralTravelStarting sendAstralTravelStarting, Supplier<NetworkEvent.Context> contextSupplier) {
        System.out.println("Received on client!");
        contextSupplier.get().enqueueWork(() -> {
            final Optional<World> optionalWorld = LogicalSidedProvider.CLIENTWORLD.get(contextSupplier.get().getDirection().getReceptionSide());
            optionalWorld.ifPresent(world -> {
                if (world.getEntityByID(sendAstralTravelStarting.id) instanceof PlayerEntity) {
                    PlayerEntity player = (PlayerEntity) world.getEntityByID(sendAstralTravelStarting.id);
                    AstralAPI.getSleepManager(player).ifPresent(sleepManager1 -> {
                        sleepManager1.setSleep(sendAstralTravelStarting.sleepManager.getSleep());
                        sleepManager1.setGoingToInnerRealm(sendAstralTravelStarting.sleepManager.isGoingToInnerRealm());
                    });
                }
            });
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
