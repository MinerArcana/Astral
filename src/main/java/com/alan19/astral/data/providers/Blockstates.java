package com.alan19.astral.data.providers;

import net.minecraft.block.Block;
import net.minecraft.block.DoublePlantBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

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
        tallPlant(TALL_REDBULB);
        tallPlant(TALL_CYANGRASS);
        tallPlant(TALL_GENTLEGRASS);
        tallPlant(TALL_WILDWEED);

        simpleCross(REDBULB);
        simpleCross(GENTLEGRASS);
        simpleCross(CYANGRASS);
        simpleCross(WILDWEED);
        simpleCross(BLUECAP_MUSHROOM);
        simpleCross(RUSTCAP_MUSHROOM);

        simpleBlock(ETHEREAL_PLANKS.get());
        doorBlock(ETHEREAL_DOOR.get(), modLoc("block/ethereal_door_bottom"), modLoc("block/ethereal_door_top"));
        trapdoorBlock(ETHEREAL_TRAPDOOR.get(), modLoc("block/ethereal_trapdoor"), true);
        logBlock(STRIPPED_ETHEREAL_LOG.get());
        logBlock(ETHEREAL_LOG.get());
        axisBlock(STRIPPED_ETHEREAL_WOOD.get(), modLoc("block/stripped_ethereal_log"), modLoc("block/stripped_ethereal_log"));
        axisBlock(ETHEREAL_WOOD.get(), modLoc("block/ethereal_log"), modLoc("block/ethereal_log"));
        horizontalBlock(COMFORTABLE_CUSHION.get(), new ModelFile.ExistingModelFile(modLoc("block/comfortable_cushion"), exFileHelper));
        simpleBlock(ETHEREAL_SAPLING.get(), new ConfiguredModel(models().cross("ethereal_sapling", modLoc("block/ethereal_sapling"))));
        simpleBlock(METAPHORIC_FLESH_BLOCK.get());
        simpleBlock(METAPHORIC_STONE.get());
        simpleBlock(METAPHORIC_BONE_BLOCK.get());
        simpleBlock(INDEX_OF_KNOWLEDGE.get(), new ModelFile.ExistingModelFile(modLoc("block/index_of_knowledge"), exFileHelper));
        simpleBlock(CRYSTAL_WEB.get(), new ConfiguredModel(models().cross("crystal_web", modLoc("block/crystal_web"))));
    }

    private void simpleCross(RegistryObject<? extends Block> block) {
        simpleBlock(block.get(), new ConfiguredModel(models().cross(block.getId().getPath(), modLoc("block/" + block.getId().getPath()))));
    }

    private void tallPlant(RegistryObject<? extends DoublePlantBlock> registryObject) {
        final String topPath = registryObject.getId().getPath() + "_top";
        final String bottomPath = registryObject.getId().getPath() + "_bottom";
        getVariantBuilder(registryObject.get())
                .partialState()
                .with(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER)
                .addModels(new ConfiguredModel(models().cross(topPath, modLoc("block/" + topPath))))
                .partialState().with(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)
                .addModels(new ConfiguredModel(models().cross(registryObject.getId().getPath(), modLoc("block/" + bottomPath))));
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
