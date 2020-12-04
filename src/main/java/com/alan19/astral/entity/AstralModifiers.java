package com.alan19.astral.entity;

import com.alan19.astral.Astral;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import java.text.MessageFormat;
import java.util.*;
import java.util.function.Supplier;

public class AstralModifiers {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Attribute.class, Astral.MOD_ID);
    public static final RegistryObject<RangedAttribute> ASTRAL_ATTACK_DAMAGE = ATTRIBUTES.register("astral_attack_damage", () -> new RangedAttribute("astral.astralAttackDamage", 0.0D, 0.0D, 2048.0D));
    public static final UUID SPIRITUAL_MOB_ASTRAL_DAMAGE_BOOST = UUID.fromString("a87fb8b3-27bc-448a-ae6b-3c26a966556d");

    private final static Set<Supplier<? extends Attribute>> attributesToAdd = new HashSet<>();
    private static boolean activated = false;

    public static void register(IEventBus modBus) {
        ATTRIBUTES.register(modBus);
        addModifier(ASTRAL_ATTACK_DAMAGE);
    }

    public static void addModifier(Supplier<? extends Attribute> modifier) {
        if (activated) {
            Astral.LOGGER.error("Attempted to add a new Player modifier after modifiers have already been added.");
        }
        else {
            attributesToAdd.add(modifier);
        }
    }

    // Hacky workaround until https://github.com/MinecraftForge/MinecraftForge/pull/7475 is merged
    public static void init() {
        if (attributesToAdd.isEmpty() || activated) {
            return;
        }
        addAttributesToPlayer();
        addAttributesToMobs();
        activated = true;
    }

    private static void addAttributesToMobs() {
        final AttributeModifierMap attributesForEntity = GlobalEntityTypeAttributes.getAttributesForEntity(EntityType.PHANTOM);
        Map<Attribute, ModifiableAttributeInstance> map = new HashMap<>(attributesForEntity.attributeMap);
        for (Supplier<? extends Attribute> modifier : attributesToAdd) {
            if (!map.containsKey(modifier.get())) {
                Attribute attr = modifier.get();
                map.put(attr, new ModifiableAttributeInstance(attr, instance -> instance.applyPersistentModifier(new AttributeModifier(SPIRITUAL_MOB_ASTRAL_DAMAGE_BOOST, "base astral damage of spiritual mobs", 3, AttributeModifier.Operation.ADDITION))));
            }
        }
        attributesForEntity.attributeMap = map;
        final String format = MessageFormat.format("Added {0} additional attributes to PlayerEntity.", attributesToAdd.size());
        Astral.LOGGER.info(format);
    }

    private static void addAttributesToPlayer() {
        AttributeModifierMap player = GlobalEntityTypeAttributes.getAttributesForEntity(EntityType.PLAYER);
        Map<Attribute, ModifiableAttributeInstance> map = new HashMap<>(player.attributeMap);
        for (Supplier<? extends Attribute> modifier : attributesToAdd) {
            Attribute attr = modifier.get();
            map.put(attr, new ModifiableAttributeInstance(attr, instance -> {
            }));
        }
        player.attributeMap = map;
    }

}
