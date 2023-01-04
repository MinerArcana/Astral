package com.minerarcana.astral.data;

import com.minerarcana.astral.Astral;
import com.minerarcana.astral.effect.AstralEffects;
import com.minerarcana.astral.items.AstralItems;
import com.minerarcana.astral.potions.AstralPotions;
import com.minerarcana.astral.potions.PotionRegistryGroup;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;

public class EnglishLocalizaton extends LanguageProvider {

    public EnglishLocalizaton(DataGenerator gen) {
        super(gen, Astral.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(AstralItems.SNOWBERRIES.get(), "Snowberries");
        add(AstralItems.FEVERWEED_ITEM.get(), "Feverweed");
        add(AstralItems.TRAVELING_MEDICINE.get(), "Traveling Medicine");
        processPotionFamily("Feverweed Brew", "Splashing Feverweed Brew", "Feverweed Mist", "Arrow of Feverweed", AstralPotions.FEVERWEED_BREW);
        processPotionFamily("Snowberry Brew", "Splashing Snowberry Brew", "Snowberry Mist", "Arrow of Snowberry", AstralPotions.SNOWBERRY_BREW);
        processPotionFamily("Potion of Astral Travel", "Splash Potion of Astral Travel", "Lingering Potion of Astral Travel", "Arrow of Astral Travel", AstralPotions.ASTRAL_TRAVEL_POTION);
        add(AstralEffects.ASTRAL_TRAVEL.get(), "Astral Travel");
        add("itemGroup.astral", "Astral");
    }

    private void processPotionFamily(String potionName, String splashPotionName, String lingeringPotionName, String arrowName, PotionRegistryGroup potionRegistryGroup) {
        potionRegistryGroup.getBasePotion().ifPresent(potion -> add(potion, potionName, splashPotionName, lingeringPotionName, arrowName));
        potionRegistryGroup.getLongPotion().ifPresent(potion -> add(potion, potionName, splashPotionName, lingeringPotionName, arrowName));
        potionRegistryGroup.getStrongPotion().ifPresent(potion -> add(potion, potionName, splashPotionName, lingeringPotionName, arrowName));
    }

    private void add(Potion potion, String potionName, String splashPotionName, String lingeringPotionName, String arrowName) {
        String base = "item.minecraft";
        String potionRegistryName = ForgeRegistries.POTIONS.getKey(potion).getPath();
        add(base + ".potion.effect." + potionRegistryName, potionName);
        add(base + ".splash_potion.effect." + potionRegistryName, splashPotionName);
        add(base + ".lingering_potion.effect." + potionRegistryName, lingeringPotionName);
        add(base + ".tipped_arrow.effect." + potionRegistryName, arrowName);
    }


}
