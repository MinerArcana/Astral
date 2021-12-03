package com.alan19.astral.entity.projectile;

import com.alan19.astral.blocks.AstralBlocks;
import com.alan19.astral.blocks.etherealblocks.CrystalWeb;
import com.alan19.astral.effects.AstralEffects;
import com.alan19.astral.entity.AstralEntities;
import com.alan19.astral.entity.IAstralBeing;
import com.alan19.astral.entity.crystalspider.CrystalSpiderEntity;
import com.alan19.astral.events.astraltravel.TravelEffects;
import com.alan19.astral.items.AstralItems;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import java.util.UUID;

@OnlyIn(value = Dist.CLIENT, _interface = IRendersAsItem.class)
public class CrystalWebProjectileEntity extends ThrowableEntity implements IRendersAsItem, IAstralBeing {

    public static final String OWNER_UUID = "OwnerUUID";
    public static final String OWNER = "Owner";
    private CrystalSpiderEntity projectileOwner;
    private CompoundNBT ownerNbt;

    public CrystalWebProjectileEntity(EntityType<? extends CrystalWebProjectileEntity> entityType, World worldIn) {
        super(entityType, worldIn);
    }

    public CrystalWebProjectileEntity(World worldIn, CrystalSpiderEntity entity) {
        this(AstralEntities.CRYSTAL_WEB_PROJECTILE_ENTITY.get(), worldIn);
        this.projectileOwner = entity;
        this.setPos(entity.getX() - (double) (entity.getBbWidth() + 1.0F) * 0.5D * (double) MathHelper.sin(entity.yBodyRot * ((float) Math.PI / 180F)), entity.getEyeY() - (double) 0.1F, entity.getZ() + (double) (entity.getBbWidth() + 1.0F) * 0.5D * (double) MathHelper.cos(entity.yBodyRot * ((float) Math.PI / 180F)));
    }

    @OnlyIn(Dist.CLIENT)
    public CrystalWebProjectileEntity(World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
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

        Vector3d vec3d = this.getDeltaMovement();
        RayTraceResult raytraceresult = ProjectileHelper.getHitResult(this, this::canHitEntity);
        if (raytraceresult.getType() != RayTraceResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
            this.onHit(raytraceresult);
        }

        double d0 = this.getX() + vec3d.x;
        double d1 = this.getY() + vec3d.y;
        double d2 = this.getZ() + vec3d.z;
        float f = MathHelper.sqrt(getHorizontalDistanceSqr(vec3d));
        this.yRot = (float) (MathHelper.atan2(vec3d.x, vec3d.z) * (double) (180F / (float) Math.PI));

        this.xRot = (float) (MathHelper.atan2(vec3d.y, f) * (double) (180F / (float) Math.PI));

        while (this.xRot - this.xRotO < -180.0F) {
            this.xRotO -= 360.0F;
        }

        while (this.xRot - this.xRotO >= 180.0F) {
            this.xRotO += 360.0F;
        }

        while (this.yRot - this.yRotO < -180.0F) {
            this.yRotO -= 360.0F;
        }

        while (this.yRot - this.yRotO >= 180.0F) {
            this.yRotO += 360.0F;
        }

        this.xRot = MathHelper.lerp(0.2F, this.xRotO, this.xRot);
        this.yRot = MathHelper.lerp(0.2F, this.yRotO, this.yRot);
        if (this.level.getBlockStates(this.getBoundingBox()).noneMatch(AbstractBlock.AbstractBlockState::isAir)) {
            this.remove();
        }
        else if (this.isInWaterOrBubble()) {
            this.remove();
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
            float f = MathHelper.sqrt(x * x + z * z);
            this.xRot = (float) (MathHelper.atan2(y, f) * (double) (180F / (float) Math.PI));
            this.yRot = (float) (MathHelper.atan2(x, z) * (double) (180F / (float) Math.PI));
            this.xRotO = this.xRot;
            this.yRotO = this.yRot;
            this.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, this.xRot);
        }

    }

    /**
     * Similar to setArrowHeading, it's point the throwable entity to a x, y, z direction.
     */
    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vector3d vec3d = (new Vector3d(x, y, z)).normalize().add(this.random.nextGaussian() * (double) 0.0075F * (double) inaccuracy, this.random.nextGaussian() * (double) 0.0075F * (double) inaccuracy, this.random.nextGaussian() * (double) 0.0075F * (double) inaccuracy).scale(velocity);
        this.setDeltaMovement(vec3d);
        float f = MathHelper.sqrt(getHorizontalDistanceSqr(vec3d));
        this.yRot = (float) (MathHelper.atan2(vec3d.x, z) * (double) (180F / (float) Math.PI));
        this.xRot = (float) (MathHelper.atan2(vec3d.y, f) * (double) (180F / (float) Math.PI));
        this.yRotO = this.yRot;
        this.xRotO = this.xRot;
    }

    public void onHit(RayTraceResult result) {
        RayTraceResult.Type resultType = result.getType();
        if (resultType == RayTraceResult.Type.ENTITY && projectileOwner != null) {
            Entity entity = ((EntityRayTraceResult) result).getEntity();
            if (entity instanceof LivingEntity && TravelEffects.isEntityAstral((LivingEntity) entity)) {
                entity.hurt(DamageSource.indirectMagic(this, this.projectileOwner), 2F);
            }
            if ((level.getDifficulty() == Difficulty.NORMAL || level.getDifficulty() == Difficulty.HARD) && entity instanceof LivingEntity) {
                ((LivingEntity) entity).addEffect(new EffectInstance(AstralEffects.MIND_VENOM.get(), 100));
            }
            this.doEnchantDamageEffects(this.projectileOwner, entity);
            this.remove();
        }
        else if (resultType == RayTraceResult.Type.BLOCK && !this.level.isClientSide) {
            this.remove();
        }

    }

    protected void defineSynchedData() {
        //Do not register any data
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    protected void readAdditionalSaveData(CompoundNBT compound) {
        if (compound.contains(OWNER, 10)) {
            this.ownerNbt = compound.getCompound(OWNER);
        }

    }

    @Override
    protected void addAdditionalSaveData(@Nonnull CompoundNBT compound) {
        if (this.projectileOwner != null) {
            CompoundNBT compoundNBT = new CompoundNBT();
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
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }


    @Override
    @Nonnull
    public ItemStack getItem() {
        return new ItemStack(AstralItems.CRYSTAL_WEB_ITEM.get());
    }

    @Override
    public void remove() {
        if (getY() >= 128) {
            setWeb(level, blockPosition());
        }
        super.remove();
    }

    private void setWeb(World world, BlockPos pos) {
        if (world.isEmptyBlock(pos) && world instanceof ServerWorld) {
            // TODO Make generation property from spewed webs configurable
            world.setBlock(pos, AstralBlocks.CRYSTAL_WEB.get().defaultBlockState().setValue(CrystalWeb.GENERATION, 9), 3);
        }
        else {
            Block.popResource(world, pos, new ItemStack(AstralItems.DREAMCORD.get()));
        }
    }
}
