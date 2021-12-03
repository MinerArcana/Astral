package com.alan19.astral.particle;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.DiggingParticle;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class EtherealReplaceParticle extends DiggingParticle {

    private final BlockState sourceState;

    public EtherealReplaceParticle(ClientWorld worldIn, double xCoordIn, double yCoordIn, double zCoordIn, BlockState state) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0, 0, 0, state);
        this.sourceState = state;
        this.setSprite(Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getParticleIcon(state));
        this.gravity = 0.0F;
        this.lifetime = 80;
        this.hasPhysics = false;

    }

    @Override
    public void tick() {
        if (this.age++ >= this.lifetime) {
            this.remove();
        }
    }

    @Nonnull
    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.TERRAIN_SHEET;
    }

    @Override
    public int getLightColor(float partialTick) {
        return WorldRenderer.getLightColor(this.level, new BlockPos(x, y, z));
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<BlockParticleData> {
        public Particle createParticle(BlockParticleData typeIn, @Nonnull ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            BlockState blockstate = typeIn.getState();
            if (!blockstate.isAir() && blockstate.getBlock() != Blocks.MOVING_PISTON) {
                return (new EtherealReplaceParticle(worldIn, x, y, z, blockstate)).updateSprite(typeIn.getPos());
            }
            return null;
        }
    }

    private Particle updateSprite(BlockPos pos) { //FORGE: we cannot assume that the x y z of the particles match the block pos of the block.
        if (pos != null) // There are cases where we are not able to obtain the correct source pos, and need to fallback to the non-model data version
            this.setSprite(Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getTexture(sourceState, level, pos));
        return this;
    }

}
