package com.alan19.astral.data.providers;

import com.alan19.astral.entity.AstralEntities;
import com.alan19.astral.entity.IAstralBeing;
import com.alan19.astral.tags.AstralTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nullable;

@SuppressWarnings("unchecked")
public class EntityTagProvider extends EntityTypeTagsProvider {

    public EntityTagProvider(DataGenerator generator, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, modId, existingFileHelper);
    }

    @Override
    protected void addTags() {
        AstralEntities.ENTITIES.getEntries().stream().filter(entityType -> entityType.get() instanceof IAstralBeing).forEach(this::addAstralBeing);
        tag(AstralTags.SPIRITUAL_BEINGS).add(EntityType.PHANTOM);
        tag(AstralTags.ATTUNED_ENTITIES).addTags(AstralTags.SPIRITUAL_BEINGS, AstralTags.ETHEREAL_BEINGS);
        tag(AstralTags.NEUTRAL_MOBS).add(EntityType.SPIDER, EntityType.ENDERMAN, EntityType.PHANTOM, EntityType.ZOMBIFIED_PIGLIN, EntityType.PIGLIN);
        tag(AstralTags.ETHEREAL_BEINGS).add(AstralEntities.CRYSTAL_SPIDER.get(), AstralEntities.CRYSTAL_WEB_PROJECTILE_ENTITY.get());
    }

    private void addAstralBeing(RegistryObject<EntityType<?>> entityTypeRegistryObject) {
        tag(AstralTags.ETHEREAL_BEINGS).add(entityTypeRegistryObject.get());
    }
}
