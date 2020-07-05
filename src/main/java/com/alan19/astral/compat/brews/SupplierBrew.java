package com.alan19.astral.compat.brews;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import vazkii.botania.api.brew.Brew;

import java.util.List;
import java.util.function.Supplier;

public class SupplierBrew extends Brew {

    private final Supplier<Integer> color;
    private final Supplier<List<EffectInstance>> effectSuppliers;

    public SupplierBrew(int cost, Supplier<List<EffectInstance>> effects) {
        super(0, cost);
        this.color = () -> PotionUtils.getPotionColorFromEffectList(effects.get());
        effectSuppliers = effects;
    }

    @Override
    public int getColor(ItemStack stack) {
        return color.get();
    }

    @Override
    public List<EffectInstance> getPotionEffects(ItemStack stack) {
        return effectSuppliers.get();
    }

}