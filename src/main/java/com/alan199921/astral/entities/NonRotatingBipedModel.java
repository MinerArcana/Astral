package com.alan199921.astral.entities;

import net.minecraft.client.renderer.entity.model.BipedModel;

public class NonRotatingBipedModel extends BipedModel<PhysicalBodyEntity> {
    public NonRotatingBipedModel(float modelSize) {
        super(modelSize, 0.0F, 64, 32);
    }

    @Override
    public void setRotationAngles(PhysicalBodyEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        //Don't rotate
    }

    @Override
    public void setLivingAnimations(PhysicalBodyEntity entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        //Don't animate
    }
}
