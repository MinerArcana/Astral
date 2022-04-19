package com.alan19.astral.entity;

import com.alan19.astral.events.astraldamage.AstralEntityDamage;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.common.ForgeHooks;

import java.util.Random;

public interface IAstralBeing {

    static boolean attackEntityAsMobWithAstralDamage(LivingEntity self, Entity target) {
        if (self.getAttribute(AstralModifiers.ASTRAL_ATTACK_DAMAGE.get()) != null) {
            float attackDamage = (float) self.getAttribute(AstralModifiers.ASTRAL_ATTACK_DAMAGE.get()).getValue();
            final AttributeInstance knockbackAttribute = self.getAttribute(Attributes.ATTACK_KNOCKBACK);
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
        if (target instanceof Player player) {
            ItemStack mainHand = self.getMainHandItem();
            ItemStack itemstack1 = player.isUsingItem() ? player.getUseItem() : ItemStack.EMPTY;
            if (!mainHand.isEmpty() && !itemstack1.isEmpty() && mainHand.canDisableShield(itemstack1, player, self) && itemstack1.getItem() instanceof ShieldItem) {
                float f2 = 0.25F + EnchantmentHelper.getBlockEfficiency(self) * 0.05F;
                if (self.getCommandSenderWorld().getRandom().nextFloat() < f2) {
                    player.getCooldowns().addCooldown(mainHand.getItem(), 100);
                    self.level.broadcastEntityEvent(player, (byte) 30);
                }
            }
        }
    }

    static void handleKnockback(LivingEntity self, Entity target, float knockback) {
        if (knockback > 0.0F && target instanceof LivingEntity) {
            ((LivingEntity) target).knockback(knockback * 0.5F, Mth.sin(self.getYRot() * ((float) Math.PI / 180F)), -Mth.cos(self.getYRot() * ((float) Math.PI / 180F)));
            self.setDeltaMovement(self.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
        }
    }

    static <T extends Mob> boolean canEtherealEntitySpawn(EntityType<T> entityType, LevelAccessor world, MobSpawnType spawnReason, BlockPos blockPos, Random random) {
        BlockPos groundPos = blockPos.below();
        if (world.getDifficulty() != Difficulty.PEACEFUL && world instanceof ServerLevel && Monster.isDarkEnoughToSpawn((ServerLevelAccessor) world, groundPos, random)) {
            return spawnReason.equals(MobSpawnType.NATURAL) || spawnReason.equals(MobSpawnType.CHUNK_GENERATION) || spawnReason == MobSpawnType.SPAWNER || world.getBlockState(groundPos).isValidSpawn(world, groundPos, entityType);
        }
        return false;
    }
}
