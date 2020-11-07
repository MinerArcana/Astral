package com.alan19.astral.util;

import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.conditions.Alternative;
import net.minecraft.loot.conditions.MatchTool;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolType;
import vazkii.patchouli.common.item.PatchouliItems;

import java.util.UUID;

public class Constants {
    public static final UUID ASTRAL_GRAVITY = UUID.fromString("c58e6f58-28e8-11ea-978f-2e728ce88125");
    public static final UUID ASTRAL_EFFECT_DAMAGE_BOOST = UUID.fromString("8ca991df-c3f6-425e-b19c-5d603fdd43b6");
    public static final String SLEEPWALKING_BED = "astral.chat_message.sleepwalking.bed";
    public static final String SLEEPWALKING_SPAWN = "astral.chat_message.sleepwalking.spawn";
    public static final String COMMANDS_BURN_SUCCESS_SINGLE = "commands.burn.success.single";
    public static final String COMMANDS_BURN_SUCCESS_MULTIPLE = "commands.burn.success.multiple";
    public static final String INVALID_WITHDRAWAL = "astral.chat_message.invalid_withdrawal";
    public static final AttributeModifier DISABLES_GRAVITY = new AttributeModifier(ASTRAL_GRAVITY, "disables gravity", -1, AttributeModifier.Operation.MULTIPLY_TOTAL);
    public static final int ETHEREAL_BLOCK_OPACITY = 0;
    public static final BooleanProperty TRACKED_CONSTRUCT = BooleanProperty.create("tracked_construct");
    public static final IntegerProperty LIBRARY_LEVEL = IntegerProperty.create("library_level", 0, 100);
    public static final BooleanProperty CAPPED_LEVEL = BooleanProperty.create("capped_level");
    public static final Attribute ASTRAL_ATTACK_DAMAGE = new RangedAttribute("astral.astralAttackDamage", 0.0D, 0.0D, 2048.0D);
    public static final AttributeModifier SPIRITUAL_MOB_MODIFER = new AttributeModifier(UUID.fromString("0acc71d8-4489-4df5-880f-6bc95fa988ff"), "adds some astral damage for spiritul entities", 4, AttributeModifier.Operation.ADDITION);

    public static final Alternative.Builder SILK_TOUCH_OR_SHEARS = MatchTool.builder(ItemPredicate.Builder.create().enchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.IntBound.atLeast(1)))).alternative(MatchTool.builder(ItemPredicate.Builder.create().tag(Tags.Items.SHEARS)));

    public static final ToolType SWORDS = ToolType.get("sword");
    public static final ToolType SHEARS = ToolType.get("shears");

    public static ItemStack getAstronomicon() {
        final ItemStack patchouliBook = new ItemStack(PatchouliItems.book);
        patchouliBook.getOrCreateTag().putString("patchouli:book", "astral:astronomicon");
        return patchouliBook;
    }
}
