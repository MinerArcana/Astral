package com.alan19.astral.particle;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.DiggingParticle;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class EtherealReplaceParticle extends DiggingParticle {

    private final BlockState sourceState;

    public EtherealReplaceParticle(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, BlockState state) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0, 0, 0, state);
        this.sourceState = state;
        this.setSprite(Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelShapes().getTexture(state));
        this.particleGravity = 0.0F;
        this.maxAge = 80;
        this.canCollide = false;

    }

    @Override
    public void tick() {
        if (this.age++ >= this.maxAge) {
            this.setExpired();
        }
    }

    @Nonnull
    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.TERRAIN_SHEET;
    }

    @Override
    public int getBrightnessForRender(float partialTick) {
        return WorldRenderer.getCombinedLight(this.world, new BlockPos(posX, posY, posZ));
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<BlockParticleData> {
        public Particle makeParticle(BlockParticleData typeIn, @Nonnull World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            BlockState blockstate = typeIn.getBlockState();
            if (!blockstate.isAir() && blockstate.getBlock() != Blocks.MOVING_PISTON) {
                return (new EtherealReplaceParticle(worldIn, x, y, z, blockstate)).updateSprite(typeIn.getPos());
            }
            return null;
        }
    }

    private Particle updateSprite(BlockPos pos) { //FORGE: we cannot assume that the x y z of the particles match the block pos of the block.
        if (pos != null) // There are cases where we are not able to obtain the correct source pos, and need to fallback to the non-model data version
            this.setSprite(Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelShapes().getTexture(sourceState, world, pos));
        return this;
    }

}
