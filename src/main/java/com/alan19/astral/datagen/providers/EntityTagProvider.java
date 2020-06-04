package com.alan19.astral.datagen.providers;

import com.alan19.astral.entities.AstralEntities;
import com.alan19.astral.tags.AstralTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.EntityTypeTagsProvider;

public class EntityTagProvider extends EntityTypeTagsProvider {
    public EntityTagProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void registerTags() {
        getBuilder(AstralTags.ASTRAL_ENTITIES).add(AstralEntities.CRYSTAL_SPIDER.get());
    }
}
