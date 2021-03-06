package com.alan19.astral.data.providers;

import com.alan19.astral.Astral;
import net.minecraft.block.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.ResourceLocation;
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
        simpleBlock(ETHEREAL_LEAVES.get());

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
        doorBlock(ETHEREAL_DOOR.get(), modLoc("block/ether_door_bottom"), modLoc("block/ether_door_top"));
        trapdoorBlock(ETHEREAL_TRAPDOOR.get(), modLoc("block/ether_trapdoor"), true);
        logBlock(STRIPPED_ETHEREAL_LOG.get());
        logBlock(ETHEREAL_LOG.get());
        axisBlock(STRIPPED_ETHEREAL_WOOD.get(), modLoc("block/stripped_ether_log"), modLoc("block/stripped_ether_log"));
        axisBlock(ETHEREAL_WOOD.get(), modLoc("block/ether_log"), modLoc("block/ether_log"));
        horizontalBlock(COMFORTABLE_CUSHION.get(), new ModelFile.ExistingModelFile(modLoc("block/comfortable_cushion"), exFileHelper));
        simpleBlock(ETHEREAL_SAPLING.get(), new ConfiguredModel(models().cross("ether_sapling", modLoc("block/ether_sapling"))));
        simpleBlock(METAPHORIC_FLESH_BLOCK.get());
        simpleBlock(METAPHORIC_STONE.get());
        simpleBlock(METAPHORIC_BONE_BLOCK.get());
        simpleBlock(INDEX_OF_KNOWLEDGE.get(), new ModelFile.ExistingModelFile(modLoc("block/index_of_knowledge"), exFileHelper));
        simpleBlock(CRYSTAL_WEB.get(), new ConfiguredModel(models().cross("crystal_web", modLoc("block/crystal_web"))));

        getVariantBuilder(SNOWBERRY_BUSH.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(new ModelFile.ExistingModelFile(new ResourceLocation(Astral.MOD_ID, "block/snowberry_bush_" + state.get(SweetBerryBushBlock.AGE)), exFileHelper)).build());

        simpleCross(FEVERWEED_BLOCK);

        getVariantBuilder(OFFERING_BRAZIER.get())
                .partialState().with(AbstractFurnaceBlock.LIT, false).modelForState().modelFile(new ModelFile.ExistingModelFile(new ResourceLocation(Astral.MOD_ID, "block/offering_brazier_off"), exFileHelper)).addModel()
                .partialState().with(AbstractFurnaceBlock.LIT, true).modelForState().modelFile(new ModelFile.ExistingModelFile(new ResourceLocation(Astral.MOD_ID, "block/offering_brazier"), exFileHelper)).addModel();


        simpleBlock(ASTRAL_MERIDIAN.get());
        simpleBlock(EGO_MEMBRANE.get());
        simpleBlock(ETHER_DIRT.get());
        simpleBlock(ETHER_GRASS.get(), new ModelFile.ExistingModelFile(new ResourceLocation(Astral.MOD_ID, "block/ether_grass"), exFileHelper));
        getVariantBuilder(ETHERIC_POWDER.get())
                .partialState().with(TripWireHookBlock.POWERED, false).modelForState().modelFile(new ModelFile.ExistingModelFile(new ResourceLocation(Astral.MOD_ID, "block/etheric_powder"), exFileHelper)).addModel()
                .partialState().with(TripWireHookBlock.POWERED, true).modelForState().modelFile(new ModelFile.ExistingModelFile(new ResourceLocation(Astral.MOD_ID, "block/etheric_powder_on"), exFileHelper)).addModel();
        simpleBlock(ETHEREAL_SPAWNER.get());
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
