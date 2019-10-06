package com.alan199921.astral.setup;

import com.alan199921.astral.blocks.ModBlocks;
import com.alan199921.astral.worldgen.OverworldVegetation;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.DeferredWorkQueue;

public class ModSetup {

    //Astral ItemGroup using  a Snowberry Bush as an icon
    public final ItemGroup astralItems = new ItemGroup("astral") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModBlocks.snowberryBush);
        }
    };

    public void init() {
        //Initializes worldgen
        DeferredWorkQueue.runLater(OverworldVegetation::addOverworldVegetation);
    }
}
