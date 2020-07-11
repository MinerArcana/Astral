package com.alan19.astral.recipe;

import com.alan19.astral.Astral;
import com.alan19.astral.recipe.serializer.BrazierRecipeSerializer;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AstralRecipeSerializer {
    private static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Astral.MOD_ID);

    public static final RegistryObject<IRecipeSerializer<BrazierRecipe>> BRAZIER_DESTROY_SERIALIZER = RECIPE_SERIALIZERS.register("brazier_destroy", BrazierRecipeSerializer::new);
}
