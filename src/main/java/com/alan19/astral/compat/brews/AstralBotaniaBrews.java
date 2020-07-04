package com.alan19.astral.compat.brews;

import com.alan19.astral.Astral;
import com.alan19.astral.potions.AstralPotions;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import vazkii.botania.api.brew.Brew;

import static com.alan19.astral.effects.AstralEffects.ASTRAL_TRAVEL;
import static com.alan19.astral.potions.AstralPotions.ASTRAL_TRAVEL_POTION;

public class AstralBotaniaBrews {
    private static final Lazy<IForgeRegistry<Brew>> BREW_REGISTRY = Lazy.of(() -> RegistryManager.ACTIVE.getRegistry(Brew.class));
    private static final DeferredRegister<Brew> BOTANIA_BREWS = DeferredRegister.create(Brew.class, Astral.MOD_ID);

    public static final RegistryObject<Brew> LONG_ASTRAL_TRAVEL = BOTANIA_BREWS.register("weak_astral_travel", () -> new Brew(ASTRAL_TRAVEL.get().getLiquidColor(), 9000, ASTRAL_TRAVEL_POTION.getLongPotion().get().getEffects().toArray(new EffectInstance[]{})));
    public static final RegistryObject<Brew> STRONG_ASTRAL_TRAVEL = BOTANIA_BREWS.register("strong_astral_travel", () -> new Brew(ASTRAL_TRAVEL.get().getLiquidColor(), 7000, ASTRAL_TRAVEL_POTION.getStrongPotion().get().getEffects().toArray(new EffectInstance[]{})));
    public static final RegistryObject<Brew> FEVERWEED_BREW = BOTANIA_BREWS.register("feverweed", () -> new Brew(PotionUtils.getPotionColor(AstralPotions.FEVERWEED_BREW.getStrongPotion().get()), 9000, AstralPotions.FEVERWEED_BREW.getStrongPotion().get().getEffects().toArray(new EffectInstance[]{})));
    public static final RegistryObject<Brew> SNOWBERRY_BREW = BOTANIA_BREWS.register("snowberry", () -> new Brew(PotionUtils.getPotionColor(AstralPotions.SNOWBERRY_BREW.getStrongPotion().get()), 9000, AstralPotions.SNOWBERRY_BREW.getStrongPotion().get().getEffects().toArray(new EffectInstance[]{})));

    public static void register(IEventBus modBus) {
        BOTANIA_BREWS.register(modBus);
    }

}
