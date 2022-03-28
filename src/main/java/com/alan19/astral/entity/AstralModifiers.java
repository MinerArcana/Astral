package com.alan19.astral.entity;

import com.alan19.astral.Astral;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import java.util.UUID;

public class AstralModifiers {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Attribute.class, Astral.MOD_ID);
    public static final RegistryObject<RangedAttribute> ASTRAL_ATTACK_DAMAGE = ATTRIBUTES.register("astral_attack_damage", () -> new RangedAttribute("astral.astralAttackDamage", 0.0D, 0.0D, 2048.0D));
    public static final UUID SPIRITUAL_MOB_ASTRAL_DAMAGE_BOOST = UUID.fromString("a87fb8b3-27bc-448a-ae6b-3c26a966556d");

    public static void register(IEventBus modBus) {
        ATTRIBUTES.register(modBus);
    }


}
