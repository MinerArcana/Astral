package com.alan19.astral.mentalconstructs;

import com.alan19.astral.Astral;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.capability.CuriosCapability;
import top.theillusivec4.curios.api.capability.ICurioItemHandler;
import top.theillusivec4.curios.api.inventory.CurioStackHandler;
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

    private static List<ItemStack> getCuriosAsList(ICurioItemHandler iCurioItemHandler) {
        final Collection<CurioStackHandler> curioStackHandlers = iCurioItemHandler.getCurioMap().values();
        List<ItemStack> curios = new ArrayList<>();
        for (CurioStackHandler curioStackHandler : curioStackHandlers) {
            for (int i = 0; i < curioStackHandler.getSlots(); i++) {
                curios.add(curioStackHandler.getStackInSlot(i));
            }
        }
        return curios;
    }

    @Override
    public void performEffect(PlayerEntity player, int level) {
        final float newSaturation = player.getFoodStats().getSaturationLevel();
        saturationCounter += (Math.max(0, saturationSnapshot - newSaturation));
        if (saturationCounter >= getConversionRatio(level) && player.getFoodStats().needFood()) {
            saturationCounter = saturationCounter - getConversionRatio(level);
            player.getFoodStats().addStats(1, 0);
        }

        if (player.ticksExisted % Math.round(getConversionRatio(level) * 10) == 0) {
            addMana(player, 1);
        }
        saturationSnapshot = newSaturation;
    }

    private void addMana(PlayerEntity playerEntity, int manaToSend) {
        List<ItemStack> items = getManaItems(playerEntity);
        items.stream()
                .filter(stack -> {
                    final IManaItem manaItem = (IManaItem) stack.getItem();
                    return manaItem.getMana(stack) + manaToSend <= manaItem.getMaxMana(stack) && manaItem.canReceiveManaFromItem(stack, ItemStack.EMPTY);
                })
                .findFirst()
                .ifPresent(itemStack -> ((IManaItem) itemStack.getItem()).addMana(itemStack, manaToSend));
    }


    private List<ItemStack> getManaItems(PlayerEntity player) {
        final List<ItemStack> curiosItems = player.getCapability(CuriosCapability.INVENTORY).map(Garden::getCuriosAsList).orElseGet(ArrayList::new);
        curiosItems.addAll(player.inventory.mainInventory);
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
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.putFloat("saturationSnapshot", saturationSnapshot);
        nbt.putFloat("saturationCounter", saturationCounter);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        saturationSnapshot = nbt.getFloat("saturationSnapshot");
        saturationCounter = nbt.getFloat("saturationCounter");
    }
}
