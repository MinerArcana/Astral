package com.alan19.astral.network;

import com.alan19.astral.particle.AstralParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

public class SendOfferingBrazierFinished {
    private final BlockPos pos;

    public SendOfferingBrazierFinished(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(SendOfferingBrazierFinished sendOfferingBrazierFinished, FriendlyByteBuf packetBuffer) {
        packetBuffer.writeBlockPos(sendOfferingBrazierFinished.pos);
    }

    public static SendOfferingBrazierFinished decode(FriendlyByteBuf packetBuffer) {
        return new SendOfferingBrazierFinished(packetBuffer.readBlockPos());
    }

    public static void handle(SendOfferingBrazierFinished sendOfferingBrazierFinished, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            final Optional<Level> optionalWorld = LogicalSidedProvider.CLIENTWORLD.get(contextSupplier.get().getDirection().getReceptionSide());
            optionalWorld.ifPresent(world ->
                    {
                        final Random random = new Random();
                        for (int i = 0; i < 20; i++) {
                            double randX = sendOfferingBrazierFinished.pos.getX() + 0.3125 + random.nextDouble() * 0.3125;
                            double randY = sendOfferingBrazierFinished.pos.getY() + 0.625 + random.nextDouble() * 0.375;
                            double randZ = sendOfferingBrazierFinished.pos.getZ() + 0.3125 + random.nextDouble() * .3125;
                            world.addParticle(AstralParticles.ETHEREAL_FLAME.get(), randX, randY, randZ, 0, 0, 0);
                            world.addParticle(ParticleTypes.LARGE_SMOKE, randX, randY, randZ, 0, 0, 0);
                        }
                    }
            );
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
