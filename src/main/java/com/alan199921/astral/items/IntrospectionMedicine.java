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
                        .effect(new EffectInstance(AstralEffects.ASTRAL_TRAVEL_EFFECT, Integer.MAX_VALUE), 1)
                        .build())
                .group(Astral.setup.astralItems)
        );
    }

    @Nonnull
    @Override
    public ItemStack onItemUseFinish(@Nonnull ItemStack stack, World worldIn, @Nonnull LivingEntity entityLiving) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) entityLiving;
            worldIn.getCapability(InnerRealmTeleporterProvider.TELEPORTER_CAPABILITY).ifPresent(cap -> cap.teleport(playerEntity));
        }
        return new ItemStack(Items.BOWL);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }
}
