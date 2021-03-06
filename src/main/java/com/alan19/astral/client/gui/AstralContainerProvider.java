package com.alan19.astral.client.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class AstralContainerProvider implements INamedContainerProvider {

    public static final String ASTRAL_CONTAINER_NAME = "astral.container.name";

    @Override
    @Nonnull
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(ASTRAL_CONTAINER_NAME);
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new AstralInventoryContainer(i, playerInventory);
    }
}
