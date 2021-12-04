package com.alan19.astral.items.tools;

import com.alan19.astral.blocks.AstralBlocks;
import com.alan19.astral.items.AstralItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class PhantasmalShears extends ShearsItem {
    public PhantasmalShears() {
        super(new Item.Properties().tab(AstralItems.ASTRAL_ITEMS).durability(120));
    }

    @Override
    public boolean isCorrectToolForDrops(@Nonnull BlockState blockIn) {
        return super.isCorrectToolForDrops(blockIn) || blockIn.getBlock() == AstralBlocks.CRYSTAL_WEB.get();
    }
}
