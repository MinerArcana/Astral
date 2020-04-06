package com.alan199921.astral.blocks.etherealblocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DecoratedFeatureConfig;
import net.minecraft.world.gen.feature.FlowersFeature;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

import java.util.List;
import java.util.Random;

public class EtherGrass extends EtherealBlock implements IGrowable {
    public EtherGrass() {
        super(Properties.create(Material.ORGANIC)
                .hardnessAndResistance(.5f)
                .harvestTool(ToolType.SHOVEL)
                .sound(SoundType.PLANT));
    }

    @Override
    public boolean canGrow(IBlockReader iBlockReader, BlockPos blockPos, BlockState blockState, boolean b) {
        return blockState.isAir(iBlockReader, blockPos.up());
    }

    @Override
    public boolean canUseBonemeal(World world, Random random, BlockPos blockPos, BlockState blockState) {
        return false;
    }

    @Override
    public void grow(ServerWorld serverWorld, Random random, BlockPos blockPos, BlockState blockState) {
        BlockPos upPos = blockPos.up();
        BlockState lvt_6_1_ = Blocks.GRASS.getDefaultState();

        label48:
        for (int lvt_7_1_ = 0; lvt_7_1_ < 128; ++lvt_7_1_) {

            for (int lvt_9_1_ = 0; lvt_9_1_ < lvt_7_1_ / 16; ++lvt_9_1_) {
                upPos = upPos.add(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
                if (serverWorld.getBlockState(upPos.down()).getBlock() != this || serverWorld.getBlockState(upPos).isCollisionShapeOpaque(serverWorld, upPos)) {
                    continue label48;
                }
            }

            BlockState lvt_9_2_ = serverWorld.getBlockState(upPos);
            if (lvt_9_2_.getBlock() == lvt_6_1_.getBlock() && random.nextInt(10) == 0) {
                ((IGrowable) lvt_6_1_.getBlock()).grow(serverWorld, random, upPos, lvt_9_2_);
            }

            if (lvt_9_2_.isAir()) {
                BlockState lvt_10_2_;
                if (random.nextInt(8) == 0) {
                    List<ConfiguredFeature<?, ?>> lvt_11_1_ = serverWorld.getBiome(upPos).getFlowers();
                    if (lvt_11_1_.isEmpty()) {
                        continue;
                    }

                    ConfiguredFeature<?, ?> lvt_12_1_ = ((DecoratedFeatureConfig) lvt_11_1_.get(0).config).feature;
                    lvt_10_2_ = ((FlowersFeature) lvt_12_1_.feature).getFlowerToPlace(random, upPos, lvt_12_1_.config);
                }
                else {
                    lvt_10_2_ = lvt_6_1_;
                }

                if (lvt_10_2_.isValidPosition(serverWorld, upPos)) {
                    serverWorld.setBlockState(upPos, lvt_10_2_, 3);
                }
            }
        }

    }
}
