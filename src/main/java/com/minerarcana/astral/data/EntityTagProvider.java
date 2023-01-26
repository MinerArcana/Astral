package com.minerarcana.astral.data;

import com.minerarcana.astral.tags.AstralTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

@SuppressWarnings("ALL")
public class EntityTagProvider extends EntityTypeTagsProvider {

    public EntityTagProvider(DataGenerator generator, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, modId, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(AstralTags.SPIRITUAL_MOBS).add(EntityType.PHANTOM);
        tag(AstralTags.ATTUNED_MOBS).addTags(AstralTags.SPIRITUAL_MOBS, AstralTags.ETHEREAL_MOBS);
    }
}
