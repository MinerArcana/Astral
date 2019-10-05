package com.alan199921.astral;

import com.alan199921.astral.blocks.AstralMeridian;
import com.alan199921.astral.blocks.EgoMembrane;
import com.alan199921.astral.blocks.FeverweedBlock;
import com.alan199921.astral.blocks.SnowberryBush;
import com.alan199921.astral.capabilities.bodylink.BodyLinkCapability;
import com.alan199921.astral.capabilities.bodylink.BodyLinkStorage;
import com.alan199921.astral.capabilities.bodylink.IBodyLinkCapability;
import com.alan199921.astral.capabilities.innerrealmchunkclaim.IInnerRealmChunkClaimCapability;
import com.alan199921.astral.capabilities.innerrealmchunkclaim.InnerRealmChunkClaimCapability;
import com.alan199921.astral.capabilities.innerrealmchunkclaim.InnerRealmChunkClaimStorage;
import com.alan199921.astral.capabilities.innerrealmteleporter.IInnerRealmTeleporterCapability;
import com.alan199921.astral.capabilities.innerrealmteleporter.InnerRealmTeleporterCapability;
import com.alan199921.astral.capabilities.innerrealmteleporter.InnerRealmTeleporterStorage;
import com.alan199921.astral.dimensions.ModDimensions;
import com.alan199921.astral.effects.AstralEffect;
import com.alan199921.astral.entities.PhysicalBodyEntity;
import com.alan199921.astral.entities.PhysicalBodyEntityRenderer;
import com.alan199921.astral.items.EnlightenmentKey;
import com.alan199921.astral.items.Feverweed;
import com.alan199921.astral.items.IntrospectionMedicine;
import com.alan199921.astral.items.Snowberry;
import com.alan199921.astral.potions.AstralTravelPotion;
import com.alan199921.astral.setup.ClientProxy;
import com.alan199921.astral.setup.IProxy;
import com.alan199921.astral.setup.ModSetup;
import com.alan199921.astral.setup.ServerProxy;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Astral.MOD_ID)
public class Astral {
    public static final String MOD_ID = "astral";

    public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());
    public static ModSetup setup = new ModSetup();

    public Astral() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        Config.register(ModLoadingContext.get());
    }

    private void setup(final FMLCommonSetupEvent event) {
        setup.init();
        proxy.init();
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
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
            // register a new block here
            event.getRegistry().register(new SnowberryBush());
            event.getRegistry().register(new FeverweedBlock());
            event.getRegistry().register(new EgoMembrane());
            event.getRegistry().register(new AstralMeridian());
        }

        @SubscribeEvent
        public static void onItemRegistry(final RegistryEvent.Register<Item> event) {
            new Item.Properties().group(setup.astralItems);
            // register a new block here
            event.getRegistry().register(new Snowberry());
            event.getRegistry().register(new Feverweed());
            event.getRegistry().register(new IntrospectionMedicine());
            event.getRegistry().register(new EgoMembrane().asItem());
            event.getRegistry().register(new EnlightenmentKey());
        }

        @SubscribeEvent
        public static void onDimensionModRegistry(final RegistryEvent.Register<ModDimension> event) {
            event.getRegistry().register(ModDimensions.innerRealm);
            DimensionManager.registerDimension(new ResourceLocation(MOD_ID, "inner_realm"), ModDimensions.innerRealm, null, true);
        }

        @SubscribeEvent
        public static void onEffectRegistry(final RegistryEvent.Register<Effect> event) {
            event.getRegistry().register(new AstralEffect());
        }

        @SubscribeEvent
        public static void onPotionRegistry(final RegistryEvent.Register<Potion> event) {
            event.getRegistry().register(new AstralTravelPotion());
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
