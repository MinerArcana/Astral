package com.alan19.astral.network;

import com.alan19.astral.events.astraltravel.flight.InputHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Borrowed from IronJetpack's packet handling code
 * https://github.com/BlakeBr0/IronJetpacks/blob/b4ef8c4a59050f2c7368a16721a2d88ae60b19ce/src/main/java/com/blakebr0/ironjetpacks/network/message/UpdateInputMessage.java
 */
public class UpdateInputMessage {
    private final boolean up;
    private final boolean down;
    private final boolean forwards;
    private final boolean backwards;
    private final boolean left;
    private final boolean right;
    private final boolean sprint;

    public UpdateInputMessage(boolean up, boolean down, boolean forwards, boolean backwards, boolean left, boolean right, boolean sprint) {
        this.up = up;
        this.down = down;
        this.forwards = forwards;
        this.backwards = backwards;
        this.left = left;
        this.right = right;
        this.sprint = sprint;
    }

    public static UpdateInputMessage decode(FriendlyByteBuf buffer) {
        return new UpdateInputMessage(buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean());
    }

    public static void encode(UpdateInputMessage message, FriendlyByteBuf buffer) {
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
            Player player = context.get().getSender();
            if (player != null) {
                InputHandler.update(player, message.up, message.down, message.forwards, message.backwards, message.left, message.right, message.sprint);
            }
        });

        context.get().setPacketHandled(true);
    }
}
