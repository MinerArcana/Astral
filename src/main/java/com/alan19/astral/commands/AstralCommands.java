package com.alan19.astral.commands;

import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.api.psychicinventory.InventoryType;
import com.alan19.astral.api.psychicinventory.PsychicInventoryInstance;
import com.alan19.astral.dimensions.AstralDimensions;
import com.alan19.astral.util.Constants;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.Collection;

public class AstralCommands {

    public static final String TARGETS = "targets";
    public static final String ITEM = "item";
    public static final String COUNT = "count";

    public static void registerCommands(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        //Burn command is structured similarly to vanilla's give command
        final LiteralArgumentBuilder<CommandSourceStack> burnCommand = Commands.literal("burn").requires(commandSource -> commandSource.hasPermission(2))
                .then(Commands.argument(TARGETS, EntityArgument.players())
                        .then(Commands.argument(ITEM, ItemArgument.item())
                                .then(Commands.argument(COUNT, IntegerArgumentType.integer(1))
                                        .executes(context -> addItemToInnerRealmInventory(context.getSource(), EntityArgument.getPlayers(context, TARGETS), new ItemStack(ItemArgument.getItem(context, ITEM).getItem(), IntegerArgumentType.getInteger(context, COUNT)))))
                                .executes(context -> addItemToInnerRealmInventory(context.getSource(), EntityArgument.getPlayers(context, TARGETS), new ItemStack(ItemArgument.getItem(context, ITEM).getItem(), 1))))
                );
        commandDispatcher.register(burnCommand);
    }

    /**
     * Adds an ItemStack to a player's Inner Realm inventory. If the player is in the Inner Realm, inserts directly into inventory instead
     *
     * @param source    The command source
     * @param targets   The players to insert the ItemStack into
     * @param itemStack The ItemStack to be inserted
     * @return The result of the command, 0 in most cases
     * @throws CommandSyntaxException Thrown when there is an error in the command syntax
     */
    private static int addItemToInnerRealmInventory(CommandSourceStack source, Collection<ServerPlayer> targets, ItemStack itemStack) throws CommandSyntaxException {
        // TODO Possibly simulate offering brazier recipes
        ItemStack copy = itemStack.copy();
        AstralAPI.getOverworldPsychicInventory(source.getPlayerOrException().getLevel()).ifPresent(iPsychicInventory -> {
            targets.forEach(target -> {
                final PsychicInventoryInstance playerInventory = iPsychicInventory.getInventoryOfPlayer(target.getUUID());
                //Insert directly into player inventory if they are using their inner realm inventory
                if (playerInventory.getInventoryType() == InventoryType.ASTRAL && target.level.dimension() == AstralDimensions.INNER_REALM) {
                    target.inventory.add(itemStack);
                }
                else {
                    ItemHandlerHelper.insertItemStacked(playerInventory.getInnerRealmMain(), itemStack, false);
                }
            });
            if (targets.size() == 1) {
                source.sendSuccess(new TranslatableComponent(Constants.COMMANDS_BURN_SUCCESS_SINGLE, copy.getCount(), copy.getDisplayName(), targets.iterator().next().getDisplayName()), true);
            }
            else {
                source.sendSuccess(new TranslatableComponent(Constants.COMMANDS_BURN_SUCCESS_MULTIPLE, copy.getCount(), copy.getDisplayName(), targets.size()), true);
            }
        });
        return 0;
    }
}
