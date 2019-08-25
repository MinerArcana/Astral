package com.alan199921.astral.items;

import com.alan199921.astral.Astral;
import com.alan199921.astral.blocks.AstralMeridian;
import com.alan199921.astral.blocks.ModBlocks;
import com.alan199921.astral.dimensions.innerrealm.InnerRealmUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunk;
import org.checkerframework.checker.nullness.qual.NonNull;

public class EnlightenmentKey extends Item {
    public EnlightenmentKey() {
        super(new Item.Properties().group(Astral.setup.astralItems));
        setRegistryName("enlightenment_key");
    }

    @Override
    @NonNull
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        IChunk meridianChunk = world.getChunk(context.getPos());
        if (world.getBlockState(context.getPos()).getBlock() == ModBlocks.astralMeridian) {
            BlockState meridianBlockState = world.getBlockState(context.getPos()).getBlockState();
            int meridianDirection = meridianBlockState.get(AstralMeridian.DIRECTION);
            destroyPlaneMeridian(world, meridianChunk, meridianDirection);
            IChunk chunkToGenerateBoxIn = getAdjacentChunk(context.getPos(), meridianDirection, world);
            final InnerRealmUtils innerRealmUtils = new InnerRealmUtils();
            innerRealmUtils.generateInnerRealmSpawnChunk(world, chunkToGenerateBoxIn);
            destroyPlaneMeridian(world, chunkToGenerateBoxIn, (meridianDirection + 2) % 4);
        }
        return super.onItemUse(context);
    }

    private IChunk getAdjacentChunk(BlockPos blockPos, int direction, IWorld world){
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

    private void destroyPlaneMeridian(World world, IChunk meridianChunk, int meridianDirection) {
        //North
        if (meridianDirection == 0){
            for (int x = 1; x < 15; x++) {
                for (int y = 1; y < 15; y++) {
                    meridianChunk.setBlockState(new BlockPos(x, world.getSeaLevel() + y, 0), Blocks.AIR.getDefaultState(), false);
                }
            }
        }

        //South
        if (meridianDirection == 2){
            for (int x = 1; x < 15; x++) {
                for (int y = 1; y < 15; y++) {
                    meridianChunk.setBlockState(new BlockPos(x, world.getSeaLevel() + y, 15), Blocks.AIR.getDefaultState(), false);
                }
            }
        }

        //East
        else if (meridianDirection == 1){
            for (int y = 1; y < 15; y++) {
                for (int z = 1; z < 15; z++) {
                    meridianChunk.setBlockState(new BlockPos(0, world.getSeaLevel() + y, z), Blocks.AIR.getDefaultState(), false);
                }
            }
        }

        //West
        else if (meridianDirection == 3){
            for (int y = 1; y < 15; y++) {
                for (int z = 1; z < 15; z++) {
                    meridianChunk.setBlockState(new BlockPos(15, world.getSeaLevel() + y, z), Blocks.AIR.getDefaultState(), false);
                }
            }
        }

    }
}
