package com.alan19.astral.data.providers;

import com.alan19.astral.blocks.etherealblocks.Ethereal;
import com.alan19.astral.blocks.etherealblocks.EthericGrowth;
import com.alan19.astral.blocks.etherealblocks.TallEthericGrowth;
import com.alan19.astral.tags.AstralTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.RegistryObject;
import org.apache.commons.lang3.ArrayUtils;

import java.util.stream.Collectors;

import static com.alan19.astral.items.AstralItems.*;

public class ItemTagGenerator extends ItemTagsProvider {

    public ItemTagGenerator(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerTags() {
        getBuilder(AstralTags.ASTRAL_PICKUP).add(getAllItemsWithBlockClass(Ethereal.class)).add(ENLIGHTENMENT_KEY.get(), ETHERIC_POWDER_ITEM.get(), PHANTOM_EDGE.get(), PHANTASMAL_AXE.get(), PHANTASMAL_PICKAXE.get(), PHANTASMAL_SHOVEL.get(), PHANTASMAL_SWORD.get(), SLEEPLESS_EYE.get(), METAPHORIC_BONE.get(), PHANTASMAL_SHEARS.get(), METAPHORIC_FLESH.get(), DREAMWEAVE.get(), DREAMCORD.get(), CRYSTAL_CHITIN.get());
        getBuilder(AstralTags.BASIC_ASTRAL_PLANTS).add(FEVERWEED.get(), SNOWBERRY.get());
        getBuilder(Tags.Items.MUSHROOMS).add(RUSTCAP_MUSHROOM_ITEM.get(), BLUECAP_MUSHROOM_ITEM.get());
        getBuilder(ItemTags.SAPLINGS).add(ETHEREAL_SAPLING_ITEM.get());
        getBuilder(ItemTags.SMALL_FLOWERS).add(getAllItemsWithBlockClass(EthericGrowth.class));
        getBuilder(ItemTags.TALL_FLOWERS).add(getAllItemsWithBlockClass(TallEthericGrowth.class));
        getBuilder(ItemTags.FLOWERS).add(ArrayUtils.addAll(getAllItemsWithBlockClass(EthericGrowth.class), getAllItemsWithBlockClass(TallEthericGrowth.class)));
        getBuilder(ItemTags.LOGS).add(ETHEREAL_LOG_ITEM.get(), ETHEREAL_WOOD_ITEM.get(), STRIPPED_ETHEREAL_LOG_ITEM.get(), STRIPPED_ETHEREAL_WOOD_ITEM.get());
        getBuilder(ItemTags.PLANKS).add(ETHEREAL_PLANKS_ITEM.get());
        getBuilder(ItemTags.LEAVES).add(ETHEREAL_LEAVES_ITEM.get());
        getBuilder(ItemTags.BUTTONS).add(ETHERIC_POWDER_ITEM.get());
        getBuilder(Tags.Items.SHEARS).add(PHANTASMAL_SHEARS.get());
        getBuilder(Tags.Items.STRING).add(DREAMCORD.get());
    }

    private Item[] getAllItemsWithBlockClass(Class<?> aClass) {
        return ITEMS.getEntries().stream().map(RegistryObject::get).filter(item -> item instanceof BlockNamedItem && (aClass.isInstance(((BlockNamedItem) item).getBlock()))).collect(Collectors.toList()).toArray(new Item[]{});
    }
}
