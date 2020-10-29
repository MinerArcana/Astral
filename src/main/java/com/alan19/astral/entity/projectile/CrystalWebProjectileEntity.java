package com.alan19.astral.entity.projectile;

import com.alan19.astral.blocks.AstralBlocks;
import com.alan19.astral.effects.AstralEffects;
import com.alan19.astral.entity.AstralEntities;
import com.alan19.astral.entity.IAstralBeing;
import com.alan19.astral.entity.crystalspider.CrystalSpiderEntity;
import com.alan19.astral.events.astraltravel.TravelEffects;
import com.alan19.astral.items.AstralItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.*;
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
public class CrystalWebProjectileEntity extends Entity implements IRendersAsItem, IAstralBeing, IProjectile {

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
        this.setPosition(entity.getPosX() - (double) (entity.getWidth() + 1.0F) * 0.5D * (double) MathHelper.sin(entity.renderYawOffset * ((float) Math.PI / 180F)), entity.getPosYEye() - (double) 0.1F, entity.getPosZ() + (double) (entity.getWidth() + 1.0F) * 0.5D * (double) MathHelper.cos(entity.renderYawOffset * ((float) Math.PI / 180F)));
    }

    @OnlyIn(Dist.CLIENT)
    public CrystalWebProjectileEntity(World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        this(AstralEntities.CRYSTAL_WEB_PROJECTILE_ENTITY.get(), worldIn);
        this.setPosition(x, y, z);

        for (int i = 0; i < 7; ++i) {
            double d0 = 0.4D + 0.1D * (double) i;
            worldIn.addParticle(ParticleTypes.SPIT, x, y, z, xSpeed * d0, ySpeed, zSpeed * d0);
        }

        this.setMotion(xSpeed, ySpeed, zSpeed);
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

        Vector3d vec3d = this.getMotion();
        RayTraceResult raytraceresult = ProjectileHelper.rayTrace(this, this.getBoundingBox().expand(vec3d).grow(1.0D), entity -> !entity.isSpectator() && entity != this.projectileOwner, RayTraceContext.BlockMode.OUTLINE, true);
        if (raytraceresult.getType() != RayTraceResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
            this.onHit(raytraceresult);
        }

        double d0 = this.getPosX() + vec3d.x;
        double d1 = this.getPosY() + vec3d.y;
        double d2 = this.getPosZ() + vec3d.z;
        float f = MathHelper.sqrt(horizontalMag(vec3d));
        this.rotationYaw = (float) (MathHelper.atan2(vec3d.x, vec3d.z) * (double) (180F / (float) Math.PI));

        this.rotationPitch = (float) (MathHelper.atan2(vec3d.y, f) * (double) (180F / (float) Math.PI));

        while (this.rotationPitch - this.prevRotationPitch < -180.0F) {
            this.prevRotationPitch -= 360.0F;
        }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
            this.prevRotationYaw += 360.0F;
        }

        this.rotationPitch = MathHelper.lerp(0.2F, this.prevRotationPitch, this.rotationPitch);
        this.rotationYaw = MathHelper.lerp(0.2F, this.prevRotationYaw, this.rotationYaw);
        if (!this.world.isMaterialInBB(this.getBoundingBox(), Material.AIR)) {
            this.remove();
        }
        else if (this.isInWaterOrBubbleColumn()) {
            this.remove();
        }
        else {
            this.setMotion(vec3d.scale(0.99F));
            if (!this.hasNoGravity()) {
                this.setMotion(this.getMotion().add(0.0D, -0.06F, 0.0D));
            }

            this.setPosition(d0, d1, d2);
        }
    }

    /**
     * Updates the entity motion clientside, called by packets from the server
     */
    @Override
    @OnlyIn(Dist.CLIENT)
    public void setVelocity(double x, double y, double z) {
        this.setMotion(x, y, z);
        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt(x * x + z * z);
            this.rotationPitch = (float) (MathHelper.atan2(y, f) * (double) (180F / (float) Math.PI));
            this.rotationYaw = (float) (MathHelper.atan2(x, z) * (double) (180F / (float) Math.PI));
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationYaw, this.rotationPitch);
        }

    }

    /**
     * Similar to setArrowHeading, it's point the throwable entity to a x, y, z direction.
     */
    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vector3d vec3d = (new Vector3d(x, y, z)).normalize().add(this.rand.nextGaussian() * (double) 0.0075F * (double) inaccuracy, this.rand.nextGaussian() * (double) 0.0075F * (double) inaccuracy, this.rand.nextGaussian() * (double) 0.0075F * (double) inaccuracy).scale(velocity);
        this.setMotion(vec3d);
        float f = MathHelper.sqrt(horizontalMag(vec3d));
        this.rotationYaw = (float) (MathHelper.atan2(vec3d.x, z) * (double) (180F / (float) Math.PI));
        this.rotationPitch = (float) (MathHelper.atan2(vec3d.y, f) * (double) (180F / (float) Math.PI));
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
    }

    public void onHit(RayTraceResult result) {
        RayTraceResult.Type resultType = result.getType();
        if (resultType == RayTraceResult.Type.ENTITY && projectileOwner != null) {
            Entity entity = ((EntityRayTraceResult) result).getEntity();
            if (entity instanceof LivingEntity && TravelEffects.isEntityAstral((LivingEntity) entity)) {
                entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, this.projectileOwner), 2F);
            }
            if ((world.getDifficulty() == Difficulty.NORMAL || world.getDifficulty() == Difficulty.HARD) && entity instanceof LivingEntity) {
                ((LivingEntity) entity).addPotionEffect(new EffectInstance(AstralEffects.MIND_VENOM.get(), 100));
            }
            this.applyEnchantments(this.projectileOwner, entity);
            this.remove();
        }
        else if (resultType == RayTraceResult.Type.BLOCK && !this.world.isRemote) {
            this.remove();
        }

    }

    protected void registerData() {
        //Do not register any data
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readAdditional(CompoundNBT compound) {
        if (compound.contains(OWNER, 10)) {
            this.ownerNbt = compound.getCompound(OWNER);
        }

    }

    protected void writeAdditional(@Nonnull CompoundNBT compound) {
        if (this.projectileOwner != null) {
            CompoundNBT compoundNBT = new CompoundNBT();
            UUID uuid = this.projectileOwner.getUniqueID();
            compoundNBT.putUniqueId(OWNER_UUID, uuid);
            compound.put(OWNER, compoundNBT);
        }

    }

    private void restoreOwnerFromSave() {
        if (this.ownerNbt != null && this.ownerNbt.hasUniqueId(OWNER_UUID)) {
            UUID uuid = this.ownerNbt.getUniqueId(OWNER_UUID);

            for (CrystalSpiderEntity entity : this.world.getEntitiesWithinAABB(CrystalSpiderEntity.class, this.getBoundingBox().grow(15.0D))) {
                if (entity.getUniqueID().equals(uuid)) {
                    this.projectileOwner = entity;
                    break;
                }
            }
        }

        this.ownerNbt = null;
    }

    @Nonnull
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }


    @Override
    @Nonnull
    public ItemStack getItem() {
        return new ItemStack(AstralItems.CRYSTAL_WEB_ITEM.get());
    }

    @Override
    public void remove() {
        if (getPosY() >= 128) {
            setWeb(world, getPosition());
        }
        super.remove();
    }

    private void setWeb(World world, BlockPos pos) {
        if (world.isAirBlock(pos) && world instanceof ServerWorld) {
            world.setBlockState(pos, AstralBlocks.CRYSTAL_WEB.get().getDefaultState(), 3);
        }
        else {
            Block.spawnAsEntity(world, pos, new ItemStack(AstralItems.DREAMCORD.get()));
        }
    }
}
