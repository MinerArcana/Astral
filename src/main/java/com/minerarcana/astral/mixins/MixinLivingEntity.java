package com.minerarcana.astral.mixins;

import com.minerarcana.astral.effect.AstralEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {
    /**
     * Allow player to hold jump to gain altitude
     *
     * @param ci
     */
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getFluidJumpThreshold()D"), method = "aiStep()V")
    private void aiStep(CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (livingEntity.hasEffect(AstralEffects.ASTRAL_TRAVEL.get()) && livingEntity.jumping) {
            livingEntity.jumpInFluid(net.minecraftforge.common.ForgeMod.WATER_TYPE.get());
        }
    }
}
