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
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EgoMembrane extends Block {
    public EgoMembrane() {
        super(Properties.create(Material.PORTAL)
                .hardnessAndResistance(2f)
                .lightValue(14));
        setRegistryName("ego_membrane");
    }

    @Override
    public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
        worldIn.setBlockState(pos, ModBlocks.egoMembrane.getDefaultState(), 2);
        if (!worldIn.isRemote()){
            TeleportationTools.changeDim((ServerPlayerEntity) player, pos, player.getSpawnDimension());
        }
        super.harvestBlock(worldIn, player, pos, state, te, stack);
    }
}
