package com.alan19.astral.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class EtherealReplaceParticle extends TerrainParticle {

    private final BlockState sourceState;

    public EtherealReplaceParticle(ClientLevel worldIn, double xCoordIn, double yCoordIn, double zCoordIn, BlockState state) {
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
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.TERRAIN_SHEET;
    }

    @Override
    public int getLightColor(float partialTick) {
        return LevelRenderer.getLightColor(this.level, new BlockPos(x, y, z));
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<BlockParticleOption> {
        public Particle createParticle(BlockParticleOption typeIn, @Nonnull ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
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
