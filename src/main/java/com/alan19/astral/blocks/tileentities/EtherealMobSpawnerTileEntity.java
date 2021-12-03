package com.alan19.astral.blocks.tileentities;

import com.alan19.astral.effects.AstralEffects;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.spawner.AbstractSpawner;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class EtherealMobSpawnerTileEntity extends TileEntity implements ITickableTileEntity {
    private final AbstractSpawner spawnerLogic = new AbstractSpawner() {

        @Override
        public void broadcastEvent(int id) {
            level.blockEvent(worldPosition, Blocks.SPAWNER, id, 0);
        }

        @Override
        public World getLevel() {
            return level;
        }

        @Nonnull
        @Override
        public BlockPos getPos() {
            return worldPosition;
        }

        @Override
        public void setNextSpawnData(@Nonnull WeightedSpawnerEntity spawnerEntity) {
            super.setNextSpawnData(spawnerEntity);
            if (this.getLevel() != null) {
                BlockState blockState = this.getLevel().getBlockState(this.getPos());
                this.getLevel().sendBlockUpdated(worldPosition, blockState, blockState, 4);
            }
        }

        @Override
        public boolean isNearPlayer() {
            return isAstralPlayerNearby((double) worldPosition.getX() + 0.5D, (double) worldPosition.getY() + 0.5D, (double) worldPosition.getZ() + 0.5D, super.requiredPlayerRange);
        }

        private boolean isAstralPlayerNearby(double x, double y, double z, double range) {
            if (level != null && range >= 0) {
                return level.players().stream()
                        .filter(EntityPredicates.LIVING_ENTITY_STILL_ALIVE)
                        .filter(EntityPredicates.NO_SPECTATORS)
                        .filter(playerEntity -> playerEntity.hasEffect(AstralEffects.ASTRAL_TRAVEL.get()))
                        .anyMatch(playerEntity -> playerEntity.distanceToSqr(x, y, z) <= range * range);
            }
            return false;
        }
    };

    public EtherealMobSpawnerTileEntity() {
        super(AstralTiles.ETHEREAL_MOB_SPAWNER.get());
    }

    @Override
    @ParametersAreNonnullByDefault
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        this.spawnerLogic.load(nbt);
    }

    @Override
    @Nonnull
    public CompoundNBT save(@Nonnull CompoundNBT compound) {
        super.save(compound);
        this.spawnerLogic.save(compound);
        return compound;
    }

    @Override
    public void tick() {
        this.spawnerLogic.tick();
    }

    /**
     * Retrieves packet to send to the client whenever this Tile Entity is resynced via World.notifyBlockUpdate. For
     * modded TE's, this packet comes back to you clientside in {@link #onDataPacket}
     */
    @Override
    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, 1, this.getUpdateTag());
    }

    @Override
    @Nonnull
    public CompoundNBT getUpdateTag() {
        CompoundNBT compoundnbt = this.save(new CompoundNBT());
        compoundnbt.remove("SpawnPotentials");
        return compoundnbt;
    }

    @Override
    public boolean triggerEvent(int id, int type) {
        return this.spawnerLogic.onEventTriggered(id) || super.triggerEvent(id, type);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        if (level != null) {
            this.load(level.getBlockState(pkt.getPos()), pkt.getTag());
        }
    }

    @Override
    public boolean onlyOpCanSetNbt() {
        return true;
    }

    public AbstractSpawner getSpawnerBaseLogic() {
        return this.spawnerLogic;
    }


}
