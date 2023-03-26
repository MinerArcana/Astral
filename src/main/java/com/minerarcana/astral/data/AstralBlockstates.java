package com.minerarcana.astral.data;

import com.minerarcana.astral.Astral;
import com.minerarcana.astral.blocks.AstralBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class AstralBlockstates extends BlockStateProvider {
    private final ExistingFileHelper exFileHelper;

    public AstralBlockstates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Astral.MOD_ID, exFileHelper);
        this.exFileHelper = exFileHelper;
    }

    @Override
    protected void registerStatesAndModels() {
        getVariantBuilder(AstralBlocks.SNOWBERRY_BUSH.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(new ModelFile.ExistingModelFile(new ResourceLocation(Astral.MOD_ID, "block/snowberry_bush_" + state.getValue(SweetBerryBushBlock.AGE)), exFileHelper)).build());
        simpleBlock(AstralBlocks.ASTRAL_MERIDIAN.get());
        simpleBlock(AstralBlocks.EGO_MEMBRANE.get());
        simpleCross(AstralBlocks.FEVERWEED);
    }

    private void simpleCross(RegistryObject<? extends Block> block) {
        simpleBlock(block.get(), new ConfiguredModel(models().cross(block.getId().getPath(), modLoc("block/" + block.getId().getPath())).renderType("cutout_mipped")));
    }

}
