package com.minerarcana.astral.items;

import com.minerarcana.astral.api.AstralCapabilities;
import com.minerarcana.astral.effect.AstralEffects;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class IntrospectionMedicine extends Item {
    public IntrospectionMedicine(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (pLivingEntity instanceof ServerPlayer playerEntity) {
            playerEntity.addItem(new ItemStack(Items.BOWL));
            playerEntity.addEffect(new MobEffectInstance(AstralEffects.ASTRAL_TRAVEL.get(), Integer.MAX_VALUE));
            AstralCapabilities.getInnerRealmTeleporter(pLevel).ifPresent(innerRealmTeleporter -> innerRealmTeleporter.teleport(playerEntity));
        }
        super.finishUsingItem(pStack, pLevel, pLivingEntity);
        pStack.shrink(1);
        return pStack;

    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack pStack) {
        return UseAnim.DRINK;
    }
}
