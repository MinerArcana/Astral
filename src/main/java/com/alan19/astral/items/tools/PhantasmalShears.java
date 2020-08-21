package com.alan19.astral.items.tools;

import com.alan19.astral.blocks.AstralBlocks;
import com.alan19.astral.items.AstralItems;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ShearsItem;

import javax.annotation.Nonnull;

public class PhantasmalShears extends ShearsItem {
    public PhantasmalShears() {
        super(new Item.Properties().group(AstralItems.ASTRAL_ITEMS).maxDamage(120));
    }

    @Override
    public boolean canHarvestBlock(@Nonnull BlockState blockIn) {
        return super.canHarvestBlock(blockIn) || blockIn.getBlock() == AstralBlocks.CRYSTAL_WEB.get();
    }
}
