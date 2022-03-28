package com.alan19.astral.events;

import com.alan19.astral.Astral;
import com.alan19.astral.blocks.etherealblocks.Ethereal;
import com.alan19.astral.effects.AstralEffects;
import com.alan19.astral.particle.AstralParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = Astral.MOD_ID)
public class EtherealBlockHandler {
    @SubscribeEvent
    public static void etherealParticles(PlayerInteractEvent.RightClickBlock event) {
        Level world = event.getWorld();
        Random random = world.getRandom();
        if (!event.getPlayer().hasEffect(AstralEffects.ASTRAL_TRAVEL.get()) && event.getPlayer().getItemInHand(event.getHand()).getItem() instanceof BlockItem && event.getFace() != null) {
            final BlockState blockState = world.getBlockState(event.getPos().relative(event.getFace()));
            if (blockState.getBlock() instanceof Ethereal) {
                BlockPos blockPos = event.getPos().relative(event.getFace());
                for (int i = 0; i < 3; i++) {
                    world.addParticle(new BlockParticleOption(AstralParticles.ETHEREAL_REPLACE_PARTICLE.get(), blockState), blockPos.getX() + random.nextDouble(), blockPos.getY() + random.nextDouble() / 2, blockPos.getZ() + random.nextDouble(), 0.0D, 0D, 0.0D);
                }
            }
        }
    }
}
