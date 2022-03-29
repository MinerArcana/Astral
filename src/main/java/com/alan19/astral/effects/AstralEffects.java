package com.alan19.astral.effects;

import com.alan19.astral.Astral;
import com.alan19.astral.entity.AstralModifiers;
import com.alan19.astral.util.Constants;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstralEffects {

    private static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, Astral.MOD_ID);

    public static final RegistryObject<MobEffect> MIND_VENOM = EFFECTS.register("mind_venom", MindVenomEffect::new);
    public static final RegistryObject<MobEffect> ASTRAL_TRAVEL = EFFECTS.register("astral_travel", () -> new AstralTravelEffect().addAttributeModifier(AstralModifiers.ASTRAL_ATTACK_DAMAGE.get(), Constants.ASTRAL_DAMAGE_BOOST.toString(), 0, AttributeModifier.Operation.ADDITION));

    public static void register(IEventBus modBus) {
        EFFECTS.register(modBus);
    }
}
