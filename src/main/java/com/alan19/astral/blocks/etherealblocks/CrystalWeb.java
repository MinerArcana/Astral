package com.alan19.astral.blocks.etherealblocks;

import com.alan19.astral.blocks.AstralBlocks;
import com.alan19.astral.configs.AstralConfig;
import com.alan19.astral.effects.AstralEffects;
import com.alan19.astral.events.astraltravel.TravelEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class CrystalWeb extends EtherealBlock {
    public static final IntegerProperty GENERATION = IntegerProperty.create("generation", 0, 256);

    public CrystalWeb() {
        super(BlockBehaviour.Properties.of(Material.WEB).noCollission().strength(4.0F).noOcclusion());
        registerDefaultState(getStateDefinition().any().setValue(GENERATION, 0));
    }

    @Override
    @ParametersAreNonnullByDefault
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        if (entityIn instanceof LivingEntity && TravelEffects.isEntityAstral((LivingEntity) entityIn) && !((LivingEntity) entityIn).hasEffect(AstralEffects.MIND_VENOM.get()) && !(entityIn instanceof Spider)) {
            entityIn.makeStuckInBlock(state, new Vec3(0.25D, 0.05F, 0.25D));
            if (worldIn.getDifficulty() == Difficulty.NORMAL || worldIn.getDifficulty() == Difficulty.HARD) {
                ((LivingEntity) entityIn).addEffect(new MobEffectInstance(AstralEffects.MIND_VENOM.get(), 100));
            }
        }
    }

    @Override
    public boolean isRandomlyTicking(@Nonnull BlockState state) {
        return true;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random rand) {
        super.tick(state, worldIn, pos, rand);
        final int moonPhase = worldIn.dimensionType().moonPhase(worldIn.getDayTime());
        //Spread in a direction based on moon phasae
        final int spreadGenerations = state.getValue(GENERATION);
        // TODO Make this configurable
        // Controls how many generations the web can spread
        int maxGenerations = 10;
        if (spreadGenerations < maxGenerations && worldIn.getBiome(pos).value().getPrecipitation() == Biome.Precipitation.RAIN && rand.nextInt(AstralConfig.getWorldgenSettings().crystalWebSpreadChance.get()) == 0 && pos.getY() >= 128 && BlockPos.betweenClosedStream(pos.offset(-2, -2, -2), pos.offset(2, 2, 2)).filter(blockPos -> worldIn.getBlockState(blockPos).getBlock() == this).count() <= 4) {
            final List<BlockPos> collect = getBoxForMoonPhase(moonPhase, pos)
                    .filter(worldIn::isEmptyBlock)
                    .map(BlockPos::immutable)
                    .toList();
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
        return switch (moonPhase) {
            case 0 -> BlockPos.betweenClosedStream(center.east().below().south(), center.east(3).above().north());
            case 1 -> BlockPos.betweenClosedStream(center.north().below().east(), center.north(3).above().east(3));
            case 2 -> BlockPos.betweenClosedStream(center.north().below().east(), center.north(3).above().west());
            case 3 -> BlockPos.betweenClosedStream(center.north().below().west(), center.north(3).above().west(3));
            case 4 -> BlockPos.betweenClosedStream(center.west().below().south(), center.west(3).above().north());
            case 5 -> BlockPos.betweenClosedStream(center.west().below().north(), center.west(3).above().south(3));
            case 6 -> BlockPos.betweenClosedStream(center.west().below().south(), center.east().above().south(3));
            case 7 -> BlockPos.betweenClosedStream(center.east().below().south(), center.east(3).above().south());
            default -> throw new IllegalStateException("Unexpected value: " + moonPhase);
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(GENERATION);
    }
}
