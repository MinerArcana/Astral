package com.alan19.astral.util;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;

import java.util.UUID;

public class Constants {
    public static final UUID astralGravity = UUID.fromString("c58e6f58-28e8-11ea-978f-2e728ce88125");
    public static final String SLEEPWALKING_BED = "astral.chat_message.sleepwalking.bed";
    public static final String SLEEPWALKING_SPAWN = "astral.chat_message.sleepwalking.spawn";
    public static final String COMMANDS_BURN_SUCCESS_SINGLE = "commands.burn.success.single";
    public static final String COMMANDS_BURN_SUCCESS_MULTIPLE = "commands.burn.success.multiple";
    public static final AttributeModifier DISABLES_GRAVITY = new AttributeModifier(astralGravity, "disables gravity", -1, AttributeModifier.Operation.MULTIPLY_TOTAL).setSaved(true);
    public static final int ETHEREAL_BLOCK_OPACITY = 0;
    public static final BooleanProperty TRACKED_CONSTRUCT = BooleanProperty.create("tracked_construct");
    public static final IntegerProperty LIBRARY_LEVEL = IntegerProperty.create("library_level", 0, 100);
    public static final BooleanProperty CAPPED_LEVEL = BooleanProperty.create("capped_level");
}
