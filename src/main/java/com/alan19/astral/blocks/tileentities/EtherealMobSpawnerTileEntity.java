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
                        .anyMatch(playerEntity -> playerEntity.getDistanceSq(x, y, z) <= range * range);
            }
            return false;
        }
    };

    public EtherealMobSpawnerTileEntity() {
        super(AstralTiles.ETHEREAL_MOB_SPAWNER.get());
    }

    @Override
    @ParametersAreNonnullByDefault
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        this.spawnerLogic.read(nbt);
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        super.write(compound);
        this.spawnerLogic.write(compound);
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
        return new SUpdateTileEntityPacket(this.pos, 1, this.getUpdateTag());
    }

    @Override
    @Nonnull
    public CompoundNBT getUpdateTag() {
        CompoundNBT compoundnbt = this.write(new CompoundNBT());
        compoundnbt.remove("SpawnPotentials");
        return compoundnbt;
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        return this.spawnerLogic.setDelayToMin(id) || super.receiveClientEvent(id, type);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        if (world != null) {
            this.read(world.getBlockState(pkt.getPos()), pkt.getNbtCompound());
        }
    }

    @Override
    public boolean onlyOpsCanSetNbt() {
        return true;
    }

    public AbstractSpawner getSpawnerBaseLogic() {
        return this.spawnerLogic;
    }


}
