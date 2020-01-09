package com.alan199921.astral.blocks;

import com.alan199921.astral.effects.AstralEffects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EgoMembrane extends Block {
    public EgoMembrane() {
        super(Properties.create(Material.DRAGON_EGG)
                .hardnessAndResistance(2f)
                .lightValue(14));
    }

    @Override
    public void harvestBlock(World worldIn, PlayerEntity player, @Nonnull BlockPos pos, BlockState state, @Nullable TileEntity te, @Nonnull ItemStack stack) {
        worldIn.setBlockState(pos, AstralBlocks.EGO_MEMBRANE.getDefaultState(), 2);
        player.removePotionEffect(new EffectInstance(AstralEffects.ASTRAL_TRAVEL).getPotion());
    }
}
