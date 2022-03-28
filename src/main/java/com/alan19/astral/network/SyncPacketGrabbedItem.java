package com.alan19.astral.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncPacketGrabbedItem {
    private final ItemStack itemStack;

    public SyncPacketGrabbedItem(ItemStack stack) {
        this.itemStack = stack;
    }

    public static SyncPacketGrabbedItem decode(PacketBuffer packetBuffer) {
        return new SyncPacketGrabbedItem(packetBuffer.readItem());
    }

    public static void encode(SyncPacketGrabbedItem syncPacketGrabbedItem, PacketBuffer packetBuffer) {
        packetBuffer.writeItem(syncPacketGrabbedItem.itemStack);
    }

    public static void handle(SyncPacketGrabbedItem syncPacketGrabbedItem, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            ClientPlayerEntity player = Minecraft.getInstance().player;
            player.inventory.setCarried(syncPacketGrabbedItem.itemStack);
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
