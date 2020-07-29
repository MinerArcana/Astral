package com.alan19.astral.network;

import com.alan19.astral.client.gui.AstralContainerProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.function.Supplier;

public class SendOpenAstralInventory {
    public static void encode(SendOpenAstralInventory sendOpenAstralInventory, PacketBuffer packetBuffer) {
        //No need to encode
    }

    public static SendOpenAstralInventory decode(PacketBuffer packetBuffer) {
        return new SendOpenAstralInventory();
    }

    public static void handle(SendOpenAstralInventory sendOpenAstralInventory, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            ServerPlayerEntity player = contextSupplier.get().getSender();
            if (player != null) {
                ItemStack stack = player.inventory.getItemStack();
                player.inventory.setItemStack(ItemStack.EMPTY);
                NetworkHooks.openGui(player, new AstralContainerProvider());

                if (!stack.isEmpty()) {
                    player.inventory.setItemStack(stack);
                    AstralNetwork.syncPacketGrabbedItem(player, stack);
                }
            }
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
