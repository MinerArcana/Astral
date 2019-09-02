package com.alan199921.astral.dimensions.innerrealm;

import com.alan199921.astral.blocks.AstralMeridian;
import com.alan199921.astral.blocks.ModBlocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunk;

public class InnerRealmUtils {
    public IChunk getAdjacentChunk(BlockPos blockPos, int direction, IWorld world){
        if (direction == 0){
            return world.getChunk(blockPos.add(0, 0, -16));
        }
        if (direction == 1){
            return world.getChunk(blockPos.add(-16, 0, 0));
        }
        if (direction == 2){
            return world.getChunk(blockPos.add(0, 0, 16));
        }
        if (direction == 3){
            return world.getChunk(blockPos.add(16, 0, 0));
        }
        System.out.println("Invalid direction, returning north!");
        return world.getChunk(blockPos.add(0, 0, -16));
    }

    public void generateInnerRealmChunk(IWorld iWorld, IChunk iChunk) {
        int x;
        int y;
        int z;
        //Make 14 block cubes with an Astral Meridian block in the center, except on the XZ plane

        //XZ plane
        for (x = 0; x < 16; x++) {
            for (z = 0; z < 16; z++) {
                iChunk.setBlockState(new BlockPos(x, iWorld.getSeaLevel(), z), ModBlocks.egoMembrane.getDefaultState(), false);
                iChunk.setBlockState(new BlockPos(x, iWorld.getSeaLevel() + 15, z), ModBlocks.egoMembrane.getDefaultState(), false);
            }
        }

        //XY Plane
        for (x = 0; x < 16; x++) {
            for (y = 0; y < 16; y++) {
                if (isBetweenInclusive(x, 7, 8) && isBetweenInclusive(y, 7, 8)) {
                    iChunk.setBlockState(new BlockPos(x, iWorld.getSeaLevel() + y, 0), ModBlocks.astralMeridian.getDefaultState().with(AstralMeridian.DIRECTION, 0), false);
                    iChunk.setBlockState(new BlockPos(x, iWorld.getSeaLevel() + y, 15), ModBlocks.astralMeridian.getDefaultState().with(AstralMeridian.DIRECTION, 2), false);
                } else {
                    iChunk.setBlockState(new BlockPos(x, iWorld.getSeaLevel() + y, 0), ModBlocks.egoMembrane.getDefaultState(), false);
                    iChunk.setBlockState(new BlockPos(x, iWorld.getSeaLevel() + y, 15), ModBlocks.egoMembrane.getDefaultState(), false);
                }
            }
        }

        //YZ Plane
        for (z = 0; z < 16; z++) {
            for (y = 0; y < 16; y++) {
                if (isBetweenInclusive(y, 7, 8) && isBetweenInclusive(z, 7, 8)) {
                    iChunk.setBlockState(new BlockPos(0, iWorld.getSeaLevel() + y, z), ModBlocks.astralMeridian.getDefaultState().with(AstralMeridian.DIRECTION, 1), false);
                    iChunk.setBlockState(new BlockPos(15, iWorld.getSeaLevel() + y, z), ModBlocks.astralMeridian.getDefaultState().with(AstralMeridian.DIRECTION, 3), false);
                } else {
                    iChunk.setBlockState(new BlockPos(0, iWorld.getSeaLevel() + y, z), ModBlocks.egoMembrane.getDefaultState(), false);
                    iChunk.setBlockState(new BlockPos(15, iWorld.getSeaLevel() + y, z), ModBlocks.egoMembrane.getDefaultState(), false);
                }
            }
        }
    }

    public boolean isBetweenInclusive(int compare, int a, int b) {
        return compare >= a && compare <= b;
    }

    public void destroyPlaneMeridian(World world, IChunk meridianChunk, int meridianDirection) {
        //North
        if (meridianDirection == 0){
            for (int x = 1; x < 15; x++) {
                for (int y = 1; y < 15; y++) {
                    world.destroyBlock(meridianChunk.getPos().getBlock(x, world.getSeaLevel() + y, 0), false);
                }
            }
        }

        //South
        if (meridianDirection == 2){
            for (int x = 1; x < 15; x++) {
                for (int y = 1; y < 15; y++) {
                    world.destroyBlock(meridianChunk.getPos().getBlock(x, world.getSeaLevel() + y, 15), false);
                }
            }
        }

        //East
        else if (meridianDirection == 1){
            for (int y = 1; y < 15; y++) {
                for (int z = 1; z < 15; z++) {
                    world.destroyBlock(meridianChunk.getPos().getBlock(0, world.getSeaLevel() + y, z), false);
                }
            }
        }

        //West
        else if (meridianDirection == 3){
            for (int y = 1; y < 15; y++) {
                for (int z = 1; z < 15; z++) {
                    world.destroyBlock(meridianChunk.getPos().getBlock(15, world.getSeaLevel() + y, z), false);
                }
            }
        }
    }
}