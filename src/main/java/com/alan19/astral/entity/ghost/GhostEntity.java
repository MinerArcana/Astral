package com.alan19.astral.entity.ghost;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class GhostEntity extends MonsterEntity {
    private static final DataParameter<Boolean> HAS_POSSESSED_ID = EntityDataManager.createKey(GhostEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> POSSESSED_TIMER_ID = EntityDataManager.createKey(GhostEntity.class, DataSerializers.VARINT);

    public GhostEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Nonnull
    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return func_233666_p_().createMutableAttribute(Attributes.ATTACK_DAMAGE, 0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, .5F, false) {
            @Override
            public boolean shouldExecute() {
                return super.shouldExecute() && !getHasPossessed();
            }
        });
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, .5D));
        this.goalSelector.addGoal(3, new SwimGoal(this));
        this.goalSelector.addGoal(0, new MoveTowardsTargetGoal(this, .5D, 32) {
            @Override
            public boolean shouldExecute() {
                return super.shouldExecute() && getHasPossessed();
            }
        });
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<PlayerEntity>(this, PlayerEntity.class, true) {
            @Override
            public boolean shouldExecute() {
                return super.shouldExecute() && !getHasPossessed();
            }
        });

    }

    @Override
    protected void registerData() {
        super.registerData();
        dataManager.register(HAS_POSSESSED_ID, false);
        dataManager.register(POSSESSED_TIMER_ID, 0);
    }

    public int getPossessedTimer() {
        return dataManager.get(POSSESSED_TIMER_ID);
    }

    private void setPossessedTimer(int possessedTimer) {
        dataManager.set(POSSESSED_TIMER_ID, possessedTimer);
    }

    public boolean getHasPossessed() {
        return dataManager.get(HAS_POSSESSED_ID);
    }

    private void setHasPossessed(boolean hasPossessed) {
        dataManager.set(HAS_POSSESSED_ID, hasPossessed);
    }

    private void incrementPossessedTimer() {
        setPossessedTimer(getPossessedTimer() + 1);
    }

    @Override
    public void livingTick() {
        super.livingTick();
        if (getHasPossessed()) {
            incrementPossessedTimer();
        }
        if (getPossessedTimer() > 100) {
            onKillCommand();
        }
    }

    @Override
    public boolean attackEntityAsMob(@Nonnull Entity entityIn) {
        if (entityIn instanceof PlayerEntity && !entityIn.isRidingSameEntity(this)) {
            entityIn.startRiding(this, true);
            setHasPossessed(true);
            return true;
        }
        return false;
    }

    @Override
    public void writeAdditional(@Nonnull CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("hasPossessed", getHasPossessed());
        compound.putInt("possessedTimer", getPossessedTimer());
    }

    @Override
    public void readAdditional(@Nonnull CompoundNBT compound) {
        super.readAdditional(compound);
        this.setHasPossessed(compound.getBoolean("hasPossessed"));
        setPossessedTimer(compound.getInt("possessedTimer"));
    }

}
