package com.minerarcana.astral.effect;

import com.minerarcana.astral.Astral;
import com.minerarcana.astral.entity.AstralAttributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AstralEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Astral.MOD_ID);

    public static final RegistryObject<MobEffect> ASTRAL_TRAVEL = EFFECTS.register("astral_travel", () -> new AstralTravelEffect()
                    .addAttributeModifier(AstralAttributes.ASTRAL_ATTACK_DAMAGE.get(), "8ac2f7b4-70ae-434b-ac1d-26de7bfcc494", 0.0D, AttributeModifier.Operation.ADDITION)
            /*.addAttributeModifier(ForgeMod.ENTITY_GRAVITY.get(), "6e379b13-4212-4aca-8f0a-57421752570f", -0.079, AttributeModifier.Operation.ADDITION)*/);
}
