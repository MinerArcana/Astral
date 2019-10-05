package com.alan199921.astral.blocks;

import com.alan199921.astral.dimensions.TeleportationTools;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EgoMembrane extends Block {
    public EgoMembrane() {
        super(Properties.create(Material.DRAGON_EGG)
                .hardnessAndResistance(2f)
                .lightValue(14));
        setRegistryName("ego_membrane");
    }

    @Override
    public void harvestBlock(World worldIn, PlayerEntity player, @Nonnull BlockPos pos, BlockState state, @Nullable TileEntity te, @Nonnull ItemStack stack) {
        worldIn.setBlockState(pos, ModBlocks.egoMembrane.getDefaultState(), 2);
        if (!worldIn.isRemote()) {
            TeleportationTools.changeDim((ServerPlayerEntity) player, pos, player.getSpawnDimension());
        }
        super.harvestBlock(worldIn, player, pos, state, te, stack);
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.east()).getBlock().equals(ModBlocks.egoMembrane) || worldIn.getBlockState(pos.west()).getBlock().equals(ModBlocks.egoMembrane) || worldIn.getBlockState(pos.north()).getBlock().equals(ModBlocks.egoMembrane) || worldIn.getBlockState(pos.south()).getBlock().equals(ModBlocks.egoMembrane);
    }
}
