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
    private static final DataParameter<Boolean> ATTACKING = EntityDataManager.createKey(CrystalSpiderEntity.class, DataSerializers.BOOLEAN);
    private static final AttributeModifier REDUCED_GRAVITY = new AttributeModifier(UUID.fromString("67a3c1a3-489d-4885-8041-3ae39896a2c0"), "drastically reduce gravity for crystal spiders", -1.1, AttributeModifier.Operation.MULTIPLY_TOTAL);

    public CrystalSpiderEntity(EntityType<? extends SpiderEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Nonnull
    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return CaveSpiderEntity.registerAttributes().createMutableAttribute(AstralModifiers.ASTRAL_ATTACK_DAMAGE.get(), 2);
    }


    @Override
    public boolean onLivingFall(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
        //Do nothing when falling
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(ATTACKING, true);
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
        dataManager.set(ATTACKING, attacking);
    }

    @Override
    public boolean attackEntityAsMob(@Nonnull Entity entityIn) {
        boolean isAttackSuccessful = IAstralBeing.attackEntityAsMobWithAstralDamage(this, entityIn);
        if (isAttackSuccessful) {
            if (entityIn instanceof LivingEntity) {
                int mindVenomDuration = 0;
                if (this.world.getDifficulty() == Difficulty.NORMAL) {
                    mindVenomDuration = 7;
                }
                else if (this.world.getDifficulty() == Difficulty.HARD) {
                    mindVenomDuration = 15;
                }

                if (mindVenomDuration > 0 && !((LivingEntity) entityIn).isPotionActive(AstralEffects.MIND_VENOM.get())) {
                    ((LivingEntity) entityIn).addPotionEffect(new EffectInstance(AstralEffects.MIND_VENOM.get(), mindVenomDuration * 20, 0));
                }
            }
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor) {
        CrystalWebProjectileEntity crystalWebProjectileEntity = new CrystalWebProjectileEntity(world, this);
        final double x = target.getPosX() - getPosX();
        final double y = target.getPosYHeight(0.3333333333333333D) - crystalWebProjectileEntity.getPosY();
        final double z = target.getPosZ() - getPosZ();
        crystalWebProjectileEntity.shoot(x, y + MathHelper.sqrt(x * x + z * z) * 0.2F, z, .5F, 10F);
        world.playSound(null, getPosX(), getPosY(), getPosZ(), SoundEvents.ENTITY_LLAMA_SPIT, getSoundCategory(), 1.0F, 2.0F + (world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.2F);
        world.addEntity(crystalWebProjectileEntity);
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
        public boolean shouldExecute() {
            return super.shouldExecute() && !this.attacker.isBeingRidden();
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        @Override
        public boolean shouldContinueExecuting() {
            float f = this.attacker.getBrightness();
            if (f >= 0.5F && this.attacker.getRNG().nextInt(100) == 0) {
                this.attacker.setAttackTarget(null);
                return false;
            }
            else {
                return super.shouldContinueExecuting();
            }
        }

        @Override
        protected double getAttackReachSqr(LivingEntity attackTarget) {
            return 4.0F + attackTarget.getWidth();
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
        public boolean shouldExecute() {
            float f = this.goalOwner.getBrightness();
            return f < 0.5F && super.shouldExecute();
        }

        @Override
        @ParametersAreNonnullByDefault
        protected boolean isSuitableTarget(@Nullable LivingEntity potentialTarget, EntityPredicate targetPredicate) {
            return potentialTarget != null && potentialTarget.isPotionActive(AstralEffects.ASTRAL_TRAVEL.get());
        }
    }

    @Override
    public void livingTick() {
        super.livingTick();
        if (getPosY() < 128) {
            setMotion(getMotion().x, Math.max(0, getMotion().y), getMotion().z);
        }
        final ModifiableAttributeInstance gravityAttribute = getAttribute(ForgeMod.ENTITY_GRAVITY.get());
        if (getPosY() < 128 && !gravityAttribute.hasModifier(REDUCED_GRAVITY)) {
            gravityAttribute.applyPersistentModifier(REDUCED_GRAVITY);
        }
        else if (getPosY() >= 128 && gravityAttribute.hasModifier(REDUCED_GRAVITY)) {
            gravityAttribute.removeModifier(REDUCED_GRAVITY);
        }
    }

    @Override
    protected boolean canBeRidden(@Nonnull Entity entityIn) {
        return entityIn instanceof LivingEntity && TravelEffects.isEntityAstral((LivingEntity) entityIn);
    }

    @Override
    @ParametersAreNonnullByDefault
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        final ILivingEntityData spawnData = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        getPassengers().stream().filter(entity -> entity instanceof LivingEntity).forEach(entity -> ((LivingEntity) entity).addPotionEffect(new EffectInstance(AstralEffects.ASTRAL_TRAVEL.get(), Integer.MAX_VALUE)));
        return spawnData;
    }
}
