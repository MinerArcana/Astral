package com.alan19.astral.datagen.providers;

import com.alan19.astral.items.AstralItems;
import com.alan19.astral.items.FeverweedItem;
import com.alan19.astral.items.Snowberry;
import com.alan19.astral.potions.AstralPotions;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtils;
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
        final BookBuilder bookBuilder = createBookBuilder("astral_guidebook", "Chains of Flesh and Silver", "A Journeyman's Astral Journal");
        bookBuilder.setSubtitle("Guide to Astral");
        bookBuilder.setCreativeTab(AstralItems.ASTRAL_ITEMS.getPath());
        final Snowberry snowberryItemStack = AstralItems.SNOWBERRY.get();
        final CategoryBuilder plantsCategory = bookBuilder.addCategory("plants", "Plants", "Useful plants you may find on your adventure", new ItemStack(snowberryItemStack));

        final EntryBuilder snowberryPage = plantsCategory.addEntry("snowberries", "Snowberries", new ItemStack(snowberryItemStack));
        snowberryPage.addSimpleTextPage("Snowberries grow in bushes found in biomes that are both snowy and forested. Snowberry Bushes grow in small groups and accumulate snow around them when found in the wild. Grown Snowberry Bushes have sharp branches, inflicting damage and slowing entities walking through it.", "Snowberry Bushes");
        snowberryPage.addSpotlightPage(new ItemStack(snowberryItemStack)).setText("Snowberries are edible and provide Regeneration II and Nausea II when eaten, making it a great treat for injured adventurers out of combat.").setTitle("Snowberries");
        snowberryPage.addSimpleTextPage("You can grow Snowberries by planting them on dirt and snow. When there's berries on the bush, you can right click the bush to harvest them, or wait a little longer to harvest a few more berries. Harvesting a bush will cause the berries to grow back faster, as opposed to breaking it and planting a new bush.", "Growing Snowberries");

        final FeverweedItem feverweedItemStack = AstralItems.FEVERWEED.get();
        final EntryBuilder feverweedPage = plantsCategory.addEntry("feverweed", "Feverweed", new ItemStack(feverweedItemStack));
        feverweedPage.addSimpleTextPage("Feverweed grow in biomes that are warm and rainy. They grow in patches on the surface of the biome, and can be passed through safely.", "Feverweed Patches");
        feverweedPage.addSpotlightPage(new ItemStack(feverweedItemStack)).setTitle("Feverweed").setText("Feverweed is edible and provide Luck II and Hunger II when eaten, making it easier to get rare drops.");
        feverweedPage.addSimpleTextPage("You can grow Feverweed by planting it on Grass or Dirt blocks and wait for it to spread slowly. It spreads similarly to a mushroom, but will spread regardless of light level", "Growing Feverweed");

        final ItemStack potionItemStack = new ItemStack(Items.POTION);
        final EntryBuilder brewPage = plantsCategory.addEntry("brews", "Astral Brews", potionItemStack).addSimpleTextPage("You can create potions that extends the effects of Feverweed and Snowberries by adding them to Thick Potions in a Brewing Stand. Adding Glowstone and Redstone to those potions strengthen the potency and duration of the effects, respectively.");
        brewPage.setAdvancement("minecraft:nether/brew_potion");
        brewPage.addSpotlightPage(PotionUtils.addPotionToItemStack(potionItemStack, AstralPotions.SNOWBERRY_BREW)).setTitle("Snowberry Brew").setText("Provides Regeneration II and Hunger II for 30 seconds");
        brewPage.addSpotlightPage(PotionUtils.addPotionToItemStack(potionItemStack, AstralPotions.FEVERWEED_BREW)).setTitle("Feverweed Brew").setText("Provides Luck II and Hunger II for 30 seconds");

        bookBuilder.build(consumer);
    }
}
