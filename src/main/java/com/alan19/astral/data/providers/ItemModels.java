package com.alan19.astral.data.providers;

import com.alan19.astral.Astral;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nonnull;

import static com.alan19.astral.items.AstralItems.*;

public class ItemModels extends ModelProvider<ItemModelBuilder> {

    private final ResourceLocation generatedItem = mcLoc("item/generated");

    public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Astral.MOD_ID, ITEM_FOLDER, ItemModelBuilder::new, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        forItem(ENLIGHTENMENT_KEY);
        forBlockItemWithParent(FEVERWEED);
        forBlockItem(ETHER_DIRT_ITEM);
        forBlockItem(ETHEREAL_PLANKS_ITEM);
        forItem(METAPHORIC_BONE);
        forBlockItemWithParent(CYANGRASS_ITEM);
        forBlockItemWithParent(WILDWEED_ITEM);
        forBlockItemWithParent(REDBULB_ITEM);
        forBlockItemWithParent(GENTLEGRASS_ITEM);
        forBlockItemWithParent(TALL_REDBULB_ITEM, modLoc("block/tall_redbulb_top"));
        forBlockItemWithParent(TALL_CYANGRASS_ITEM, modLoc("block/tall_cyangrass_top"));
        forBlockItemWithParent(TALL_GENTLEGRASS_ITEM, modLoc("block/tall_gentlegrass_top"));
        forBlockItemWithParent(TALL_WILDWEED_ITEM, modLoc("block/tall_wildweed_top"));
        forBlockItemWithParent(RUSTCAP_MUSHROOM_ITEM);
        forBlockItemWithParent(BLUECAP_MUSHROOM_ITEM);
        forBlockItemWithParent(ETHEREAL_DOOR_ITEM, modLoc("item/ether_door"));
        forBlockItem(ETHEREAL_TRAPDOOR_ITEM, modLoc("block/ether_trapdoor_bottom"));
        forBlockItem(ETHEREAL_PLANKS_ITEM);
        forBlockItem(STRIPPED_ETHEREAL_LOG_ITEM);
        forBlockItem(STRIPPED_ETHEREAL_WOOD_ITEM);
        forBlockItem(ETHEREAL_WOOD_ITEM);

        forBlockItem(COMFORTABLE_CUSHION_ITEM, modLoc("block/comfortable_cushion"));
        forItem(DREAMCORD);
        forItem(DREAMWEAVE);
        forBlockItemWithParent(ETHEREAL_SAPLING_ITEM, modLoc("block/ether_sapling"));
        forBlockItem(METAPHORIC_FLESH_BLOCK_ITEM);
        forItem(METAPHORIC_FLESH);
        forItem(CRYSTAL_CHITIN);
        singleTexture("astronomicon", mcLoc("item/handheld"), "layer0", modLoc("item/astronomicon"));
        forItem(SLEEPLESS_EYE);
        forItem(PHANTOM_EDGE);
        forBlockItem(INDEX_OF_KNOWLEDGE_ITEM, modLoc("block/index_of_knowledge"));
        forBlockItem(METAPHORIC_STONE_ITEM);
        forBlockItem(METAPHORIC_BONE_BLOCK_ITEM);
        forBlockItemWithParent(CRYSTAL_WEB_ITEM);
        forItem(PHANTASMAL_SWORD);
        forItem(PHANTASMAL_SHOVEL);
        forItem(PHANTASMAL_PICKAXE);
        forItem(PHANTASMAL_AXE);
        forItem(PHANTASMAL_SHEARS);
        forBlockItem(ETHEREAL_LEAVES_ITEM);
        forBlockItem(ETHEREAL_LOG_ITEM);
        forBlockItem(ETHEREAL_SPAWNER_ITEM);
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

    private void forBlockItemWithParent(RegistryObject<? extends BlockNamedItem> item, ResourceLocation modelLocation) {
        singleTexture(item.getId().getPath(), generatedItem, "layer0", modelLocation);
    }

    private void forBlockItemWithParent(RegistryObject<? extends BlockNamedItem> item) {
        singleTexture(item.getId().getPath(), generatedItem, "layer0", modLoc("block/" + item.getId().getPath()));
    }

    @Nonnull
    @Override
    public String getName() {
        return "Astral item models";
    }
}
