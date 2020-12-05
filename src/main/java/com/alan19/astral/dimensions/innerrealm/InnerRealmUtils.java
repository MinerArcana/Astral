package com.alan19.astral.dimensions.innerrealm;

import com.alan19.astral.Astral;
import com.alan19.astral.blocks.AstralBlocks;
import com.alan19.astral.blocks.etherealblocks.AstralMeridian;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Range;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunk;
import org.apache.logging.log4j.Level;

import java.util.stream.Stream;

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
        Astral.LOGGER.log(Level.ERROR, "Invalid direction, returning north!");
        return world.getChunkAt(blockPos.add(0, 0, -16));
    }

    public void generateInnerRealmChunk(World world, Chunk chunk) {
        // Make 14 block cubes with an Astral Meridian block in the center, except on the XZ plane

        //XZ plane
        BlockPos.getAllInBox(chunk.getPos().asBlockPos().add(0, world.getSeaLevel(), 0), chunk.getPos().asBlockPos().add(15, world.getSeaLevel(), 15))
                .flatMap(blockPos -> Stream.of(blockPos.toImmutable(), blockPos.up(15).toImmutable()))
                .forEach(blockPos -> world.setBlockState(blockPos, AstralBlocks.EGO_MEMBRANE.get().getDefaultState()));

        //XY Plane
        BlockPos.getAllInBox(chunk.getPos().asBlockPos().add(0, world.getSeaLevel(), 0), chunk.getPos().asBlockPos().add(15, world.getSeaLevel() + 15, 0))
                .forEach(blockPos -> {
                    if (Range.closed(7, 8).containsAll(ImmutableList.of(Math.abs(blockPos.getX() % 16), Math.abs(blockPos.getY() % 16)))) {
                        world.setBlockState(blockPos.toImmutable(), AstralBlocks.ASTRAL_MERIDIAN.get().getDefaultState().with(AstralMeridian.DIRECTION, 0));
                    }
                    else {
                        world.setBlockState(blockPos.toImmutable(), AstralBlocks.EGO_MEMBRANE.get().getDefaultState());
                    }
                    BlockPos otherSide = blockPos.add(0, 0, 15).toImmutable();
                    if (Range.closed(7, 8).containsAll(ImmutableList.of(Math.abs(otherSide.getX() % 16), Math.abs(otherSide.getY() % 16)))) {
                        world.setBlockState(otherSide, AstralBlocks.ASTRAL_MERIDIAN.get().getDefaultState().with(AstralMeridian.DIRECTION, 2));
                    }
                    else {
                        world.setBlockState(otherSide, AstralBlocks.EGO_MEMBRANE.get().getDefaultState());
                    }
                });

        //YZ Plane
        BlockPos.getAllInBox(chunk.getPos().asBlockPos().add(0, world.getSeaLevel(), 0), chunk.getPos().asBlockPos().add(0, world.getSeaLevel() + 15, 15))
                .forEach(blockPos -> {
                    if (Range.closed(7, 8).containsAll(ImmutableList.of(Math.abs(blockPos.getZ() % 16), Math.abs(blockPos.getY() % 16)))) {
                        world.setBlockState(blockPos.toImmutable(), AstralBlocks.ASTRAL_MERIDIAN.get().getDefaultState().with(AstralMeridian.DIRECTION, 1));
                    }
                    else {
                        world.setBlockState(blockPos.toImmutable(), AstralBlocks.EGO_MEMBRANE.get().getDefaultState());
                    }
                    BlockPos otherSide = blockPos.add(15, 0, 0).toImmutable();
                    if (Range.closed(7, 8).containsAll(ImmutableList.of(Math.abs(otherSide.getZ() % 16), Math.abs(otherSide.getY() % 16)))) {
                        world.setBlockState(otherSide, AstralBlocks.ASTRAL_MERIDIAN.get().getDefaultState().with(AstralMeridian.DIRECTION, 3));
                    }
                    else {
                        world.setBlockState(otherSide, AstralBlocks.EGO_MEMBRANE.get().getDefaultState());
                    }
                });
    }

    public void destroyWall(World world, IChunk chunk, int meridianDirection) {
        switch (meridianDirection) {
            //North
            case 0:
                BlockPos.getAllInBox(chunk.getPos().asBlockPos().add(1, world.getSeaLevel() + 1, 0), chunk.getPos().asBlockPos().add(14, world.getSeaLevel() + 14, 0)).forEach(blockPos -> world.destroyBlock(blockPos, false));
                break;

            //South
            case 2:
                BlockPos.getAllInBox(chunk.getPos().asBlockPos().add(1, world.getSeaLevel() + 1, 15), chunk.getPos().asBlockPos().add(14, world.getSeaLevel() + 14, 15)).forEach(blockPos -> world.destroyBlock(blockPos, false));
                break;

            //East
            case 1:
                BlockPos.getAllInBox(chunk.getPos().asBlockPos().add(0, world.getSeaLevel() + 1, 0), chunk.getPos().asBlockPos().add(0, world.getSeaLevel() + 14, 14)).forEach(blockPos -> world.destroyBlock(blockPos, false));
                break;

            //West
            case 3:
                BlockPos.getAllInBox(chunk.getPos().asBlockPos().add(15, world.getSeaLevel() + 1, 0), chunk.getPos().asBlockPos().add(15, world.getSeaLevel() + 14, 14)).forEach(blockPos -> world.destroyBlock(blockPos, false));
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + meridianDirection);
        }
    }
}