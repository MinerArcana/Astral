package com.alan199921.astral.items;

import com.alan199921.astral.Astral;
import com.alan199921.astral.blocks.ModBlocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import org.checkerframework.checker.nullness.qual.NonNull;

public class EnlightenmentKey extends Item {
    public EnlightenmentKey() {
        super(new Item.Properties().group(Astral.setup.astralItems));
        setRegistryName("enlightenment_key");
    }

    @Override
    @NonNull
    public ActionResultType onItemUse(ItemUseContext context) {
        if (!context.getWorld().isRemote() && context.getWorld().getBlockState(context.getPos()).getBlock() == ModBlocks.astralMeridian) {

            context.getWorld().destroyBlock(context.getPos(), false);
        }
        return super.onItemUse(context);
    }
}
