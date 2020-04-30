package com.alan199921.astral.blocks;

import com.alan199921.astral.Astral;
import com.alan199921.astral.blocks.etherealblocks.*;
import net.minecraft.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AstralBlocks {
    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, Astral.MOD_ID);

    //Plants
    public static final RegistryObject<SnowberryBush> SNOWBERRY_BUSH = BLOCKS.register("snowberry_bush", SnowberryBush::new);
    public static final RegistryObject<Feverweed> FEVERWEED_BLOCK = BLOCKS.register("feverweed_block", Feverweed::new);

    //Inner Realm
    public static final RegistryObject<EgoMembrane> EGO_MEMBRANE = BLOCKS.register("ego_membrane", EgoMembrane::new);
    public static final RegistryObject<AstralMeridian> ASTRAL_MERIDIAN = BLOCKS.register("astral_meridian", AstralMeridian::new);
    public static final RegistryObject<OfferingBrazier> OFFERING_BRAZIER = BLOCKS.register("offering_brazier", OfferingBrazier::new);

    //Etheric Isles
    public static final RegistryObject<EtherDirt> ETHER_DIRT = BLOCKS.register("ether_dirt", EtherDirt::new);
    public static final RegistryObject<EtherGrass> ETHER_GRASS = BLOCKS.register("ether_grass", EtherGrass::new);
    public static final RegistryObject<EthericPowder> ETHERIC_POWDER = BLOCKS.register("etheric_powder", EthericPowder::new);
    public static final RegistryObject<EtherealTallGrassBlock> ETHEREAL_GRASS = BLOCKS.register("ethereal_grass", EtherealTallGrassBlock::new);
    public static final RegistryObject<EtherealTallGrassBlock> ETHEREAL_FERN = BLOCKS.register("ethereal_fern", EtherealTallGrassBlock::new);
    public static final RegistryObject<EtherealDoublePlantBlock> TALL_ETHEREAL_GRASS = BLOCKS.register("tall_ethereal_grass", EtherealDoublePlantBlock::new);
    public static final RegistryObject<EtherealDoublePlantBlock> LARGE_ETHEREAL_FERN = BLOCKS.register("large_ethereal_fern", EtherealDoublePlantBlock::new);

    //Ethereal Trees
    public static final RegistryObject<EtherealLogs> ETHEREAL_WOOD = BLOCKS.register("ethereal_wood", EtherealLogs::new);
    public static final RegistryObject<EtherealLeaves> ETHEREAL_LEAVES = BLOCKS.register("ethereal_leaves", EtherealLeaves::new);
    public static final RegistryObject<EtherealLogs> STRIPPED_ETHEREAL_LOG = BLOCKS.register("stripped_ethereal_log", EtherealLogs::new);
    //    public static final RegistryObject<StrippedEtherealWood> STRIPPED_ETHEREAL_WOOD = BLOCKS.register("stripped_ethereal_wood", StrippedEtherealWood::new);
    public static final RegistryObject<EtherealBlock> ETHEREAL_PLANKS = BLOCKS.register("ethereal_planks", EtherealPlanks::new);
    public static final RegistryObject<EtherealDoor> ETHEREAL_DOOR = BLOCKS.register("ethereal_door", EtherealDoor::new);
    public static final RegistryObject<EtherealTrapdoor> ETHEREAL_TRAPDOOR = BLOCKS.register("ethereal_trapdoor", EtherealTrapdoor::new);

    public static void register(IEventBus modBus) {
        BLOCKS.register(modBus);
    }

}
