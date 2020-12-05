package com.alan19.astral.world.features;

import com.alan19.astral.blocks.AstralBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class FeverweedFeature extends Feature<FeverweedFeatureConfig> {

    public FeverweedFeature() {
        super(FeverweedFeatureConfig.CODEC);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean generate(ISeedReader worldIn, ChunkGenerator generator, Random rand, BlockPos pos, FeverweedFeatureConfig config) {
        boolean generated = false;
        final int numberOfPlants = rand.nextInt(config.getMaxPatchSize() - config.getMinPatchSize()) + config.getMinPatchSize();
        if (rand.nextInt(config.getPatchChance()) == 0) {
            //Choose random spot in chunk as the center for generating Feverweed
            int centerX = pos.getX() + rand.nextInt(16);
            int centerZ = pos.getZ() + rand.nextInt(16);

            BlockState feverweed = AstralBlocks.FEVERWEED_BLOCK.get().getDefaultState();
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
                }
            }
        }
        return generated;
    }
}