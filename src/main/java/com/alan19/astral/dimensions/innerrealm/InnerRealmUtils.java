package com.alan19.astral.dimensions.innerrealm;

import com.alan19.astral.blocks.AstralBlocks;
import com.alan19.astral.blocks.etherealblocks.AstralMeridian;
import com.alan19.astral.dimensions.AstralDimensions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Range;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;

import java.util.stream.Stream;

public class InnerRealmUtils {
    private InnerRealmUtils() {

    }

    public static Chunk getAdjacentChunk(BlockPos blockPos, int direction, World world) {
        switch (direction) {
            case 0:
                return world.getChunkAt(blockPos.add(0, 0, -16));
            case 1:
                return world.getChunkAt(blockPos.add(16, 0, 0));
            case 2:
                return world.getChunkAt(blockPos.add(0, 0, 16));
            case 3:
                return world.getChunkAt(blockPos.add(-16, 0, 0));
            default:
                throw new IllegalStateException("Unexpected value: " + direction);
        }
    }

    /**
     * Converts a coordinate to a coordinate within the chunk
     * We use this because the x and z coordinates in the negative direction needs to be offset by one since it starts at -1 in a chunk and goes down while regular coordinates start from 0 and goes up, which messes up the % operation
     *
     * @param coordinate The x or z coordinate of a blockPos in one axis
     * @return The number of blocks a coordinate is past the origin of the chunk it's in
     */
    private static int convertNegativeCoordinate(int coordinate) {
        final int convertedCoordinate = coordinate < 0 ? Math.abs(coordinate) - 1 : coordinate;
        return convertedCoordinate % 16;
    }

    // Make 14 block cubes with an Astral Meridian block in the center, except on the XZ plane
    public static void generateInnerRealmChunk(ServerWorld world, ChunkPos chunk) {
        ServerWorld innerRealm = getInnerRealm(world);
        //XZ plane
        BlockPos.getAllInBox(chunk.asBlockPos().add(0, innerRealm.getSeaLevel(), 0), chunk.asBlockPos().add(15, innerRealm.getSeaLevel(), 15))
                .flatMap(blockPos -> Stream.of(blockPos.toImmutable(), blockPos.up(15).toImmutable()))
                .forEach(blockPos -> innerRealm.setBlockState(blockPos, AstralBlocks.EGO_MEMBRANE.get().getDefaultState()));

        //XY Plane
        BlockPos.getAllInBox(chunk.asBlockPos().add(0, innerRealm.getSeaLevel(), 0), chunk.asBlockPos().add(15, innerRealm.getSeaLevel() + 15, 0))
                .forEach(blockPos -> {
                    if (Range.closed(7, 8).containsAll(ImmutableList.of(convertNegativeCoordinate(blockPos.getX()), convertNegativeCoordinate(blockPos.getY())))) {
                        innerRealm.setBlockState(blockPos.toImmutable(), AstralBlocks.ASTRAL_MERIDIAN.get().getDefaultState().with(AstralMeridian.DIRECTION, 0));
                    }
                    else {
                        innerRealm.setBlockState(blockPos.toImmutable(), AstralBlocks.EGO_MEMBRANE.get().getDefaultState());
                    }
                    BlockPos otherSide = blockPos.add(0, 0, 15).toImmutable();
                    if (Range.closed(7, 8).containsAll(ImmutableList.of(convertNegativeCoordinate(otherSide.getX()), convertNegativeCoordinate(otherSide.getY())))) {
                        innerRealm.setBlockState(otherSide, AstralBlocks.ASTRAL_MERIDIAN.get().getDefaultState().with(AstralMeridian.DIRECTION, 2));
                    }
                    else {
                        innerRealm.setBlockState(otherSide, AstralBlocks.EGO_MEMBRANE.get().getDefaultState());
                    }
                });

        //YZ Plane
        BlockPos.getAllInBox(chunk.asBlockPos().add(0, innerRealm.getSeaLevel(), 0), chunk.asBlockPos().add(0, innerRealm.getSeaLevel() + 15, 15))
                .forEach(blockPos -> {
                    if (Range.closed(7, 8).containsAll(ImmutableList.of(convertNegativeCoordinate(blockPos.getY()), convertNegativeCoordinate(blockPos.getZ())))) {
                        innerRealm.setBlockState(blockPos.toImmutable(), AstralBlocks.ASTRAL_MERIDIAN.get().getDefaultState().with(AstralMeridian.DIRECTION, 3));
                    }
                    else {
                        innerRealm.setBlockState(blockPos.toImmutable(), AstralBlocks.EGO_MEMBRANE.get().getDefaultState());
                    }
                    BlockPos otherSide = blockPos.add(15, 0, 0).toImmutable();
                    if (Range.closed(7, 8).containsAll(ImmutableList.of(convertNegativeCoordinate(otherSide.getY()), convertNegativeCoordinate(otherSide.getZ())))) {
                        innerRealm.setBlockState(otherSide, AstralBlocks.ASTRAL_MERIDIAN.get().getDefaultState().with(AstralMeridian.DIRECTION, 1));
                    }
                    else {
                        innerRealm.setBlockState(otherSide, AstralBlocks.EGO_MEMBRANE.get().getDefaultState());
                    }
                });
    }

    public static void destroyWall(ServerWorld world, ChunkPos chunk, int meridianDirection) {
        ServerWorld innerRealm = getInnerRealm(world);
        switch (meridianDirection) {
            //North
            case 0:
                BlockPos.getAllInBox(chunk.asBlockPos().add(1, world.getSeaLevel() + 1, 0), chunk.asBlockPos().add(14, world.getSeaLevel() + 14, 0)).forEach(blockPos -> innerRealm.destroyBlock(blockPos, false));
                break;

            //South
            case 2:
                BlockPos.getAllInBox(chunk.asBlockPos().add(1, world.getSeaLevel() + 1, 15), chunk.asBlockPos().add(14, world.getSeaLevel() + 14, 15)).forEach(blockPos -> innerRealm.destroyBlock(blockPos, false));
                break;

            //East
            case 1:
                BlockPos.getAllInBox(chunk.asBlockPos().add(15, world.getSeaLevel() + 1, 1), chunk.asBlockPos().add(15, world.getSeaLevel() + 14, 14)).forEach(blockPos -> innerRealm.destroyBlock(blockPos, false));
                break;

            //West
            case 3:
                BlockPos.getAllInBox(chunk.asBlockPos().add(0, world.getSeaLevel() + 1, 1), chunk.asBlockPos().add(0, world.getSeaLevel() + 14, 14)).forEach(blockPos -> innerRealm.destroyBlock(blockPos, false));
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + meridianDirection);
        }
    }

    private static ServerWorld getInnerRealm(ServerWorld world) {
        return world.getServer().getWorld(AstralDimensions.INNER_REALM);
    }
}