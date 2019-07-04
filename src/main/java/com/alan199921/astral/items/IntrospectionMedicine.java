package com.alan199921.astral.items;

import com.alan199921.astral.Astral;
import com.alan199921.astral.dimensions.TeleportationTools;
import com.alan199921.astral.dimensions.ModDimensions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class IntrospectionMedicine extends Item {
    public IntrospectionMedicine() {
        super(new Properties()
                .food(new Food.Builder()
                        .setAlwaysEdible()
                        .build())
                .group(Astral.setup.astralItems)
        );
        setRegistryName("introspection_medicine");
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if(!worldIn.isRemote) {
            TeleportationTools.changeDim((ServerPlayerEntity) entityLiving, entityLiving.getPosition(), entityLiving.dimension != DimensionType.byName(ModDimensions.INNER_REALM) ? DimensionType.byName(ModDimensions.INNER_REALM) : DimensionType.OVERWORLD);
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }

    //    @Override
//    public ActionResultType onItemUse(ItemUseContext context) {
//        Objects.requireNonNull(context.getPlayer()).changeDimension(context.getPlayer().dimension == DimensionType.byName(ModDimensions.INNER_REALM) ? Objects.requireNonNull(DimensionType.OVERWORLD) : Objects.requireNonNull(DimensionType.byName(ModDimensions.INNER_REALM)));
//        return super.onItemUse(context);
//    }
}
