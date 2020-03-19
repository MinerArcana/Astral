package com.alan199921.astral.worldgen;

import com.alan199921.astral.blocks.AstralBlocks;
import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.function.Function;

public class FeverweedFeature extends Feature<FeverweedFeatureConfig> {

    public FeverweedFeature(Function<Dynamic<?>, ? extends FeverweedFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
    }

    @Override
    public boolean place(@Nonnull IWorld worldIn, @Nonnull ChunkGenerator<? extends GenerationSettings> generator, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull FeverweedFeatureConfig config) {
        boolean generated = false;
        final int numberOfPlants = rand.nextInt(config.getMaxPatchSize() - config.getMinPatchSize()) + config.getMinPatchSize();
        if (rand.nextInt(config.getPatchChance()) == 0) {
            //Choose random spot in chunk as the center for generating Feverweed
            int centerX = pos.getX() + rand.nextInt(16);
            int centerZ = pos.getZ() + rand.nextInt(16);
            int centerY = worldIn.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, centerX, centerZ);

            BlockState feverweed = AstralBlocks.FEVERWEED_BLOCK.getDefaultState();
            int spawned = 0;
            int dist = config.getDistribution();
            for (int tries = 0; spawned < numberOfPlants && tries < config.getMaxTries(); tries++) {
                //Attempt to spawn Feverweed in a square
                int x = centerX + rand.nextInt(dist * 2) - dist;
                int z = centerZ + rand.nextInt(dist * 2) - dist;
                int y = worldIn.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z);
                BlockPos generatingPos = new BlockPos(x, y, z);
                if (worldIn.isAirBlock(generatingPos) && feverweed.isValidPosition(worldIn, generatingPos)) {
                    worldIn.setBlockState(generatingPos, feverweed, 2);
                    generated = true;
                    spawned++;
                    System.out.println(generatingPos);
                }
            }
            System.out.println(numberOfPlants + " " + spawned);
        }
        return generated;
    }
}
