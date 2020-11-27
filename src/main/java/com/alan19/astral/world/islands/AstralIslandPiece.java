package com.alan19.astral.world.islands;

import com.alan19.astral.Astral;
import com.alan19.astral.blocks.AstralBlocks;
import com.alan19.astral.tags.AstralTags;
import com.alan19.astral.world.AstralStructures;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoublePlantBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;

public class AstralIslandPiece extends TemplateStructurePiece {
    private final String templateName;
    private final AstralIslandVariant variant;
    private final Rotation rotation;
    private final Mirror mirror;
    private final ArrayList<BlockPos> treeLocations;
    private int numberOfTreesPlaced;
    private int numberOfWebsPlaced;
    private ChunkGenerator chunkGenerator;

    public AstralIslandPiece(TemplateManager templateManager, AstralIslandVariant variant, String templateName, BlockPos templatePosition, Rotation rotation) {
        this(templateManager, variant, templateName, templatePosition, rotation, Mirror.NONE);
    }

    public AstralIslandPiece(TemplateManager templateManager, AstralIslandVariant variant, String templateName, BlockPos templatePosition, Rotation rotation, Mirror mirror) {
        super(AstralStructures.ASTRAL_ISLAND_PIECE, 0);
        this.templateName = templateName;
        this.variant = variant;
        this.templatePosition = templatePosition;
        this.rotation = rotation;
        this.mirror = mirror;
        this.numberOfTreesPlaced = 0;
        this.numberOfWebsPlaced = 0;
        this.treeLocations = new ArrayList<>();
        this.loadTemplate(templateManager);
    }

    public AstralIslandPiece(TemplateManager templateManager, CompoundNBT nbt) {
        super(AstralStructures.ASTRAL_ISLAND_PIECE, nbt);
        this.templateName = nbt.getString("Template");
        this.variant = AstralIslandVariant.getVariantFromIndex(nbt.getInt("Variant"));
        this.rotation = nbt.getString("Rot").equals("") ? Rotation.NONE : Rotation.valueOf(nbt.getString("Rot"));
        this.mirror = Mirror.valueOf(nbt.getString("Mi"));
        this.numberOfTreesPlaced = nbt.getInt("NumberOfTreesPlaced");
        this.treeLocations = new ArrayList<>();
        this.numberOfWebsPlaced = nbt.getInt("numberOfWebsPlaced");
        nbt.getList("treeLocations", Constants.NBT.TAG_COMPOUND).forEach(inbt -> treeLocations.add(NBTUtil.readBlockPos((CompoundNBT) inbt)));

        this.loadTemplate(templateManager);
    }

    private void loadTemplate(TemplateManager templateManager) {
        Template template = templateManager.getTemplateDefaulted(new ResourceLocation(Astral.MOD_ID, "astral_island/" + templateName));
        PlacementSettings placementsettings = (new PlacementSettings()).setIgnoreEntities(true).setRotation(this.rotation).setMirror(this.mirror).addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
        this.setup(template, this.templatePosition, placementsettings);
    }

    @Override
    protected void handleDataMarker(@Nonnull String s, @Nonnull BlockPos blockPos, @Nonnull IServerWorld world, @Nonnull Random random, @Nonnull MutableBoundingBox mutableBoundingBox) {
        if ("astral:island_feature".equals(s)) {
            int i = random.nextInt(60);
            if (i < 10) {
                world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 2);
            }
            else if (i < 20) {
//                if (this.numberOfTreesPlaced < 3 && farEnoughFromAnotherTree(blockPos)) {
//                    world.setBlockState(blockPos, AstralBlocks.ETHER_GRASS.get().getDefaultState(), 2);
//                    final boolean generated = AstralFeatures.ETHEREAL_TREE.get().generate(world.getWorld(), chunkGenerator, random, blockPos.up(), EtherealTree.ETHEREAL_TREE_CONFIG.get());
//                    if (generated) {
//                        this.numberOfTreesPlaced++;
//                        treeLocations.add(blockPos);
//                    }
//                }
            }
            else if (i == 20) {
                if (numberOfWebsPlaced < 2) {
                    world.setBlockState(blockPos, AstralBlocks.CRYSTAL_WEB.get().getDefaultState(), 2);
                    numberOfWebsPlaced++;
                }
            }
            else if (i < 30) {
                world.setBlockState(blockPos, AstralBlocks.ETHER_GRASS.get().getDefaultState(), 2);
                world.setBlockState(blockPos.up(), AstralTags.SMALL_ETHERIC_GROWTHS.getRandomElement(random).getDefaultState(), 2);
            }
            else if (i < 40) {
                world.setBlockState(blockPos, AstralBlocks.ETHER_GRASS.get().getDefaultState(), 2);
                Block randomLargeEthericGrowth = AstralTags.LARGE_ETHERIC_GROWTHS.getRandomElement(random);
                world.setBlockState(blockPos.up(), randomLargeEthericGrowth.getDefaultState(), 2);
                world.setBlockState(blockPos.up().up(), randomLargeEthericGrowth.getDefaultState().with(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER), 2);
            }
            else {
                world.setBlockState(blockPos, AstralBlocks.ETHER_GRASS.get().getDefaultState(), 2);
            }

        }

    }

    private boolean farEnoughFromAnotherTree(BlockPos blockPos) {
        return IntStream.range(0, treeLocations.size()).noneMatch(i -> blockPos.withinDistance(blockPos, 8));
    }

    /**
     * (abstract) Helper method to read subclass data from NBT
     */
    @Override
    protected void readAdditional(@Nonnull CompoundNBT tagCompound) {
        super.readAdditional(tagCompound);
        tagCompound.putString("Template", this.templateName);
        tagCompound.putInt("Variant", this.variant.getIndex());
        tagCompound.putString("Rot", this.placeSettings.getRotation().name());
        tagCompound.putString("Mi", this.placeSettings.getMirror().name());
        tagCompound.putInt("NumberOfTreesPlaced", this.numberOfTreesPlaced);
        tagCompound.putInt("numberOfWebsPlaced", this.numberOfWebsPlaced);
        ListNBT posList = new ListNBT();
        treeLocations.forEach(pos -> posList.add(NBTUtil.writeBlockPos(pos)));
        tagCompound.put("treeLocations", posList);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean func_230383_a_(ISeedReader seedReader, StructureManager structureManager, ChunkGenerator chunkGenerator, Random random, MutableBoundingBox mutableBoundingBox, ChunkPos chunkPos, BlockPos blockPos) {
        this.chunkGenerator = chunkGenerator;
        return super.func_230383_a_(seedReader, structureManager, chunkGenerator, random, mutableBoundingBox, chunkPos, blockPos);
    }
}
