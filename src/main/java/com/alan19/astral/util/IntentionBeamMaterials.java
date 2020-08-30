package com.alan19.astral.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.material.Material;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.block.material.Material.*;

public class IntentionBeamMaterials {
    public static final ImmutableList<Material> level0Materials = ImmutableList.of(AIR, STRUCTURE_VOID, PLANTS, OCEAN_PLANT, TALL_PLANTS, SEA_GRASS, WATER, BUBBLE_COLUMN, FIRE, MISCELLANEOUS, WEB, LEAVES, BAMBOO_SAPLING);
    public static final ImmutableList<Material> level1Materials = ImmutableList.of(GLASS, LAVA, PACKED_ICE, SNOW, SNOW_BLOCK, CAKE, ICE);
    public static final ImmutableList<Material> level2Materials = ImmutableList.of(REDSTONE_LIGHT, CLAY, ROCK, EARTH, ORGANIC, SAND, SPONGE, SHULKER, WOOD, BAMBOO, WOOL, TNT, CACTUS, PISTON, CORAL, GOURD, DRAGON_EGG);
    public static final ImmutableList<Material> level3Materials = ImmutableList.of(IRON, ANVIL);

    public static List<Material> getMaterialsForLevel(int level){
        ArrayList<Material> materials = new ArrayList<>();
        if (level >= 0){
            materials.addAll(level0Materials);
        }
        if (level >= 1){
            materials.addAll(level1Materials);
        }
        if (level >= 2){
            materials.addAll(level2Materials);
        }
        if (level >= 3){
            materials.addAll(level3Materials);
        }
        return materials;
    }
}
