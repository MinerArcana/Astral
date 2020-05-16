package com.alan19.astral.dimensions.innerrealm;

import com.alan19.astral.blocks.AstralBlocks;
import com.alan19.astral.blocks.AstralMeridian;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunk;

public class InnerRealmUtils {
    public Chunk getAdjacentChunk(BlockPos blockPos, int direction, World world) {
        if (direction == 0) {
            return world.getChunkAt(blockPos.add(0, 0, -16));
        }
        if (direction == 1) {
            return world.getChunkAt(blockPos.add(-16, 0, 0));
        }
        if (direction == 2) {
            return world.getChunkAt(blockPos.add(0, 0, 16));
        }
        if (direction == 3) {
            return world.getChunkAt(blockPos.add(16, 0, 0));
        }
        System.out.println("Invalid direction, returning north!");
        return world.getChunkAt(blockPos.add(0, 0, -16));
    }

    public void generateInnerRealmChunk(World world, Chunk chunk) {
        int x;
        int y;
        int z;
        //Make 14 block cubes with an Astral Meridian block in the center, except on the XZ plane

        //XZ plane
        for (x = 0; x < 16; x++) {
            for (z = 0; z < 16; z++) {
                world.setBlockState(chunk.getPos().getBlock(x, world.getSeaLevel(), z), AstralBlocks.EGO_MEMBRANE.get().getDefaultState());
                world.setBlockState(chunk.getPos().getBlock(x, world.getSeaLevel() + 15, z), AstralBlocks.EGO_MEMBRANE.get().getDefaultState());
            }
        }

        //XY Plane
        for (x = 0; x < 16; x++) {
            for (y = 0; y < 16; y++) {
                if (isBetweenInclusive(x, 7, 8) && isBetweenInclusive(y, 7, 8)) {
                    world.setBlockState(chunk.getPos().getBlock(x, world.getSeaLevel() + y, 0), AstralBlocks.ASTRAL_MERIDIAN.get().getDefaultState().with(AstralMeridian.DIRECTION, 0));
                    world.setBlockState(chunk.getPos().getBlock(x, world.getSeaLevel() + y, 15), AstralBlocks.ASTRAL_MERIDIAN.get().getDefaultState().with(AstralMeridian.DIRECTION, 2));
                }
                else {
                    world.setBlockState(chunk.getPos().getBlock(x, world.getSeaLevel() + y, 0), AstralBlocks.EGO_MEMBRANE.get().getDefaultState());
                    world.setBlockState(chunk.getPos().getBlock(x, world.getSeaLevel() + y, 15), AstralBlocks.EGO_MEMBRANE.get().getDefaultState());
                }
            }
        }

        //YZ Plane
        for (z = 0; z < 16; z++) {
            for (y = 0; y < 16; y++) {
                if (isBetweenInclusive(y, 7, 8) && isBetweenInclusive(z, 7, 8)) {
                    world.setBlockState(chunk.getPos().getBlock(0, world.getSeaLevel() + y, z), AstralBlocks.ASTRAL_MERIDIAN.get().getDefaultState().with(AstralMeridian.DIRECTION, 1));
                    world.setBlockState(chunk.getPos().getBlock(15, world.getSeaLevel() + y, z), AstralBlocks.ASTRAL_MERIDIAN.get().getDefaultState().with(AstralMeridian.DIRECTION, 3));
                }
                else {
                    world.setBlockState(chunk.getPos().getBlock(0, world.getSeaLevel() + y, z), AstralBlocks.EGO_MEMBRANE.get().getDefaultState());
                    world.setBlockState(chunk.getPos().getBlock(15, world.getSeaLevel() + y, z), AstralBlocks.EGO_MEMBRANE.get().getDefaultState());
                }
            }
        }
    }

    public boolean isBetweenInclusive(int compare, int a, int b) {
        return compare >= a && compare <= b;
    }

    public void destroyWall(World world, IChunk meridianChunk, int meridianDirection) {
        //North
        if (meridianDirection == 0) {
            for (int x = 1; x < 15; x++) {
                for (int y = 1; y < 15; y++) {
                    world.destroyBlock(meridianChunk.getPos().getBlock(x, world.getSeaLevel() + y, 0), false);
                }
            }
        }

        //South
        if (meridianDirection == 2) {
            for (int x = 1; x < 15; x++) {
                for (int y = 1; y < 15; y++) {
                    world.destroyBlock(meridianChunk.getPos().getBlock(x, world.getSeaLevel() + y, 15), false);
                }
            }
        }

        //East
        else if (meridianDirection == 1) {
            for (int y = 1; y < 15; y++) {
                for (int z = 1; z < 15; z++) {
                    world.destroyBlock(meridianChunk.getPos().getBlock(0, world.getSeaLevel() + y, z), false);
                }
            }
        }

        //West
        else if (meridianDirection == 3) {
            for (int y = 1; y < 15; y++) {
                for (int z = 1; z < 15; z++) {
                    world.destroyBlock(meridianChunk.getPos().getBlock(15, world.getSeaLevel() + y, z), false);
                }
            }
        }
    }
}