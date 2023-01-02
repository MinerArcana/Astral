package com.minerarcana.astral.entity;

import com.minerarcana.astral.Astral;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AstralAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Astral.MOD_ID);

    public static final RegistryObject<RangedAttribute> ASTRAL_ATTACK_DAMAGE = ATTRIBUTES.register("generic.astral_attack_damage", () -> new RangedAttribute("attribute.name.generic.astral_attack_damage", 0.0D, 0.0D, 2048.0D));
}
