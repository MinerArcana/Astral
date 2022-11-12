package com.minerarcana.astral.items;

import com.minerarcana.astral.Astral;
import com.minerarcana.astral.blocks.AstralBlocks;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class AstralItems {
    public static final CreativeModeTab ASTRAL_ITEMS = new CreativeModeTab("astral") {
        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(SNOWBERRIES.get());
        }
    };

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Astral.MOD_ID);

    public static final RegistryObject<ItemNameBlockItem> SNOWBERRIES = ITEMS.register("snowberries", () -> new ItemNameBlockItem(AstralBlocks.SNOWBERRY_BUSH.get(),
            new Item.Properties()
                    .tab(AstralItems.ASTRAL_ITEMS)
                    .food(new FoodProperties.Builder()
                            .alwaysEat()
                            .saturationMod(-1F)
                            .nutrition(1)
                            .fast()
                            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 15, 1), 1)
                            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 15, 1), 1)
                            .build())));
    public static final RegistryObject<BlockItem> FEVERWEED_ITEM = ITEMS.register("feverweed", () -> new BlockItem(AstralBlocks.FEVERWEED.get(),
            new Item.Properties()
                    .tab(AstralItems.ASTRAL_ITEMS)
                    .food(new FoodProperties.Builder()
                            .alwaysEat()
                            .saturationMod(-1F)
                            .nutrition(1)
                            .fast()
                            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 15, 1), 1)
                            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 15, 1), 1)
                            .build())));
}
