package com.alan199921.astral.events;


import com.alan199921.astral.Astral;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

import static net.minecraft.client.gui.AbstractGui.GUI_ICONS_LOCATION;
import static net.minecraftforge.client.ForgeIngameGui.left_height;

public class AstralHealthBar {

    public static final ResourceLocation HEART_TEXTURE = new ResourceLocation(Astral.MOD_ID, "textures/gui/astral_health_v2.png");

    public static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
        Minecraft.getInstance().ingameGUI.blit(x, y, textureX, textureY, width, height);
    }

    static void renderAstralHearts(Minecraft mc, PlayerEntity player) {
        int scaledWidth = mc.mainWindow.getScaledWidth();
        int scaledHeight = mc.mainWindow.getScaledHeight();

        mc.getProfiler().startSection("health");
        GlStateManager.enableBlend();
        mc.getTextureManager().bindTexture(HEART_TEXTURE);
        int health = MathHelper.ceil(player.getHealth());
        int ticks = 0;
        int playerHealth = 0;
        long lastSystemTime = 0;
        boolean highlight = false;
        if (health < playerHealth && player.hurtResistantTime > 0) {
            lastSystemTime = Util.milliTime();
        } else if (health > playerHealth && player.hurtResistantTime > 0) {
            lastSystemTime = Util.milliTime();
        }

        int lastPlayerHealth = 0;
        if (Util.milliTime() - lastSystemTime > 1000L) {
            playerHealth = health;
            lastPlayerHealth = health;
            lastSystemTime = Util.milliTime();
        }

        playerHealth = health;
        int healthLast = lastPlayerHealth;

        IAttributeInstance attrMaxHealth = player.getAttribute(SharedMonsterAttributes.MAX_HEALTH);
        float healthMax = (float) attrMaxHealth.getValue();
        float absorb = MathHelper.ceil(player.getAbsorptionAmount());

        int healthRows = MathHelper.ceil((healthMax + absorb) / 2.0F / 10.0F);
        int rowHeight = Math.max(10 - (healthRows - 2), 3);

        Random rand = new Random();
        rand.setSeed(ticks * 312871);

        int left = scaledWidth / 2 - 91;
        int top = scaledHeight - left_height;
        left_height += (healthRows * rowHeight);
        if (rowHeight != 10) left_height += 10 - rowHeight;

        int regen = -1;
        if (player.isPotionActive(Effects.REGENERATION)) {
            regen = ticks % 25;
        }

        final int TOP = 9 * (highlight ? 1 : 0);
        final int BACKGROUND = 16;
        int MARGIN = 16;

        //Renders hearts?
        int numberOfHearts = MathHelper.ceil((healthMax + absorb) / 2.0F) - 1;
        for (int heartNumber = numberOfHearts; heartNumber >= 0; --heartNumber) {
            int row = MathHelper.ceil((float) (heartNumber + 1) / 10.0F) - 1;
            int x = left + heartNumber % 10 * 8;
            int y = top - row * rowHeight;

            if (health <= 4) y += rand.nextInt(2);
            if (heartNumber == regen) y -= 2;

            boolean fullHeart = heartNumber * 2 + 1 < health;
            boolean halfHeart = heartNumber * 2 + 1 == health;
            boolean fullBar = heartNumber * 2 + 1 < healthMax;
            boolean halfBar = heartNumber * 2 + 1 == healthMax;
            //Outlines
            if (heartNumber == 0 && fullBar) {
                //Full ghost
                drawTexturedModalRect(x, y, 54, TOP, 9, 9);
            } else if (heartNumber == 0 && halfBar) {
                //Half ghost
                drawTexturedModalRect(x, y, 54, TOP, 5, 9);
            } else if (heartNumber == numberOfHearts && fullBar) {
                //Full end of bar
                drawTexturedModalRect(x, y, 72, TOP, 9, 9);
            } else if (heartNumber == numberOfHearts && halfBar) {
                //Half end of bar
                drawTexturedModalRect(x, y, 81, TOP, 9, 9);
            } else if (fullBar) {
                //Full bar
                drawTexturedModalRect(x, y, 63, TOP, 9, 9);
            } else if (halfBar) {
                //Half heart
                drawTexturedModalRect(x, y, 63, TOP, 5, 9);
            }

            if (heartNumber == 0 && fullHeart) {
                //Full ghost
                drawTexturedModalRect(x, y, 0, 0, 9, 9);
            } else if (heartNumber == 0 && halfHeart) {
                //Half ghost
                drawTexturedModalRect(x, y, 0, 0, 5, 9);
            } else if (heartNumber == numberOfHearts && halfHeart) {
                //Half end of bar
                drawTexturedModalRect(x, y, 18, 0, 5, 9);
            } else if (heartNumber == numberOfHearts && fullHeart) {
                //Full end of bar
                drawTexturedModalRect(x, y, 18, 0, 9, 9);
            } else if (fullHeart) {
                //Full bar
                drawTexturedModalRect(x, y, 9, 0, 9, 9);
            } else if (halfHeart) {
                //Half heart
                drawTexturedModalRect(x, y, 9, 0, 5, 9);
            }

        }

        GlStateManager.disableBlend();
        mc.getTextureManager().bindTexture(GUI_ICONS_LOCATION);
        mc.getProfiler().endSection();
    }
}
