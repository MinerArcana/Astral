package com.alan199921.astral.datagen.providers;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoublePlantBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile;

import java.io.IOException;

import static com.alan199921.astral.blocks.AstralBlocks.*;

public class Blockstates extends BlockStateProvider {
    public Blockstates(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
        super(gen, modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        getVariantBuilder(LARGE_ETHEREAL_FERN.get())
                .partialState()
                .with(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER)
                .addModels(new ConfiguredModel(models().withExistingParent("large_ethereal_fern_top", "block/large_fern_top")))
                .partialState().with(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)
                .addModels(new ConfiguredModel(models().withExistingParent("large_ethereal_fern_bottom", "block/large_fern_bottom")));

        getVariantBuilder(TALL_ETHEREAL_GRASS.get())
                .partialState()
                .with(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER)
                .addModels(new ConfiguredModel(models().withExistingParent("tall_ethereal_grass_top", "block/tall_grass_top")))
                .partialState()
                .with(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)
                .addModels(new ConfiguredModel(models().withExistingParent("tall_ethereal_grass_bottom", "block/tall_grass_bottom")));

        simpleBlock(ETHEREAL_FERN.get(), new ConfiguredModel(models().withExistingParent("ethereal_fern", mcLoc(Blocks.FERN.getRegistryName().getPath()))));
        simpleBlock(ETHEREAL_GRASS.get(), new ConfiguredModel(models().withExistingParent("ethereal_grass", mcLoc(Blocks.GRASS.getRegistryName().getPath()))));
        simpleBlock(ETHEREAL_PLANKS.get());
        doorBlock(ETHEREAL_DOOR.get(), modLoc("block/ethereal_door_bottom"), modLoc("block/ethereal_door_top"));
        trapdoorBlock(ETHEREAL_TRAPDOOR.get(), modLoc("block/ethereal_trapdoor"), true);
        logBlock(STRIPPED_ETHEREAL_LOG.get());
        axisBlock(STRIPPED_ETHEREAL_WOOD.get(), modLoc("block/stripped_ethereal_log"), modLoc("block/stripped_ethereal_log"));
    }

    private ModelFile modelDefault(Block block) {
        String name = block.getRegistryName().getPath();
        return models().cross(name, modLoc("block/" + name));
    }

    @Override
    public void act(DirectoryCache cache) throws IOException {
        super.act(cache);
    }
}
