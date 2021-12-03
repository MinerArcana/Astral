package com.alan19.astral.data.providers;

import com.alan19.astral.Astral;
import com.alan19.astral.client.gui.AstralContainerProvider;
import com.alan19.astral.compat.brews.AstralBotaniaBrews;
import com.alan19.astral.effects.AstralEffects;
import com.alan19.astral.entity.AstralEntities;
import com.alan19.astral.entity.AstralModifiers;
import com.alan19.astral.potions.PotionRegistryGroup;
import com.alan19.astral.util.Constants;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.data.LanguageProvider;
import vazkii.botania.api.brew.Brew;

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
        addEntities();
        addEffects();
        addBrews();
        add(AstralModifiers.ASTRAL_ATTACK_DAMAGE.get(), "Astral Attack Damage");
        add("itemGroup.astral", "Astral");
    }

    private void addBrews() {
        add(AstralBotaniaBrews.ASTRAL_TRAVEL.get(), "Extrospection");
        add(AstralBotaniaBrews.FEVERWEED_BREW.get(), "Ravenous Hunting");
        add(AstralBotaniaBrews.SNOWBERRY_BREW.get(), "Reconstruction");
    }

    private void add(Brew brew, String name) {
        add(brew.getTranslationKey(), name);
    }

    private void add(Attribute attribute, String name) {
        add("attribute.name." + attribute.getDescriptionId(), name);
    }

    private void addEntities() {
        add(AstralEntities.PHYSICAL_BODY_ENTITY.get(), "Physical Body");
        add(AstralEntities.CRYSTAL_SPIDER.get(), "Crystal Spider");
        add(AstralEntities.CRYSTAL_WEB_PROJECTILE_ENTITY.get(), "Crystal Web");
    }

    private void addEffects() {
        add(AstralEffects.ASTRAL_TRAVEL.get(), "Astral Travel");
        add(AstralEffects.MIND_VENOM.get(), "Mind Venom");
    }

    private void addConstants() {
        add(Constants.COMMANDS_BURN_SUCCESS_MULTIPLE, "Offered %s %s to %s players' Inner Realm");
        add(Constants.COMMANDS_BURN_SUCCESS_SINGLE, "Offered %s %s to %s's Inner Realm");
        add(Constants.SLEEPWALKING_BED, "You find yourself in your bed as you wake up...");
        add(Constants.SLEEPWALKING_SPAWN, "You find yourself in a familiar place as you wake up...");
        add(Constants.INVALID_WITHDRAWAL, "Crude physical substances cannot be brought to this state of existence so easily!");
        add(AstralContainerProvider.ASTRAL_CONTAINER_NAME, "Psychic Inventory");
    }

    private void addBlocks() {
        add(EGO_MEMBRANE.get(), "Ego Membrane");
        add(ASTRAL_MERIDIAN.get(), "Astral Meridian");
        add(SNOWBERRY_BUSH.get(), "Snowberry Bush");
        add(FEVERWEED_BLOCK.get(), "Feverweed");
        add(OFFERING_BRAZIER.get(), "Offering Brazier");
        add(ETHER_DIRT.get(), "Ether Dirt");
        add(ETHERIC_POWDER.get(), "Etheric Powder");
        add(ETHEREAL_LOG.get(), "Ether Log");
        add(ETHEREAL_LEAVES.get(), "Ether Leaves");
        add(REDBULB.get(), "Redbulb");
        add(CYANGRASS.get(), "Cyangrass");
        add(GENTLEGRASS.get(), "Gentlegrass");
        add(WILDWEED.get(), "Wildweed");
        add(TALL_REDBULB.get(), "Tall Redbulb");
        add(TALL_CYANGRASS.get(), "Tall Cyangrass");
        add(TALL_GENTLEGRASS.get(), "Tall Gentlegrass");
        add(TALL_WILDWEED.get(), "Tall Wildweed");
        add(BLUECAP_MUSHROOM.get(), "Bluecap Mushroom");
        add(RUSTCAP_MUSHROOM.get(), "Rustcap Mushroom");
        add(ETHEREAL_PLANKS.get(), "Ether Planks");
        add(ETHEREAL_TRAPDOOR.get(), "Ether Trapdoor");
        add(STRIPPED_ETHEREAL_LOG.get(), "Stripped Ether Log");
        add(ETHEREAL_WOOD.get(), "Ether Wood");
        add(ETHEREAL_DOOR.get(), "Ether Door");
        add(STRIPPED_ETHEREAL_WOOD.get(), "Stripped Ether Wood");
        add(COMFORTABLE_CUSHION.get(), "Comfortable Cushion");
        add(ETHEREAL_SAPLING.get(), "Ether Sapling");
        add(INDEX_OF_KNOWLEDGE.get(), "Index of Knowledge");
        add(METAPHORIC_BONE_BLOCK.get(), "Metaphoric Bone Block");
        add(METAPHORIC_STONE.get(), "Metaphoric Stone");
        add(METAPHORIC_FLESH_BLOCK.get(), "Metaphoric Flesh Block");
        add(CRYSTAL_WEB.get(), "Crystal Web");
        add(ETHEREAL_SPAWNER.get(), "Ethereal Spawner");
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
        add(ETHEREAL_LOG_ITEM.get(), "Ethere Log");
        add(ETHEREAL_WOOD_ITEM.get(), "Ether Wood");
        add(ETHEREAL_LEAVES_ITEM.get(), "Ether Leaves");
        add(REDBULB_ITEM.get(), "Redbulb");
        add(CYANGRASS_ITEM.get(), "Cyangrass");
        add(GENTLEGRASS_ITEM.get(), "Gentlegrass");
        add(WILDWEED_ITEM.get(), "Wildweed");
        add(TALL_REDBULB_ITEM.get(), "Tall Redbulb");
        add(TALL_CYANGRASS_ITEM.get(), "Tall Cyangrass");
        add(TALL_GENTLEGRASS_ITEM.get(), "Tall Gentlegrass");
        add(TALL_WILDWEED_ITEM.get(), "Tall Wildweed");
        add(RUSTCAP_MUSHROOM_ITEM.get(), "Rustcap Mushroom");
        add(BLUECAP_MUSHROOM_ITEM.get(), "Bluecap Mushroom");
        add(METAPHORIC_BONE.get(), "Metaphoric Bone");
        add(ETHEREAL_PLANKS_ITEM.get(), "Ether Planks");
        add(ETHEREAL_DOOR_ITEM.get(), "Ether Door");
        add(ETHEREAL_TRAPDOOR_ITEM.get(), "Ether Trapdoor");
        add(STRIPPED_ETHEREAL_LOG_ITEM.get(), "Stripped Ether Log");
        add(STRIPPED_ETHEREAL_WOOD_ITEM.get(), "Stripped Ether Wood");
        add(COMFORTABLE_CUSHION_ITEM.get(), "Comfortable Cushion");
        add(DREAMCORD.get(), "Dreamcord");
        add(DREAMWEAVE.get(), "Dreamweave");
        add(ETHEREAL_SAPLING_ITEM.get(), "Ether Sapling");
        add(INDEX_OF_KNOWLEDGE_ITEM.get(), "Index of Knowledge");
        add(METAPHORIC_BONE_BLOCK_ITEM.get(), "Metaphoric Bone Block");
        add(METAPHORIC_STONE_ITEM.get(), "Metaphoric Stone");
        add(SLEEPLESS_EYE.get(), "Sleepless Eye");
        add(CRYSTAL_CHITIN.get(), "Crystal Chitin");
        add(METAPHORIC_FLESH.get(), "Metaphoric Flesh");
        add(METAPHORIC_FLESH_BLOCK_ITEM.get(), "Metaphoric Flesh Block");
        add(CRYSTAL_WEB_ITEM.get(), "Crystal Web");
        add(PHANTOM_EDGE.get(), "Phantom Edge");
        add(PHANTASMAL_SWORD.get(), "Phantasmal Sword");
        add(PHANTASMAL_PICKAXE.get(), "Phantasmal Pickaxe");
        add(PHANTASMAL_SHOVEL.get(), "Phantasmal Shovel");
        add(PHANTASMAL_AXE.get(), "Phantasmal Axe");
        add(PHANTASMAL_SHEARS.get(), "Phantasmal Shears");
        add(ETHEREAL_SPAWNER_ITEM.get(), "Ethereal Spawner");
    }

    private void addPotions() {
        processPotionFamily("Potion of Astral Travel", "Splash Potion of Astral Travel", "Lingering Potion of Astral Travel", "Arrow of Astral Travel", ASTRAL_TRAVEL_POTION);
        processPotionFamily("Feverweed Brew", "Splashing Feverweed Brew", "Feverweed Mist", "Arrow of Feverweed", FEVERWEED_BREW);
        processPotionFamily("Snowberry Brew", "Splashing Snowberry Brew", "Snowberry Mist", "Arrow of Snowberry", SNOWBERRY_BREW);
        processPotionFamily("Potion of Mind Venom", "Splash Potion of Mind Venom", "Lingering Potion of Mind Venom", "Arrow of Mind Venom", MIND_VENOM_POTION);
    }

    private void processPotionFamily(String potionName, String splashPotionName, String lingeringPotionName, String arrowName, PotionRegistryGroup potionRegistryGroup) {
        potionRegistryGroup.getBasePotion().ifPresent(potion -> add(potion, potionName, splashPotionName, lingeringPotionName, arrowName));
        potionRegistryGroup.getLongPotion().ifPresent(potion -> add(potion, potionName, splashPotionName, lingeringPotionName, arrowName));
        potionRegistryGroup.getStrongPotion().ifPresent(potion -> add(potion, potionName, splashPotionName, lingeringPotionName, arrowName));
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
        add(advancements.getEtherealHunter(), "Ethereal Hunter", "Kill any entity while Astral");
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
        add(display.getTitle().getString(), title);
        add(display.getDescription().getString(), description);
    }
}
