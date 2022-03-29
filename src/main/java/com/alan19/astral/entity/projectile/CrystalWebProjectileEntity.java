package com.alan19.astral.entity.projectile;

import com.alan19.astral.blocks.AstralBlocks;
import com.alan19.astral.blocks.etherealblocks.CrystalWeb;
import com.alan19.astral.effects.AstralEffects;
import com.alan19.astral.entity.AstralEntities;
import com.alan19.astral.entity.IAstralBeing;
import com.alan19.astral.entity.crystalspider.CrystalSpiderEntity;
import com.alan19.astral.events.astraltravel.TravelEffects;
import com.alan19.astral.items.AstralItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import java.util.UUID;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class CrystalWebProjectileEntity extends ThrowableProjectile implements ItemSupplier, IAstralBeing {

    public static final String OWNER_UUID = "OwnerUUID";
    public static final String OWNER = "Owner";
    private CrystalSpiderEntity projectileOwner;
    private CompoundTag ownerNbt;

    public CrystalWebProjectileEntity(EntityType<? extends CrystalWebProjectileEntity> entityType, Level worldIn) {
        super(entityType, worldIn);
    }

    public CrystalWebProjectileEntity(Level worldIn, CrystalSpiderEntity entity) {
        this(AstralEntities.CRYSTAL_WEB_PROJECTILE_ENTITY.get(), worldIn);
        this.projectileOwner = entity;
        this.setPos(entity.getX() - (double) (entity.getBbWidth() + 1.0F) * 0.5D * (double) Mth.sin(entity.yBodyRot * ((float) Math.PI / 180F)), entity.getEyeY() - (double) 0.1F, entity.getZ() + (double) (entity.getBbWidth() + 1.0F) * 0.5D * (double) Mth.cos(entity.yBodyRot * ((float) Math.PI / 180F)));
    }

    @OnlyIn(Dist.CLIENT)
    public CrystalWebProjectileEntity(Level worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        this(AstralEntities.CRYSTAL_WEB_PROJECTILE_ENTITY.get(), worldIn);
        this.setPos(x, y, z);

        for (int i = 0; i < 7; ++i) {
            double d0 = 0.4D + 0.1D * (double) i;
            worldIn.addParticle(ParticleTypes.SPIT, x, y, z, xSpeed * d0, ySpeed, zSpeed * d0);
        }

        this.setDeltaMovement(xSpeed, ySpeed, zSpeed);
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void tick() {
        super.tick();
        if (this.ownerNbt != null) {
            this.restoreOwnerFromSave();
        }

        Vec3 vec3d = this.getDeltaMovement();
        HitResult raytraceresult = ProjectileUtil.getHitResult(this, this::canHitEntity);
        if (raytraceresult.getType() != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
            this.onHit(raytraceresult);
        }

        double d0 = this.getX() + vec3d.x;
        double d1 = this.getY() + vec3d.y;
        double d2 = this.getZ() + vec3d.z;
        this.updateRotation();
        double f = vec3d.horizontalDistance();
        setYRot((float) (Mth.atan2(vec3d.x, vec3d.z) * (double) (180F / (float) Math.PI)));

        setXRot((float) (Mth.atan2(vec3d.y, f) * (double) (180F / (float) Math.PI)));

        while (this.getXRot() - this.xRotO < -180.0F) {
            this.xRotO -= 360.0F;
        }

        while (this.getXRot() - this.xRotO >= 180.0F) {
            this.xRotO += 360.0F;
        }

        while (this.getYRot() - this.yRotO < -180.0F) {
            this.yRotO -= 360.0F;
        }

        while (this.getYRot() - this.yRotO >= 180.0F) {
            this.yRotO += 360.0F;
        }

        setXRot(Mth.lerp(0.2F, this.xRotO, this.getXRot()));
        setYRot(Mth.lerp(0.2F, this.yRotO, this.getYRot()));
        if (this.level.getBlockStates(this.getBoundingBox()).noneMatch(BlockBehaviour.BlockStateBase::isAir)) {
            this.remove(RemovalReason.DISCARDED);
        }
        else if (this.isInWaterOrBubble()) {
            this.remove(RemovalReason.DISCARDED);
        }
        else {
            this.setDeltaMovement(vec3d.scale(0.99F));
            if (!this.isNoGravity()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.06F, 0.0D));
            }

            this.setPos(d0, d1, d2);
        }
    }

    /**
     * Updates the entity motion clientside, called by packets from the server
     */
    @Override
    @OnlyIn(Dist.CLIENT)
    public void lerpMotion(double x, double y, double z) {
        this.setDeltaMovement(x, y, z);
        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            float f = Mth.sqrt((float) (x * x + z * z));
            setXRot((float) (Mth.atan2(y, f) * (double) (180F / (float) Math.PI)));
            setYRot((float) (Mth.atan2(x, z) * (double) (180F / (float) Math.PI)));
            this.xRotO = this.getXRot();
            this.yRotO = this.getYRot();
            this.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
        }

    }

    /**
     * Similar to setArrowHeading, it's point the throwable entity to a x, y, z direction.
     */
    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vec3 vec3d = (new Vec3(x, y, z)).normalize().add(this.random.nextGaussian() * 0.0075F * inaccuracy, this.random.nextGaussian() * 0.0075F * inaccuracy, this.random.nextGaussian() * 0.0075F * inaccuracy).scale(velocity);
        this.setDeltaMovement(vec3d);
        double f = vec3d.horizontalDistance();
        setYRot((float) (Mth.atan2(vec3d.x, z) * (180F / (float) Math.PI)));
        setYRot((float) (Mth.atan2(vec3d.y, f) * (180F / (float) Math.PI)));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    public void onHit(HitResult result) {
        HitResult.Type resultType = result.getType();
        if (resultType == HitResult.Type.ENTITY && projectileOwner != null) {
            Entity entity = ((EntityHitResult) result).getEntity();
            if (entity instanceof LivingEntity && TravelEffects.isEntityAstral((LivingEntity) entity)) {
                entity.hurt(DamageSource.indirectMagic(this, this.projectileOwner), 2F);
            }
            if ((level.getDifficulty() == Difficulty.NORMAL || level.getDifficulty() == Difficulty.HARD) && entity instanceof LivingEntity) {
                ((LivingEntity) entity).addEffect(new MobEffectInstance(AstralEffects.MIND_VENOM.get(), 100));
            }
            this.doEnchantDamageEffects(this.projectileOwner, entity);
            this.remove(RemovalReason.DISCARDED);
        }
        else if (resultType == HitResult.Type.BLOCK && !this.level.isClientSide) {
            this.remove(RemovalReason.DISCARDED);
        }

    }

    protected void defineSynchedData() {
        //Do not register any data
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains(OWNER, 10)) {
            this.ownerNbt = compound.getCompound(OWNER);
        }

    }

    @Override
    protected void addAdditionalSaveData(@Nonnull CompoundTag compound) {
        if (this.projectileOwner != null) {
            CompoundTag compoundNBT = new CompoundTag();
            UUID uuid = this.projectileOwner.getUUID();
            compoundNBT.putUUID(OWNER_UUID, uuid);
            compound.put(OWNER, compoundNBT);
        }

    }

    private void restoreOwnerFromSave() {
        if (this.ownerNbt != null && this.ownerNbt.hasUUID(OWNER_UUID)) {
            UUID uuid = this.ownerNbt.getUUID(OWNER_UUID);

            for (CrystalSpiderEntity entity : this.level.getEntitiesOfClass(CrystalSpiderEntity.class, this.getBoundingBox().inflate(15.0D))) {
                if (entity.getUUID().equals(uuid)) {
                    this.projectileOwner = entity;
                    break;
                }
            }
        }

        this.ownerNbt = null;
    }

    @Nonnull
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }


    @Override
    @Nonnull
    public ItemStack getItem() {
        return new ItemStack(AstralItems.CRYSTAL_WEB_ITEM.get());
    }

    @Override
    public void remove(RemovalReason pReason) {
        if (getY() >= 128) {
            setWeb(level, blockPosition());
        }
        super.remove(pReason);
    }

    private void setWeb(Level world, BlockPos pos) {
        if (world.isEmptyBlock(pos) && world instanceof ServerLevel) {
            // TODO Make generation property from spewed webs configurable
            world.setBlock(pos, AstralBlocks.CRYSTAL_WEB.get().defaultBlockState().setValue(CrystalWeb.GENERATION, 9), 3);
        }
        else {
            Block.popResource(world, pos, new ItemStack(AstralItems.DREAMCORD.get()));
        }
    }
}
