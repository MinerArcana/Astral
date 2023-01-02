package com.minerarcana.astral.mixins;

import com.minerarcana.astral.effect.AstralEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class MixinEntity {
    @Inject(at = @At("HEAD"), method = "isInWater()Z", cancellable = true)
    private void isInWater(CallbackInfoReturnable<Boolean> cir) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof LivingEntity livingEntity && livingEntity.hasEffect(AstralEffects.ASTRAL_TRAVEL.get())) {
            cir.setReturnValue(true);
        }
    }

    @Inject(at = @At("HEAD"), method = "isUnderWater()Z", cancellable = true)
    private void isUnderWater(CallbackInfoReturnable<Boolean> cir) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof LivingEntity livingEntity && livingEntity.hasEffect(AstralEffects.ASTRAL_TRAVEL.get())) {
            cir.setReturnValue(true);
        }
    }
}
