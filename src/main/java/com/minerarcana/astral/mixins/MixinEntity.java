package com.minerarcana.astral.mixins;

import com.minerarcana.astral.effect.AstralEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class MixinEntity {
    /**
     * Gives the player water physics in jumping and floating while Astral Traveling
     *
     * @param cir
     */
    @Inject(at = @At("HEAD"), method = "isInWater()Z", cancellable = true)
    private void isInWater(CallbackInfoReturnable<Boolean> cir) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof LivingEntity livingEntity && livingEntity.hasEffect(AstralEffects.ASTRAL_TRAVEL.get())) {
            cir.setReturnValue(true);
        }
    }

    /**
     * Gives players proper swim physics
     *
     * @param cir
     */
    @Inject(at = @At("HEAD"), method = "isUnderWater()Z", cancellable = true)
    private void isUnderWater(CallbackInfoReturnable<Boolean> cir) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof LivingEntity livingEntity && livingEntity.hasEffect(AstralEffects.ASTRAL_TRAVEL.get())) {
            cir.setReturnValue(true);
        }
    }

    @Inject(at = @At("TAIL"), method = "updateSwimming", cancellable = true)
    private void updateSwimming(CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof LivingEntity livingEntity && livingEntity.hasEffect(AstralEffects.ASTRAL_TRAVEL.get())) {
            entity.setSwimming(entity.isSprinting() && !entity.isPassenger());
            ci.cancel();
        }

    }
}
