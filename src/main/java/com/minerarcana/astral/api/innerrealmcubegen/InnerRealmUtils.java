package com.minerarcana.astral.api.innerrealmcubegen;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Range;
import com.minerarcana.astral.blocks.AstralBlocks;
import com.minerarcana.astral.blocks.AstralMeridian;
import com.minerarcana.astral.world.feature.dimensions.AstralDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.stream.Stream;

public final class InnerRealmUtils {
    private InnerRealmUtils() {
        throw new UnsupportedOperationException();
    }

    public static LevelChunk getAdjacentChunk(BlockPos blockPos, int direction, Level world) {
        return switch (direction) {
            case 0 -> world.getChunkAt(blockPos.north(16));
            case 1 -> world.getChunkAt(blockPos.east(16));
            case 2 -> world.getChunkAt(blockPos.south(16));
            case 3 -> world.getChunkAt(blockPos.west(16));
            default -> throw new IllegalStateException("Unexpected value: " + direction);
        };
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
    public static void generateInnerRealmChunk(Level world, ChunkPos chunk) {
        Level innerRealm = getInnerRealm(world);
        //XZ plane
        BlockPos.betweenClosedStream(chunk.getWorldPosition().offset(0, innerRealm.getSeaLevel(), 0), chunk.getWorldPosition().offset(15, innerRealm.getSeaLevel(), 15))
                .flatMap(blockPos -> Stream.of(blockPos.immutable(), blockPos.above(15).immutable()))
                .forEach(blockPos -> innerRealm.setBlockAndUpdate(blockPos, AstralBlocks.EGO_MEMBRANE.get().defaultBlockState()));

        //XY Plane
        BlockPos.betweenClosedStream(chunk.getWorldPosition().offset(0, innerRealm.getSeaLevel(), 0), chunk.getWorldPosition().offset(15, innerRealm.getSeaLevel() + 15, 0))
                .forEach(blockPos -> {
                    if (Range.closed(7, 8).containsAll(ImmutableList.of(convertNegativeCoordinate(blockPos.getX()), convertNegativeCoordinate(blockPos.getY())))) {
                        innerRealm.setBlockAndUpdate(blockPos.immutable(), AstralBlocks.ASTRAL_MERIDIAN.get().defaultBlockState().setValue(AstralMeridian.DIRECTION, 0));
                    } else {
                        innerRealm.setBlockAndUpdate(blockPos.immutable(), AstralBlocks.EGO_MEMBRANE.get().defaultBlockState());
                    }
                    BlockPos otherSide = blockPos.offset(0, 0, 15).immutable();
                    if (Range.closed(7, 8).containsAll(ImmutableList.of(convertNegativeCoordinate(otherSide.getX()), convertNegativeCoordinate(otherSide.getY())))) {
                        innerRealm.setBlockAndUpdate(otherSide, AstralBlocks.ASTRAL_MERIDIAN.get().defaultBlockState().setValue(AstralMeridian.DIRECTION, 2));
                    } else {
                        innerRealm.setBlockAndUpdate(otherSide, AstralBlocks.EGO_MEMBRANE.get().defaultBlockState());
                    }
                });

        //YZ Plane
        BlockPos.betweenClosedStream(chunk.getWorldPosition().offset(0, innerRealm.getSeaLevel(), 0), chunk.getWorldPosition().offset(0, innerRealm.getSeaLevel() + 15, 15))
                .forEach(blockPos -> {
                    if (Range.closed(7, 8).containsAll(ImmutableList.of(convertNegativeCoordinate(blockPos.getY()), convertNegativeCoordinate(blockPos.getZ())))) {
                        innerRealm.setBlockAndUpdate(blockPos.immutable(), AstralBlocks.ASTRAL_MERIDIAN.get().defaultBlockState().setValue(AstralMeridian.DIRECTION, 3));
                    } else {
                        innerRealm.setBlockAndUpdate(blockPos.immutable(), AstralBlocks.EGO_MEMBRANE.get().defaultBlockState());
                    }
                    BlockPos otherSide = blockPos.offset(15, 0, 0).immutable();
                    if (Range.closed(7, 8).containsAll(ImmutableList.of(convertNegativeCoordinate(otherSide.getY()), convertNegativeCoordinate(otherSide.getZ())))) {
                        innerRealm.setBlockAndUpdate(otherSide, AstralBlocks.ASTRAL_MERIDIAN.get().defaultBlockState().setValue(AstralMeridian.DIRECTION, 1));
                    } else {
                        innerRealm.setBlockAndUpdate(otherSide, AstralBlocks.EGO_MEMBRANE.get().defaultBlockState());
                    }
                });
    }

    public static void destroyWall(Level world, ChunkPos chunk, int meridianDirection) {
        Level innerRealm = getInnerRealm(world);
        switch (meridianDirection) {
            //North
            case 0 ->
                    BlockPos.betweenClosed(chunk.getWorldPosition().offset(1, world.getSeaLevel() + 1, 0), chunk.getWorldPosition().offset(14, world.getSeaLevel() + 14, 0)).forEach(blockPos -> innerRealm.destroyBlock(blockPos, false));


            //South
            case 2 ->
                    BlockPos.betweenClosed(chunk.getWorldPosition().offset(1, world.getSeaLevel() + 1, 15), chunk.getWorldPosition().offset(14, world.getSeaLevel() + 14, 15)).forEach(blockPos -> innerRealm.destroyBlock(blockPos, false));


            //East
            case 1 ->
                    BlockPos.betweenClosed(chunk.getWorldPosition().offset(15, world.getSeaLevel() + 1, 1), chunk.getWorldPosition().offset(15, world.getSeaLevel() + 14, 14)).forEach(blockPos -> innerRealm.destroyBlock(blockPos, false));


            //West
            case 3 ->
                    BlockPos.betweenClosed(chunk.getWorldPosition().offset(0, world.getSeaLevel() + 1, 1), chunk.getWorldPosition().offset(0, world.getSeaLevel() + 14, 14)).forEach(blockPos -> innerRealm.destroyBlock(blockPos, false));
            default -> throw new IllegalStateException("Unexpected value: " + meridianDirection);
        }
    }

    private static ServerLevel getInnerRealm(Level world) {
        return world.getServer().getLevel(AstralDimensions.INNER_REALM);
    }
}