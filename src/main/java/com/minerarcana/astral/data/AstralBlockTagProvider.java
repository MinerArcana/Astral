package com.minerarcana.astral.data;

import com.minerarcana.astral.Astral;
import com.minerarcana.astral.tags.AstralTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unchecked")
public class AstralBlockTagProvider extends BlockTagsProvider {

    public AstralBlockTagProvider(DataGenerator pGenerator, @Nullable ExistingFileHelper existingFileHelper) {
        super(pGenerator, Astral.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(AstralTags.FEVERWEED_PLANTABLE_ON).addTags(BlockTags.DIRT, BlockTags.LEAVES);
        tag(AstralTags.SNOWBERRY_PLANTABLE_ON).addTags(BlockTags.SNOW, Tags.Blocks.GRAVEL, BlockTags.ICE, BlockTags.DIRT);
    }
}
