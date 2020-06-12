package com.alan19.astral.data.providers;

import com.alan19.astral.blocks.etherealblocks.Ethereal;
import com.alan19.astral.items.AstralItems;
import com.alan19.astral.tags.AstralTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

import java.util.stream.Collectors;

public class ItemTagGenerator extends ItemTagsProvider {
    public ItemTagGenerator(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerTags() {
        final Item[] etherealItems = AstralItems.ITEMS.getEntries().stream().map(RegistryObject::get).filter(item -> item instanceof BlockNamedItem).filter(item -> ((BlockNamedItem) item).getBlock() instanceof Ethereal).collect(Collectors.toList()).toArray(new Item[]{});
        getBuilder(AstralTags.ASTRAL_PICKUP).add(etherealItems).add(AstralItems.ENLIGHTENMENT_KEY.get(), AstralItems.ETHERIC_POWDER_ITEM.get());
        getBuilder(AstralTags.BASIC_ASTRAL_PLANTS).add(AstralItems.FEVERWEED.get(), AstralItems.SNOWBERRY.get());
    }
}
