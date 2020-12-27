package com.alan19.astral.blocks.etherealblocks;

import com.alan19.astral.blocks.tileentities.EtherealMobSpawnerTileEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.SpawnerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nonnull;

public class EtherealSpawner extends SpawnerBlock implements Ethereal {
    public EtherealSpawner() {
        super(AbstractBlock.Properties.create(Material.ROCK).setRequiresTool().hardnessAndResistance(5.0F).sound(SoundType.METAL).notSolid());
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull IBlockReader worldIn) {
        return new EtherealMobSpawnerTileEntity();
    }
}
