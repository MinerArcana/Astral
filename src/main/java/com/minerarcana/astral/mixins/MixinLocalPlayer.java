package com.minerarcana.astral.mixins;

import com.minerarcana.astral.effect.AstralEffects;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class MixinLocalPlayer {
    @Inject(at = @At("TAIL"), method = "aiStep()V")
    private void aiStep(CallbackInfo ci) {
        LocalPlayer localPlayer = (LocalPlayer) (Object) this;
        if (localPlayer.hasEffect(AstralEffects.ASTRAL_TRAVEL.get())) {
            boolean flag1 = localPlayer.input.shiftKeyDown;
            boolean flag2 = localPlayer.hasEnoughImpulseToStartSprinting();

            if (!flag1 && !flag2 && localPlayer.hasEnoughImpulseToStartSprinting() && !localPlayer.isSprinting() && !localPlayer.isUsingItem() && !localPlayer.hasEffect(MobEffects.BLINDNESS)) {
                if (localPlayer.sprintTriggerTime <= 0 && !localPlayer.minecraft.options.keySprint.isDown()) {
                    localPlayer.sprintTriggerTime = 7;
                } else {
                    localPlayer.setSprinting(true);
                }
            }

            if (!localPlayer.isSprinting() && localPlayer.hasEnoughImpulseToStartSprinting() && !localPlayer.isUsingItem() && !localPlayer.hasEffect(MobEffects.BLINDNESS) && localPlayer.minecraft.options.keySprint.isDown()) {
                localPlayer.setSprinting(true);
            }

            boolean flag5 = !localPlayer.input.hasForwardImpulse();
            boolean flag6 = flag5 || localPlayer.horizontalCollision && !localPlayer.minorHorizontalCollision;
            if (flag6) {
                localPlayer.setSprinting(false);
            } else if (localPlayer.isSwimming()) {
                localPlayer.setSprinting(true);
            }
        }
    }

}
