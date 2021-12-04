package com.alan19.astral.client.gui;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class AstralContainerProvider implements MenuProvider {

    public static final String ASTRAL_CONTAINER_NAME = "astral.container.name";

    @Override
    @Nonnull
    public Component getDisplayName() {
        return new TranslatableComponent(ASTRAL_CONTAINER_NAME);
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new AstralInventoryContainer(i, playerInventory);
    }
}
