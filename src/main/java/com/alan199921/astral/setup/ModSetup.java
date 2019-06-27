package com.alan199921.astral.setup;

import com.alan199921.blocks.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModSetup {

    public ItemGroup itemGroup = new ItemGroup("astral") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModBlocks.snowberryBush);
        }
    };

    public void init() {

    }
}
