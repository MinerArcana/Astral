package com.alan19.astral.data.providers;

import com.alan19.astral.entity.AstralEntities;
import com.alan19.astral.entity.IAstralBeing;
import com.alan19.astral.tags.AstralTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.EntityTypeTagsProvider;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;

public class EntityTagProvider extends EntityTypeTagsProvider {
    public EntityTagProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void registerTags() {
        AstralEntities.ENTITIES.getEntries().stream().filter(entityType -> entityType.get() instanceof IAstralBeing).forEach(this::addAstralBeing);
        getBuilder(AstralTags.SPIRITUAL_BEINGS).add(EntityType.PHANTOM);
        getBuilder(AstralTags.ATTUNED_ENTITIES).add(AstralTags.SPIRITUAL_BEINGS, AstralTags.ETHEREAL_BEINGS);
    }

    private void addAstralBeing(RegistryObject<EntityType<?>> entityTypeRegistryObject) {
        getBuilder(AstralTags.ETHEREAL_BEINGS).add(entityTypeRegistryObject.get());
    }
}
