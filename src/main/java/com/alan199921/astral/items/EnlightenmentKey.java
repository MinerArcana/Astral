package com.alan199921.astral.items;

import com.alan199921.astral.Astral;
import com.alan199921.astral.blocks.AstralMeridian;
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
        if (context.getWorld().getBlockState(context.getPos()).getBlock() == ModBlocks.astralMeridian) {
            int meridianPlane = context.getWorld().getBlockState(context.getPos()).getBlockState().get(AstralMeridian.PLANE);

            //XY Plane
            if (meridianPlane == 0){
                for (int i = -7; i < 7; i++){
                    for (int j = -7; j < 7; j++){
                        context.getWorld().destroyBlock(context.getPos().add(i, j, 0), false);
                    }
                }
            }

            //YZ Plane
            else if (meridianPlane == 1){
                for (int i = -7; i < 7; i++){
                    for (int j = -7; j < 7; j++){
                        context.getWorld().destroyBlock(context.getPos().add(0, i, j), false);
                    }
                }
            }

            //XZ Plane
            else if (meridianPlane == 2){
                for (int i = -7; i < 7; i++){
                    for (int j = -7; j < 7; j++){
                        context.getWorld().destroyBlock(context.getPos().add(i, 0, j), false);
                    }
                }
            }
        }
        return super.onItemUse(context);
    }
}
