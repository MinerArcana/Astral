package com.alan199921.astral.commands;

import com.alan199921.astral.api.AstralAPI;
import com.alan199921.astral.api.psychicinventory.InventoryType;
import com.alan199921.astral.api.psychicinventory.PsychicInventoryInstance;
import com.alan199921.astral.util.UtilityFunctions;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.ItemArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;

public class AstralCommands {

    public static void registerCommands(CommandDispatcher<CommandSource> commandDispatcher) {
        //Burn command is structured similarly to vanilla's give command
        final LiteralArgumentBuilder<CommandSource> burnCommand = Commands.literal("burn").requires(commandSource -> commandSource.hasPermissionLevel(2))
                .then(Commands.argument("targets", EntityArgument.players())
                        .then(Commands.argument("item", ItemArgument.item())
                                .then(Commands.argument("count", IntegerArgumentType.integer(1))
                                        .executes(context -> addItemToInnerRealmInventory(context.getSource(), EntityArgument.getPlayers(context, "targets"), new ItemStack(ItemArgument.getItem(context, "item").getItem(), IntegerArgumentType.getInteger(context, "count")))))
                                .executes(context -> addItemToInnerRealmInventory(context.getSource(), EntityArgument.getPlayers(context, "targets"), new ItemStack(ItemArgument.getItem(context, "item").getItem(), 1))))
                );
        commandDispatcher.register(burnCommand);
    }

    /**
     * Adds an ItemStack to a player's Inner Realm inventory. If the player is using their Inner Realm inventory, inserts directly into inventory instead
     *
     * @param source    The command source
     * @param targets   The players to insert the ItemStack into
     * @param itemStack The ItemStack to be inserted
     * @return The result of the command, 0 in most cases
     * @throws CommandSyntaxException Thrown when there is an error in the command syntax
     */
    private static int addItemToInnerRealmInventory(CommandSource source, Collection<ServerPlayerEntity> targets, ItemStack itemStack) throws CommandSyntaxException {
        ItemStack copy = itemStack.copy();
        AstralAPI.getOverworldPsychicInventory(source.asPlayer().getServerWorld()).ifPresent(iPsychicInventory -> {
            targets.forEach(target -> {
                final PsychicInventoryInstance playerInventory = iPsychicInventory.getInventoryOfPlayer(target.getUniqueID());
                //Insert directly into player inventory if they are using their inner realm inventory
                if (playerInventory.getInventoryType() == InventoryType.INNER_REALM) {
                    target.inventory.addItemStackToInventory(itemStack);
                }
                else {
                    UtilityFunctions.insertIntoAnySlot(playerInventory.getInnerRealmMain(), itemStack);
                }
            });
            if (targets.size() == 1) {
                source.sendFeedback(new TranslationTextComponent("commands.burn.success.single", copy.getCount(), copy.getTextComponent(), targets.iterator().next().getDisplayName()), true);
            }
            else {
                source.sendFeedback(new TranslationTextComponent("commands.burn.success.multiple", copy.getCount(), copy.getTextComponent(), targets.size()), true);
            }
        });
        return 0;
    }
}
