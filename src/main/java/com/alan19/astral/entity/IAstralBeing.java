package com.alan19.astral.entity;

import com.alan19.astral.events.AstralDamageSource;
import com.alan19.astral.util.Constants;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

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

        boolean flag = target.attackEntityFrom(AstralDamageSource.causeAstralMobDamage(self), attackDamage);
        if (flag) {
            handleKnockback(self, target, knockback);
            handleShieldBreak(self, target);
            handleThornAndBaneOfArthropods(self, target);
            self.setLastAttackedEntity(target);
        }
        return flag;
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
}
