package com.alan199921.astral;

import com.alan199921.astral.blocks.AstralMeridian;
import com.alan199921.astral.blocks.EgoMembrane;
import com.alan199921.astral.blocks.FeverweedBlock;
import com.alan199921.astral.blocks.SnowberryBush;
import com.alan199921.astral.capabilities.InnerRealmCapability;
import com.alan199921.astral.dimensions.ModDimensions;
import com.alan199921.astral.dimensions.TeleportationTools;
import com.alan199921.astral.items.EnlightenmentKey;
import com.alan199921.astral.items.Feverweed;
import com.alan199921.astral.items.IntrospectionMedicine;
import com.alan199921.astral.items.Snowberry;
import com.alan199921.astral.setup.ClientProxy;
import com.alan199921.astral.setup.IProxy;
import com.alan199921.astral.setup.ModSetup;
import com.alan199921.astral.setup.ServerProxy;
import net.minecraft.block.Block;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.UUID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Astral.MOD_ID)
public class Astral {
    public static final String MOD_ID = "astral";

    public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());
    public static ModSetup setup = new ModSetup();

    public Astral() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
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
        public static void onAttachWorldCapabilities(final AttachCapabilitiesEvent<World> event){
            event.addCapability(new ResourceLocation(MOD_ID, "inner_realm"), new ICapabilityProvider() {

                private final InnerRealmCapability innerRealmCapability= new InnerRealmCapability();

                @Nonnull
                @Override
                public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
                    return null;;
                }

            });
        }
    }
}
