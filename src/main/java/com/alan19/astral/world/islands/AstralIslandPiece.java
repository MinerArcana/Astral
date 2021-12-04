package com.alan19.astral.world.islands;

import com.alan19.astral.Astral;
import com.alan19.astral.world.AstralStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class AstralIslandPiece extends TemplateStructurePiece {
    private final String templateName;
    private final AstralIslandVariant variant;
    private final Rotation rotation;
    private final Mirror mirror;

    public AstralIslandPiece(StructureManager templateManager, CompoundTag nbt) {
        super(AstralStructures.ASTRAL_ISLAND_PIECE, nbt);
        this.templateName = nbt.getString("Template");
        this.variant = AstralIslandVariant.getVariantFromIndex(nbt.getInt("Variant"));
        this.rotation = nbt.getString("Rot").equals("") ? Rotation.NONE : Rotation.valueOf(nbt.getString("Rot"));
        this.mirror = Mirror.valueOf(nbt.getString("Mi"));
        this.loadTemplate(templateManager);
    }

    private void loadTemplate(StructureManager templateManager) {
        StructureTemplate template = templateManager.getOrCreate(new ResourceLocation(Astral.MOD_ID, "astral_island/" + templateName));
        StructurePlaceSettings placementsettings = (new StructurePlaceSettings()).setIgnoreEntities(true).setRotation(this.rotation).setMirror(this.mirror).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        this.setup(template, this.templatePosition, placementsettings);
    }

    /**
     * (abstract) Helper method to read subclass data from NBT
     */
    @Override
    protected void addAdditionalSaveData(@Nonnull CompoundTag tagCompound) {
        super.addAdditionalSaveData(tagCompound);
        tagCompound.putString("Template", this.templateName);
        tagCompound.putInt("Variant", this.variant.getIndex());
        tagCompound.putString("Rot", this.placeSettings.getRotation().name());
        tagCompound.putString("Mi", this.placeSettings.getMirror().name());
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean postProcess(WorldGenLevel seedReader, StructureFeatureManager structureManager, ChunkGenerator chunkGenerator, Random random, BoundingBox mutableBoundingBox, ChunkPos chunkPos, BlockPos blockPos) {
        return super.postProcess(seedReader, structureManager, chunkGenerator, random, mutableBoundingBox, chunkPos, blockPos);
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void handleDataMarker(String function, BlockPos pos, ServerLevelAccessor worldIn, Random rand, BoundingBox sbb) {
        // We don't need data markers
    }
}
