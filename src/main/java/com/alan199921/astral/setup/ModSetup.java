package com.alan199921.astral.setup;

import com.alan199921.astral.blocks.ModBlocks;
import com.alan199921.astral.worldgen.OverworldVegetation;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.DeferredWorkQueue;

public class ModSetup {

    public final ItemGroup astralItems = new ItemGroup("astral") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModBlocks.snowberryBush);
        }
    };

    public void init() {
        DeferredWorkQueue.runLater(OverworldVegetation::addOverworldVegetation);
//        CapabilityManager.INSTANCE.register(IInnerRealmTeleporter.class, new InnerRealmStorage(), InnerRealmTeleporter::new);
    }
}
