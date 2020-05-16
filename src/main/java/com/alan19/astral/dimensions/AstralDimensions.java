package com.alan19.astral.dimensions;

import com.alan19.astral.Astral;
import com.alan19.astral.dimensions.innerrealm.InnerRealmChunkGenerator;
import com.alan19.astral.dimensions.innerrealm.InnerRealmDimension;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.BiFunction;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstralDimensions {
    public static final ResourceLocation INNER_REALM = new ResourceLocation(Astral.MOD_ID, "inner_realm");
    public static final ModDimension innerRealm = new ModDimension() {
        @Override
        public BiFunction<World, DimensionType, ? extends Dimension> getFactory() {
            return InnerRealmDimension::new;
        }
    };

    public static final ChunkGeneratorType<GenerationSettings, InnerRealmChunkGenerator> INNER_REALM_CHUNK_GENERATOR = new ChunkGeneratorType<>(InnerRealmChunkGenerator::new, false, GenerationSettings::new);

    @SubscribeEvent
    public static void onDimensionModRegistry(final RegistryEvent.Register<ModDimension> event) {
        event.getRegistry().register(AstralDimensions.innerRealm.setRegistryName(INNER_REALM));
        DimensionManager.registerDimension(INNER_REALM, AstralDimensions.innerRealm, null, true);
    }

    public static boolean isEntityNotInInnerRealm(Entity entity) {
        return entity.dimension != DimensionType.byName(INNER_REALM);
    }


}