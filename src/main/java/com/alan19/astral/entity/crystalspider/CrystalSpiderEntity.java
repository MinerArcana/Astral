package com.alan19.astral.entity.crystalspider;

import com.alan19.astral.effects.AstralEffects;
import com.alan19.astral.entity.AstralModifiers;
import com.alan19.astral.entity.IAstralBeing;
import com.alan19.astral.entity.projectile.CrystalWebProjectileEntity;
import com.alan19.astral.events.astraltravel.TravelEffects;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.CaveSpiderEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

public class CrystalSpiderEntity extends SpiderEntity implements IAstralBeing, IRangedAttackMob {
    private static final DataParameter<Boolean> ATTACKING = EntityDataManager.defineId(CrystalSpiderEntity.class, DataSerializers.BOOLEAN);
    private static final AttributeModifier REDUCED_GRAVITY = new AttributeModifier(UUID.fromString("67a3c1a3-489d-4885-8041-3ae39896a2c0"), "drastically reduce gravity for crystal spiders", -1.1, AttributeModifier.Operation.MULTIPLY_TOTAL);

    public CrystalSpiderEntity(EntityType<? extends SpiderEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Nonnull
    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return CaveSpiderEntity.createCaveSpider().add(AstralModifiers.ASTRAL_ATTACK_DAMAGE.get(), 2);
    }


    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
        //Do nothing when falling
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACKING, true);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, new CrystalSpiderEntity.AttackGoal(this));
        this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
        this.targetSelector.addGoal(2, new TargetGoal<>(this, PlayerEntity.class));
        this.targetSelector.addGoal(3, new TargetGoal<>(this, IronGolemEntity.class));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(7, new WebAttackGoal(this, 1.25D, 20, 10F));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomFlyingGoal(this, 1.5));
    }

    public void setAttacking(boolean attacking) {
        entityData.set(ATTACKING, attacking);
    }

    @Override
    public boolean doHurtTarget(@Nonnull Entity entityIn) {
        boolean isAttackSuccessful = IAstralBeing.attackEntityAsMobWithAstralDamage(this, entityIn);
        if (isAttackSuccessful) {
            if (entityIn instanceof LivingEntity) {
                int mindVenomDuration = 0;
                if (this.level.getDifficulty() == Difficulty.NORMAL) {
                    mindVenomDuration = 7;
                }
                else if (this.level.getDifficulty() == Difficulty.HARD) {
                    mindVenomDuration = 15;
                }

                if (mindVenomDuration > 0 && !((LivingEntity) entityIn).hasEffect(AstralEffects.MIND_VENOM.get())) {
                    ((LivingEntity) entityIn).addEffect(new EffectInstance(AstralEffects.MIND_VENOM.get(), mindVenomDuration * 20, 0));
                }
            }
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        CrystalWebProjectileEntity crystalWebProjectileEntity = new CrystalWebProjectileEntity(level, this);
        final double x = target.getX() - getX();
        final double y = target.getY(0.3333333333333333D) - crystalWebProjectileEntity.getY();
        final double z = target.getZ() - getZ();
        crystalWebProjectileEntity.shoot(x, y + MathHelper.sqrt(x * x + z * z) * 0.2F, z, .5F, 10F);
        level.playSound(null, getX(), getY(), getZ(), SoundEvents.LLAMA_SPIT, getSoundSource(), 1.0F, 2.0F + (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.2F);
        level.addFreshEntity(crystalWebProjectileEntity);
    }

    static class AttackGoal extends MeleeAttackGoal {
        public AttackGoal(SpiderEntity spider) {
            super(spider, 1.0D, true);
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        @Override
        public boolean canUse() {
            return super.canUse() && !this.mob.isVehicle();
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        @Override
        public boolean canContinueToUse() {
            float f = this.mob.getBrightness();
            if (f >= 0.5F && this.mob.getRandom().nextInt(100) == 0) {
                this.mob.setTarget(null);
                return false;
            }
            else {
                return super.canContinueToUse();
            }
        }

        @Override
        protected double getAttackReachSqr(LivingEntity attackTarget) {
            return 4.0F + attackTarget.getBbWidth();
        }
    }

    /**
     * Crystal Spiders target Astral Players and Iron Golems when it's dark
     *
     * @param <T> The entity target as a class
     */
    static class TargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
        public TargetGoal(SpiderEntity spider, Class<T> classTarget) {
            super(spider, classTarget, true);
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        @Override
        public boolean canUse() {
            float f = this.mob.getBrightness();
            return f < 0.5F && super.canUse();
        }

        @Override
        @ParametersAreNonnullByDefault
        protected boolean canAttack(@Nullable LivingEntity potentialTarget, EntityPredicate targetPredicate) {
            return potentialTarget != null && potentialTarget.hasEffect(AstralEffects.ASTRAL_TRAVEL.get());
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (getY() < 128) {
            setDeltaMovement(getDeltaMovement().x, Math.max(0, getDeltaMovement().y), getDeltaMovement().z);
        }
        final ModifiableAttributeInstance gravityAttribute = getAttribute(ForgeMod.ENTITY_GRAVITY.get());
        if (getY() < 128 && !gravityAttribute.hasModifier(REDUCED_GRAVITY)) {
            gravityAttribute.addPermanentModifier(REDUCED_GRAVITY);
        }
        else if (getY() >= 128 && gravityAttribute.hasModifier(REDUCED_GRAVITY)) {
            gravityAttribute.removeModifier(REDUCED_GRAVITY);
        }
    }

    @Override
    protected boolean canRide(@Nonnull Entity entityIn) {
        return entityIn instanceof LivingEntity && TravelEffects.isEntityAstral((LivingEntity) entityIn);
    }

    @Override
    @ParametersAreNonnullByDefault
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        final ILivingEntityData spawnData = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        getPassengers().stream().filter(entity -> entity instanceof LivingEntity).forEach(entity -> ((LivingEntity) entity).addEffect(new EffectInstance(AstralEffects.ASTRAL_TRAVEL.get(), Integer.MAX_VALUE)));
        return spawnData;
    }
}
