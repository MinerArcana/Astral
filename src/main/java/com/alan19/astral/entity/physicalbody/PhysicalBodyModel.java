package com.alan19.astral.entity.physicalbody;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;

public class PhysicalBodyModel extends PlayerModel<PhysicalBodyEntity> {


    public PhysicalBodyModel(ModelPart pRoot, boolean pSlim) {
        super(pRoot, pSlim);
    }

    @Override
    public void prepareMobModel(PhysicalBodyEntity entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        //Disable animations
    }

    @Override
    public void setupAnim(PhysicalBodyEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        //Disable rotations
    }
}
