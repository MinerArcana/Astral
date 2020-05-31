package com.alan19.astral.datagen.providers;

import net.minecraft.block.Block;
import net.minecraft.block.DoublePlantBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile;

import java.io.IOException;

import static com.alan19.astral.blocks.AstralBlocks.*;

public class Blockstates extends BlockStateProvider {
    private final ExistingFileHelper exFileHelper;

    public Blockstates(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
        super(gen, modid, exFileHelper);
        this.exFileHelper = exFileHelper;
    }

    @Override
    protected void registerStatesAndModels() {
        getVariantBuilder(LARGE_CYAN_CYST.get())
                .partialState()
                .with(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER)
                .addModels(new ConfiguredModel(models().cross("cyan_cyst_top", modLoc("block/cyan_cyst_top"))))
                .partialState().with(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)
                .addModels(new ConfiguredModel(models().cross("cyan_cyst_bottom", modLoc("block/cyan_cyst_bottom"))));

        getVariantBuilder(TALL_CYAN_SWARD.get())
                .partialState()
                .with(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER)
                .addModels(new ConfiguredModel(models().cross("cyan_sward_top", modLoc("block/cyan_sward_top"))))
                .partialState()
                .with(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)
                .addModels(new ConfiguredModel(models().cross("cyan_sward_bottom", modLoc("block/cyan_sward_bottom"))));

        simpleBlock(CYAN_CYST.get(), new ConfiguredModel(models().cross("cyan_cyst", modLoc("block/cyan_cyst"))));
        simpleBlock(CYAN_SWARD.get(), new ConfiguredModel(models().cross("cyan_sward", modLoc("block/cyan_sward"))));
        simpleBlock(CYAN_BELLEVINE.get(), new ConfiguredModel(models().cross("cyan_bellevine", modLoc("block/cyan_bellevine"))));
        simpleBlock(CYAN_BLISTERWART.get(), new ConfiguredModel(models().cross("cyan_blisterwart", modLoc("block/cyan_blisterwart"))));
        simpleBlock(CYAN_KLORID.get(), new ConfiguredModel(models().cross("cyan_klorid", modLoc("block/cyan_klorid"))));
        simpleBlock(CYAN_MORKEL.get(), new ConfiguredModel(models().cross("cyan_morkel", modLoc("block/cyan_morkel"))));
        simpleBlock(CYAN_PODS.get(), new ConfiguredModel(models().cross("cyan_pods", modLoc("block/cyan_pods"))));

        simpleBlock(ETHEREAL_PLANKS.get());
        doorBlock(ETHEREAL_DOOR.get(), modLoc("block/ethereal_door_bottom"), modLoc("block/ethereal_door_top"));
        trapdoorBlock(ETHEREAL_TRAPDOOR.get(), modLoc("block/ethereal_trapdoor"), true);
        logBlock(STRIPPED_ETHEREAL_LOG.get());
        axisBlock(STRIPPED_ETHEREAL_WOOD.get(), modLoc("block/stripped_ethereal_log"), modLoc("block/stripped_ethereal_log"));
        horizontalBlock(COMFORTABLE_CUSHION.get(), new ModelFile.ExistingModelFile(modLoc("block/comfortable_cushion"), exFileHelper));
        simpleBlock(ETHEREAL_SAPLING.get(), new ConfiguredModel(models().cross("ethereal_sapling", modLoc("block/ethereal_sapling"))));
        simpleBlock(METAPHORIC_FLESH_BLOCK.get());
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
