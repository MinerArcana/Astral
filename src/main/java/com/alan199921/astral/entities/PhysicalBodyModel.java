package com.alan199921.astral.entities;

import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.LivingEntity;

public class PhysicalBodyModel extends PlayerModel {
    public PhysicalBodyModel(float p_i46304_1_, boolean p_i46304_2_) {
        super(p_i46304_1_, p_i46304_2_);
    }

    @Override
    public void setLivingAnimations(LivingEntity p_212843_1_, float p_212843_2_, float p_212843_3_, float p_212843_4_) {
        this.bipedHead.rotateAngleX = 0;
        this.bipedHead.rotateAngleY = 0;
        this.bipedHead.rotateAngleZ = 0;
    }
}
