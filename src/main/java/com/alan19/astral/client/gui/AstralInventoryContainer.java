package com.alan19.astral.client.gui;

import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.api.psychicinventory.PsychicInventoryInstance;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.stream.IntStream;

public class AstralInventoryContainer extends RecipeBookMenu<CraftingContainer> {
    private final CraftingContainer craftMatrix = new CraftingContainer(this, 2, 2);
    private final ResultContainer craftResult = new ResultContainer();
    private final Inventory playerInventory;
    private PsychicInventoryInstance inventory = new PsychicInventoryInstance();


    public AstralInventoryContainer(int id, Inventory playerInventory) {
        super(AstralContainers.ASTRAL_INVENTORY_CONTAINER.get(), id);
        this.playerInventory = playerInventory;
        final Player player = playerInventory.player;
        if (player instanceof ServerPlayer && AstralAPI.getOverworldPsychicInventory(((ServerPlayer) player).getLevel()).isPresent()) {
            AstralAPI.getOverworldPsychicInventory(((ServerPlayer) player).getLevel()).map(iPsychicInventory -> iPsychicInventory.getInventoryOfPlayer(player.getUUID())).ifPresent(psychicInventoryInstance -> inventory = psychicInventoryInstance);
        }

        //Add crafting result
        this.addSlot(new ResultSlot(playerInventory.player, this.craftMatrix, this.craftResult, 0, 154, 28));

        //Add crafting grid
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                this.addSlot(new Slot(this.craftMatrix, j + i * 2, 98 + j * 18, 18 + i * 18));
            }
        }


        //Add inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new SlotItemHandler(inventory.getAstralMainInventory(), col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        //Add hotbar
        IntStream.range(0, 9)
                .mapToObj(hotbarSlot -> new SlotItemHandler(inventory.getAstralMainInventory(), hotbarSlot, 8 + hotbarSlot * 18, 142))
                .forEach(this::addSlot);

        //Add armor slots
        IntStream.range(0, 4)
                .mapToObj(i -> EquipmentSlot.byTypeAndIndex(EquipmentSlot.Type.ARMOR, i))
                .map(equipmentSlotType -> new EquipmentSlotHandler(equipmentSlotType, player, inventory.getAstralArmorInventory(), equipmentSlotType.getIndex(), 8, 8 + (3 - equipmentSlotType.getIndex()) * 18))
                .forEach(this::addSlot);

        //Add offhand slot
        addSlot(new EquipmentSlotHandler(EquipmentSlot.OFFHAND, player, inventory.getAstralHandsInventory(), 1, 77, 62));


    }

    @Override
    public boolean stillValid(@Nonnull Player playerIn) {
        return true;
    }

    @Override
    public void fillCraftSlotsStackedContents(@Nonnull StackedContents itemHelperIn) {
        craftMatrix.fillStackedContents(itemHelperIn);
    }

    @Override
    public void clearCraftingContent() {
        this.craftResult.clearContent();
        this.craftMatrix.clearContent();
    }

    @Override
    public boolean recipeMatches(@Nonnull Recipe<? super CraftingContainer> recipeIn) {
        return recipeIn.matches(this.craftMatrix, playerInventory.player.getCommandSenderWorld());
    }

    @Override
    public int getResultSlotIndex() {
        return 0;
    }

    @Override
    public int getGridWidth() {
        return 2;
    }

    @Override
    public int getGridHeight() {
        return 2;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public int getSize() {
        return 5;
    }

    @Nonnull
    @Override
    @OnlyIn(Dist.CLIENT)
    public RecipeBookType getRecipeBookType() {
        return RecipeBookType.CRAFTING;
    }

    @Override
    public void removed(@Nonnull Player playerIn) {
        super.removed(playerIn);
        this.craftResult.clearContent();
        if (playerIn.level instanceof ServerLevel) {
            this.clearContainer(playerIn, playerIn.level, this.craftMatrix);
        }
    }

    @Override
    public void slotsChanged(@Nonnull Container inventoryIn) {
        updateCraftingResult(this.containerId, playerInventory.player.level, playerInventory.player, this.craftMatrix, this.craftResult);
    }

    private void updateCraftingResult(int id, Level worldIn, Player playerIn, CraftingContainer inventoryIn, ResultContainer inventoryResult) {
        if (worldIn instanceof ServerLevel && worldIn.getServer() != null) {
            ServerPlayer serverplayerentity = (ServerPlayer) playerIn;
            ItemStack itemstack = ItemStack.EMPTY;
            Optional<CraftingRecipe> optional = worldIn.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, inventoryIn, worldIn);
            if (optional.isPresent()) {
                CraftingRecipe icraftingrecipe = optional.get();
                if (inventoryResult.setRecipeUsed(worldIn, serverplayerentity, icraftingrecipe)) {
                    itemstack = icraftingrecipe.assemble(inventoryIn);
                }
            }

            inventoryResult.setItem(0, itemstack);
            serverplayerentity.connection.send(new ClientboundContainerSetSlotPacket(id, 0, itemstack));
        }
    }


    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
    @Override
    @Nonnull
    public ItemStack quickMoveStack(@Nonnull Player playerIn, int index) {
        return ItemStack.EMPTY;
    }

    /**
     * Called to determine if the current slot is valid for the stack merging (double-click) code. The stack passed in is
     * null for the initial slot that was double-clicked.
     */
    @Override
    public boolean canTakeItemForPickAll(@Nonnull ItemStack stack, Slot slotIn) {
        return slotIn.container != this.craftResult && super.canTakeItemForPickAll(stack, slotIn);
    }

}
