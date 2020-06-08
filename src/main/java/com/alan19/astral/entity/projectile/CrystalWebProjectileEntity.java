package com.alan19.astral.entity.projectile;

import com.alan19.astral.blocks.AstralBlocks;
import com.alan19.astral.effects.AstralEffects;
import com.alan19.astral.events.astraltravel.TravelEffects;
import com.alan19.astral.items.AstralItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nonnull;

@OnlyIn(value = Dist.CLIENT, _interface = IRendersAsItem.class)
public class CrystalWebProjectileEntity extends DamagingProjectileEntity implements IRendersAsItem {
    public CrystalWebProjectileEntity(EntityType<? extends DamagingProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    @Nonnull
    public ItemStack getItem() {
        return new ItemStack(AstralItems.CRYSTAL_WEB_ITEM.get());
    }

    @Override
    protected void onImpact(@Nonnull RayTraceResult result) {
        super.onImpact(result);
        if (world instanceof ServerWorld) {
            if (result.getType() == RayTraceResult.Type.ENTITY) {
                Entity entity = ((EntityRayTraceResult) result).getEntity();
                if (entity instanceof LivingEntity && TravelEffects.isEntityAstral((LivingEntity) entity)) {
                    handleEntityCollision(entity);
                    return;
                }
            }
            if (result instanceof BlockRayTraceResult) {
                handleBlockCollision((BlockRayTraceResult) result);
            }
        }
    }

    private void handleBlockCollision(@Nonnull BlockRayTraceResult result) {
        final BlockPos newWebBlockLocation = result.getPos().offset(result.getFace());
        if (world.isAirBlock(newWebBlockLocation)) {
            boolean flag = ForgeEventFactory.getMobGriefingEvent(this.world, this.shootingEntity);
            if (flag) {
                world.setBlockState(getPosition(), AstralBlocks.CRYSTAL_WEB.get().getDefaultState(), 3);
            }
            remove();
        }
    }

    private void handleEntityCollision(Entity entity) {
        entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, this.shootingEntity), 2F);
        if ((world.getDifficulty() == Difficulty.NORMAL || world.getDifficulty() == Difficulty.HARD) && entity instanceof LivingEntity) {
            ((LivingEntity) entity).addPotionEffect(new EffectInstance(AstralEffects.MIND_VENOM.get(), 100));
        }
        this.applyEnchantments(this.shootingEntity, entity);
        remove();
        boolean flag = ForgeEventFactory.getMobGriefingEvent(this.world, this.shootingEntity);
        if (flag) {
            world.setBlockState(getPosition(), AstralBlocks.CRYSTAL_WEB.get().getDefaultState(), 3);
        }
    }
}
