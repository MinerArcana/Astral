package com.alan19.astral.entities.physicalbody;

import net.minecraft.client.renderer.entity.model.PlayerModel;

public class PhysicalBodyModel extends PlayerModel<PhysicalBodyEntity> {
    public PhysicalBodyModel(float p_i46304_1_, boolean p_i46304_2_) {
        super(p_i46304_1_, p_i46304_2_);
    }

    @Override
    public void setLivingAnimations(PhysicalBodyEntity entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        //Disable animations
    }

    @Override
    public void setRotationAngles(PhysicalBodyEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        //Disable rotations
    }
}
