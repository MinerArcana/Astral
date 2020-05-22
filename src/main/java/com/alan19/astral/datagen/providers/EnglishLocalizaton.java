package com.alan19.astral.datagen.providers;

import com.alan19.astral.Astral;
import com.alan19.astral.effects.AstralEffects;
import com.alan19.astral.entities.AstralEntityRegistry;
import com.alan19.astral.util.Constants;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.data.DataGenerator;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.data.LanguageProvider;

import static com.alan19.astral.blocks.AstralBlocks.*;
import static com.alan19.astral.items.AstralItems.*;
import static com.alan19.astral.potions.AstralPotions.*;

public class EnglishLocalizaton extends LanguageProvider {
    private final Advancements advancements;

    public EnglishLocalizaton(DataGenerator gen, Advancements advancements) {
        super(gen, Astral.MOD_ID, "en_us");
        this.advancements = advancements;
    }

    @Override
    protected void addTranslations() {
        addAdvancements();
        addItems();
        addBlocks();
        addPotions();
        addConstants();
        add(AstralEntityRegistry.PHYSICAL_BODY_ENTITY.get(), "Physical Body");
        add(AstralEffects.ASTRAL_TRAVEL, "Astral Travel");
        add("itemGroup.astral", "Astral");
    }

    private void addConstants() {
        add(Constants.COMMANDS_BURN_SUCCESS_MULTIPLE, "Offered %s %s to %s players' Inner Realm");
        add(Constants.COMMANDS_BURN_SUCCESS_SINGLE, "Offered %s %s to %s's Inner Realm");
        add(Constants.SLEEPWALKING_BED, "You find yourself in your bed as you wake up...");
        add(Constants.SLEEPWALKING_SPAWN, "You find yourself in a familiar place as you wake up...");
    }

    private void addBlocks() {
        add(EGO_MEMBRANE.get(), "Ego Membrane");
        add(ASTRAL_MERIDIAN.get(), "Astral Meridian");
        add(SNOWBERRY_BUSH.get(), "Snowberry Bush");
        add(FEVERWEED_BLOCK.get(), "Feverweed");
        add(OFFERING_BRAZIER.get(), "Offering Brazier");
        add(ETHER_DIRT.get(), "Ether Dirt");
        add(ETHERIC_POWDER.get(), "Etheric Powder");
        add(ETHEREAL_WOOD.get(), "Ethereal Wood");
        add(ETHEREAL_LEAVES.get(), "Ethereal Leaves");
        add(ETHEREAL_FERN.get(), "Ethereal Fern");
        add(LARGE_ETHEREAL_FERN.get(), "Large Ethereal Fern");
        add(ETHEREAL_GRASS.get(), "Ethereal Grass");
        add(TALL_ETHEREAL_GRASS.get(), "Tall Ethereal Grass");
        add(ETHEREAL_PLANKS.get(), "Ethereal Planks");
        add(ETHEREAL_TRAPDOOR.get(), "Ethereal Trapdoor");
        add(STRIPPED_ETHEREAL_LOG.get(), "Stripped Ethereal Log");
        add(ETHEREAL_DOOR.get(), "Ethereal Door");
        add(STRIPPED_ETHEREAL_WOOD.get(), "Stripped Ethereal Wood");
        add(COMFORTABLE_CUSHION.get(), "Comfortable Cushion");
        add(ETHEREAL_SAPLING.get(), "Ethereal Sapling");
    }

    private void addItems() {
        add(ENLIGHTENMENT_KEY.get(), "Key of Enlightenment");
        add(TRAVELING_MEDICINE.get(), "Traveling Medicine");
        add(FEVERWEED.get(), "Feverweed");
        add(SNOWBERRY.get(), "Snowberries");
        add(INTROSPECTION_MEDICINE.get(), "Introspection Medicine");
        add(OFFERING_BRAZIER_ITEM.get(), "Offering Brazier");
        add(ETHER_DIRT_ITEM.get(), "Ether Dirt");
        add(ETHER_GRASS_ITEM.get(), "Ether Grass");
        add(ETHERIC_POWDER_ITEM.get(), "Etheric Powder");
        add(ETHEREAL_WOOD_ITEM.get(), "Ethereal Wood");
        add(ETHEREAL_LEAVES_ITEM.get(), "Ethereal Leaves");
        add(ETHEREAL_FERN_ITEM.get(), "Ethereal Fern");
        add(LARGE_ETHEREAL_FERN_ITEM.get(), "Large Ethereal Fern");
        add(ETHEREAL_GRASS_ITEM.get(), "Ethereal Grass");
        add(TALL_ETHEREAL_GRASS_ITEM.get(), "Tall Ethereal Grass");
        add(METAPHORIC_BONE.get(), "Metaphoric Bone");
        add(ETHEREAL_PLANKS_ITEM.get(), "Ethereal Planks");
        add(ETHEREAL_DOOR_ITEM.get(), "Ethereal Door");
        add(ETHEREAL_TRAPDOOR_ITEM.get(), "Ethereal Trapdoor");
        add(STRIPPED_ETHEREAL_LOG_ITEM.get(), "Stripped Ethereal Log");
        add(STRIPPED_ETHEREAL_WOOD_ITEM.get(), "Stripped Ethereal Wood");
        add(COMFORTABLE_CUSHION_ITEM.get(), "Comfortable Cushion");
        add(DREAMCORD.get(), "Dreamcord");
        add(DREAMWEAVE.get(), "Dreamweave");
        add(ETHEREAL_SAPLING_ITEM.get(), "Ethereal Sapling");
    }

    private void addPotions() {
        processPotionFamily("Potion of Astral Travel", "Splash Potion of Astral Travel", "Lingering Potion of Astral Travel", "Arrow of Astral Travel", ASTRAL_TRAVEL_POTION, LONG_ASTRAL_TRAVEL_POTION, STRONG_ASTRAL_TRAVEL_POTION);
        processPotionFamily("Feverweed Brew", "Splashing Feverweed Brew", "Feverweed Mist", "Arrow of Feverweed", FEVERWEED_BREW, LONG_FEVERWEED_BREW, STRONG_FEVERWEED_BREW);
        processPotionFamily("Snowberry Brew", "Splashing Snowberry Brew", "Snowberry Mist", "Arrow of Snowberry", SNOWBERRY_BREW, LONG_SNOWBERRY_BREW, STRONG_SNOWBERRY_BREW);
    }

    private void processPotionFamily(String potionName, String splashPotionName, String lingeringPotionName, String arrowName, Potion... potions) {
        for (Potion potion : potions) {
            add(potion, potionName, splashPotionName, lingeringPotionName, arrowName);
        }
    }

    private void addAdvancements() {
        add(advancements.getRoot(), "Astral", "A journey of introspection and projection");
        add(advancements.getCraftTravelingMedicine(), "Are you experienced", "Craft Traveling Medicine to fly");
        add(advancements.getBecomeAstral(), "Steps on the Wind", "Become a spooky Astral ghost");
        add(advancements.getBrewStrongAstralPotion(), "Mile Stride", "Brew Potion of Astral Travel II for faster flight");
        add(advancements.getInnerRealm(), "Withdrawal", "Enter the Inner Realm");
        add(advancements.getMagicalPuissance(), "Magical Puissance", "A trove of activities to expand your mind");
        add(advancements.getBrewingInsight(), "Improved Medicines", "Expand your mind by brewing a potion");
        add(advancements.getAutonomousInsight(), "Questioning Intelligence", "Expand your mind by creating an Iron Golem");
        add(advancements.getCraftIntrospectionMedicine(), "Compartmentalization", "Craft Introspection Medicine");
        add(advancements.getEnchantingInsight(), "Imparting the Spirit", "Expand your mind with the knowledge of enchantments");
        add(advancements.getMedicalInsight(), "Minds Renewed", "Expand your mind by curing a Zombie Villager");
        add(advancements.getEnterStronghold(), "Seeing Through Other Eyes", "Expand your mind by following the Eye of Ender to its destination");
        add(advancements.getRadiantPower(), "Radiant Power", "Expand your mind by unlocking the full power of the beacon");
        add(advancements.getDimensionalTravel(), "Dimensional Travel", "A list of transdimensional mind expanding tasks");
        add(advancements.getInfiniteExpanse(), "Infinite Expanse", "Expand your mind with images of The End");
        add(advancements.getRelativeDistance(), "Relative Distance", "Expand your mind by traveling to faraway lands through The Nether");
        add(advancements.getSpectralWorld(), "Spectral World", "Expand your mind with images of The Nether");
        add(advancements.getVoidSubstance(), "Substance of the Void", "Expand your mind by warping through an End Gateway");
        add(advancements.getYourWings(), "Wings of Your Own", "Expand your mind by studying the structure of an Elytra");
        add(advancements.getReaperCreeper(), "Reaper Creeper", "Obtain Etheric Powder, a device that allows Astral beings to affect the world");
    }

    private void add(Potion potion, String potionName, String splashPotionName, String lingeringPotionName, String arrowName) {
        String base = "item.minecraft";
        String potionRegistryName = potion.getRegistryName().getPath();
        add(base + ".potion.effect." + potionRegistryName, potionName);
        add(base + ".splash_potion.effect." + potionRegistryName, splashPotionName);
        add(base + ".lingering_potion.effect." + potionRegistryName, lingeringPotionName);
        add(base + ".tipped_arrow.effect." + potionRegistryName, arrowName);
    }

    /**
     * Helper function to add advancements to the lang generator
     *
     * @param advancement The advancement for localizations to be added
     * @param title       The title of the advancement
     * @param description The description of the advancement
     */
    private void add(Advancement advancement, String title, String description) {
        final DisplayInfo display = advancement.getDisplay();
        add(display.getTitle().getUnformattedComponentText(), title);
        add(display.getDescription().getUnformattedComponentText(), description);
    }
}
