package com.minerarcana.astral.mixins;

import com.minerarcana.astral.effect.AstralEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
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

    @ModifyVariable(method = "travel", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;getValue()D", ordinal = 1), ordinal = 1)
    private float f5 (float value){
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (livingEntity.hasEffect(AstralEffects.ASTRAL_TRAVEL.get())) {
            System.out.println("Modifying swim rate!");
            return (float) 9999999;
        }
        return value;
    }

    /**
     * Gets the number of air blocks below the player
     *
     * @param player The player to check
     * @return The number of air blocks below the player
     */
    private int getNumberOfAirBlocksBelowPlayer(LivingEntity player) {
        BlockPos pos = player.getOnPos();
        int count = 0;
        while (pos.getY() >= 0 && !player.getLevel().getBlockState(pos).isCollisionShapeFullBlock(player.getLevel(), pos)) {
            pos = pos.below();
            count++;
        }
        return count;
    }
}
