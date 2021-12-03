package com.alan19.astral.entity;

import com.alan19.astral.events.astraldamage.AstralEntityDamage;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;

import java.util.Random;

public interface IAstralBeing {

    static boolean attackEntityAsMobWithAstralDamage(LivingEntity self, Entity target) {
        if (self.getAttribute(AstralModifiers.ASTRAL_ATTACK_DAMAGE.get()) != null) {
            float attackDamage = (float) self.getAttribute(AstralModifiers.ASTRAL_ATTACK_DAMAGE.get()).getValue();
            final ModifiableAttributeInstance knockbackAttribute = self.getAttribute(Attributes.ATTACK_KNOCKBACK);
            float knockback = knockbackAttribute != null ? (float) knockbackAttribute.getValue() : 0;
            if (target instanceof LivingEntity) {
                attackDamage += EnchantmentHelper.getDamageBonus(self.getMainHandItem(), ((LivingEntity) target).getMobType());
                knockback += (float) EnchantmentHelper.getKnockbackBonus(self);
            }

            int fireAspectModifier = EnchantmentHelper.getFireAspect(self);
            if (fireAspectModifier > 0) {
                target.setSecondsOnFire(fireAspectModifier * 4);
            }
            if (target instanceof LivingEntity && ForgeHooks.onPlayerAttack((LivingEntity) target, new AstralEntityDamage(self), attackDamage)) {
                boolean flag = target.hurt(new AstralEntityDamage(self), attackDamage);
                if (flag) {
                    handleKnockback(self, target, knockback);
                    handleShieldBreak(self, target);
                    handleThornAndBaneOfArthropods(self, target);
                    self.setLastHurtMob(target);
                }
                return flag;
            }
            else {
                return false;
            }
        }
        return false;
    }

    static void handleThornAndBaneOfArthropods(LivingEntity self, Entity target) {
        if (target instanceof LivingEntity) {
            EnchantmentHelper.doPostHurtEffects((LivingEntity) target, self);
        }

        EnchantmentHelper.doPostDamageEffects(self, target);
    }

    static void handleShieldBreak(LivingEntity self, Entity target) {
        if (target instanceof PlayerEntity) {
            PlayerEntity playerentity = (PlayerEntity) target;
            ItemStack mainHand = self.getMainHandItem();
            ItemStack itemstack1 = playerentity.isUsingItem() ? playerentity.getUseItem() : ItemStack.EMPTY;
            if (!mainHand.isEmpty() && !itemstack1.isEmpty() && mainHand.canDisableShield(itemstack1, playerentity, self) && itemstack1.isShield(playerentity)) {
                float f2 = 0.25F + (float) EnchantmentHelper.getBlockEfficiency(self) * 0.05F;
                if (self.getCommandSenderWorld().getRandom().nextFloat() < f2) {
                    playerentity.getCooldowns().addCooldown(mainHand.getItem(), 100);
                    self.level.broadcastEntityEvent(playerentity, (byte) 30);
                }
            }
        }
    }

    static void handleKnockback(LivingEntity self, Entity target, float knockback) {
        if (knockback > 0.0F && target instanceof LivingEntity) {
            ((LivingEntity) target).knockback(knockback * 0.5F, MathHelper.sin(self.yRot * ((float) Math.PI / 180F)), -MathHelper.cos(self.yRot * ((float) Math.PI / 180F)));
            self.setDeltaMovement(self.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
        }
    }

    static <T extends MobEntity> boolean canEtherealEntitySpawn(EntityType<T> entityType, IWorld world, SpawnReason spawnReason, BlockPos blockPos, Random random) {
        BlockPos groundPos = blockPos.below();
        if (world.getDifficulty() != Difficulty.PEACEFUL && world instanceof ServerWorld && MonsterEntity.isDarkEnoughToSpawn((IServerWorld) world, groundPos, random)) {
            return spawnReason.equals(SpawnReason.NATURAL) || spawnReason.equals(SpawnReason.CHUNK_GENERATION) || spawnReason == SpawnReason.SPAWNER || world.getBlockState(groundPos).isValidSpawn(world, groundPos, entityType);
        }
        return false;
    }
}
