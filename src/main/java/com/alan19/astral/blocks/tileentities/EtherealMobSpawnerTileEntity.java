package com.alan19.astral.blocks.tileentities;

import com.alan19.astral.effects.AstralEffects;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.spawner.AbstractSpawner;

import javax.annotation.Nonnull;

public class EtherealMobSpawnerTileEntity extends MobSpawnerTileEntity {
    public EtherealMobSpawnerTileEntity() {
        super();
        super.spawnerLogic = new AbstractSpawner() {

            @Override
            public void broadcastEvent(int id) {
                world.addBlockEvent(pos, Blocks.SPAWNER, id, 0);
            }

            @Override
            public World getWorld() {
                return world;
            }

            @Nonnull
            @Override
            public BlockPos getSpawnerPosition() {
                return pos;
            }

            @Override
            public void setNextSpawnData(@Nonnull WeightedSpawnerEntity spawnerEntity) {
                super.setNextSpawnData(spawnerEntity);
                if (this.getWorld() != null) {
                    BlockState blockState = this.getWorld().getBlockState(this.getSpawnerPosition());
                    this.getWorld().notifyBlockUpdate(pos, blockState, blockState, 4);
                }
            }

            @Override
            public boolean isActivated() {
                return isAstralPlayerNearby((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, super.activatingRangeFromPlayer);
            }

            private boolean isAstralPlayerNearby(double x, double y, double z, double range) {
                if (world != null && range >= 0) {
                    return world.getPlayers().stream()
                            .filter(EntityPredicates.IS_LIVING_ALIVE)
                            .filter(EntityPredicates.NOT_SPECTATING)
                            .filter(playerEntity -> playerEntity.isPotionActive(AstralEffects.ASTRAL_TRAVEL.get()))
                            .anyMatch(playerEntity -> playerEntity.getDistanceSq(x, y, z) >= range * range);
                }
                return false;
            }
        };
    }


}
