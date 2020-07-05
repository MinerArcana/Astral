package com.alan19.astral.potions;

import com.alan19.astral.Astral;
import com.alan19.astral.items.AstralItems;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.alan19.astral.effects.AstralEffects.MIND_VENOM;

public class AstralPotions {

    private static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTION_TYPES, Astral.MOD_ID);

    public static final PotionRegistryGroup ASTRAL_TRAVEL_POTION = new PotionRegistryGroup("astral_travel_potion", () -> new Potion(PotionEffectInstances.BASE_ASTRAL_TRAVEL_INSTANCE.toArray(new EffectInstance[]{})), () -> Ingredient.fromItems(AstralItems.TRAVELING_MEDICINE.get(), AstralItems.ETHERIC_POWDER_ITEM.get()))
            .addLongBrew(() -> new Potion(PotionEffectInstances.LONG_ASTRAL_TRAVEL_INSTANCE.toArray(new EffectInstance[]{})))
            .addStrongBrew(() -> new Potion(PotionEffectInstances.STRONG_ASTRAL_TRAVEL_INSTANCE.toArray(new EffectInstance[]{})))
            .register(POTIONS);

    public static final PotionRegistryGroup FEVERWEED_BREW = new PotionRegistryGroup("feverweed_brew", () -> new Potion(PotionEffectInstances.BASE_FEVERWEED_INSTANCE.toArray(new EffectInstance[]{})), () -> Ingredient.fromItems(AstralItems.FEVERWEED.get()))
            .setBase(() -> Potions.THICK)
            .addLongBrew(() -> new Potion(PotionEffectInstances.LONG_FEVERWEED_INSTANCE.toArray(new EffectInstance[]{})))
            .addStrongBrew(() -> new Potion(PotionEffectInstances.STRONG_FEVERWEED_INSTANCE.toArray(new EffectInstance[]{}))).register(POTIONS);
    public static final PotionRegistryGroup SNOWBERRY_BREW = new PotionRegistryGroup("snowberry_brew", () -> new Potion(PotionEffectInstances.SNOWBERRY_BASE_INSTANCE.toArray(new EffectInstance[]{})), () -> Ingredient.fromItems(AstralItems.SNOWBERRY.get()))
            .setBase(() -> Potions.THICK)
            .addLongBrew(() -> new Potion(PotionEffectInstances.LONG_SNOWBERRY_INSTANCE.toArray(new EffectInstance[]{})))
            .addStrongBrew(() -> new Potion(PotionEffectInstances.STRONG_SNOWBERRY_INSTANCE.toArray(new EffectInstance[]{})))
            .register(POTIONS);

    public static final PotionRegistryGroup MIND_VENOM_POTION = new PotionRegistryGroup("mind_venom_potion", () -> new Potion(new EffectInstance(MIND_VENOM.get(), 900)), () -> Ingredient.fromItems(AstralItems.CRYSTAL_WEB_ITEM.get()))
            .addLongBrew(() -> new Potion(new EffectInstance(MIND_VENOM.get(), 1800)))
            .addStrongBrew(() -> new Potion(new EffectInstance(MIND_VENOM.get(), 450, 1)))
            .register(POTIONS);

    @SubscribeEvent
    public static void registerRecipes(FMLCommonSetupEvent event) {
        ASTRAL_TRAVEL_POTION.registerBrewingRecipes();
        FEVERWEED_BREW.registerBrewingRecipes();
        SNOWBERRY_BREW.registerBrewingRecipes();
        MIND_VENOM_POTION.registerBrewingRecipes();
    }

    public static void register(IEventBus modBus) {
        POTIONS.register(modBus);
    }
}