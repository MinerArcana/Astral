package com.alan19.astral.mentalconstructs;

import com.alan19.astral.Astral;
import com.alan19.astral.util.ModCompat;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import vazkii.botania.api.mana.IManaItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = Astral.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Garden extends MentalConstruct {
    private float saturationSnapshot = 0;
    private float saturationCounter = 0;

    public static float getConversionRatio(int level) {
        return 14 / (.002f * level + 1) + 1;
    }

    /**
     * Gets all of the curios in a player's inventory as a List
     *
     * @param curiosItemHandler The ICurioItemHandler capability on a player
     * @return A list of Curios as ItemStacks in an ArrayList
     */
    private static List<ItemStack> getCuriosAsList(ICuriosItemHandler curiosItemHandler) {
        final Collection<ICurioStacksHandler> curioStackHandlers = curiosItemHandler.getCurios().values();
        List<ItemStack> curios = new ArrayList<>();
        for (ICurioStacksHandler curioStackHandler : curioStackHandlers) {
            for (int i = 0; i < curioStackHandler.getSlots(); i++) {
                curios.add(curioStackHandler.getStacks().getStackInSlot(i));
            }
        }
        return curios;
    }

    @Override
    public void performEffect(Player player, int level) {
        final float newSaturation = player.getFoodData().getSaturationLevel();
        saturationCounter += (Math.max(0, saturationSnapshot - newSaturation));
        if (saturationCounter >= getConversionRatio(level) && player.getFoodData().needsFood()) {
            saturationCounter = saturationCounter - getConversionRatio(level);
            player.getFoodData().eat(1, 0);
        }

        if (ModCompat.IS_BOTANIA_LOADED && player.tickCount % Math.round(getConversionRatio(level) * 10) == 0) {
            addMana(player, 1);
        }
        saturationSnapshot = newSaturation;
    }

    /**
     * Adds the specified amount of mana to the first item that accepts mana in a player's inventory
     *
     * @param playerEntity The player to send the mana to
     * @param manaToSend   The amount of mana to send
     */
    private void addMana(Player playerEntity, int manaToSend) {
        List<ItemStack> items = getManaItems(playerEntity);
        items.stream().filter(stack -> canItemstackReceiveMana(manaToSend, stack)).findFirst().ifPresent(itemStack -> ((IManaItem) itemStack.getItem()).addMana(itemStack, manaToSend));
    }

    private boolean canItemstackReceiveMana(int manaToSend, ItemStack stack) {
        final IManaItem manaItem = (IManaItem) stack.getItem();
        return manaItem.getMana(stack) + manaToSend <= manaItem.getMaxMana(stack) && manaItem.canReceiveManaFromItem(stack, ItemStack.EMPTY);
    }


    private List<ItemStack> getManaItems(Player player) {
        final List<ItemStack> curiosItems = player.getCapability(CuriosCapability.INVENTORY).map(Garden::getCuriosAsList).orElseGet(ArrayList::new);
        curiosItems.addAll(player.inventory.items);
        return curiosItems.stream().filter(itemStack -> itemStack.getItem() instanceof IManaItem).collect(Collectors.toList());
    }

    @Override
    public EffectType getEffectType() {
        return EffectType.PASSIVE;
    }

    @Override
    public MentalConstructType getType() {
        return AstralMentalConstructs.GARDEN.get();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = super.serializeNBT();
        nbt.putFloat("saturationSnapshot", saturationSnapshot);
        nbt.putFloat("saturationCounter", saturationCounter);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        saturationSnapshot = nbt.getFloat("saturationSnapshot");
        saturationCounter = nbt.getFloat("saturationCounter");
    }
}
