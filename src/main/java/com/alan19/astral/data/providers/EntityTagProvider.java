package com.alan19.astral.data.providers;

import com.alan19.astral.entity.AstralEntities;
import com.alan19.astral.entity.IAstralBeing;
import com.alan19.astral.tags.AstralTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.EntityTypeTagsProvider;
import net.minecraft.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nullable;

@SuppressWarnings("unchecked")
public class EntityTagProvider extends EntityTypeTagsProvider {

    public EntityTagProvider(DataGenerator generator, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, modId, existingFileHelper);
    }

    @Override
    protected void registerTags() {
        AstralEntities.ENTITIES.getEntries().stream().filter(entityType -> entityType.get() instanceof IAstralBeing).forEach(this::addAstralBeing);
        getOrCreateBuilder(AstralTags.SPIRITUAL_BEINGS).add(EntityType.PHANTOM);
        getOrCreateBuilder(AstralTags.ATTUNED_ENTITIES).addTags(AstralTags.SPIRITUAL_BEINGS, AstralTags.ETHEREAL_BEINGS);
        getOrCreateBuilder(AstralTags.NEUTRAL_MOBS).add(EntityType.SPIDER, EntityType.ENDERMAN, EntityType.PHANTOM, EntityType.ZOMBIFIED_PIGLIN, EntityType.PIGLIN);
        getOrCreateBuilder(AstralTags.ETHEREAL_BEINGS).add(AstralEntities.CRYSTAL_SPIDER.get(), AstralEntities.CRYSTAL_WEB_PROJECTILE_ENTITY.get());
    }

    private void addAstralBeing(RegistryObject<EntityType<?>> entityTypeRegistryObject) {
        getOrCreateBuilder(AstralTags.ETHEREAL_BEINGS).add(entityTypeRegistryObject.get());
    }
}
