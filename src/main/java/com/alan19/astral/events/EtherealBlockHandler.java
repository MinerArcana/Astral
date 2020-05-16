package com.alan19.astral.events;

import com.alan19.astral.Astral;
import com.alan19.astral.blocks.etherealblocks.Ethereal;
import com.alan19.astral.effects.AstralEffects;
import com.alan19.astral.particle.AstralParticles;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.particles.BlockParticleData;
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
        if (!event.getPlayer().isPotionActive(AstralEffects.ASTRAL_TRAVEL) && event.getPlayer().getHeldItem(event.getHand()).getItem() instanceof BlockItem && event.getFace() != null) {
            final BlockState blockState = world.getBlockState(event.getPos().offset(event.getFace()));
            if (blockState.getBlock() instanceof Ethereal) {
                BlockPos blockPos = event.getPos().offset(event.getFace());
                for (int i = 0; i < 3; i++) {
                    world.addParticle(new BlockParticleData(AstralParticles.ETHEREAL_REPLACE_PARTICLE.get(), blockState), blockPos.getX() + random.nextDouble(), blockPos.getY() + random.nextDouble() / 2, blockPos.getZ() + random.nextDouble(), 0.0D, 0D, 0.0D);
                }
            }
        }
    }
}
