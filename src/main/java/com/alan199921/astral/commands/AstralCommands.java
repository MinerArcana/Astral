package com.alan199921.astral.commands;

import com.alan199921.astral.api.AstralAPI;
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
import net.minecraftforge.items.IItemHandler;

import java.util.Collection;
import java.util.stream.Stream;

public class AstralCommands {

    public static void registerCommands(CommandDispatcher<CommandSource> commandDispatcher) {
        final LiteralArgumentBuilder<CommandSource> burnCommand = Commands.literal("burn").requires(commandSource -> commandSource.hasPermissionLevel(2))
                .then(Commands.argument("targets", EntityArgument.players())
                        .then(Commands.argument("item", ItemArgument.item())
                                .then(Commands.argument("count", IntegerArgumentType.integer(1))
                                        .executes(context -> addItemToInnerRealmInventory(context.getSource(), EntityArgument.getPlayers(context, "targets"), new ItemStack(ItemArgument.getItem(context, "item").getItem(), IntegerArgumentType.getInteger(context, "count")))))
                                .executes(context -> addItemToInnerRealmInventory(context.getSource(), EntityArgument.getPlayers(context, "targets"), new ItemStack(ItemArgument.getItem(context, "item").getItem(), 1))))
                );
        commandDispatcher.register(burnCommand);
    }

    private static int addItemToInnerRealmInventory(CommandSource source, Collection<ServerPlayerEntity> targets, ItemStack itemStack) throws CommandSyntaxException {
        AstralAPI.getOverworldPsychicInventory(source.asPlayer().getServerWorld()).ifPresent(iPsychicInventory -> {
            final Stream<IItemHandler> targetAstralInventories = targets.stream().map(target -> iPsychicInventory.getInventoryOfPlayer(target.getUniqueID()).getInnerRealmMain());
            targetAstralInventories.forEach(targetAstralInventory -> UtilityFunctions.insertIntoAnySlot(targetAstralInventory, itemStack));
            if (targets.size() == 1) {
                source.sendFeedback(new TranslationTextComponent("commands.burn.success.single", itemStack.getCount(), itemStack.getTextComponent(), targets.iterator().next().getDisplayName()), true);
            }
            else {
                source.sendFeedback(new TranslationTextComponent("commands.burn.success.multiple", itemStack.getCount(), itemStack.getTextComponent(), targets.size()), true);
            }
        });
        return 0;
    }
}
