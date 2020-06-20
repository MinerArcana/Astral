package com.alan19.astral.blocks.etherealblocks;

import com.alan19.astral.blocks.AstralBlocks;
import com.alan19.astral.effects.AstralEffects;
import com.alan19.astral.events.astraltravel.TravelEffects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CrystalWeb extends EtherealBlock {
    public CrystalWeb() {
        super(Block.Properties.create(Material.WEB).doesNotBlockMovement().hardnessAndResistance(4.0F).notSolid());
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (entityIn instanceof LivingEntity && TravelEffects.isEntityAstral((LivingEntity) entityIn) && !(entityIn instanceof SpiderEntity)) {
            entityIn.setMotionMultiplier(state, new Vec3d(0.25D, 0.05F, 0.25D));
            if (worldIn.getDifficulty() == Difficulty.NORMAL || worldIn.getDifficulty() == Difficulty.HARD) {
                ((LivingEntity) entityIn).addPotionEffect(new EffectInstance(AstralEffects.MIND_VENOM.get(), 100));
            }
        }
    }

    @Override
    public boolean ticksRandomly(BlockState state) {
        return true;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        super.tick(state, worldIn, pos, rand);
        if (rand.nextInt(30) == 0 && pos.getY() >= 128) {
            final int moonPhase = worldIn.getMoonPhase();
            if (BlockPos.getAllInBox(pos.add(-2, -1, -2), pos.add(2, 1, 2)).filter(blockPos -> worldIn.getBlockState(blockPos).getBlock() == this).count() <= 5) {
                final List<BlockPos> collect = getBoxForMoonPhase(moonPhase, pos).filter(worldIn::isAirBlock).collect(Collectors.toList());
                final BlockPos newWebPos = collect.get(rand.nextInt(collect.size()));
                worldIn.setBlockState(newWebPos, AstralBlocks.CRYSTAL_WEB.get().getDefaultState(), 3);
            }
        }
    }

    public Stream<BlockPos> getBoxForMoonPhase(int moonPhase, BlockPos center) {
        switch (moonPhase) {
            case 0:
                return BlockPos.getAllInBox(center.east().down().south(), center.east(3).up().north());
            case 1:
                return BlockPos.getAllInBox(center.north().down().east(), center.north(3).up().east(3));
            case 2:
                return BlockPos.getAllInBox(center.north().down().east(), center.north(3).up().west());
            case 3:
                return BlockPos.getAllInBox(center.north().down().west(), center.north(3).up().west(3));
            case 4:
                return BlockPos.getAllInBox(center.west().down().south(), center.west(3).up().north());
            case 5:
                return BlockPos.getAllInBox(center.west().down().north(), center.west(3).up().south(3));
            case 6:
                return BlockPos.getAllInBox(center.west().down().south(), center.east().up().south(3));
            case 7:
                return BlockPos.getAllInBox(center.east().down().south(), center.east(3).up().south());
            default:
                throw new IllegalStateException("Unexpected value: " + moonPhase);
        }
    }
}