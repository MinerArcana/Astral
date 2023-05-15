package com.minerarcana.astral.data;

import com.minerarcana.astral.Astral;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class DisplayBuilder {
    private final String advancementName;
    private final ItemStack displayItem;
    private FrameType frameType = FrameType.TASK;
    private boolean showToast = true;
    private boolean announceToChat = true;
    private boolean hidden = false;
    private ResourceLocation background = null;

    public DisplayBuilder(ItemLike displayItem, String advancementName) {
        this.advancementName = advancementName;
        this.displayItem = new ItemStack(displayItem);
    }

    public DisplayBuilder(ItemStack displayItem, String advancementName) {
        this.advancementName = advancementName;
        this.displayItem = displayItem;

    }

    public DisplayBuilder frameType(FrameType frameType) {
        this.frameType = frameType;
        return this;
    }

    public DisplayBuilder showToast(boolean showToast) {
        this.showToast = showToast;
        return this;
    }

    public DisplayBuilder announceToChat(boolean announceToChat) {
        this.announceToChat = announceToChat;
        return this;
    }

    public DisplayBuilder hidden(boolean hidden) {
        this.hidden = hidden;
        return this;
    }

    public DisplayBuilder background(ResourceLocation resourceLocation) {
        background = resourceLocation;
        return this;
    }

    public DisplayInfo build() {
        return new DisplayInfo(displayItem, Component.translatable(Astral.MOD_ID + ".advancement." + advancementName + ".name"), Component.translatable(Astral.MOD_ID + ".advancement." + advancementName + ".desc"), background, frameType, showToast, announceToChat, hidden);
    }
}
