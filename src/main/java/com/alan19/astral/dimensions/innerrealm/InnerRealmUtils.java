package com.alan19.astral.dimensions.innerrealm;

import com.alan19.astral.blocks.AstralBlocks;
import com.alan19.astral.blocks.etherealblocks.AstralMeridian;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunk;

import java.util.stream.Stream;

public class InnerRealmUtils {
    public static Chunk getAdjacentChunk(BlockPos blockPos, int direction, World world) {
        switch (direction) {
            case 0:
                return world.getChunkAt(blockPos.add(0, 0, -16));
            case 1:
                return world.getChunkAt(blockPos.add(-16, 0, 0));
            case 2:
                return world.getChunkAt(blockPos.add(0, 0, 16));
            case 3:
                return world.getChunkAt(blockPos.add(16, 0, 0));
            default:
                throw new IllegalStateException("Unexpected value: " + direction);
        }
    }

    public static Stream<BlockPos> getFace(IChunk chunk, int direction, World world) {
        ChunkPos pos = chunk.getPos();
        int seaLevel = world.getSeaLevel();
        switch (direction) {
            case 3:
                return BlockPos.getAllInBox(pos.getBlock(15, world.getSeaLevel() + 1, 1), pos.getBlock(16, world.getSeaLevel() + 14, 14));
            case 2:
                return BlockPos.getAllInBox(pos.getBlock(1, seaLevel + 1, 15), pos.getBlock(14, seaLevel + 14, 16));
            case 1:
                return BlockPos.getAllInBox(pos.getBlock(0, seaLevel + 1, 1), pos.getBlock(-1, seaLevel + 14, 14));
            case 0:
                return BlockPos.getAllInBox(pos.getBlock(1, seaLevel + 1, 0), pos.getBlock(14, seaLevel + 14, -1));
            default:
                throw new IllegalStateException("Unexpected value: " + direction);
        }
    }

    public static void generateInnerRealmChunk(World world, Chunk chunk) {
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

    public static boolean isBetweenInclusive(int compare, int a, int b) {
        return compare >= a && compare <= b;
    }

    public static void destroyWall(World world, IChunk meridianChunk, int meridianDirection) {
        getFace(meridianChunk, meridianDirection, world).forEach(blockPos -> world.destroyBlock(blockPos, false));
    }
}