package com.alan19.astral.compat.brews;

import com.alan19.astral.Astral;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import vazkii.botania.api.brew.Brew;

public class AstralBotaniaBrews {
    private static final DeferredRegister<Brew> BOTANIA_BREWS = DeferredRegister.create(Brew.class, Astral.MOD_ID);

//    public static final RegistryObject<Brew> LONG_ASTRAL_TRAVEL = BOTANIA_BREWS.register("weak_astral_travel", () -> new Brew(0xc8c8c8, 9000, new EffectInstance(ASTRAL_TRAVEL.get(), AstralPotions.ASTRAL_TRAVEL_DURATION)));
//    public static final RegistryObject<Brew> STRONG_ASTRAL_TRAVEL = BOTANIA_BREWS.register("strong_astral_travel", () -> new Brew(ASTRAL_TRAVEL.get().getLiquidColor(), 7000, ASTRAL_TRAVEL_POTION.getStrongPotion().get().getEffects().toArray(new EffectInstance[]{})));
//    public static final RegistryObject<Brew> FEVERWEED_BREW = BOTANIA_BREWS.register("feverweed", () -> new Brew(PotionUtils.getPotionColor(AstralPotions.FEVERWEED_BREW.getStrongPotion().get()), 9000, AstralPotions.FEVERWEED_BREW.getStrongPotion().get().getEffects().toArray(new EffectInstance[]{})));
//    public static final RegistryObject<Brew> SNOWBERRY_BREW = BOTANIA_BREWS.register("snowberry", () -> new Brew(PotionUtils.getPotionColor(AstralPotions.SNOWBERRY_BREW.getStrongPotion().get()), 9000, AstralPotions.SNOWBERRY_BREW.getStrongPotion().get().getEffects().toArray(new EffectInstance[]{})));

    public static void register(IEventBus modBus) {
        BOTANIA_BREWS.register(modBus);
    }

}
