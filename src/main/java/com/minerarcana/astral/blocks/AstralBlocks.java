package com.minerarcana.astral.blocks;

import com.minerarcana.astral.Astral;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AstralBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Astral.MOD_ID);

    //Plants
    public static final RegistryObject<SnowberryBush> SNOWBERRY_BUSH = BLOCKS.register("snowberry_bush", SnowberryBush::new);
    public static final RegistryObject<Feverweed> FEVERWEED = BLOCKS.register("feverweed", Feverweed::new);

    public static void register(IEventBus modBus) {
        BLOCKS.register(modBus);
    }

}
