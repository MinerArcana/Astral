package com.alan19.astral.events;


import com.alan19.astral.Astral;
import com.alan19.astral.configs.AstralConfig;
import com.alan19.astral.effects.AstralEffects;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.opengl.GL11;
import staticnet.minecraft.client.gui.GuiComponentIngameGui.left_height;

import java.util.Random;

import static net.minecraft.client.gui.AbstractGui.GUI_ICONS_LOCATION;

public class AstralHealthBarRendering {

    public static final ResourceLocation HEART_TEXTURE = new ResourceLocation(Astral.MOD_ID, "textures/gui/astral_health_v2.png");
    public static final ResourceLocation VENOM_HEART_TEXTURE = new ResourceLocation(Astral.MOD_ID, "textures/gui/mind_venom_hearts.png");
    private static long healthUpdateCounter;
    private static int playerHealth;
    private static long lastSystemTime;

    public static void drawTexturedModalRect(PoseStack matrixStack, int x, int y, int textureX, int textureY, int width, int height) {
        Minecraft.getInstance().gui.blit(matrixStack, x, y, textureX, textureY, width, height);
    }

    public static void renderAstralHearts(PoseStack matrixStack, Minecraft mc, Player player) {
        int scaledWidth = mc.getWindow().getGuiScaledWidth();
        int scaledHeight = mc.getWindow().getGuiScaledHeight();

        mc.getProfiler().push("health");
        GlStateManager._enableBlend();
        mc.getTextureManager().bind(player.hasEffect(AstralEffects.MIND_VENOM.get()) ? VENOM_HEART_TEXTURE : HEART_TEXTURE);
        int health = Mth.ceil(player.getHealth());
        int ticks = mc.gui.getGuiTicks();
        boolean highlight = healthUpdateCounter > (long) ticks && (healthUpdateCounter - (long) ticks) / 3L % 2L == 1L;
        if (health < playerHealth && player.invulnerableTime > 0) {
            lastSystemTime = Util.getMillis();
            healthUpdateCounter = (long) ticks + 20;

        }
        else if (health > playerHealth && player.invulnerableTime > 0) {
            lastSystemTime = Util.getMillis();
            healthUpdateCounter = (long) ticks + 10;
        }

        if (Util.getMillis() - lastSystemTime > 1000L) {
            playerHealth = health;
            lastSystemTime = Util.getMillis();
        }

        playerHealth = health;

        AttributeInstance attrMaxHealth = player.getAttribute(Attributes.MAX_HEALTH);
        float healthMax = (float) attrMaxHealth.getValue();
        float absorb = Mth.ceil(player.getAbsorptionAmount());

        int healthRows = Mth.ceil((healthMax + absorb) / 2.0F / 10.0F);
        int rowHeight = Math.max(10 - (healthRows - 2), 3);

        Random rand = new Random();
        rand.setSeed(ticks * 312871);

        int left = scaledWidth / 2 - 91;
        int top = scaledHeight - left_height;
        left_height += (healthRows * rowHeight);
        if (rowHeight != 10)
            left_height += 10 - rowHeight;

        int regen = -1;
        if (player.hasEffect(MobEffects.REGENERATION)) {
            regen = ticks % 25;
        }

        final int TOP = 9 * (highlight ? 1 : 0);

        int numberOfHearts = Mth.ceil((healthMax + absorb) / 2.0F) - 1;
        for (int heartNumber = numberOfHearts; heartNumber >= 0; --heartNumber) {
            int row = Mth.ceil((float) (heartNumber + 1) / 10.0F) - 1;
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

        GlStateManager._disableBlend();
        mc.getTextureManager().bind(GUI_ICONS_LOCATION);
        mc.getProfiler().pop();
    }

    public static void renderAstralScreenFade(PoseStack matrixStack, int sleep) {
        Minecraft mc = Minecraft.getInstance();
        GlStateManager._enableBlend();
        GlStateManager._color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager._disableDepthTest();
        GlStateManager._disableAlphaTest();
        int sleepTime = Math.toIntExact((long) (sleep * (100.0 / AstralConfig.getTravelingSettings().startupTime.get())));
        float opacity = (float) sleepTime / 100.0F;

        if (opacity > 1.0F) {
            opacity = 1.0F - (float) (sleepTime - 100) / 10.0F;
        }

        int color = (int) (220.0F * opacity) << 24 | 1052704;
        int scaledWidth = mc.getWindow().getGuiScaledWidth();
        int scaledHeight = mc.getWindow().getGuiScaledHeight();

        GuiComponent.fill(matrixStack, 0, 0, scaledWidth, scaledHeight, color);
        GlStateManager._enableAlphaTest();
        GlStateManager._enableDepthTest();
        GlStateManager._enableBlend();
        GlStateManager._blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager._disableAlphaTest();
        GlStateManager._color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager._disableLighting();
        GlStateManager._enableAlphaTest();

    }

    public static void renderMindVenomHearts(PoseStack matrixStack, Minecraft mc, Player player) {
        int scaledWidth = mc.getWindow().getGuiScaledWidth();
        int scaledHeight = mc.getWindow().getGuiScaledHeight();

        mc.getProfiler().push("health");
        GlStateManager._enableBlend();
        mc.getTextureManager().bind(player.hasEffect(AstralEffects.MIND_VENOM.get()) ? VENOM_HEART_TEXTURE : HEART_TEXTURE);
        int health = Mth.ceil(player.getHealth());
        int ticks = mc.gui.getGuiTicks();
        boolean highlight = healthUpdateCounter > (long) ticks && (healthUpdateCounter - (long) ticks) / 3L % 2L == 1L;
        if (health < playerHealth && player.invulnerableTime > 0) {
            lastSystemTime = Util.getMillis();
            healthUpdateCounter = (long) ticks + 20;

        }
        else if (health > playerHealth && player.invulnerableTime > 0) {
            lastSystemTime = Util.getMillis();
            healthUpdateCounter = (long) ticks + 10;
        }

        if (Util.getMillis() - lastSystemTime > 1000L) {
            playerHealth = health;
            lastSystemTime = Util.getMillis();
        }

        playerHealth = health;

        AttributeInstance attrMaxHealth = player.getAttribute(Attributes.MAX_HEALTH);
        float healthMax = (float) attrMaxHealth.getValue();
        float absorb = Mth.ceil(player.getAbsorptionAmount());

        int healthRows = Mth.ceil((healthMax + absorb) / 2.0F / 10.0F);
        int rowHeight = Math.max(10 - (healthRows - 2), 3);

        Random rand = new Random();
        rand.setSeed(ticks * 312871);

        int left = scaledWidth / 2 - 91;
        int top = scaledHeight - left_height;
        left_height += (healthRows * rowHeight);
        if (rowHeight != 10)
            left_height += 10 - rowHeight;

        int regen = -1;
        if (player.hasEffect(MobEffects.REGENERATION)) {
            regen = ticks % 25;
        }

        final int TOP = 9 * (highlight ? 1 : 0);

        int numberOfHearts = Mth.ceil((healthMax + absorb) / 2.0F) - 1;
        for (int heartNumber = numberOfHearts; heartNumber >= 0; --heartNumber) {
            int row = Mth.ceil((float) (heartNumber + 1) / 10.0F) - 1;
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

        GlStateManager._disableBlend();
        mc.getTextureManager().bind(GUI_ICONS_LOCATION);
        mc.getProfiler().pop();
    }
}
