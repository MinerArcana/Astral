package com.alan19.astral.util;

import net.minecraft.world.level.material.Material;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.world.level.material.Material.*;

public class IntentionBeamMaterials {
    public static final List<Material> level0Materials = List.of(AIR, STRUCTURAL_AIR, PLANT, WATER_PLANT, REPLACEABLE_PLANT, REPLACEABLE_WATER_PLANT, WATER, BUBBLE_COLUMN, FIRE, DECORATION, WEB, LEAVES, BAMBOO_SAPLING);
    public static final List<Material> level1Materials = List.of(GLASS, LAVA, ICE_SOLID, TOP_SNOW, SNOW, CAKE, ICE);
    public static final List<Material> level2Materials = List.of(BUILDABLE_GLASS, CLAY, STONE, DIRT, GRASS, SAND, SPONGE, SHULKER_SHELL, WOOD, BAMBOO, WOOL, EXPLOSIVE, CACTUS, PISTON, VEGETABLE, EGG);
    public static final List<Material> level3Materials = List.of(METAL, HEAVY_METAL);

    public static List<Material> getMaterialsForLevel(int level) {
        ArrayList<Material> materials = new ArrayList<>();
        if (level >= 0) {
            materials.addAll(level0Materials);
        }
        if (level >= 1) {
            materials.addAll(level1Materials);
        }
        if (level >= 2) {
            materials.addAll(level2Materials);
        }
        if (level >= 3){
            materials.addAll(level3Materials);
        }
        return materials;
    }
}
