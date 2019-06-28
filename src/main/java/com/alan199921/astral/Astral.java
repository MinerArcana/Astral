package com.alan199921.astral;

import com.alan199921.astral.blocks.FeverweedBlock;
import com.alan199921.astral.blocks.SnowberryBush;
import com.alan199921.astral.dimensions.innerrealm.InnerRealmDimension;
import com.alan199921.astral.items.Feverweed;
import com.alan199921.astral.items.IntrospectionMedicine;
import com.alan199921.astral.items.Snowberry;
import com.alan199921.astral.setup.ClientProxy;
import com.alan199921.astral.setup.IProxy;
import com.alan199921.astral.setup.ModSetup;
import com.alan199921.astral.setup.ServerProxy;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.BiFunction;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Astral.MOD_ID)
public class Astral {
    public static final String MOD_ID = "astral";
    public static final ResourceLocation MINING_DIM = new ResourceLocation("minecraft:inner_realm");

    public static ModDimension innerRealm = new ModDimension() {
        @Override
        public BiFunction<World, DimensionType, ? extends Dimension> getFactory() {
            return InnerRealmDimension::new;
        }
    }.setRegistryName(MINING_DIM);

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
        }

        @SubscribeEvent
        public static void onItemRegistry(final RegistryEvent.Register<Item> event) {
            new Item.Properties().group(setup.itemGroup);
            // register a new block here
            event.getRegistry().register(new Snowberry());
            event.getRegistry().register(new Feverweed());
            event.getRegistry().register(new IntrospectionMedicine());
        }

        @SubscribeEvent
        public static void onDimensionModRegistry(final RegistryEvent.Register<ModDimension> event) {
            event.getRegistry().register(innerRealm);
            DimensionManager.registerDimension(new ResourceLocation("minecraft", "inner_realm"), innerRealm, null, true);
        }
    }
}
