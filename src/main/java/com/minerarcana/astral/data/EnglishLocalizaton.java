package com.minerarcana.astral.data;

import com.minerarcana.astral.Astral;
import com.minerarcana.astral.blocks.AstralBlocks;
import com.minerarcana.astral.effect.AstralEffects;
import com.minerarcana.astral.items.AstralItems;
import com.minerarcana.astral.potions.AstralPotions;
import com.minerarcana.astral.potions.PotionRegistryGroup;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;

public class EnglishLocalizaton extends LanguageProvider {
    private final AstralAdvancements astralAdvancements;


    public EnglishLocalizaton(DataGenerator gen, AstralAdvancements astralAdvancements) {
        super(gen, Astral.MOD_ID, "en_us");
        this.astralAdvancements = astralAdvancements;
    }

    @Override
    protected void addTranslations() {
        addAdvancements();
        add(AstralItems.SNOWBERRIES.get(), "Snowberries");
        add(AstralItems.FEVERWEED_ITEM.get(), "Feverweed");
        add(AstralItems.TRAVELING_MEDICINE.get(), "Traveling Medicine");
        add(AstralItems.KEY_OF_ENLIGHTENMENT.get(), "Key of Enlightenment");
        add(AstralItems.INTROSPECTION_MEDICINE.get(), "Introspection Medicine");
        add(AstralItems.SLEEPLESS_EYE.get(), "Sleepless Eye");
        add(AstralItems.PHANTOM_EDGE.get(), "Phantom Edge");
        add(AstralBlocks.ASTRAL_MERIDIAN.get(), "Astral Meridian");
        add(AstralBlocks.EGO_MEMBRANE.get(), "Ego Membrane");
        processPotionFamily("Feverweed Brew", "Splashing Feverweed Brew", "Feverweed Mist", "Arrow of Feverweed", AstralPotions.FEVERWEED_BREW);
        processPotionFamily("Snowberry Brew", "Splashing Snowberry Brew", "Snowberry Mist", "Arrow of Snowberry", AstralPotions.SNOWBERRY_BREW);
        processPotionFamily("Potion of Astral Travel", "Splash Potion of Astral Travel", "Lingering Potion of Astral Travel", "Arrow of Astral Travel", AstralPotions.ASTRAL_TRAVEL_POTION);
        add(AstralEffects.ASTRAL_TRAVEL.get(), "Astral Travel");
        add("itemGroup.astral", "Astral");
    }

    private void addAdvancements() {
        add(astralAdvancements.getRoot(), "Astral", "A journey of introspection and projection");
        add(astralAdvancements.getCraftTravelingMedicine(), "Are you experienced", "Craft Traveling Medicine to fly");
        add(astralAdvancements.getBecomeAstral(), "Steps on the Wind", "Become a spooky Astral ghost");
        add(astralAdvancements.getBrewStrongAstralPotion(), "Mile Stride", "Brew Potion of Astral Travel II for faster flight");
        add(astralAdvancements.getInnerRealm(), "Withdrawal", "Enter the Inner Realm");
        add(astralAdvancements.getMagicalPuissance(), "Magical Puissance", "A trove of activities to expand your mind");
        add(astralAdvancements.getBrewingInsight(), "Improved Medicines", "Expand your mind by brewing a potion");
        add(astralAdvancements.getAutonomousInsight(), "Questioning Intelligence", "Expand your mind by creating an Iron Golem");
        add(astralAdvancements.getCraftIntrospectionMedicine(), "Compartmentalization", "Craft Introspection Medicine");
        add(astralAdvancements.getEnchantingInsight(), "Imparting the Spirit", "Expand your mind with the knowledge of enchantments");
        add(astralAdvancements.getMedicalInsight(), "Minds Renewed", "Expand your mind by curing a Zombie Villager");
        add(astralAdvancements.getEnterStronghold(), "Seeing Through Other Eyes", "Expand your mind by following the Eye of Ender to its destination");
        add(astralAdvancements.getRadiantPower(), "Radiant Power", "Expand your mind by unlocking the full power of the beacon");
        add(astralAdvancements.getDimensionalTravel(), "Dimensional Travel", "A list of transdimensional mind expanding tasks");
        add(astralAdvancements.getInfiniteExpanse(), "Infinite Expanse", "Expand your mind with images of The End");
        add(astralAdvancements.getRelativeDistance(), "Relative Distance", "Expand your mind by traveling to faraway lands through The Nether");
        add(astralAdvancements.getSpectralWorld(), "Spectral World", "Expand your mind with images of The Nether");
        add(astralAdvancements.getVoidSubstance(), "Substance of the Void", "Expand your mind by warping through an End Gateway");
        add(astralAdvancements.getYourWings(), "Wings of Your Own", "Expand your mind by studying the structure of an Elytra");
        add(astralAdvancements.getEtherealHunter(), "Ethereal Hunter", "Kill any entity while Astral");
        // TODO Readd when reimplemented
        // add(astralAdvancements.getReaperCreeper(), "Reaper Creeper", "Obtain Etheric Powder, a device that allows Astral beings to affect the world");
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

    /**
     * Helper function to add advancements to the localization generator
     *
     * @param advancement The advancement for localizations to be added
     * @param title       The title of the advancement
     * @param description The description of the advancement
     */
    private void add(Advancement advancement, String title, String description) {
        final DisplayInfo display = advancement.getDisplay();
        add(display.getTitle().getString(), title);
        add(display.getDescription().getString(), description);
    }
}
