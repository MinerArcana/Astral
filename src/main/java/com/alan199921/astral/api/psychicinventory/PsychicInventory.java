package com.alan199921.astral.api.psychicinventory;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.ItemStackHandler;

public class PsychicInventory implements IPsychicInventory {

    //Physical inventory
    private final ItemStackHandler physicalMainInventory = new ItemStackHandler(42);
    private final ItemStackHandler physicalArmor = new ItemStackHandler(4);
    private final ItemStackHandler physicalHands = new ItemStackHandler(2);
    //Psychic inventory
    private final ItemStackHandler psychicMainInventory = new ItemStackHandler(42);
    private final ItemStackHandler psychicArmor = new ItemStackHandler(4);
    private final ItemStackHandler psychicHands = new ItemStackHandler(2);
    private int sleepGauge = 0;
    private boolean activeGhost = false;

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT psychicInventoryNBT = new CompoundNBT();
        psychicInventoryNBT.put("physicalMainInventory", physicalMainInventory.serializeNBT());
        psychicInventoryNBT.put("physicalArmor", physicalArmor.serializeNBT());
        psychicInventoryNBT.put("physicalHands", physicalHands.serializeNBT());

        psychicInventoryNBT.put("psychicMainInventory", psychicMainInventory.serializeNBT());
        psychicInventoryNBT.put("psychicArmor", psychicArmor.serializeNBT());
        psychicInventoryNBT.put("psychicHands", psychicHands.serializeNBT());

        psychicInventoryNBT.putInt("sleepGauge", sleepGauge);
        psychicInventoryNBT.putBoolean("activeGhost", activeGhost);
        return psychicInventoryNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        physicalMainInventory.deserializeNBT(nbt.getCompound("physicalMainInventory"));
        physicalArmor.deserializeNBT(nbt.getCompound("physicalArmor"));
        physicalHands.deserializeNBT(nbt.getCompound("physicalHands"));

        psychicMainInventory.deserializeNBT(nbt.getCompound("psychicMainInventory"));
        psychicArmor.deserializeNBT(nbt.getCompound("psychicArmor"));
        psychicHands.deserializeNBT(nbt.getCompound("psychicHands"));

        sleepGauge = nbt.getInt("sleepGauge");
        activeGhost = nbt.getBoolean("activeGhost");
    }

    @Override
    public void addSleep() {
        sleepGauge++;
        if (canPlayerStartTraveling()) {
            activeGhost = true;
        }
    }

    @Override
    public void addSleep(int ticks) {
        sleepGauge += ticks;
    }

    @Override
    public void clearSleep() {
        sleepGauge = 0;
        activeGhost = false;
    }

    @Override
    public boolean canPlayerStartTraveling() {
        return sleepGauge >= 50;
    }

    @Override
    public int getSleep() {
        return sleepGauge;
    }
}
