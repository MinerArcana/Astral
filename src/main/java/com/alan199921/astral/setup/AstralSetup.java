package com.alan199921.astral.setup;

import com.alan199921.astral.blocks.AstralBlocks;
import com.alan199921.astral.worldgen.OverworldVegetation;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class AstralSetup {

    //Astral ItemGroup using  a Snowberry Bush as an icon
    public final ItemGroup astralItems = new ItemGroup("astral") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(AstralBlocks.SNOWBERRY_BUSH);
        }
    };

    public void init() {
        //Initializes worldgen
        OverworldVegetation.addOverworldVegetation();
    }
}
