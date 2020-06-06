package com.alan19.astral.potions;

import com.alan19.astral.Astral;
import com.alan19.astral.configs.AstralConfig;
import com.alan19.astral.items.AstralItems;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.alan19.astral.effects.AstralEffects.ASTRAL_TRAVEL;
import static com.alan19.astral.effects.AstralEffects.MIND_VENOM;

public class AstralPotions {
    private static final DeferredRegister<Potion> POTIONS = new DeferredRegister<>(ForgeRegistries.POTION_TYPES, Astral.MOD_ID);

    public static final int ASTRAL_TRAVEL_DURATION = AstralConfig.getPotionEffectDurations().getAstralTravelDuration();

    public static final PotionRegistryGroup ASTRAL_TRAVEL_POTION = new PotionRegistryGroup("astral_travel_potion", () -> new Potion(new EffectInstance(ASTRAL_TRAVEL.get(), ASTRAL_TRAVEL_DURATION)), () -> Ingredient.fromItems(AstralItems.TRAVELING_MEDICINE.get()))
            .addLongBrew(() -> new Potion(new EffectInstance(ASTRAL_TRAVEL.get(), ASTRAL_TRAVEL_DURATION * 2)))
            .addStrongBrew(() -> new Potion(new EffectInstance(ASTRAL_TRAVEL.get(), ASTRAL_TRAVEL_DURATION / 2, 1)))
            .register(POTIONS);

    public static final int FEVERWEED_BREW_LUCK_DURATION = AstralConfig.getPotionEffectDurations().getFeverweedBrewLuckDuration();
    public static final int FEVERWEED_BREW_HUNGER_DURATION = AstralConfig.getPotionEffectDurations().getFeverweedBrewHungerDuration();

    public static final PotionRegistryGroup FEVERWEED_BREW = new PotionRegistryGroup("feverweed_brew", () -> new Potion(new EffectInstance(Effects.LUCK, FEVERWEED_BREW_LUCK_DURATION, 1), new EffectInstance(Effects.HUNGER, FEVERWEED_BREW_HUNGER_DURATION, 1)), () -> Ingredient.fromItems(AstralItems.FEVERWEED.get()))
            .setBase(() -> Potions.THICK)
            .addLongBrew(() -> new Potion(new EffectInstance(Effects.LUCK, FEVERWEED_BREW_LUCK_DURATION * 2, 1), new EffectInstance(Effects.HUNGER, FEVERWEED_BREW_HUNGER_DURATION * 2, 1)))
            .addStrongBrew(() -> new Potion(new EffectInstance(Effects.LUCK, FEVERWEED_BREW_LUCK_DURATION * 2 / 3, 2), new EffectInstance(Effects.HUNGER, FEVERWEED_BREW_HUNGER_DURATION * 2 / 2, 2))).register(POTIONS);

    public static final int SNOWBERRY_BREW_REGENERATION_DURATION = AstralConfig.getPotionEffectDurations().getSnowberryBrewRegenerationDuration();
    public static final int SNOWBERRY_BREW_NAUSEA_DURATION = AstralConfig.getPotionEffectDurations().getSnowberryBrewNauseaDuration();

    public static final PotionRegistryGroup SNOWBERRY_BREW = new PotionRegistryGroup("snowberry_brew", () -> new Potion(new EffectInstance(Effects.REGENERATION, SNOWBERRY_BREW_REGENERATION_DURATION, 1), new EffectInstance(Effects.NAUSEA, SNOWBERRY_BREW_NAUSEA_DURATION, 1)), () -> Ingredient.fromItems(AstralItems.SNOWBERRY.get()))
            .setBase(() -> Potions.THICK)
            .addLongBrew(() -> new Potion(new EffectInstance(Effects.REGENERATION, SNOWBERRY_BREW_REGENERATION_DURATION * 2, 1), new EffectInstance(Effects.NAUSEA, SNOWBERRY_BREW_NAUSEA_DURATION * 2, 1)))
            .addStrongBrew(() -> new Potion(new EffectInstance(Effects.REGENERATION, SNOWBERRY_BREW_REGENERATION_DURATION * 2 / 3, 2), new EffectInstance(Effects.NAUSEA, SNOWBERRY_BREW_NAUSEA_DURATION * 2 / 3, 2)))
            .register(POTIONS);

    public static final PotionRegistryGroup MIND_VENOM_POTION = new PotionRegistryGroup("mind_venom_potion", () -> new Potion(new EffectInstance(MIND_VENOM.get(), 900)), () -> Ingredient.fromItems(AstralItems.CRYSTAL_WEB_ITEM.get()))
            .addLongBrew(() -> new Potion(new EffectInstance(MIND_VENOM.get(), 1800)))
            .addStrongBrew(() -> new Potion(new EffectInstance(MIND_VENOM.get(), 450, 1)))
            .register(POTIONS);

    public static void register(IEventBus modBus) {
        POTIONS.register(modBus);
    }
}