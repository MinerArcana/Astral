package com.minerarcana.astral.mixins;

import com.minerarcana.astral.effect.AstralEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class MixinPlayer {
    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/world/entity/player/Player;isSwimming()Z", cancellable = true)
    private void isSwimming(CallbackInfoReturnable<Boolean> cir) {
        Player player = (Player) (Object) this;
        if (!player.getAbilities().flying && !player.isSpectator() && (Minecraft.getInstance().options.keySprint.isDown()) && player.hasEffect(AstralEffects.ASTRAL_TRAVEL.get())) {
            cir.setReturnValue(true);
        }
    }

    /**
     * Make player move in direction they are looking at when they have Astral Travel
     *
     * @param pTravelVector
     * @param ci
     */
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"), method = "travel(Lnet/minecraft/world/phys/Vec3;)V")
    private void travel(Vec3 pTravelVector, CallbackInfo ci) {
        Player player = (Player) (Object) this;
        double d3 = player.getLookAngle().y;
        double d4 = d3 < -0.2D ? 0.085D : 0.06D;
        if (d3 <= 0.0D || player.jumping || player.hasEffect(AstralEffects.ASTRAL_TRAVEL.get()) || !player.level.getBlockState(new BlockPos(player.getX(), player.getY() + 1.0D - 0.1D, player.getZ())).getFluidState().isEmpty()) {
            Vec3 vec31 = player.getDeltaMovement();
            player.setDeltaMovement(vec31.add(0.0D, (d3 - vec31.y) * d4, 0.0D));
        }
    }
}
