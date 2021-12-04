package com.alan19.astral.blocks.etherealblocks;

import com.alan19.astral.blocks.AstralBlocks;
import com.alan19.astral.effects.AstralEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.ParametersAreNonnullByDefault;

public class AstralMeridian extends Block implements Ethereal {

    /**
     * Direction IntegerProperty:
     * 0 = North
     * 1 = East
     * 2 = South
     * 3 = West
     * <p>
     * Each Astral Meridian have a cardinal direction associated with it when it is right clicked with a Key of
     * Enlightenment for the direction to break blocks in
     */
    public static final IntegerProperty DIRECTION = IntegerProperty.create("direction", 0, 3);

    public AstralMeridian() {
        super(Properties.of(Material.PORTAL)
                .strength(-1.0F, 3600000.0F)
                .noDrops()
                .lightLevel(value -> 14));

        this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(DIRECTION));
    }

    @Override
    @ParametersAreNonnullByDefault
    public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
        worldIn.setBlock(pos, AstralBlocks.EGO_MEMBRANE.get().defaultBlockState(), 2);
    }

    @Override
    @ParametersAreNonnullByDefault
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (handIn == InteractionHand.MAIN_HAND && player.getItemInHand(handIn).isEmpty()) {
            player.removeEffect(AstralEffects.ASTRAL_TRAVEL.get());
            return InteractionResult.SUCCESS;
        }
        return super.use(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.IGNORE;
    }
}
