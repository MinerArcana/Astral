package com.alan19.astral.blocks.etherealblocks;

import com.alan19.astral.effects.AstralEffects;
import com.alan19.astral.events.astraltravel.TravelEffects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

public class CrystalWeb extends EtherealBlock {
    public CrystalWeb() {
        super(Block.Properties.create(Material.WEB).doesNotBlockMovement().hardnessAndResistance(4.0F).notSolid());
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (entityIn instanceof LivingEntity && TravelEffects.isEntityAstral((LivingEntity) entityIn)) {
            entityIn.setMotionMultiplier(state, new Vec3d(0.25D, 0.05F, 0.25D));
            ((LivingEntity) entityIn).addPotionEffect(new EffectInstance(AstralEffects.MIND_VENOM.get(), 100));
        }
    }
}
