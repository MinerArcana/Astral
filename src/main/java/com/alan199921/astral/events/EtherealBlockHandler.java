package com.alan199921.astral.events;

import com.alan199921.astral.Astral;
import com.alan199921.astral.blocks.etherealblocks.Ethereal;
import com.alan199921.astral.effects.AstralEffects;
import net.minecraft.block.BlockState;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = Astral.MOD_ID)
public class EtherealBlockHandler {
    @SubscribeEvent
    public static void etherealParticles(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        Random random = world.getRandom();
        if (!event.getPlayer().isPotionActive(AstralEffects.ASTRAL_TRAVEL) && event.getFace() != null) {
            final BlockState blockState = world.getBlockState(event.getPos().offset(event.getFace()));
            if (blockState.getBlock() instanceof Ethereal) {
                BlockPos blockPos = event.getPos().offset(event.getFace());
                world.addParticle(ParticleTypes.PORTAL, blockPos.getX() + random.nextDouble(), blockPos.getY(), blockPos.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
            }
        }
//        world.addParticle(ParticleTypes.LARGE_SMOKE, event.getPos().getX() + .5, event.getPos().getY() + .5, event.getPos().getZ() + .5, 0.0D, 0.0D, 0.0D);
    }
}
