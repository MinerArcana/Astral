package com.alan19.astral.compat.brews;

import com.alan19.astral.potions.PotionEffectInstances;
import net.minecraftforge.eventbus.api.GenericEvent;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;

public class AstralBotaniaBrews {
    public static final Brew ASTRAL_TRAVEL = new SupplierBrew(9000, () -> PotionEffectInstances.STRONG_ASTRAL_TRAVEL_INSTANCE).setNotBloodPendantInfusable().setNotIncenseInfusable().setRegistryName("astral:astral_travel");

    public static final Brew FEVERWEED_BREW = new SupplierBrew(9000, () -> PotionEffectInstances.FEVERWEED_BOTANICAL_BREW).setRegistryName("astral:feverweed");

    public static final Brew SNOWBERRY_BREW = new SupplierBrew(9000, () -> PotionEffectInstances.SNOWBERRY_BOTANICAL_BREW).setRegistryName("astral:snowberry");

    public static void registerBrews(GenericEvent<? extends Brew> genericEvent) {
        BotaniaAPI.instance().getBrewRegistry().registerAll(ASTRAL_TRAVEL, FEVERWEED_BREW, SNOWBERRY_BREW);
    }
}
