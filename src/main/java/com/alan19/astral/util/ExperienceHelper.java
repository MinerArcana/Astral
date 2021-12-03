package com.alan19.astral.util;

import net.minecraft.entity.player.PlayerEntity;

/**
 * Helper class that manages XP. Adapted the ExperienceHelper class from Reskillable: https://github.com/Coders-After-Dark/Reskillable which is from OpenBlocksLib: https://github.com/OpenMods/OpenModsLib to 1.15
 */
public class ExperienceHelper {
    public static int getPlayerXP(PlayerEntity player) {
        return (int) (getExperienceForLevel(player.experienceLevel) + player.experienceProgress * player.getXpNeededForNextLevel());
    }

    public static void drainPlayerXP(PlayerEntity player, int amount) {
        addPlayerXP(player, -amount);
    }

    public static void addPlayerXP(PlayerEntity player, int amount) {
        int experience = getPlayerXP(player) + amount;
        player.totalExperience = experience;
        player.experienceLevel = getLevelForExperience(experience);
        int expForLevel = getExperienceForLevel(player.experienceLevel);
        player.experienceProgress = (float) (experience - expForLevel) / (float) player.getXpNeededForNextLevel();
    }

    public static int getExperienceForLevel(int level) {
        if (level == 0) {
            return 0;
        }
        if (level > 0 && level < 17) {
            return level * level + 6 * level;
        }
        else if (level > 16 && level < 32) {
            return (int) (2.5 * level * level - 40.5 * level + 360);
        }
        return (int) (4.5 * level * level - 162.5 * level + 2220);
    }

    public static int getLevelForExperience(int experience) {
        int i = 0;
        while (getExperienceForLevel(i) <= experience) {
            i++;
        }
        return i - 1;
    }
}
