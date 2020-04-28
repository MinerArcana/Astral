package com.alan199921.astral.datagen;

import com.alan199921.astral.Astral;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.fml.RegistryObject;

import static com.alan199921.astral.items.AstralItems.*;

public class ItemModels extends ModelProvider<ItemModelBuilder> {

    private final ResourceLocation generatedItem = mcLoc("item/generated");

    public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Astral.MOD_ID, ITEM_FOLDER, ItemModelBuilder::new, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        forBlockItem(ETHER_DIRT_ITEM);
        forBlockItem(ETHEREAL_PLANKS_ITEM);
        forItem(METAPHORIC_BONE);
        withExistingParent("large_ethereal_fern", mcLoc("item/large_fern"));
        withExistingParent("tall_ethereal_grass", mcLoc("item/tall_grass"));
        withExistingParent("ethereal_grass", mcLoc("item/grass"));
        withExistingParent("ethereal_fern", mcLoc("item/fern"));
    }

    private void forItem(RegistryObject<? extends Item> item) {
        this.singleTexture(item.getId().getPath(), mcLoc("item/handheld"), "layer0", modLoc("item/" + item.getId().getPath()));
    }

    private void forBlockItem(RegistryObject<? extends BlockNamedItem> item) {
        withExistingParent(item.getId().getPath(), new ResourceLocation(Astral.MOD_ID, "block/" + item.get().getBlock().getRegistryName().getPath()));
    }

    @Override
    public String getName() {
        return "Astral item models";
    }
}
