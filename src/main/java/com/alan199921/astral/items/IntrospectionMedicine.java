package com.alan199921.astral.items;

import com.alan199921.astral.Astral;
import com.alan199921.astral.capabilities.innerrealmteleporter.InnerRealmTeleporterProvider;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class IntrospectionMedicine extends Item {
    public IntrospectionMedicine() {
        super(new Properties()
                .food(new Food.Builder()
                        .setAlwaysEdible()
                        .hunger(1)
                        .saturation(-2F)
                        .build())
                .group(Astral.setup.astralItems)
        );
    }

    @Nonnull
    @Override
    public ItemStack onItemUseFinish(@Nonnull ItemStack stack, World worldIn, @Nonnull LivingEntity entityLiving) {
        PlayerEntity playerEntity = (PlayerEntity) entityLiving;
        worldIn.getCapability(InnerRealmTeleporterProvider.TELEPORTER_CAPABILITY).ifPresent(cap -> cap.teleport(playerEntity));
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }

}
