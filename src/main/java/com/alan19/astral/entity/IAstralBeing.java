package com.alan19.astral.entity;

import com.alan19.astral.blocks.etherealblocks.EtherealBlock;
import com.alan19.astral.events.astraldamage.AstralEntityDamage;
import com.alan19.astral.tags.AstralTags;
import com.alan19.astral.util.Constants;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.ForgeHooks;

import java.util.Random;

public interface IAstralBeing {

    static boolean attackEntityAsMobWithAstralDamage(LivingEntity self, Entity target) {
        float attackDamage = (float) self.getAttribute(Constants.ASTRAL_ATTACK_DAMAGE).getValue();
        final IAttributeInstance knockbackAttribute = self.getAttribute(SharedMonsterAttributes.ATTACK_KNOCKBACK);
        float knockback = knockbackAttribute != null ? (float) knockbackAttribute.getValue() : 0;
        if (target instanceof LivingEntity) {
            attackDamage += EnchantmentHelper.getModifierForCreature(self.getHeldItemMainhand(), ((LivingEntity) target).getCreatureAttribute());
            knockback += (float) EnchantmentHelper.getKnockbackModifier(self);
        }

        int fireAspectModifier = EnchantmentHelper.getFireAspectModifier(self);
        if (fireAspectModifier > 0) {
            target.setFire(fireAspectModifier * 4);
        }
        if (target instanceof LivingEntity && ForgeHooks.onPlayerAttack((LivingEntity) target, new AstralEntityDamage(self), attackDamage)) {
            boolean flag = target.attackEntityFrom(new AstralEntityDamage(self), attackDamage);
            if (flag) {
                handleKnockback(self, target, knockback);
                handleShieldBreak(self, target);
                handleThornAndBaneOfArthropods(self, target);
                self.setLastAttackedEntity(target);
            }
            return flag;
        }
        else {
            return false;
        }
    }

    static void handleThornAndBaneOfArthropods(LivingEntity self, Entity target) {
        if (target instanceof LivingEntity) {
            EnchantmentHelper.applyThornEnchantments((LivingEntity) target, self);
        }

        EnchantmentHelper.applyArthropodEnchantments(self, target);
    }

    static void handleShieldBreak(LivingEntity self, Entity target) {
        if (target instanceof PlayerEntity) {
            PlayerEntity playerentity = (PlayerEntity) target;
            ItemStack mainHand = self.getHeldItemMainhand();
            ItemStack itemstack1 = playerentity.isHandActive() ? playerentity.getActiveItemStack() : ItemStack.EMPTY;
            if (!mainHand.isEmpty() && !itemstack1.isEmpty() && mainHand.canDisableShield(itemstack1, playerentity, self) && itemstack1.isShield(playerentity)) {
                float f2 = 0.25F + (float) EnchantmentHelper.getEfficiencyModifier(self) * 0.05F;
                if (self.getEntityWorld().getRandom().nextFloat() < f2) {
                    playerentity.getCooldownTracker().setCooldown(mainHand.getItem(), 100);
                    self.world.setEntityState(playerentity, (byte) 30);
                }
            }
        }
    }

    static void handleKnockback(LivingEntity self, Entity target, float knockback) {
        if (knockback > 0.0F && target instanceof LivingEntity) {
            ((LivingEntity) target).knockBack(self, knockback * 0.5F, MathHelper.sin(self.rotationYaw * ((float) Math.PI / 180F)), -MathHelper.cos(self.rotationYaw * ((float) Math.PI / 180F)));
            self.setMotion(self.getMotion().mul(0.6D, 1.0D, 0.6D));
        }
    }

    static <T extends MobEntity> boolean canEtherealEntitySpawn(EntityType<T> entityType, IWorld world, SpawnReason spawnReason, BlockPos blockPos, Random random) {
        BlockPos groundPos = blockPos.down();
        if (world.getDifficulty() != Difficulty.PEACEFUL && MonsterEntity.isValidLightLevel(world, groundPos, random)) {
            if (spawnReason.equals(SpawnReason.NATURAL) || spawnReason.equals(SpawnReason.CHUNK_GENERATION)) {
                if (world.getBlockState(groundPos).getBlock() instanceof EtherealBlock) {
                    return world.getBlockState(blockPos).getBlock() == Blocks.AIR || AstralTags.ETHERIC_GROWTHS.contains(world.getBlockState(blockPos).getBlock());
                }
                else {
                    return false;
                }
            }
            return spawnReason == SpawnReason.SPAWNER || world.getBlockState(groundPos).canEntitySpawn(world, groundPos, entityType);
        }
        return false;
    }
}
