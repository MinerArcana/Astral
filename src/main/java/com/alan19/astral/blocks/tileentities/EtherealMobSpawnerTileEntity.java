package com.alan19.astral.blocks.tileentities;

import com.alan19.astral.effects.AstralEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class EtherealMobSpawnerTileEntity extends BlockEntity implements TickableBlockEntity {
    private final BaseSpawner spawnerLogic = new BaseSpawner() {

        @Override
        public void broadcastEvent(int id) {
            level.blockEvent(worldPosition, Blocks.SPAWNER, id, 0);
        }

        @Override
        public Level getLevel() {
            return level;
        }

        @Nonnull
        @Override
        public BlockPos getPos() {
            return worldPosition;
        }

        @Override
        public void setNextSpawnData(@Nonnull SpawnData spawnerEntity) {
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
                        .filter(EntitySelector.LIVING_ENTITY_STILL_ALIVE)
                        .filter(EntitySelector.NO_SPECTATORS)
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
    public void load(BlockState state, CompoundTag nbt) {
        super.load(state, nbt);
        this.spawnerLogic.load(nbt);
    }

    @Override
    @Nonnull
    public CompoundTag save(@Nonnull CompoundTag compound) {
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
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(this.worldPosition, 1, this.getUpdateTag());
    }

    @Override
    @Nonnull
    public CompoundTag getUpdateTag() {
        CompoundTag compoundnbt = this.save(new CompoundTag());
        compoundnbt.remove("SpawnPotentials");
        return compoundnbt;
    }

    @Override
    public boolean triggerEvent(int id, int type) {
        return this.spawnerLogic.onEventTriggered(id) || super.triggerEvent(id, type);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        if (level != null) {
            this.load(level.getBlockState(pkt.getPos()), pkt.getTag());
        }
    }

    @Override
    public boolean onlyOpCanSetNbt() {
        return true;
    }

    public BaseSpawner getSpawnerBaseLogic() {
        return this.spawnerLogic;
    }


}
