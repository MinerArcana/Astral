package com.alan199921.astral.world.islands;

import com.alan199921.astral.Astral;
import com.alan199921.astral.blocks.AstralBlocks;
import com.alan199921.astral.world.AstralFeatures;
import com.alan199921.astral.world.trees.EtherealTreeConfig;
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
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
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
    private ChunkGenerator<?> chunkGenerator;

    public AstralIslandPiece(TemplateManager templateManager, AstralIslandVariant variant, String templateName, BlockPos templatePosition, Rotation rotation) {
        this(templateManager, variant, templateName, templatePosition, rotation, Mirror.NONE);
    }

    public AstralIslandPiece(TemplateManager templateManager, AstralIslandVariant variant, String templateName, BlockPos templatePosition, Rotation rotation, Mirror mirror) {
        super(AstralFeatures.ASTRAL_ISLAND_PIECE, 0);
        this.templateName = templateName;
        this.variant = variant;
        this.templatePosition = templatePosition;
        this.rotation = rotation;
        this.mirror = mirror;
        this.numberOfTreesPlaced = 0;
        this.treeLocations = new ArrayList<>();
        this.loadTemplate(templateManager);
    }

    public AstralIslandPiece(TemplateManager templateManager, CompoundNBT nbt) {
        super(AstralFeatures.ASTRAL_ISLAND_PIECE, nbt);
        this.templateName = nbt.getString("Template");
        this.variant = AstralIslandVariant.getVariantFromIndex(nbt.getInt("Variant"));
        this.rotation = nbt.getString("Rot").equals("") ? Rotation.NONE : Rotation.valueOf(nbt.getString("Rot"));
        this.mirror = Mirror.valueOf(nbt.getString("Mi"));
        this.numberOfTreesPlaced = nbt.getInt("NumberOfTreesPlaced");
        this.treeLocations = new ArrayList<>();
        nbt.getList("treeLocations", Constants.NBT.TAG_COMPOUND).forEach(inbt -> treeLocations.add(NBTUtil.readBlockPos((CompoundNBT) inbt)));

        this.loadTemplate(templateManager);
    }

    private void loadTemplate(TemplateManager templateManager) {
        Template template = templateManager.getTemplateDefaulted(new ResourceLocation(Astral.MOD_ID, "astral_island/" + templateName));
        PlacementSettings placementsettings = (new PlacementSettings()).setIgnoreEntities(true).setRotation(this.rotation).setMirror(this.mirror).addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
        this.setup(template, this.templatePosition, placementsettings);
    }

    @Override
    protected void handleDataMarker(@Nonnull String s, @Nonnull BlockPos blockPos, @Nonnull IWorld world, @Nonnull Random random, @Nonnull MutableBoundingBox mutableBoundingBox) {
        if ("astral:island_feature".equals(s)) {
            int i = random.nextInt(15);
            switch (i) {
                case 1:
                    world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 2);
                    break;
                case 2:

                    if (this.numberOfTreesPlaced < 3 && farEnoughFromAnotherTree(blockPos)) {
                        world.setBlockState(blockPos, AstralBlocks.ETHER_GRASS.get().getDefaultState(), 2);
                        ConfiguredFeature<EtherealTreeConfig, ?> treeFeature = AstralFeatures.ETHEREAL_TREE.get().withConfiguration(new EtherealTreeConfig());
                        treeFeature.place(world, chunkGenerator, random, blockPos.up());
                        this.numberOfTreesPlaced++;
                        treeLocations.add(blockPos);
                    }
                    break;
                case 3:
                case 7:
                    world.setBlockState(blockPos, AstralBlocks.ETHER_GRASS.get().getDefaultState(), 2);
                    world.setBlockState(blockPos.up(), AstralBlocks.ETHEREAL_FERN.get().getDefaultState(), 2);
                    break;
                case 4:
                case 8:
                    world.setBlockState(blockPos, AstralBlocks.ETHER_GRASS.get().getDefaultState(), 2);
                    world.setBlockState(blockPos.up(), AstralBlocks.ETHEREAL_GRASS.get().getDefaultState(), 2);
                    break;
                case 5:
                    world.setBlockState(blockPos, AstralBlocks.ETHER_GRASS.get().getDefaultState(), 2);
                    world.setBlockState(blockPos.up(), AstralBlocks.LARGE_ETHEREAL_FERN.get().getDefaultState(), 2);
                    world.setBlockState(blockPos.up().up(), AstralBlocks.LARGE_ETHEREAL_FERN.get().getDefaultState().with(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER), 2);
                    break;
                case 6:
                    world.setBlockState(blockPos, AstralBlocks.ETHER_GRASS.get().getDefaultState(), 2);
                    world.setBlockState(blockPos.up(), AstralBlocks.TALL_ETHEREAL_GRASS.get().getDefaultState(), 2);
                    world.setBlockState(blockPos.up().up(), AstralBlocks.TALL_ETHEREAL_GRASS.get().getDefaultState().with(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER), 2);
                    break;
                default:
                    world.setBlockState(blockPos, AstralBlocks.ETHER_GRASS.get().getDefaultState(), 2);
                    break;
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
        ListNBT posList = new ListNBT();
        treeLocations.forEach(pos -> posList.add(NBTUtil.writeBlockPos(pos)));
        tagCompound.put("treeLocations", posList);
    }

    @Override
    public boolean func_225577_a_(@Nonnull IWorld world, @Nonnull ChunkGenerator<?> chunkGenerator, @Nonnull Random random, @Nonnull MutableBoundingBox mutableBoundingBox, @Nonnull ChunkPos chunkPos) {
        this.chunkGenerator = chunkGenerator;
        return super.func_225577_a_(world, chunkGenerator, random, mutableBoundingBox, chunkPos);
    }
}
