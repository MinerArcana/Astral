package com.alan19.astral.datagen.providers;

import com.alan19.astral.items.AstralItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.ItemStack;
import xyz.brassgoggledcoders.patchouliprovider.BookBuilder;
import xyz.brassgoggledcoders.patchouliprovider.PatchouliBookProvider;

import java.util.function.Consumer;

public class PatchouliBooks extends PatchouliBookProvider {
    public PatchouliBooks(DataGenerator gen, String modid, String locale) {
        super(gen, modid, locale);
    }

    @Override
    protected void addBooks(Consumer<BookBuilder> consumer) {
        final BookBuilder bookBuilder = createBookBuilder("astral_guidebook", "Astral Guidebook", "A Journeyman's Astral Journal");
        bookBuilder.addCategory("plants", "Plants", "Useful plants", new ItemStack(AstralItems.SNOWBERRY.get()));
        bookBuilder.build(consumer);
    }
}
