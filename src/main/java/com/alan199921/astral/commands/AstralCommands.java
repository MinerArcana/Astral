package com.alan199921.astral.commands;

import com.alan199921.astral.api.AstralAPI;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.ItemArgument;
import net.minecraft.command.arguments.ItemInput;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

public class AstralCommands {

    private static int addItemToPsychicInventory(CommandSource source, ItemInput item) throws CommandSyntaxException {
        ServerPlayerEntity serverPlayerEntity = source.asPlayer();
        final LazyOptional<ItemStackHandler> astralInventoryOptional = AstralAPI.getOverworldPsychicInventory(source.getWorld()).map(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(serverPlayerEntity.getUniqueID()).getAstralMainInventory());
        astralInventoryOptional.ifPresent(itemStackHandler -> {
            for (int i = 0; i < itemStackHandler.getSlots(); i++) {
                if (itemStackHandler.insertItem(i, new ItemStack(item.getItem()), true).isEmpty()) {
                    itemStackHandler.insertItem(i, new ItemStack(item.getItem()), false);
                }
            }
        });
        return 0;
    }

    public static LiteralArgumentBuilder<CommandSource> create() {
        return Commands.literal("givePsychic")
                .requires(commandSource -> commandSource.hasPermissionLevel(2))
                .then(Commands.argument("item", ItemArgument.item()))
                .executes(context -> addItemToPsychicInventory(context.getSource(), ItemArgument.getItem(context, "item")));
    }
}
