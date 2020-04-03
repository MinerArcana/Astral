package com.alan199921.astral.worldgen.islands;

import com.alan199921.astral.Astral;
import com.alan199921.astral.worldgen.AstralFeatures;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

public class AstralIslandPiece extends TemplateStructurePiece {
    private final String templateName;
    private final AstralIslandVariant variant;
    private final Rotation rotation;
    private final Mirror mirror;
    private final int numberOfTreesPlaced;

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
        this.loadTemplate(templateManager);
    }

    public AstralIslandPiece(TemplateManager templateManager, CompoundNBT nbt) {
        super(AstralFeatures.ASTRAL_ISLAND_PIECE, nbt);
        this.templateName = nbt.getString("Template");
        this.variant = AstralIslandVariant.getVariantFromIndex(nbt.getInt("Variant"));
        this.rotation = nbt.getString("Rot").equals("") ? Rotation.NONE : Rotation.valueOf(nbt.getString("Rot"));
        this.mirror = Mirror.valueOf(nbt.getString("Mi"));
        this.numberOfTreesPlaced = nbt.getInt("NumberOfTreesPlaced");
        this.loadTemplate(templateManager);
    }

    private void loadTemplate(TemplateManager templateManager) {
        Template template = templateManager.getTemplateDefaulted(new ResourceLocation(Astral.MOD_ID, "astral_island/" + templateName));
        PlacementSettings placementsettings = (new PlacementSettings()).setIgnoreEntities(true).setRotation(this.rotation).setMirror(this.mirror).addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
        this.setup(template, this.templatePosition, placementsettings);
    }

    @Override
    protected void handleDataMarker(String s, BlockPos blockPos, IWorld iWorld, Random random, MutableBoundingBox mutableBoundingBox) {
    }
}
