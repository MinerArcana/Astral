package com.alan19.astral.blocks;

import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.dimensions.AstralDimensions;
import com.alan19.astral.mentalconstructs.AstralMentalConstructs;
import com.alan19.astral.util.Constants;
import com.alan19.astral.util.ExperienceHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SPlaySoundEventPacket;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;

public class IndexOfKnowledge extends Block implements MentalConstructController {
    public IndexOfKnowledge() {
        super(Block.Properties.create(Material.ROCK, MaterialColor.RED).hardnessAndResistance(1.5F));
        this.setDefaultState(getStateContainer().getBaseState().with(Constants.TRACKED_CONSTRUCT, false).with(Constants.LIBRARY_LEVEL, 0));
    }

    @Override
    public ActionResultType onBlockActivated(@Nonnull BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand handIn, @Nonnull BlockRayTraceResult hit) {
        if (worldIn instanceof ServerWorld && worldIn.getDimension().getType() == DimensionType.byName(AstralDimensions.INNER_REALM) && handIn == Hand.MAIN_HAND) {
            final int libraryLevel = state.get(Constants.LIBRARY_LEVEL);
            int levelRequirement = (libraryLevel + 1) * 10;
            if (player.experienceLevel >= levelRequirement && calculateLevel(worldIn, pos) > libraryLevel) {
                ExperienceHelper.drainPlayerXP(player, ExperienceHelper.getExperienceForLevel(levelRequirement));
                AstralAPI.getConstructTracker((ServerWorld) worldIn).ifPresent(tracker -> tracker.getMentalConstructsForPlayer(player).modifyConstructInfo(pos, (ServerWorld) worldIn, AstralMentalConstructs.LIBRARY.get(), libraryLevel + 1));
                worldIn.setBlockState(pos, state.with(Constants.TRACKED_CONSTRUCT, true));
                worldIn.setBlockState(pos, state.with(Constants.LIBRARY_LEVEL, libraryLevel + 1));
                if (player instanceof ServerPlayerEntity) {
                    ((ServerPlayerEntity) player).connection.sendPacket(new SPlaySoundEventPacket());
                }
                worldIn.playSound(null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, worldIn.getRandom().nextFloat() * 0.1F + 0.9F);
                return ActionResultType.SUCCESS;
            }
        }
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder.add(Constants.TRACKED_CONSTRUCT).add(Constants.LIBRARY_LEVEL));
    }

    @Override
    public int calculateLevel(World world, BlockPos pos) {
        return 5;
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        MentalConstructController.onReplaced(worldIn, pos, this, AstralMentalConstructs.LIBRARY.get());
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }
}
