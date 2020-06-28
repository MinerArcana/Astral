package com.alan19.astral.client.gui;

import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.api.psychicinventory.PsychicInventory;
import com.alan19.astral.api.psychicinventory.PsychicInventoryInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.stream.IntStream;

public class AstralInventoryContainer extends RecipeBookContainer<CraftingInventory> {
    private final PlayerInventory playerInventory;
    private PsychicInventoryInstance inventory = new PsychicInventoryInstance();

    public AstralInventoryContainer(int id, PlayerInventory playerInventory) {
        super(AstralContainers.ASTRAL_INVENTORY_CONTAINER.get(), id);
        this.playerInventory = playerInventory;
        final PlayerEntity player = playerInventory.player;
        if (player instanceof ServerPlayerEntity && AstralAPI.getOverworldPsychicInventory(((ServerPlayerEntity) player).getServerWorld()).isPresent()) {
            inventory = AstralAPI.getOverworldPsychicInventory(((ServerPlayerEntity) player).getServerWorld()).orElseGet(PsychicInventory::new).getInventoryOfPlayer(player.getUniqueID());
        }

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new SlotItemHandler(inventory.getAstralMainInventory(), col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        IntStream.range(0, 9)
                .mapToObj(hotbarSlot -> new SlotItemHandler(inventory.getAstralMainInventory(), hotbarSlot, 8 + hotbarSlot * 18, 142))
                .forEach(this::addSlot);
    }

    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity playerIn) {
        return true;
    }

    @Override
    public void fillStackedContents(@Nonnull RecipeItemHelper itemHelperIn) {

    }

    @Override
    public void clear() {

    }

    @Override
    public boolean matches(@Nonnull IRecipe<? super CraftingInventory> recipeIn) {
        return false;
    }

    @Override
    public int getOutputSlot() {
        return 0;
    }

    @Override
    public int getWidth() {
        return 2;
    }

    @Override
    public int getHeight() {
        return 2;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public int getSize() {
        return 5;
    }
}
