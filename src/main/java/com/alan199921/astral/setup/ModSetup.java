package com.alan199921.astral.setup;

import com.alan199921.astral.worldgen.OverworldVegetation;
import com.alan199921.astral.blocks.ModBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.DeferredWorkQueue;

public class ModSetup {

    public ItemGroup itemGroup = new ItemGroup("astral") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModBlocks.snowberryBush);
        }
    };

    public void init() {
        DeferredWorkQueue.runLater(OverworldVegetation::addOverworldVegetation);
    }
}
