package com.alan199921.astral.blocks;

import com.alan199921.astral.blocks.etherealblocks.EtherDirt;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstralBlocks {

    //Astral blocks
    @ObjectHolder("astral:snowberry_bush")
    public static final Block SNOWBERRY_BUSH = null;

    @ObjectHolder("astral:feverweed_block")
    public static final Block FEVERWEED_BLOCK = null;

    @ObjectHolder("astral:ego_membrane")
    public static final Block EGO_MEMBRANE = null;

    @ObjectHolder("astral:astral_meridian")
    public static final Block ASTRAL_MERIDIAN = null;

    @ObjectHolder("astral:offering_brazier")
    public static final Block OFFERING_BRAZIER = null;

    @ObjectHolder("astral:ether_dirt")
    public static final Block ETHER_DIRT = null;

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
        final Block snowberryBush = registerBlock(event.getRegistry(), new SnowberryBush(), "snowberry_bush");
        final Block feverweed = registerBlock(event.getRegistry(), new FeverweedBlock(), "feverweed_block");
        registerBlock(event.getRegistry(), new EgoMembrane(), "ego_membrane");
        registerBlock(event.getRegistry(), new AstralMeridian(), "astral_meridian");
        final Block offeringBrazier = registerBlock(event.getRegistry(), new OfferingBrazier(), "offering_brazier");
        registerBlock(event.getRegistry(), new EtherDirt(), "ether_dirt");

        if (FMLEnvironment.dist.isClient()) {
            RenderType cutout = RenderType.getCutout();
            RenderTypeLookup.setRenderLayer(feverweed, cutout);
            RenderTypeLookup.setRenderLayer(snowberryBush, cutout);
            RenderTypeLookup.setRenderLayer(offeringBrazier, cutout);
        }
    }

    /**
     * Creates a block and puts it in the Astral item tab
     *
     * @param registry The event registry to register a block
     * @param block    A Block object
     * @param name     The registry name of the block
     * @return A Block object
     */
    private static Block registerBlock(IForgeRegistry<Block> registry, Block block, String name) {
        block.setRegistryName(name);
        registry.register(block);
        return block;
    }
}
