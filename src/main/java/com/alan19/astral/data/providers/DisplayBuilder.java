package com.alan19.astral.data.providers;

import com.alan19.astral.Astral;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class DisplayBuilder {
    private final String advancementName;
    private final ItemStack displayItem;
    private FrameType frameType = FrameType.TASK;
    private boolean showToast = true;
    private boolean announceToChat = true;
    private boolean hidden = false;
    private ResourceLocation background = null;

    public DisplayBuilder(IItemProvider displayItem, String advancementName) {
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
        return new DisplayInfo(displayItem, new TranslationTextComponent(Astral.MOD_ID + ".advancement." + advancementName + ".name"), new TranslationTextComponent(Astral.MOD_ID + ".advancement." + advancementName + ".desc"), background, frameType, showToast, announceToChat, hidden);
    }
}
