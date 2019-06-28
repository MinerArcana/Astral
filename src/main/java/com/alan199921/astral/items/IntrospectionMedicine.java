package com.alan199921.astral.items;

import com.alan199921.astral.Astral;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.world.World;

public class IntrospectionMedicine extends Item {
    public IntrospectionMedicine() {
        super(new Properties()
                .food(new Food.Builder()
                        .setAlwaysEdible()
                        .build())
                .group(Astral.setup.itemGroup)
        );
        setRegistryName("introspection_medicine");
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (!worldIn.isRemote){
            entityLiving.sendMessage(new TextComponent() {
                @Override
                public String getUnformattedComponentText() {
                    return "The Inner Realm is under construction. Please check back later.";
                }

                @Override
                public ITextComponent shallowCopy() {
                    return null;
                }
            });
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
}
