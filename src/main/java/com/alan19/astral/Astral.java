package com.alan19.astral;

import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.api.intentiontracker.intentiontrackerbehaviors.EtherealSaplingIntentionTrackerBehavior;
import com.alan19.astral.api.intentiontracker.intentiontrackerbehaviors.EthericPowderIntentionTrackerBehavior;
import com.alan19.astral.api.intentiontracker.intentiontrackerbehaviors.IndexOfKnowledgeIntentionTrackerBehavior;
import com.alan19.astral.biomes.AstralBiomes;
import com.alan19.astral.blocks.AstralBlocks;
import com.alan19.astral.blocks.BlockRenderHandler;
import com.alan19.astral.blocks.tileentities.AstralTiles;
import com.alan19.astral.client.gui.AstralContainers;
import com.alan19.astral.commands.AstralCommands;
import com.alan19.astral.compat.brews.AstralBotaniaBrews;
import com.alan19.astral.configs.AstralConfig;
import com.alan19.astral.dimensions.AstralDimensions;
import com.alan19.astral.effects.AstralEffects;
import com.alan19.astral.entity.AstralEntities;
import com.alan19.astral.entity.AstralModifiers;
import com.alan19.astral.items.AstralItems;
import com.alan19.astral.mentalconstructs.AstralMentalConstructs;
import com.alan19.astral.mentalconstructs.MentalConstructType;
import com.alan19.astral.network.AstralNetwork;
import com.alan19.astral.particle.AstralParticles;
import com.alan19.astral.particle.EtherealFlame;
import com.alan19.astral.particle.EtherealReplaceParticle;
import com.alan19.astral.particle.IntentionBeamParticle;
import com.alan19.astral.potions.AstralPotions;
import com.alan19.astral.recipe.AstralRecipeSerializer;
import com.alan19.astral.renderer.OfferingBrazierTileEntityRenderer;
import com.alan19.astral.renderer.entity.PhysicalBodyEntityRenderer;
import com.alan19.astral.util.ModCompat;
import com.alan19.astral.world.AstralConfiguredFeatures;
import com.alan19.astral.world.AstralFeatures;
import com.alan19.astral.world.AstralStructures;
import net.minecraft.client.Minecraft;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.alan19.astral.serializing.AstralSerializers.OPTIONAL_GAME_PROFILE;
import static com.alan19.astral.serializing.AstralSerializers.OPTIONAL_ITEMSTACK_HANDLER;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Astral.MOD_ID)
public class Astral {
    public static final String MOD_ID = "astral";
    public static final SimpleChannel INSTANCE = AstralNetwork.getNetworkChannel();
    public static final Logger LOGGER = LogManager.getLogger(Astral.MOD_ID);

    public Astral() {
        // Register the setup method for modloading
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        final IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::registerModels);
        modEventBus.addListener(this::setRenderLayers);
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> modEventBus.addListener(this::registerParticleFactories));

        // Register and load configs
        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        modLoadingContext.registerConfig(ModConfig.Type.COMMON, AstralConfig.initialize());
        AstralConfig.loadConfig(AstralConfig.getInstance().getSpec(), FMLPaths.CONFIGDIR.get().resolve("astral-common.toml"));
        MinecraftForge.EVENT_BUS.addListener(Astral::serverLoad);

        AstralEntities.register(modEventBus);
        AstralModifiers.register(modEventBus);
        AstralBlocks.register(modEventBus);
        AstralItems.register(modEventBus);
        AstralFeatures.register(modEventBus);
        AstralStructures.register(modEventBus);
        AstralParticles.register(modEventBus);
        AstralTiles.register(modEventBus);
        AstralBiomes.register(modEventBus);
        AstralEffects.register(modEventBus);
        AstralPotions.register(modEventBus);
        AstralRecipeSerializer.register(modEventBus);
        if (ModCompat.IS_BOTANIA_LOADED) {
            AstralBotaniaBrews.registerBrews(modEventBus);
        }
        AstralContainers.register(modEventBus);
        modEventBus.addListener(AstralPotions::registerRecipes);

        modEventBus.addListener(this::newRegistry);
        forgeBus.addListener(AstralFeatures::addFeatures);
        forgeBus.addListener(AstralFeatures::addDimensionSpacing);
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> modEventBus.addListener(ClientSetup::clientSetup));
    }

    private static <T extends IForgeRegistryEntry<T>> void makeRegistry(String name, Class<T> type) {
        new RegistryBuilder<T>()
                .setName(new ResourceLocation(Astral.MOD_ID, name))
                .setType(type)
                .getCreate();
    }

    @SubscribeEvent
    public static void serverLoad(RegisterCommandsEvent event) {
        AstralCommands.registerCommands(event.getDispatcher());
    }

    public void registerModels(ModelRegistryEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(AstralEntities.PHYSICAL_BODY_ENTITY.get(), PhysicalBodyEntityRenderer::new);
    }

    public void setRenderLayers(FMLClientSetupEvent event) {
        BlockRenderHandler.setRenderLayers();
        ClientRegistry.bindTileEntityRenderer(AstralTiles.OFFERING_BRAZIER.get(), OfferingBrazierTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(AstralTiles.ETHEREAL_MOB_SPAWNER.get(), EtherealMobSpawnerRenderer::new);
    }

    @OnlyIn(Dist.CLIENT)
    public void registerParticleFactories(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(AstralParticles.ETHEREAL_REPLACE_PARTICLE.get(), spriteSetIn -> new EtherealReplaceParticle.Factory());
        Minecraft.getInstance().particleEngine.register(AstralParticles.ETHEREAL_FLAME.get(), EtherealFlame.Factory::new);
        Minecraft.getInstance().particleEngine.register(AstralParticles.INTENTION_BEAM_PARTICLE.get(), IntentionBeamParticle.Factory::new);
    }

    public void newRegistry(RegistryEvent.NewRegistry newRegistry) {
        makeRegistry("mental_constructs", MentalConstructType.class);
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        AstralMentalConstructs.register(modBus);
    }

    private void setup(final FMLCommonSetupEvent event) {
        //Initializes worldgen
        AstralEntities.setupSpawnPlacement();
        event.enqueueWork(AstralDimensions::setupDimension);

        //Register Serializers
        EntityDataSerializers.registerSerializer(OPTIONAL_GAME_PROFILE);
        EntityDataSerializers.registerSerializer(OPTIONAL_ITEMSTACK_HANDLER);
//        BiomeManager.addBiome(Constants.ASTRAL, new BiomeManager.BiomeEntry(AstralBiomes.PSYSCAPE_BIOME.get(), 0));

        //Add Intention Beam behavior
        AstralAPI.addIntentionTrackerBehavior(AstralBlocks.ETHEREAL_SAPLING.get(), new EtherealSaplingIntentionTrackerBehavior());
        AstralAPI.addIntentionTrackerBehavior(AstralBlocks.INDEX_OF_KNOWLEDGE.get(), new IndexOfKnowledgeIntentionTrackerBehavior());
        AstralAPI.addIntentionTrackerBehavior(AstralBlocks.ETHERIC_POWDER.get(), new EthericPowderIntentionTrackerBehavior());

        // Registers features and structures
        event.enqueueWork(() -> {
            AstralStructures.setupStructures();
            AstralConfiguredFeatures.registerConfiguredFeatures();
        });

    }
}