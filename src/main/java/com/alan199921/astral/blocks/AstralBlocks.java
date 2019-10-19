package com.alan199921.astral.blocks;

import com.alan199921.astral.Astral;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstralBlocks {

    //Astral blocks
    @ObjectHolder("astral:snowberry_bush")
    public static final Block snowberryBush = null;

    @ObjectHolder("astral:feverweed_block")
    public static final Block feverweedBlock = null;

    @ObjectHolder("astral:ego_membrane")
    public static final Block egoMembrane = null;

    @ObjectHolder("astral:astral_meridian")
    public static final Block astralMeridian = null;

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
        registerBlock(event.getRegistry(), new SnowberryBush(), "snowberry_bush", false);
        registerBlock(event.getRegistry(), new FeverweedBlock(), "feverweed_block", false);
        registerBlock(event.getRegistry(), new EgoMembrane(), "ego_membrane", false);
        registerBlock(event.getRegistry(), new AstralMeridian(), "astral_meridian", false);
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
