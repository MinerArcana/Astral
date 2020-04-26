package com.alan199921.astral;

import com.alan199921.astral.api.NBTCapStorage;
import com.alan199921.astral.api.bodylink.BodyLinkCapability;
import com.alan199921.astral.api.bodylink.IBodyLinkCapability;
import com.alan199921.astral.api.heightadjustment.HeightAdjustmentCapability;
import com.alan199921.astral.api.heightadjustment.IHeightAdjustmentCapability;
import com.alan199921.astral.api.innerrealmchunkclaim.IInnerRealmChunkClaimCapability;
import com.alan199921.astral.api.innerrealmchunkclaim.InnerRealmChunkClaimCapability;
import com.alan199921.astral.api.innerrealmchunkclaim.InnerRealmChunkClaimStorage;
import com.alan199921.astral.api.innerrealmteleporter.IInnerRealmTeleporterCapability;
import com.alan199921.astral.api.innerrealmteleporter.InnerRealmTeleporterCapability;
import com.alan199921.astral.api.innerrealmteleporter.InnerRealmTeleporterStorage;
import com.alan199921.astral.api.psychicinventory.IPsychicInventory;
import com.alan199921.astral.api.psychicinventory.PsychicInventory;
import com.alan199921.astral.api.sleepmanager.ISleepManager;
import com.alan199921.astral.api.sleepmanager.SleepManager;
import com.alan199921.astral.blocks.AstralBlocks;
import com.alan199921.astral.blocks.BlockRenderHandler;
import com.alan199921.astral.commands.AstralCommands;
import com.alan199921.astral.configs.AstralConfig;
import com.alan199921.astral.entities.AstralEntityRegistry;
import com.alan199921.astral.entities.PhysicalBodyEntityRenderer;
import com.alan199921.astral.items.AstralItemGroups;
import com.alan199921.astral.items.AstralItems;
import com.alan199921.astral.network.AstralNetwork;
import com.alan199921.astral.world.AstralFeatures;
import com.alan199921.astral.world.OverworldVegetation;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import static com.alan199921.astral.serializing.AstralSerializers.OPTIONAL_GAME_PROFILE;
import static com.alan199921.astral.serializing.AstralSerializers.OPTIONAL_ITEMSTACK_HANDLER;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Astral.MOD_ID)
public class Astral {
    public static final String MOD_ID = "astral";
    public static final SimpleChannel INSTANCE = AstralNetwork.getNetworkChannel();


    public static final AstralItemGroups setup = new AstralItemGroups();

    public Astral() {
        // Register the setup method for modloading
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);

        // Register and load configs
        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        modLoadingContext.registerConfig(ModConfig.Type.COMMON, AstralConfig.initialize());
        AstralConfig.loadConfig(AstralConfig.getInstance().getSpec(), FMLPaths.CONFIGDIR.get().resolve("astral-common.toml"));
        MinecraftForge.EVENT_BUS.addListener(Astral::serverLoad);
        AstralEntityRegistry.register(modEventBus);
        AstralBlocks.register(modEventBus);
        AstralItems.register(modEventBus);
        AstralFeatures.register(modEventBus);
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> modEventBus.addListener(ClientEventHandler::clientSetup));
    }

    private void setup(final FMLCommonSetupEvent event) {
        //Initializes worldgen
        OverworldVegetation.addOverworldVegetation();

        //Register Serializers
        DataSerializers.registerSerializer(OPTIONAL_GAME_PROFILE);
        DataSerializers.registerSerializer(OPTIONAL_ITEMSTACK_HANDLER);

    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void init(final FMLCommonSetupEvent event) {
            //TODO Refactor Teleporter and Chunk Claim
            CapabilityManager.INSTANCE.register(IInnerRealmTeleporterCapability.class, new InnerRealmTeleporterStorage(), InnerRealmTeleporterCapability::new);
            CapabilityManager.INSTANCE.register(IInnerRealmChunkClaimCapability.class, new InnerRealmChunkClaimStorage(), InnerRealmChunkClaimCapability::new);
            CapabilityManager.INSTANCE.register(IBodyLinkCapability.class, new NBTCapStorage<>(), BodyLinkCapability::new);
            CapabilityManager.INSTANCE.register(IHeightAdjustmentCapability.class, new NBTCapStorage<>(), HeightAdjustmentCapability::new);
            CapabilityManager.INSTANCE.register(IPsychicInventory.class, new NBTCapStorage<>(), PsychicInventory::new);
            CapabilityManager.INSTANCE.register(ISleepManager.class, new NBTCapStorage<>(), SleepManager::new);
        }
    }

    @Mod.EventBusSubscriber(modid = Astral.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void registerModels(ModelRegistryEvent event) {
            RenderingRegistry.registerEntityRenderingHandler(AstralEntityRegistry.PHYSICAL_BODY_ENTITY.get(), PhysicalBodyEntityRenderer::new);
        }

        @SubscribeEvent
        public static void setRenderLayers(FMLClientSetupEvent event) {
            BlockRenderHandler.setRenderLayers();
            BlockRenderHandler.registerBiomeBasedBlockColors();
        }

    }

    @SubscribeEvent
    public static void serverLoad(FMLServerAboutToStartEvent event) {
        AstralCommands.registerCommands(event.getServer().getCommandManager().getDispatcher());
    }

}
