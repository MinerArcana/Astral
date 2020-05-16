package com.alan19.astral.particle;

import net.minecraft.client.particle.*;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class EtherealFlame extends SpriteTexturedParticle {
    public EtherealFlame(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
        this.motionX = this.motionX * (double) 0.01F + xSpeed;
        this.motionY = this.motionY * (double) 0.01F + ySpeed;
        this.motionZ = this.motionZ * (double) 0.01F + zSpeed;
        this.posX += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F;
        this.posY += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F;
        this.posZ += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F;
        this.maxAge = (int) (8.0D / (new Random().nextDouble() * 0.8D + 0.2D)) + 4;
    }

    @Nonnull
    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void move(double x, double y, double z) {
        this.setBoundingBox(this.getBoundingBox().offset(x, y, z));
        this.resetPositionToBB();
    }

    @Override
    public float getScale(float scaleFactor) {
        float f = ((float) this.age + scaleFactor) / (float) this.maxAge;
        return this.particleScale * (1.0F - f * f * 0.5F);
    }

    @Override
    public int getBrightnessForRender(float partialTick) {
        float f = ((float) this.age + partialTick) / (float) this.maxAge;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        int i = super.getBrightnessForRender(partialTick);
        int j = i & 255;
        int k = i >> 16 & 255;
        j = j + (int) (f * 15.0F * 16.0F);
        if (j > 240) {
            j = 240;
        }

        return j | k << 16;
    }

    @Override
    public void tick() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.age++ >= this.maxAge) {
            this.setExpired();
        }
        else {
            this.move(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.96F;
            this.motionY *= 0.96F;
            this.motionZ *= 0.96F;
            if (this.onGround) {
                this.motionX *= 0.7F;
                this.motionZ *= 0.7F;
            }

        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSet;

        public Factory(IAnimatedSprite animatedSprite) {
            this.spriteSet = animatedSprite;
        }

        public Particle makeParticle(BasicParticleType typeIn, World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            EtherealFlame etherealFlame = new EtherealFlame(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            etherealFlame.selectSpriteRandomly(this.spriteSet);
            return etherealFlame;
        }
    }
}
