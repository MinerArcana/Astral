package com.alan199921.astral.datagen.providers;

import com.alan199921.astral.Astral;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
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
        forBlockItem(LARGE_ETHEREAL_FERN_ITEM, mcLoc("item/large_fern"));
        forBlockItem(TALL_ETHEREAL_GRASS_ITEM, mcLoc("item/tall_grass"));
        forBlockItem(ETHEREAL_GRASS_ITEM, mcLoc("item/grass"));
        forBlockItem(ETHEREAL_FERN_ITEM, mcLoc("item/fern"));
        singleTexture(ETHEREAL_DOOR_ITEM.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/ethereal_door"));
        forBlockItem(ETHEREAL_TRAPDOOR_ITEM, modLoc("block/ethereal_trapdoor_bottom"));
        forBlockItem(ETHEREAL_PLANKS_ITEM);
        forBlockItem(STRIPPED_ETHEREAL_LOG_ITEM);
        forBlockItem(STRIPPED_ETHEREAL_WOOD_ITEM);

        forBlockItem(COMFORTABLE_CUSHION_ITEM, modLoc("block/comfortable_cushion"));
        forItem(DREAMCORD);
        forItem(DREAMWEAVE);
    }

    private void forItem(RegistryObject<? extends Item> item) {
        this.singleTexture(item.getId().getPath(), mcLoc("item/handheld"), "layer0", modLoc("item/" + item.getId().getPath()));
    }

    private void forBlockItem(RegistryObject<? extends BlockNamedItem> item) {
        getBuilder(item.getId().getPath()).parent(new ModelFile.UncheckedModelFile(new ResourceLocation(Astral.MOD_ID, "block/" + item.get().getBlock().getRegistryName().getPath())));
    }

    private void forBlockItem(RegistryObject<? extends BlockNamedItem> item, ResourceLocation modelLocation) {
        getBuilder(item.getId().getPath()).parent(new ModelFile.UncheckedModelFile(modelLocation));

    }

    @Override
    public String getName() {
        return "Astral item models";
    }
}
