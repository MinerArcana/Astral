package com.alan19.astral.data.providers;

import com.alan19.astral.blocks.etherealblocks.Ethereal;
import com.alan19.astral.tags.AstralTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

import java.util.stream.Collectors;

import static com.alan19.astral.items.AstralItems.*;

public class ItemTagGenerator extends ItemTagsProvider {
    public ItemTagGenerator(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerTags() {
        final Item[] etherealItems = ITEMS.getEntries().stream().map(RegistryObject::get).filter(item -> item instanceof BlockNamedItem).filter(item -> ((BlockNamedItem) item).getBlock() instanceof Ethereal).collect(Collectors.toList()).toArray(new Item[]{});
        getBuilder(AstralTags.ASTRAL_PICKUP).add(etherealItems).add(ENLIGHTENMENT_KEY.get(), ETHERIC_POWDER_ITEM.get(), PHANTOM_EDGE.get(), PHANTASMAL_AXE.get(), PHANTASMAL_PICKAXE.get(), PHANTASMAL_SHOVEL.get(), PHANTASMAL_SWORD.get(), SLEEPLESS_EYE.get(), METAPHORIC_BONE.get(), PHANTASMAL_SHEARS.get(), METAPHORIC_FLESH.get(), DREAMWEAVE.get(), DREAMCORD.get(), CRYSTAL_CHITIN.get());
        getBuilder(AstralTags.BASIC_ASTRAL_PLANTS).add(FEVERWEED.get(), SNOWBERRY.get());
    }
}
