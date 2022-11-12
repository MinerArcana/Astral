package com.minerarcana.astral.data;

import com.minerarcana.astral.Astral;
import com.minerarcana.astral.items.AstralItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;

public class ItemModels extends ModelProvider<ItemModelBuilder> {

    private final ResourceLocation generatedItem = mcLoc("item/generated");

    public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Astral.MOD_ID, ITEM_FOLDER, ItemModelBuilder::new, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        forBlockItem(AstralItems.FEVERWEED_ITEM);
        forItem(AstralItems.SNOWBERRIES);
    }

    private void forItem(RegistryObject<? extends Item> item) {
        this.singleTexture(item.getId().getPath(), mcLoc("item/handheld"), "layer0", modLoc("item/" + item.getId().getPath()));
    }

    private void forBlockItem(RegistryObject<? extends BlockItem> item) {
        getBuilder(item.getId().getPath()).parent(new ModelFile.UncheckedModelFile(new ResourceLocation(Astral.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(item.get().getBlock()).getPath())));
    }

    private void forBlockItem(RegistryObject<? extends ItemNameBlockItem> item, ResourceLocation modelLocation) {
        getBuilder(item.getId().getPath()).parent(new ModelFile.UncheckedModelFile(modelLocation));
    }

    private void forBlockItemWithParent(RegistryObject<? extends ItemNameBlockItem> item, ResourceLocation modelLocation) {
        singleTexture(item.getId().getPath(), generatedItem, "layer0", modelLocation);
    }

    private void forBlockItemWithParent(RegistryObject<? extends ItemNameBlockItem> item) {
        singleTexture(item.getId().getPath(), generatedItem, "layer0", modLoc("block/" + item.getId().getPath()));
    }

    @Nonnull
    @Override
    public String getName() {
        return "Astral item models";
    }
}
