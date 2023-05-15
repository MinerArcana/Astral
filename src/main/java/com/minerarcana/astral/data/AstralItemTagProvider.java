package com.minerarcana.astral.data;

import com.minerarcana.astral.Astral;
import com.minerarcana.astral.items.AstralItems;
import com.minerarcana.astral.tags.AstralTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class AstralItemTagProvider extends ItemTagsProvider {
    public AstralItemTagProvider(DataGenerator generator, BlockTagsProvider provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, provider, Astral.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(AstralTags.ASTRAL_CAN_PICKUP).add(AstralItems.KEY_OF_ENLIGHTENMENT.get(), AstralItems.SLEEPLESS_EYE.get(), AstralItems.PHANTOM_EDGE.get());

    }
}
