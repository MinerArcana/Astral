package com.alan19.astral.events;


import com.alan19.astral.Astral;
import com.alan19.astral.configs.AstralConfig;
import com.alan19.astral.effects.AstralEffects;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

import java.util.Random;

import static net.minecraft.client.gui.AbstractGui.GUI_ICONS_LOCATION;
import static net.minecraftforge.client.gui.ForgeIngameGui.left_height;

public class AstralHealthBarRendering {

    public static final ResourceLocation HEART_TEXTURE = new ResourceLocation(Astral.MOD_ID, "textures/gui/astral_health_v2.png");
    public static final ResourceLocation VENOM_HEART_TEXTURE = new ResourceLocation(Astral.MOD_ID, "textures/gui/mind_venom_hearts.png");
    private static long healthUpdateCounter;
    private static int playerHealth;
    private static long lastSystemTime;

    public static void drawTexturedModalRect(MatrixStack matrixStack, int x, int y, int textureX, int textureY, int width, int height) {
        Minecraft.getInstance().ingameGUI.blit(matrixStack, x, y, textureX, textureY, width, height);
    }

    public static void renderAstralHearts(MatrixStack matrixStack, Minecraft mc, PlayerEntity player) {
        int scaledWidth = mc.getMainWindow().getScaledWidth();
        int scaledHeight = mc.getMainWindow().getScaledHeight();

        mc.getProfiler().startSection("health");
        GlStateManager.enableBlend();
        mc.getTextureManager().bindTexture(player.isPotionActive(AstralEffects.MIND_VENOM.get()) ? VENOM_HEART_TEXTURE : HEART_TEXTURE);
        int health = MathHelper.ceil(player.getHealth());
        int ticks = mc.ingameGUI.getTicks();
        boolean highlight = healthUpdateCounter > (long) ticks && (healthUpdateCounter - (long) ticks) / 3L % 2L == 1L;
        if (health < playerHealth && player.hurtResistantTime > 0) {
            lastSystemTime = Util.milliTime();
            healthUpdateCounter = (long) ticks + 20;

        }
        else if (health > playerHealth && player.hurtResistantTime > 0) {
            lastSystemTime = Util.milliTime();
            healthUpdateCounter = (long) ticks + 10;
        }

        if (Util.milliTime() - lastSystemTime > 1000L) {
            playerHealth = health;
            lastSystemTime = Util.milliTime();
        }

        playerHealth = health;

        ModifiableAttributeInstance attrMaxHealth = player.getAttribute(Attributes.MAX_HEALTH);
        float healthMax = (float) attrMaxHealth.getValue();
        float absorb = MathHelper.ceil(player.getAbsorptionAmount());

        int healthRows = MathHelper.ceil((healthMax + absorb) / 2.0F / 10.0F);
        int rowHeight = Math.max(10 - (healthRows - 2), 3);

        Random rand = new Random();
        rand.setSeed(ticks * 312871);

        int left = scaledWidth / 2 - 91;
        int top = scaledHeight - left_height;
        left_height += (healthRows * rowHeight);
        if (rowHeight != 10)
            left_height += 10 - rowHeight;

        int regen = -1;
        if (player.isPotionActive(Effects.REGENERATION)) {
            regen = ticks % 25;
        }

        final int TOP = 9 * (highlight ? 1 : 0);

        int numberOfHearts = MathHelper.ceil((healthMax + absorb) / 2.0F) - 1;
        for (int heartNumber = numberOfHearts; heartNumber >= 0; --heartNumber) {
            int row = MathHelper.ceil((float) (heartNumber + 1) / 10.0F) - 1;
            int x = left + heartNumber % 10 * 8;
            int y = top - row * rowHeight;

            if (health <= 4)
                y += rand.nextInt(2);
            if (heartNumber == regen)
                y -= 2;

            boolean fullHeart = heartNumber * 2 + 1 < health;
            boolean halfHeart = heartNumber * 2 + 1 == health;
            boolean fullBar = heartNumber * 2 + 1 < healthMax;
            boolean halfBar = heartNumber * 2 + 1 == healthMax;

            //Current health rendering
            //Display injured ghost icon when taking damage or at 4 HP or less
            if (heartNumber == 0 && fullHeart) {
                //Full ghost
                drawTexturedModalRect(matrixStack, x, y, playerHealth <= 4 ? 81 : 27, 0, 9, 9);
            }
            else if (heartNumber == 0 && halfHeart) {
                //Half ghost
                drawTexturedModalRect(matrixStack, x, y, playerHealth <= 4 ? 81 : 27, 0, 5, 9);
            }
            else if (heartNumber == numberOfHearts && halfHeart) {
                //Half end of bar
                drawTexturedModalRect(matrixStack, x, y, 45, 0, 5, 9);
            }
            else if (heartNumber == numberOfHearts && fullHeart) {
                //Full end of bar
                drawTexturedModalRect(matrixStack, x, y, 45, 0, 9, 9);
            }
            else if (fullHeart) {
                //Full bar
                drawTexturedModalRect(matrixStack, x, y, 36, 0, 9, 9);
            }
            else if (halfHeart) {
                //Half heart
                drawTexturedModalRect(matrixStack, x, y, 36, 0, 5, 9);
            }

            //Outlines
            if (heartNumber == 0 && fullBar) {
                //Full ghost
                drawTexturedModalRect(matrixStack, x, y, 54, TOP, 9, 9);
            }
            else if (heartNumber == 0 && halfBar) {
                //Half ghost
                drawTexturedModalRect(matrixStack, x, y, 54, TOP, 5, 9);
            }
            else if (heartNumber == numberOfHearts && fullBar) {
                //Full end of bar
                drawTexturedModalRect(matrixStack, x, y, 72, TOP, 9, 9);
            }
            else if (heartNumber == numberOfHearts && halfBar) {
                //Half end of bar
                drawTexturedModalRect(matrixStack, x, y, 76, TOP, 5, 9);
            }
            else if (fullBar) {
                //Full bar
                drawTexturedModalRect(matrixStack, x, y, 63, TOP, 9, 9);
            }
            else if (halfBar) {
                //Half heart
                drawTexturedModalRect(matrixStack, x, y, 63, TOP, 5, 9);
            }

        }

        GlStateManager.disableBlend();
        mc.getTextureManager().bindTexture(GUI_ICONS_LOCATION);
        mc.getProfiler().endSection();
    }

    public static void renderAstralScreenFade(MatrixStack matrixStack, int sleep) {
        Minecraft mc = Minecraft.getInstance();
        GlStateManager.enableBlend();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableDepthTest();
        GlStateManager.disableAlphaTest();
        int sleepTime = Math.toIntExact((long) (sleep * (100.0 / AstralConfig.getTravelingSettings().getStartupTime())));
        float opacity = (float) sleepTime / 100.0F;

        if (opacity > 1.0F) {
            opacity = 1.0F - (float) (sleepTime - 100) / 10.0F;
        }

        int color = (int) (220.0F * opacity) << 24 | 1052704;
        int scaledWidth = mc.getMainWindow().getScaledWidth();
        int scaledHeight = mc.getMainWindow().getScaledHeight();

        AbstractGui.fill(matrixStack, 0, 0, scaledWidth, scaledHeight, color);
        GlStateManager.enableAlphaTest();
        GlStateManager.enableDepthTest();
        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.disableAlphaTest();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableLighting();
        GlStateManager.enableAlphaTest();

    }

    public static void renderMindVenomHearts(MatrixStack matrixStack, Minecraft mc, PlayerEntity player) {
        int scaledWidth = mc.getMainWindow().getScaledWidth();
        int scaledHeight = mc.getMainWindow().getScaledHeight();

        mc.getProfiler().startSection("health");
        GlStateManager.enableBlend();
        mc.getTextureManager().bindTexture(player.isPotionActive(AstralEffects.MIND_VENOM.get()) ? VENOM_HEART_TEXTURE : HEART_TEXTURE);
        int health = MathHelper.ceil(player.getHealth());
        int ticks = mc.ingameGUI.getTicks();
        boolean highlight = healthUpdateCounter > (long) ticks && (healthUpdateCounter - (long) ticks) / 3L % 2L == 1L;
        if (health < playerHealth && player.hurtResistantTime > 0) {
            lastSystemTime = Util.milliTime();
            healthUpdateCounter = (long) ticks + 20;

        }
        else if (health > playerHealth && player.hurtResistantTime > 0) {
            lastSystemTime = Util.milliTime();
            healthUpdateCounter = (long) ticks + 10;
        }

        if (Util.milliTime() - lastSystemTime > 1000L) {
            playerHealth = health;
            lastSystemTime = Util.milliTime();
        }

        playerHealth = health;

        ModifiableAttributeInstance attrMaxHealth = player.getAttribute(Attributes.MAX_HEALTH);
        float healthMax = (float) attrMaxHealth.getValue();
        float absorb = MathHelper.ceil(player.getAbsorptionAmount());

        int healthRows = MathHelper.ceil((healthMax + absorb) / 2.0F / 10.0F);
        int rowHeight = Math.max(10 - (healthRows - 2), 3);

        Random rand = new Random();
        rand.setSeed(ticks * 312871);

        int left = scaledWidth / 2 - 91;
        int top = scaledHeight - left_height;
        left_height += (healthRows * rowHeight);
        if (rowHeight != 10)
            left_height += 10 - rowHeight;

        int regen = -1;
        if (player.isPotionActive(Effects.REGENERATION)) {
            regen = ticks % 25;
        }

        final int TOP = 9 * (highlight ? 1 : 0);

        int numberOfHearts = MathHelper.ceil((healthMax + absorb) / 2.0F) - 1;
        for (int heartNumber = numberOfHearts; heartNumber >= 0; --heartNumber) {
            int row = MathHelper.ceil((float) (heartNumber + 1) / 10.0F) - 1;
            int x = left + heartNumber % 10 * 8;
            int y = top - row * rowHeight;

            if (health <= 4)
                y += rand.nextInt(2);
            if (heartNumber == regen)
                y -= 2;

            boolean fullHeart = heartNumber * 2 + 1 < health;
            boolean halfHeart = heartNumber * 2 + 1 == health;
            boolean fullBar = heartNumber * 2 + 1 < healthMax;
            boolean halfBar = heartNumber * 2 + 1 == healthMax;

            //Current health rendering
            //Display injured ghost icon when taking damage or at 4 HP or less
            if (heartNumber == 0 && fullHeart) {
                //Full ghost
                drawTexturedModalRect(matrixStack, x, y, playerHealth <= 4 ? 81 : 27, 0, 9, 9);
            }
            else if (heartNumber == 0 && halfHeart) {
                //Half ghost
                drawTexturedModalRect(matrixStack, x, y, playerHealth <= 4 ? 81 : 27, 0, 5, 9);
            }
            else if (heartNumber == numberOfHearts && halfHeart) {
                //Half end of bar
                drawTexturedModalRect(matrixStack, x, y, 45, 0, 5, 9);
            }
            else if (heartNumber == numberOfHearts && fullHeart) {
                //Full end of bar
                drawTexturedModalRect(matrixStack, x, y, 45, 0, 9, 9);
            }
            else if (fullHeart) {
                //Full bar
                drawTexturedModalRect(matrixStack, x, y, 36, 0, 9, 9);
            }
            else if (halfHeart) {
                //Half heart
                drawTexturedModalRect(matrixStack, x, y, 36, 0, 5, 9);
            }

            //Outlines
            if (heartNumber == 0 && fullBar) {
                //Full ghost
                drawTexturedModalRect(matrixStack, x, y, 54, TOP, 9, 9);
            }
            else if (heartNumber == 0 && halfBar) {
                //Half ghost
                drawTexturedModalRect(matrixStack, x, y, 54, TOP, 5, 9);
            }
            else if (heartNumber == numberOfHearts && fullBar) {
                //Full end of bar
                drawTexturedModalRect(matrixStack, x, y, 72, TOP, 9, 9);
            }
            else if (heartNumber == numberOfHearts && halfBar) {
                //Half end of bar
                drawTexturedModalRect(matrixStack, x, y, 76, TOP, 5, 9);
            }
            else if (fullBar) {
                //Full bar
                drawTexturedModalRect(matrixStack, x, y, 63, TOP, 9, 9);
            }
            else if (halfBar) {
                //Half heart
                drawTexturedModalRect(matrixStack, x, y, 63, TOP, 5, 9);
            }

        }

        GlStateManager.disableBlend();
        mc.getTextureManager().bindTexture(GUI_ICONS_LOCATION);
        mc.getProfiler().endSection();
    }
}
