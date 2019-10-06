package com.alan199921.astral.blocks;

import com.alan199921.astral.Astral;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstralBlocks {

    //Astral blocks
    public static Block snowberryBush;
    public static Block feverweedBlock;
    public static Block egoMembrane;
    public static Block astralMeridian;

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
        snowberryBush = registerBlock(new SnowberryBush(), "snowberry_bush", false);
        feverweedBlock = registerBlock(new FeverweedBlock(), "feverweed_block", false);
        egoMembrane = registerBlock(new EgoMembrane(), "ego_membrane", false);
        astralMeridian = registerBlock(new AstralMeridian(), "astral_meridian", false);
    }

    /**
     * Creates a block and puts it in the Astral item tab
     *
     * @param block           A Block object
     * @param name            The registry name of the block
     * @param createBlockItem Whether an item version of the block should be created (for items that the player should
     *                        not be able to access)
     * @return A Block object
     */
    private static Block registerBlock(Block block, String name, boolean createBlockItem) {
        if (createBlockItem) {
            BlockItem itemBlock = new BlockItem(block, new Item.Properties().group(Astral.setup.astralItems));
            itemBlock.setRegistryName(name);
            ForgeRegistries.ITEMS.register(itemBlock);
        }
        block.setRegistryName(name);
        ForgeRegistries.BLOCKS.register(block);
        return block;
    }
}
