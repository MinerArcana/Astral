package com.alan19.astral.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncPacketGrabbedItem {
    private final ItemStack itemStack;

    public SyncPacketGrabbedItem(ItemStack stack) {
        this.itemStack = stack;
    }

    public static SyncPacketGrabbedItem decode(FriendlyByteBuf packetBuffer) {
        return new SyncPacketGrabbedItem(packetBuffer.readItem());
    }

    public static void encode(SyncPacketGrabbedItem syncPacketGrabbedItem, FriendlyByteBuf packetBuffer) {
        packetBuffer.writeItem(syncPacketGrabbedItem.itemStack);
    }

    public static void handle(SyncPacketGrabbedItem syncPacketGrabbedItem, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            LocalPlayer player = Minecraft.getInstance().player;
            player.inventory.setCarried(syncPacketGrabbedItem.itemStack);
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
