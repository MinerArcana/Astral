package com.alan199921.astral;

import com.alan199921.astral.capabilities.bodylink.BodyLinkCapability;
import com.alan199921.astral.capabilities.bodylink.BodyLinkStorage;
import com.alan199921.astral.capabilities.bodylink.IBodyLinkCapability;
import com.alan199921.astral.capabilities.innerrealmchunkclaim.IInnerRealmChunkClaimCapability;
import com.alan199921.astral.capabilities.innerrealmchunkclaim.InnerRealmChunkClaimCapability;
import com.alan199921.astral.capabilities.innerrealmchunkclaim.InnerRealmChunkClaimStorage;
import com.alan199921.astral.capabilities.innerrealmteleporter.IInnerRealmTeleporterCapability;
import com.alan199921.astral.capabilities.innerrealmteleporter.InnerRealmTeleporterCapability;
import com.alan199921.astral.capabilities.innerrealmteleporter.InnerRealmTeleporterStorage;
import com.alan199921.astral.configs.AstralConfig;
import com.alan199921.astral.dimensions.AstralDimensions;
import com.alan199921.astral.effects.AstralEffect;
import com.alan199921.astral.entities.PhysicalBodyEntity;
import com.alan199921.astral.entities.PhysicalBodyEntityRenderer;
import com.alan199921.astral.potions.AstralTravelPotion;
import com.alan199921.astral.setup.ModSetup;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Astral.MOD_ID)
public class Astral {
    public static final String MOD_ID = "astral";

    public static ModSetup setup = new ModSetup();

    public Astral() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        // Register and load configs
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AstralConfig.initialize());
        AstralConfig.loadConfig(AstralConfig.getInstance().getSpec(), FMLPaths.CONFIGDIR.get().resolve("astral-common.toml"));
    }

    private void setup(final FMLCommonSetupEvent event) {
        setup.init();
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void init(final FMLCommonSetupEvent event) {
            CapabilityManager.INSTANCE.register(IInnerRealmTeleporterCapability.class, new InnerRealmTeleporterStorage(), InnerRealmTeleporterCapability::new);
            CapabilityManager.INSTANCE.register(IInnerRealmChunkClaimCapability.class, new InnerRealmChunkClaimStorage(), InnerRealmChunkClaimCapability::new);
            CapabilityManager.INSTANCE.register(IBodyLinkCapability.class, new BodyLinkStorage(), BodyLinkCapability::new);
        }

        @SubscribeEvent
        public static void onEffectRegistry(final RegistryEvent.Register<Effect> event) {
            event.getRegistry().register(new AstralEffect().setRegistryName("astral_travel"));
        }

        @SubscribeEvent
        public static void onPotionRegistry(final RegistryEvent.Register<Potion> event) {
            event.getRegistry().register(new AstralTravelPotion().setRegistryName("astral_travel_potion"));
        }
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void registerModels(FMLClientSetupEvent event) {
            RenderingRegistry.registerEntityRenderingHandler(PhysicalBodyEntity.class, renderer -> new PhysicalBodyEntityRenderer(renderer, 0.5f));
        }
    }
}
