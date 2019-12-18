package com.alan199921.astral.items;

import com.alan199921.astral.Astral;
import com.alan199921.astral.capabilities.innerrealmteleporter.InnerRealmTeleporterProvider;
import com.alan199921.astral.effects.AstralEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
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
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) entityLiving;
            playerEntity.addPotionEffect(new EffectInstance(AstralEffects.ASTRAL_TRAVEL, Integer.MAX_VALUE));
            worldIn.getCapability(InnerRealmTeleporterProvider.TELEPORTER_CAPABILITY).ifPresent(cap -> cap.teleport(playerEntity));
            playerEntity.addItemStackToInventory(new ItemStack(Items.BOWL));
        }
        super.onItemUseFinish(stack, worldIn, entityLiving);
        return ItemStack.EMPTY;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }
}
