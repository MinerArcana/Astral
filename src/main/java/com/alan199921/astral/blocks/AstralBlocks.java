package com.alan199921.astral.blocks;

import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstralBlocks {

    //Astral blocks
    public static Block snowberryBush;
    public static Block feverweedBlock;
    public static Block egoMembrane;
    public static Block astralMeridian;

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
        snowberryBush = registerBlock(event.getRegistry(), new SnowberryBush(), "snowberry_bush", false);
        feverweedBlock = registerBlock(event.getRegistry(), new FeverweedBlock(), "feverweed_block", false);
        egoMembrane = registerBlock(event.getRegistry(), new EgoMembrane(), "ego_membrane", false);
        astralMeridian = registerBlock(event.getRegistry(), new AstralMeridian(), "astral_meridian", false);
    }

    /**
     * Creates a block and puts it in the Astral item tab
     *
     * @param registry        The event registry to register a block
     * @param block           A Block object
     * @param name            The registry name of the block
     * @param createBlockItem Whether an item version of the block should be created (for items that the player should
     *                        not be able to access)
     * @return A Block object
     */
    private static Block registerBlock(IForgeRegistry<Block> registry, Block block, String name, boolean createBlockItem) {
        block.setRegistryName(name);
        registry.register(block);
        return block;
    }
}
