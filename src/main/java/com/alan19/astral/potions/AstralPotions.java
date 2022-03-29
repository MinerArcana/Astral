package com.alan19.astral.potions;

import com.alan19.astral.Astral;
import com.alan19.astral.items.AstralItems;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AstralPotions {

    private static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, Astral.MOD_ID);

    public static final PotionRegistryGroup ASTRAL_TRAVEL_POTION = new PotionRegistryGroup("astral_travel_potion", () -> new Potion(PotionEffectInstances.BASE_ASTRAL_TRAVEL_INSTANCE.toArray(new MobEffectInstance[]{})), () -> Ingredient.of(AstralItems.TRAVELING_MEDICINE.get(), AstralItems.ETHERIC_POWDER_ITEM.get()))
            .addLongBrew(() -> new Potion(PotionEffectInstances.LONG_ASTRAL_TRAVEL_INSTANCE.toArray(new MobEffectInstance[]{})))
            .addStrongBrew(() -> new Potion(PotionEffectInstances.STRONG_ASTRAL_TRAVEL_INSTANCE.toArray(new MobEffectInstance[]{})))
            .register(POTIONS);

    public static final PotionRegistryGroup FEVERWEED_BREW = new PotionRegistryGroup("feverweed_brew", () -> new Potion(PotionEffectInstances.BASE_FEVERWEED_INSTANCE.toArray(new MobEffectInstance[]{})), () -> Ingredient.of(AstralItems.FEVERWEED.get()))
            .setBase(() -> Potions.THICK)
            .addLongBrew(() -> new Potion(PotionEffectInstances.LONG_FEVERWEED_INSTANCE.toArray(new MobEffectInstance[]{})))
            .addStrongBrew(() -> new Potion(PotionEffectInstances.STRONG_FEVERWEED_INSTANCE.toArray(new MobEffectInstance[]{}))).register(POTIONS);
    public static final PotionRegistryGroup SNOWBERRY_BREW = new PotionRegistryGroup("snowberry_brew", () -> new Potion(PotionEffectInstances.SNOWBERRY_BASE_INSTANCE.toArray(new MobEffectInstance[]{})), () -> Ingredient.of(AstralItems.SNOWBERRY.get()))
            .setBase(() -> Potions.THICK)
            .addLongBrew(() -> new Potion(PotionEffectInstances.LONG_SNOWBERRY_INSTANCE.toArray(new MobEffectInstance[]{})))
            .addStrongBrew(() -> new Potion(PotionEffectInstances.STRONG_SNOWBERRY_INSTANCE.toArray(new MobEffectInstance[]{})))
            .register(POTIONS);

    public static final PotionRegistryGroup MIND_VENOM_POTION = new PotionRegistryGroup("mind_venom_potion", () -> new Potion(PotionEffectInstances.BASE_MIND_VENOM_INSTANCE.toArray(new MobEffectInstance[]{})), () -> Ingredient.of(AstralItems.CRYSTAL_WEB_ITEM.get()))
            .addLongBrew(() -> new Potion(PotionEffectInstances.LONG_MIND_VENOM_INSTANCE.toArray(new MobEffectInstance[]{})))
            .addStrongBrew(() -> new Potion(PotionEffectInstances.STRONG_MIND_VENOM_INSTANCE.toArray(new MobEffectInstance[]{})))
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