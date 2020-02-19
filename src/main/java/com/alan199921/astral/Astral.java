package com.alan199921.astral;

import com.alan199921.astral.api.bodylink.BodyLinkCapability;
import com.alan199921.astral.api.bodylink.BodyLinkStorage;
import com.alan199921.astral.api.bodylink.IBodyLinkCapability;
import com.alan199921.astral.api.heightadjustment.HeightAdjustmentCapability;
import com.alan199921.astral.api.heightadjustment.HeightAdjustmentStorage;
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
import com.alan199921.astral.configs.AstralConfig;
import com.alan199921.astral.entities.PhysicalBodyEntity;
import com.alan199921.astral.entities.PhysicalBodyEntityRenderer;
import com.alan199921.astral.network.AstralNetwork;
import com.alan199921.astral.setup.AstralSetup;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import javax.annotation.Nullable;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Astral.MOD_ID)
public class Astral {
    public static final String MOD_ID = "astral";
    public static final SimpleChannel INSTANCE = AstralNetwork.getNetworkChannel();

    public static final AstralSetup setup = new AstralSetup();

    public Astral() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        // Register and load configs
        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        modLoadingContext.registerConfig(ModConfig.Type.COMMON, AstralConfig.initialize());
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
            CapabilityManager.INSTANCE.register(IHeightAdjustmentCapability.class, new HeightAdjustmentStorage(), HeightAdjustmentCapability::new);
            CapabilityManager.INSTANCE.register(IPsychicInventory.class, new Capability.IStorage<IPsychicInventory>() {
                @Nullable
                @Override
                public INBT writeNBT(Capability<IPsychicInventory> capability, IPsychicInventory instance, Direction side) {
                    return instance.serializeNBT();
                }

                @Override
                public void readNBT(Capability<IPsychicInventory> capability, IPsychicInventory instance, Direction side, INBT nbt) {
                    instance.deserializeNBT((CompoundNBT) nbt);
                }
            }, PsychicInventory::new);
            CapabilityManager.INSTANCE.register(ISleepManager.class, new Capability.IStorage<ISleepManager>() {
                @Nullable
                @Override
                public INBT writeNBT(Capability<ISleepManager> capability, ISleepManager instance, Direction side) {
                    return instance.serializeNBT();
                }

                @Override
                public void readNBT(Capability<ISleepManager> capability, ISleepManager instance, Direction side, INBT nbt) {
                    instance.deserializeNBT((CompoundNBT) nbt);
                }
            }, SleepManager::new);
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
