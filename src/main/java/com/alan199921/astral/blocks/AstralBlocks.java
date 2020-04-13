package com.alan199921.astral.blocks;

import com.alan199921.astral.Astral;
import com.alan199921.astral.blocks.etherealblocks.EtherDirt;
import com.alan199921.astral.blocks.etherealblocks.EtherGrass;
import com.alan199921.astral.blocks.etherealblocks.EtherealLeaves;
import com.alan199921.astral.blocks.etherealblocks.EtherealWood;
import net.minecraft.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AstralBlocks {
    private static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, Astral.MOD_ID);

    public static final RegistryObject<SnowberryBush> SNOWBERRY_BUSH = BLOCKS.register("snowberry_bush", SnowberryBush::new);
    public static final RegistryObject<FeverweedBlock> FEVERWEED_BLOCK = BLOCKS.register("feverweed_block", FeverweedBlock::new);
    public static final RegistryObject<EgoMembrane> EGO_MEMBRANE = BLOCKS.register("ego_membrane", EgoMembrane::new);
    public static final RegistryObject<AstralMeridian> ASTRAL_MERIDIAN = BLOCKS.register("astral_meridian", AstralMeridian::new);
    public static final RegistryObject<OfferingBrazier> OFFERING_BRAZIER = BLOCKS.register("offering_brazier", OfferingBrazier::new);
    public static final RegistryObject<EtherDirt> ETHER_DIRT = BLOCKS.register("ether_dirt", EtherDirt::new);
    public static final RegistryObject<EtherGrass> ETHER_GRASS = BLOCKS.register("ether_grass", EtherGrass::new);
    public static final RegistryObject<EthericPowder> ETHERIC_POWDER = BLOCKS.register("etheric_powder", EthericPowder::new);
    public static final RegistryObject<EtherealLeaves> ETHEREAL_LEAVES = BLOCKS.register("ethereal_leaves", EtherealLeaves::new);
    public static final RegistryObject<EtherealWood> ETHEREAL_WOOD = BLOCKS.register("ethereal_wood", EtherealWood::new);


    public static void register(IEventBus modBus) {
        BLOCKS.register(modBus);
    }

}
