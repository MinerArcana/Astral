package com.alan19.astral.client.gui;

import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.api.psychicinventory.PsychicInventory;
import com.alan19.astral.api.psychicinventory.PsychicInventoryInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.stream.IntStream;

public class AstralInventoryContainer extends RecipeBookContainer<CraftingInventory> {
    private final CraftingInventory craftMatrix = new CraftingInventory(this, 2, 2);
    private final CraftResultInventory craftResult = new CraftResultInventory();
    private PsychicInventoryInstance inventory = new PsychicInventoryInstance();


    public AstralInventoryContainer(int id, PlayerInventory playerInventory) {
        super(AstralContainers.ASTRAL_INVENTORY_CONTAINER.get(), id);
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

        IntStream.range(0, 4)
                .mapToObj(i -> EquipmentSlotType.fromSlotTypeAndIndex(EquipmentSlotType.Group.ARMOR, i))
                .map(equipmentSlotType -> new EquipmentSlotHandler(equipmentSlotType, player, inventory.getAstralArmorInventory(), equipmentSlotType.getIndex(), 8, 8 + (3 - equipmentSlotType.getIndex()) * 18))
                .forEach(this::addSlot);
    }

    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity playerIn) {
        return true;
    }

    @Override
    public void fillStackedContents(@Nonnull RecipeItemHelper itemHelperIn) {
        craftMatrix.fillStackedContents(itemHelperIn);
    }

    @Override
    public void clear() {
        this.craftResult.clear();
        this.craftMatrix.clear();
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

    @Override
    public void onContainerClosed(@Nonnull PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        this.craftResult.clear();
        if (playerIn.world instanceof ServerWorld) {
            this.clearContainer(playerIn, playerIn.world, this.craftMatrix);
        }
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
    @Override
    @Nonnull
    public ItemStack transferStackInSlot(@Nonnull PlayerEntity playerIn, int index) {
        return ItemStack.EMPTY;
    }

    /**
     * Called to determine if the current slot is valid for the stack merging (double-click) code. The stack passed in is
     * null for the initial slot that was double-clicked.
     */
    @Override
    public boolean canMergeSlot(@Nonnull ItemStack stack, Slot slotIn) {
        return slotIn.inventory != this.craftResult && super.canMergeSlot(stack, slotIn);
    }

}
