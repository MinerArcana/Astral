package com.alan19.astral.entity.physicalbody;

import net.minecraft.client.model.HumanoidModel;

public class NonRotatingBipedModel extends HumanoidModel<PhysicalBodyEntity> {
    public NonRotatingBipedModel(float modelSize) {
        super(modelSize, 0.0F, 64, 32);
    }

    @Override
    public void setupAnim(PhysicalBodyEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        //Don't rotate
    }

    @Override
    public void prepareMobModel(PhysicalBodyEntity entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        //Don't animate
    }
}
