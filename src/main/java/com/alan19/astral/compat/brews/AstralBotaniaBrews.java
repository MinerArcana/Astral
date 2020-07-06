package com.alan19.astral.compat.brews;

import com.alan19.astral.Astral;
import com.alan19.astral.potions.PotionEffectInstances;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import vazkii.botania.api.brew.Brew;

public class AstralBotaniaBrews {
    private static final DeferredRegister<Brew> BOTANIA_BREWS = DeferredRegister.create(Brew.class, Astral.MOD_ID);

    public static final RegistryObject<Brew> STRONG_ASTRAL_TRAVEL = BOTANIA_BREWS.register("astral_travel", () -> new SupplierBrew(9000, () -> PotionEffectInstances.STRONG_ASTRAL_TRAVEL_INSTANCE).setNotBloodPendantInfusable().setNotIncenseInfusable());
    public static final RegistryObject<Brew> FEVERWEED_BREW = BOTANIA_BREWS.register("feverweed", () -> new SupplierBrew(9000, () -> PotionEffectInstances.FEVERWEED_BOTANICAL_BREW));
    public static final RegistryObject<Brew> SNOWBERRY_BREW = BOTANIA_BREWS.register("snowberry", () -> new SupplierBrew(9000, () -> PotionEffectInstances.SNOWBERRY_BOTANICAL_BREW));

    public static void register(IEventBus modBus) {
        BOTANIA_BREWS.register(modBus);
    }

}
