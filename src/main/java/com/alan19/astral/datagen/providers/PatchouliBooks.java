package com.alan19.astral.datagen.providers;

import com.alan19.astral.Astral;
import com.alan19.astral.items.AstralItems;
import com.alan19.astral.items.FeverweedItem;
import com.alan19.astral.items.Snowberry;
import com.alan19.astral.potions.AstralPotions;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.patchouliprovider.BookBuilder;
import xyz.brassgoggledcoders.patchouliprovider.CategoryBuilder;
import xyz.brassgoggledcoders.patchouliprovider.EntryBuilder;
import xyz.brassgoggledcoders.patchouliprovider.PatchouliBookProvider;

import java.util.function.Consumer;

public class PatchouliBooks extends PatchouliBookProvider {
    public PatchouliBooks(DataGenerator gen, String modid, String locale) {
        super(gen, modid, locale);
    }

    @Override
    protected void addBooks(Consumer<BookBuilder> consumer) {
        final BookBuilder bookBuilder = createBookBuilder("astral_guidebook", "Astronomicon", "Mechanics of Astral Travel");
        bookBuilder.setSubtitle("Mechanics of Astral Travel");
        bookBuilder.setCreativeTab(AstralItems.ASTRAL_ITEMS.getPath());
        final Snowberry snowberryItemStack = AstralItems.SNOWBERRY.get();
        final CategoryBuilder plantsCategory = bookBuilder.addCategory("plants", "Natural Resources", "There’s a few Resources added to the Material Overworld you’ll need - Feverweed and Snowberries.", new ItemStack(snowberryItemStack));

        final EntryBuilder snowberryPage = plantsCategory.addEntry("snowberries", "Snowberry Bush / Snowberries", new ItemStack(snowberryItemStack));
        snowberryPage.addSimpleTextPage("These berry bushes generate in Snowy Biomes in small clumps, shedding snow under them.  Like many other bushes they can slow or hurt you while walking through them. They can be placed on or adjacent to sufficiently cold blocks.", "Snowberry Bushes");
        snowberryPage.addSpotlightPage(new ItemStack(snowberryItemStack)).setText("Consuming the berry inflicts Nausea 2 and Regeneration 2 for 15 seconds.").setTitle("Snowberries");
        snowberryPage.addSimpleTextPage("You can grow Snowberries by planting them on dirt and snow. When there's berries on the bush, you can right click the bush to harvest them, or wait a little longer to harvest a few more berries. Harvesting a bush will cause the berries to grow back faster, as opposed to breaking it and planting a new bush.", "Growing Snowberries");

        final FeverweedItem feverweedItemStack = AstralItems.FEVERWEED.get();
        final EntryBuilder feverweedPage = plantsCategory.addEntry("feverweed", "Feverweed", new ItemStack(feverweedItemStack));
        feverweedPage.addSimpleTextPage("This spiral growing grass can be found on the surface of Jungles or other Hot and Wet Biomes.", "Feverweed Patches");
        feverweedPage.addSpotlightPage(new ItemStack(feverweedItemStack)).setTitle("Feverweed").setText("Consuming this inflicts Hunger II and Luck II for 15 seconds.");
        feverweedPage.addSimpleTextPage("You can grow Feverweed by planting it on Grass or Dirt blocks and wait for it to spread slowly. It spreads similarly to a mushroom, but will spread regardless of light level", "Cultivating Feverweed");

        final ItemStack potionItemStack = new ItemStack(Items.POTION);
        final EntryBuilder brewPage = plantsCategory.addEntry("brews", "Astral Brews", potionItemStack);
        brewPage.addSimpleTextPage("You can create potions that extends the effects of Feverweed and Snowberries by adding them to Thick Potions in a Brewing Stand. Adding Glowstone and Redstone to those potions strengthen the potency and duration of the effects, respectively.");
        brewPage.setAdvancement("minecraft:nether/brew_potion");
        brewPage.addSpotlightPage(PotionUtils.addPotionToItemStack(potionItemStack, AstralPotions.SNOWBERRY_BREW.getBasePotion().get())).setTitle("Snowberry Brew").setText("Provides Regeneration II and Hunger II for 30 seconds");
        brewPage.addSpotlightPage(PotionUtils.addPotionToItemStack(potionItemStack, AstralPotions.FEVERWEED_BREW.getBasePotion().get())).setTitle("Feverweed Brew").setText("Provides Luck II and Hunger II for 30 seconds");

        final EntryBuilder travelingMedicinePage = plantsCategory.addEntry("traveling_medicine", "Traveling Medicine", new ItemStack(AstralItems.TRAVELING_MEDICINE.get()));
        travelingMedicinePage.addSimpleTextPage("Made from Snowberries, Sugar, and Feverweed this is the starting source of Astral Travel. The Brewing Stand will allow more advanced forms.");
        travelingMedicinePage.addCraftingPage(new ResourceLocation(Astral.MOD_ID, "traveling_medicine"));

        final EntryBuilder astralTravelPage = plantsCategory.addEntry("astral_travel", "Astral Travel", PotionUtils.addPotionToItemStack(potionItemStack.copy(), AstralPotions.ASTRAL_TRAVEL_POTION.getBasePotion().get()));
        astralTravelPage.addSimpleTextPage("While Astral Traveling your spirit soars across the land and through the skies, free from gravity. Your physical goods stay with your physical body. There are resources to be collected while Astral Traveling, search high in the sky over oceans to find Etheric Isles.");

        bookBuilder.build(consumer);
    }
}
