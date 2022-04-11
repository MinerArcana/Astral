package com.alan19.astral.compat.brews;

import com.alan19.astral.Astral;
import com.alan19.astral.potions.PotionEffectInstances;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import vazkii.botania.api.brew.Brew;

public class AstralBotaniaBrews {
    public static final DeferredRegister<Brew> BREWS = DeferredRegister.createOptional(new ResourceLocation("botania", "brews"), Astral.MOD_ID);
    public static final RegistryObject<Brew> ASTRAL_TRAVEL = BREWS.register("astral_travel", () -> new Brew(9000, () -> PotionEffectInstances.STRONG_ASTRAL_TRAVEL_INSTANCE).setNotBloodPendantInfusable().setNotIncenseInfusable());
    public static final RegistryObject<Brew> FEVERWEED_BREW = BREWS.register("feverweed_brew", () -> new Brew(9000, () -> PotionEffectInstances.FEVERWEED_BOTANICAL_BREW));
    public static final RegistryObject<Brew> SNOWBERRY_BREW = BREWS.register("snowberry_brew", () -> new Brew(9000, () -> PotionEffectInstances.SNOWBERRY_BOTANICAL_BREW));

    public static void registerBrews(IEventBus bus) {
        BREWS.register(bus);
    }
}
