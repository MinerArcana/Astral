package com.alan199921.astral.items;

import com.alan199921.astral.blocks.AstralBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class AstralItemGroups {

    //Astral ItemGroup using  a Snowberry Bush as an icon
    public final ItemGroup astralItems = new ItemGroup("astral") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(AstralBlocks.SNOWBERRY_BUSH.get());
        }
    };

}
