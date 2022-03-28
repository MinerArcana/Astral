package com.alan19.astral.data.providers;

import com.alan19.astral.Astral;
import com.alan19.astral.items.AstralItems;
import com.alan19.astral.items.FeverweedItem;
import com.alan19.astral.items.Snowberry;
import com.alan19.astral.potions.AstralPotions;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.patchouliprovider.*;

import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PatchouliBooks extends PatchouliBookProvider {
    public PatchouliBooks(DataGenerator gen, String modid, String locale) {
        super(gen, modid, locale);
    }

    @Override
    protected void addBooks(Consumer<BookBuilder> consumer) {
        final BookBuilder bookBuilder = createBookBuilder("astronomicon", "Astronomicon", "Mechanics of Astral Travel");
        bookBuilder.setModel(new ResourceLocation(Astral.MOD_ID, "astronomicon"));
        bookBuilder.setSubtitle("Mechanics of Astral Travel");
        bookBuilder.setCreativeTab(AstralItems.ASTRAL_ITEMS.getRecipeFolderName());

        final CategoryBuilder astralWorlds = bookBuilder.addCategory("astral_worlds", "Astral Worlds", "Astral Arts provide access to many Astral Worlds", String.valueOf(new ResourceLocation(Astral.MOD_ID, "textures/mob_effect/astral_travel.png")));

        final ItemStack potionItemStack = new ItemStack(Items.POTION);
        final EntryBuilder astralTravelEntry = astralWorlds.addEntry("astral_travel", "Astral Travel", PotionUtils.setPotion(potionItemStack.copy(), AstralPotions.ASTRAL_TRAVEL_POTION.getBasePotion().get()));
        astralTravelEntry.addSimpleTextPage(String.format("While Astral Traveling your spirit soars across the land and through the skies, free from gravity. Your physical goods stay with your physical body. There are resources to be collected while Astral Traveling, search high in the sky over oceans to find %s.", FormatHelper.internalLink("etheric_isles", "Etheric Isles")));

        final EntryBuilder innerRealmEntry = astralWorlds.addEntry("inner_realm", "Inner Realm", new ItemStack(AstralItems.ENLIGHTENMENT_KEY.get()));
        innerRealmEntry.addSimpleTextPage("While introspective your focus is in your Mindscape. Each Inner realm begins as a certain volume of space. The Ego Membranes define the limits of your inner realm.");
        innerRealmEntry.addSpotlightPage(new ItemStack(AstralItems.ENLIGHTENMENT_KEY.get())).setText("In each Ego Membrane wall there will be an Astral Meridian block which can be used with Keys of Enlightenment to expand your sense of self and give you more room and you can shift right click them with an empty hand to leave and return your focus to your body.");
        innerRealmEntry.addSimpleTextPage(String.format("While in your Inner Realm you have access to your Astral Inventory with the resources gathered from %s and the creatures which live among them. Some resources can be transferred from the waking world via the Offering Brazier.", FormatHelper.internalLink("etheric_isles", "Etheric Isles")));
        innerRealmEntry.addSimpleTextPage("These resources are needed to create Mental Constructs.");

        final Snowberry snowberryItemStack = AstralItems.SNOWBERRY.get();
        final CategoryBuilder naturalResources = bookBuilder.addCategory("natural_resources", "Natural Resources", "There's a few Resources added to the Material Overworld you'll need - Feverweed and Snowberries.", new ItemStack(snowberryItemStack));

        final EntryBuilder snowberryPage = naturalResources.addEntry("snowberries", "Snowberries", new ItemStack(snowberryItemStack));
        snowberryPage.addSimpleTextPage("These berry bushes generate in snowy biomes in small clumps, shedding snow under them.  Like many other bushes, they can slow or hurt you while walking through them. They can be placed on or adjacent to sufficiently cold blocks.", "Snowberry Bushes");
        snowberryPage.addSpotlightPage(new ItemStack(snowberryItemStack)).setText("Consuming the berry inflicts Nausea 2 and Regeneration 2 for 15 seconds.").setTitle("Snowberries");
        snowberryPage.addSimpleTextPage("You can grow Snowberries by planting them on dirt and snow. When there's berries on the bush, you can right click the bush to harvest them, or wait a little longer to harvest a few more berries. Harvesting a bush will cause the berries to grow back faster, as opposed to breaking it and planting a new bush.", "Growing Snowberries");
        snowberryPage.addImagePage(new ResourceLocation(Astral.MOD_ID, "textures/book_images/snowberry_generation.png"));

        final FeverweedItem feverweedItemStack = AstralItems.FEVERWEED.get();
        final EntryBuilder feverweedPage = naturalResources.addEntry("feverweed", "Feverweed", new ItemStack(feverweedItemStack));
        feverweedPage.addSimpleTextPage("This spiral growing grass can be found on the surface of Jungles or other Hot and Wet Biomes.", "Feverweed Patches");
        feverweedPage.addSpotlightPage(new ItemStack(feverweedItemStack)).setTitle("Feverweed").setText("Consuming this inflicts Hunger II and Luck II for 15 seconds.");
        feverweedPage.addSimpleTextPage("You can grow Feverweed by planting it on Grass or Dirt blocks and wait for it to spread slowly. It spreads similarly to a mushroom, but will spread regardless of light level", "Cultivating Feverweed");
        feverweedPage.addImagePage(new ResourceLocation(Astral.MOD_ID, "textures/book_images/feverweed_generation.png"));

        final EntryBuilder ethericIsles = naturalResources.addEntry("etheric_isles", "Etheric Isles", new ItemStack(AstralItems.ETHER_GRASS_ITEM.get()));
        ethericIsles.addSimpleTextPage("Floating high in the sky over the Oceans you can find Etheric Isles. Etheric Isles are made of Ether, and are covered with Etheric Growths, and occasional Etheric Trees.");
        ethericIsles.addImagePage(new ResourceLocation(Astral.MOD_ID, "textures/book_images/etheric_isles_generation.png"));

        final ItemStack travelingMedicineItemStack = new ItemStack(AstralItems.TRAVELING_MEDICINE.get());
        final CategoryBuilder medicinesAndPotions = bookBuilder.addCategory("medicines_and_potions", "Medicines and Potions", "There's lot of medicines and potions to be made", travelingMedicineItemStack);

        final EntryBuilder travelingMedicinePage = medicinesAndPotions.addEntry("traveling_medicine", "Traveling Medicine", travelingMedicineItemStack);
        travelingMedicinePage.addSimpleTextPage(String.format("Made from %s, Sugar, and %s this is the starting source of %s. The Brewing Stand will allow more advanced forms of this medicine.", FormatHelper.internalLink("snowberries", "Snowberries"), FormatHelper.internalLink("feverweed", "Feverweed"), FormatHelper.internalLink("astral_travel", "Astral Travel")));
        travelingMedicinePage.addCraftingPage(new ResourceLocation(Astral.MOD_ID, "traveling_medicine"));

        final EntryBuilder introspectionMedicineEntry = medicinesAndPotions.addEntry("introspection_medicine", "Introspection Medicine", new ItemStack(AstralItems.INTROSPECTION_MEDICINE.get()));
        introspectionMedicineEntry.addSimpleTextPage(String.format("Made from Mushrooms, Poisonous Potatoes, and %s this allows you to shut off your senses and perceive your %s, the Mindscape.", FormatHelper.internalLink("feverweed", "Feverweed"), FormatHelper.internalLink("inner_realm", "Inner Realm")));
        introspectionMedicineEntry.addCraftingPage(new ResourceLocation(Astral.MOD_ID, "introspection_medicine"));

        final ItemStack feverweedAndSnowberryBrew = new ItemStack(Items.POTION);
        PotionUtils.setCustomEffects(feverweedAndSnowberryBrew, Stream.concat(AstralPotions.FEVERWEED_BREW.getBasePotion().get().getEffects().stream(), AstralPotions.SNOWBERRY_BREW.getBasePotion().get().getEffects().stream()).collect(Collectors.toList()));
        final EntryBuilder brewPage = medicinesAndPotions.addEntry("feverweed_and_snowberry_brews", "Snowberry & Feverweed Brews", feverweedAndSnowberryBrew);
        brewPage.setAdvancement("minecraft:nether/brew_potion");
        brewPage.addSpotlightPage(PotionUtils.setPotion(potionItemStack, Potions.THICK)).setText("Have you ever wanted the effects of Feverweed or Snowberries but for a longer or stronger effect? Now you can! These brews use Thick Potions as their base, and have the two effects at level II for 30 seconds as the starting potion.");
        brewPage.addSimpleTextPage("Adding Redstone lowers the effects to level I but increases the duration to 60 seconds." + FormatHelper.newParagraph() + "Adding Glowstone increases the effects to level III but decreases the duration to 20 seconds.");

        final CategoryBuilder erratas = bookBuilder.addCategory("errata_and_sundries", "Errata and Sundries", "There are many miscellaneous blocks and items that can help with Astral Travel and building in the Inner Realm. These are the rare physical tools which help with physical workings.", new ItemStack(AstralItems.OFFERING_BRAZIER_ITEM.get()));
        final EntryBuilder offeringBrazier = erratas.addEntry("offering_brazier", "Offering Brazier", new ItemStack(AstralItems.OFFERING_BRAZIER_ITEM.get()));
        offeringBrazier.addTextPage("The Offering Brazier allows you to make many items astral when there is no other way to acquire equivalents while astral.");
        offeringBrazier.addTextPage("The Offering Brazier cannot be used to transfer anything which burns, inventories or tile entities. Similarly anything which boils away doesn't transfer, resulting in buckets of liquids arriving empty, and potions being reduced to glass bottles etc.");
        offeringBrazier.addTextPage("To acquire the items from the Offering Brazier, first, put the item into the brazier in-world while awake by right clicking the brazier with the item in your hand. Next, add fuel to it by right clicking it with the fuel item in your hand. Once the non-fuel item is consumed, you should see blue flames rising from the brazier around your item.");
        offeringBrazier.addTextPage("When you arrive in your Inner Realm, the items will be added to your inventory. Note: All NBT data is lost upon travel; do not invest in anything you wish to bring with you. You just get the basic item upon arrival.");
        offeringBrazier.addCraftingPage(new ResourceLocation(Astral.MOD_ID, AstralItems.OFFERING_BRAZIER_ITEM.getId().getPath()));

        final CategoryBuilder mentalConstructs = bookBuilder.addCategory("mental_constructs", "Mental Constructs", "Mental constructs are arrays of astral blocks which take on special meaning and thus special powers when assembled within your mind. Each Mental Construct's benefits are unique to the person who hosts them in their Inner Realm, and more than one of the same type have no additional positive effects.", new ItemStack(AstralItems.COMFORTABLE_CUSHION_ITEM.get()));
        final EntryBuilder garden = mentalConstructs.addEntry("garden", "The Garden", new ItemStack(AstralItems.COMFORTABLE_CUSHION_ITEM.get()));
        garden.addTextPage(FormatHelper.formatCode('o', "\"There is Contentment to be Found in Contemplating Nature, and Endurance born of the memory of Contentment\""));
        garden.addTextPage(String.format("Centered on the %s, the Garden counts the amount of natural passive life and water around you and generates a level of Contentment based on it. Collect Astral plants of various sorts to maximize it's efficiency.", FormatHelper.internalLink("astral:garden", "cushion", "Comfortable Cushion")));
        garden.addTextPage("Benefits: When naturally healing you last longer the greater your contentment level. It may also prove to lend power to your inner energies, and assist with botanical magics.");
        garden.addCraftingPage(AstralItems.COMFORTABLE_CUSHION_ITEM.getId()).setAnchor("cushion");

        final EntryBuilder library = mentalConstructs.addEntry("library", "The Library", new ItemStack(AstralItems.INDEX_OF_KNOWLEDGE_ITEM.get()));
        library.addTextPage(FormatHelper.formatCode('o', "\"Writing is the quite fantastic power to ensure your thoughts are known to others, even beyond death.\""));
        library.addTextPage(String.format("Centered on the %s, the Library measures your internal knowledge as detected by bookshelves and lecterns with appropriate books on them.", FormatHelper.internalLink("astral:library", "index", "Index of Knowledge")));
        library.addTextPage("Benefits: You can channel your experience and intentions into it, or regain health, and regain a tenth of your invested levels and learning thereafter. Each tier of knowledge requires ten times the new sought level in levels from you.");
        library.addCraftingPage(AstralItems.INDEX_OF_KNOWLEDGE_ITEM.getId()).setAnchor("index");

        bookBuilder.build(consumer);
    }
}
