package com.alan199921.astral.items;

import com.alan199921.astral.Astral;
import com.alan199921.astral.dimensions.ModDimensions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

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
        if (!worldIn.isRemote){
            if (worldIn.getDimension().getType() != DimensionType.byName(ModDimensions.INNER_REALM)){
                entityLiving.changeDimension(Objects.requireNonNull(DimensionType.byName(ModDimensions.INNER_REALM)));
            }
            else{
                entityLiving.changeDimension(DimensionType.OVERWORLD);
            }
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
}
