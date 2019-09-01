package com.alan199921.astral.dimensions.innerrealm;

import com.alan199921.astral.blocks.AstralMeridian;
import com.alan199921.astral.blocks.ModBlocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;

public class InnerRealmUtils {
    public void generateInnerRealmChunk(IWorld iWorld, ChunkPos iChunk) {
        int x;
        int y;
        int z;
        //Make 14 block cubes with an Astral Meridian block in the center, except on the XZ plane

        //XZ plane
        for (x = 0; x < 16; x++) {
            for (z = 0; z < 16; z++) {
                iWorld.setBlockState(iChunk.getBlock(x, iWorld.getSeaLevel(), z), ModBlocks.egoMembrane.getDefaultState(), 0);
                iWorld.setBlockState(iChunk.getBlock(x, iWorld.getSeaLevel() + 15, z), ModBlocks.egoMembrane.getDefaultState(), 0);
            }
        }

        //XY Plane
        for (x = 0; x < 16; x++) {
            for (y = 0; y < 16; y++) {
                if (isBetweenInclusive(x, 7, 8) && isBetweenInclusive(y, 7, 8)) {
                    iWorld.setBlockState(iChunk.getBlock(x, iWorld.getSeaLevel() + y, 0), ModBlocks.astralMeridian.getDefaultState().with(AstralMeridian.DIRECTION, 0), 2);
                    iWorld.setBlockState(iChunk.getBlock(x, iWorld.getSeaLevel() + y, 15), ModBlocks.astralMeridian.getDefaultState().with(AstralMeridian.DIRECTION, 2), 2);
                } else {
                    iWorld.setBlockState(iChunk.getBlock(x, iWorld.getSeaLevel() + y, 0), ModBlocks.egoMembrane.getDefaultState(), 0);
                    iWorld.setBlockState(iChunk.getBlock(x, iWorld.getSeaLevel() + y, 15), ModBlocks.egoMembrane.getDefaultState(), 0);
                }
            }
        }

        //YZ Plane
        for (z = 0; z < 16; z++) {
            for (y = 0; y < 16; y++) {
                if (isBetweenInclusive(y, 7, 8) && isBetweenInclusive(z, 7, 8)) {
                    iWorld.setBlockState(iChunk.getBlock(0, iWorld.getSeaLevel() + y, z), ModBlocks.astralMeridian.getDefaultState().with(AstralMeridian.DIRECTION, 1), 2);
                    iWorld.setBlockState(iChunk.getBlock(15, iWorld.getSeaLevel() + y, z), ModBlocks.astralMeridian.getDefaultState().with(AstralMeridian.DIRECTION, 3), 2);
                } else {
                    iWorld.setBlockState(iChunk.getBlock(0, iWorld.getSeaLevel() + y, z), ModBlocks.egoMembrane.getDefaultState(), 0);
                    iWorld.setBlockState(iChunk.getBlock(15, iWorld.getSeaLevel() + y, z), ModBlocks.egoMembrane.getDefaultState(), 0);
                }
            }
        }
    }

    private boolean isBetweenInclusive(int compare, int a, int b) {
        return compare >= a && compare <= b;
    }
}