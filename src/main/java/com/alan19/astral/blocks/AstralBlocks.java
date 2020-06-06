package com.alan19.astral.blocks;

import com.alan19.astral.Astral;
import com.alan19.astral.blocks.etherealblocks.*;
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

    //Etheric Growths
    public static final RegistryObject<EtherealTallGrassBlock> CYAN_SWARD = BLOCKS.register("ethereal_grass", EtherealTallGrassBlock::new);
    public static final RegistryObject<EtherealTallGrassBlock> CYAN_CYST = BLOCKS.register("ethereal_fern", EtherealTallGrassBlock::new);
    public static final RegistryObject<EtherealDoublePlantBlock> TALL_CYAN_SWARD = BLOCKS.register("tall_ethereal_grass", EtherealDoublePlantBlock::new);
    public static final RegistryObject<EtherealDoublePlantBlock> LARGE_CYAN_CYST = BLOCKS.register("large_ethereal_fern", EtherealDoublePlantBlock::new);
    public static final RegistryObject<EtherealTallGrassBlock> CYAN_BELLEVINE = BLOCKS.register("cyan_bellevine", EtherealTallGrassBlock::new);
    public static final RegistryObject<EtherealTallGrassBlock> CYAN_BLISTERWART = BLOCKS.register("cyan_blisterwart", EtherealTallGrassBlock::new);
    public static final RegistryObject<EtherealTallGrassBlock> CYAN_KLORID = BLOCKS.register("cyan_klorid", EtherealTallGrassBlock::new);
    public static final RegistryObject<EtherealTallGrassBlock> CYAN_MORKEL = BLOCKS.register("cyan_morkel", EtherealTallGrassBlock::new);
    public static final RegistryObject<EtherealTallGrassBlock> CYAN_PODS = BLOCKS.register("cyan_pods", EtherealTallGrassBlock::new);

    //Ethereal Trees
    public static final RegistryObject<EtherealLog> ETHEREAL_WOOD = BLOCKS.register("ethereal_wood", StrippableEtherealLog::new);
    public static final RegistryObject<EtherealLeaves> ETHEREAL_LEAVES = BLOCKS.register("ethereal_leaves", EtherealLeaves::new);
    public static final RegistryObject<EtherealLog> STRIPPED_ETHEREAL_LOG = BLOCKS.register("stripped_ethereal_log", EtherealLog::new);
    public static final RegistryObject<StrippedEtherealWood> STRIPPED_ETHEREAL_WOOD = BLOCKS.register("stripped_ethereal_wood", StrippedEtherealWood::new);
    public static final RegistryObject<EtherealBlock> ETHEREAL_PLANKS = BLOCKS.register("ethereal_planks", EtherealPlanks::new);
    public static final RegistryObject<EtherealDoor> ETHEREAL_DOOR = BLOCKS.register("ethereal_door", EtherealDoor::new);
    public static final RegistryObject<EtherealTrapdoor> ETHEREAL_TRAPDOOR = BLOCKS.register("ethereal_trapdoor", EtherealTrapdoor::new);
    public static final RegistryObject<EtherealSapling> ETHEREAL_SAPLING = BLOCKS.register("ethereal_sapling", EtherealSapling::new);

    //Mental Constructs
    public static final RegistryObject<ComfortableCushion> COMFORTABLE_CUSHION = BLOCKS.register("comfortable_cushion", ComfortableCushion::new);
    public static final RegistryObject<IndexOfKnowledge> INDEX_OF_KNOWLEDGE = BLOCKS.register("index_of_knowledge", IndexOfKnowledge::new);

    //Other Ethereal Blocks
    public static final RegistryObject<MetaphoricStone> METAPHORIC_STONE = BLOCKS.register("metaphoric_stone", MetaphoricStone::new);
    public static final RegistryObject<BoneSheets> BONE_SHEETS = BLOCKS.register("bone_sheets", BoneSheets::new);
    public static final RegistryObject<MetaphoricFleshBlock> METAPHORIC_FLESH_BLOCK = BLOCKS.register("metaphoric_flesh_block", MetaphoricFleshBlock::new);
    public static final RegistryObject<Dreamweb> DREAMWEB = BLOCKS.register("dreamweb", Dreamweb::new);

    public static void register(IEventBus modBus) {
        BLOCKS.register(modBus);
    }

}
