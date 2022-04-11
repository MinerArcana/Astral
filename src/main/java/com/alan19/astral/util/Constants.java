package com.alan19.astral.util;

import com.alan19.astral.Astral;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.predicates.AlternativeLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraftforge.common.Tags;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.UUID;

public class Constants {

    public static final UUID ASTRAL_GRAVITY = UUID.fromString("c58e6f58-28e8-11ea-978f-2e728ce88125");
    public static final UUID ASTRAL_DAMAGE_BOOST = UUID.fromString("8ca991df-c3f6-425e-b19c-5d603fdd43b6");
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
    public static final AttributeModifier SPIRITUAL_MOB_MODIFER = new AttributeModifier(UUID.fromString("0acc71d8-4489-4df5-880f-6bc95fa988ff"), "adds some astral damage for spiritul entities", 4, AttributeModifier.Operation.ADDITION);

    public static final AlternativeLootItemCondition.Builder SILK_TOUCH_OR_SHEARS = MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1)))).or(MatchTool.toolMatches(ItemPredicate.Builder.item().of(Tags.Items.SHEARS)));

    public static ItemStack getAstronomicon() {
        return PatchouliAPI.get().getBookStack(new ResourceLocation(Astral.MOD_ID, "astronomicon"));
    }
}
