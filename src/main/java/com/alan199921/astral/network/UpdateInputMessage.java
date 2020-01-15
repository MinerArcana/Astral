package com.alan199921.astral.network;

import com.alan199921.astral.flight.InputHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Borrowed from IronJetpack's packet handling code
 * https://github.com/BlakeBr0/IronJetpacks/blob/b4ef8c4a59050f2c7368a16721a2d88ae60b19ce/src/main/java/com/blakebr0/ironjetpacks/network/message/UpdateInputMessage.java
 */
public class UpdateInputMessage {
    private boolean up;
    private boolean down;
    private boolean forwards;
    private boolean backwards;
    private boolean left;
    private boolean right;
    private boolean sprint;

    public UpdateInputMessage(boolean up, boolean down, boolean forwards, boolean backwards, boolean left, boolean right, boolean sprint) {
        this.up = up;
        this.down = down;
        this.forwards = forwards;
        this.backwards = backwards;
        this.left = left;
        this.right = right;
        this.sprint = sprint;
    }

    public static UpdateInputMessage decode(PacketBuffer buffer) {
        return new UpdateInputMessage(buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean());
    }

    public static void encode(UpdateInputMessage message, PacketBuffer buffer) {
        buffer.writeBoolean(message.up);
        buffer.writeBoolean(message.down);
        buffer.writeBoolean(message.forwards);
        buffer.writeBoolean(message.backwards);
        buffer.writeBoolean(message.left);
        buffer.writeBoolean(message.right);
        buffer.writeBoolean(message.sprint);
    }

    public static void handle(UpdateInputMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            PlayerEntity player = context.get().getSender();
            if (player != null) {
                InputHandler.update(player, message.up, message.down, message.forwards, message.backwards, message.left, message.right, message.sprint);
            }
        });

        context.get().setPacketHandled(true);
    }
}
