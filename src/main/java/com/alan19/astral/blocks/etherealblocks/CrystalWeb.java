package com.alan19.astral.blocks.etherealblocks;

import com.alan19.astral.blocks.AstralBlocks;
import com.alan19.astral.configs.AstralConfig;
import com.alan19.astral.effects.AstralEffects;
import com.alan19.astral.events.astraltravel.TravelEffects;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CrystalWeb extends EtherealBlock {
    public static final IntegerProperty GENERATION = IntegerProperty.create("generation", 0, 256);

    public CrystalWeb() {
        super(AbstractBlock.Properties.of(Material.WEB).noCollission().strength(4.0F).noOcclusion());
        registerDefaultState(getStateDefinition().any().setValue(GENERATION, 0));
    }

    @Override
    @ParametersAreNonnullByDefault
    public void entityInside(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (entityIn instanceof LivingEntity && TravelEffects.isEntityAstral((LivingEntity) entityIn) && !((LivingEntity) entityIn).hasEffect(AstralEffects.MIND_VENOM.get()) && !(entityIn instanceof SpiderEntity)) {
            entityIn.makeStuckInBlock(state, new Vector3d(0.25D, 0.05F, 0.25D));
            if (worldIn.getDifficulty() == Difficulty.NORMAL || worldIn.getDifficulty() == Difficulty.HARD) {
                ((LivingEntity) entityIn).addEffect(new EffectInstance(AstralEffects.MIND_VENOM.get(), 100));
            }
        }
    }

    @Override
    public boolean isRandomlyTicking(@Nonnull BlockState state) {
        return true;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        super.tick(state, worldIn, pos, rand);
        final int moonPhase = worldIn.dimensionType().moonPhase(worldIn.getDayTime());
        //Spread in a direction based on moon phasae
        final Integer spreadGenerations = state.getValue(GENERATION);
        // TODO Make this configurable
        // Controls how many generations the web can spread
        Integer maxGenerations = 10;
        if (spreadGenerations < maxGenerations && worldIn.getBiome(pos).getPrecipitation() == Biome.RainType.RAIN && rand.nextInt(AstralConfig.getWorldgenSettings().crystalWebSpreadChance.get()) == 0 && pos.getY() >= 128 && BlockPos.betweenClosedStream(pos.offset(-2, -2, -2), pos.offset(2, 2, 2)).filter(blockPos -> worldIn.getBlockState(blockPos).getBlock() == this).count() <= 4) {
            final List<BlockPos> collect = getBoxForMoonPhase(moonPhase, pos).filter(worldIn::isEmptyBlock).map(BlockPos::immutable).collect(Collectors.toList());
            if (!collect.isEmpty()) {
                final BlockPos newWebPos = collect.get(rand.nextInt(collect.size()));
                worldIn.setBlock(newWebPos, AstralBlocks.CRYSTAL_WEB.get().defaultBlockState().setValue(GENERATION, spreadGenerations + 1), 3);
            }
        }
        //Remove block if exposed to rain or any liquid
        if (worldIn.isRainingAt(pos) || !worldIn.getFluidState(pos).isEmpty()) {
            worldIn.removeBlock(pos, false);
        }
    }

    public Stream<BlockPos> getBoxForMoonPhase(int moonPhase, BlockPos center) {
        switch (moonPhase) {
            case 0:
                return BlockPos.betweenClosedStream(center.east().below().south(), center.east(3).above().north());
            case 1:
                return BlockPos.betweenClosedStream(center.north().below().east(), center.north(3).above().east(3));
            case 2:
                return BlockPos.betweenClosedStream(center.north().below().east(), center.north(3).above().west());
            case 3:
                return BlockPos.betweenClosedStream(center.north().below().west(), center.north(3).above().west(3));
            case 4:
                return BlockPos.betweenClosedStream(center.west().below().south(), center.west(3).above().north());
            case 5:
                return BlockPos.betweenClosedStream(center.west().below().north(), center.west(3).above().south(3));
            case 6:
                return BlockPos.betweenClosedStream(center.west().below().south(), center.east().above().south(3));
            case 7:
                return BlockPos.betweenClosedStream(center.east().below().south(), center.east(3).above().south());
            default:
                throw new IllegalStateException("Unexpected value: " + moonPhase);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(GENERATION);
    }
}
