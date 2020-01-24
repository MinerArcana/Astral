package com.alan199921.astral.api.psychicinventory;

import com.alan199921.astral.configs.AstralConfig;
import com.alan199921.astral.effects.AstralEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
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

    /**
     * Adds 1 to the sleep counter, returns true if this switches the player to astral mode
     *
     * @return Whether the player gets the full benefits of Astral travel from incrementing
     */
    @Override
    public boolean addSleep() {
        sleepGauge++;
        if (sleepGauge >= AstralConfig.getTravelingSettings().getStartupTime() && !activeGhost) {
            activeGhost = true;
            return true;
        }
        return false;
    }

    @Override
    public void clearSleep() {
        sleepGauge = 0;
        activeGhost = false;
    }

    @Override
    public int getSleep() {
        return sleepGauge;
    }

    @Override
    public boolean isEntityTraveling(LivingEntity entity) {
        if (entity.isPotionActive(AstralEffects.ASTRAL_TRAVEL)) {
            if (entity instanceof PlayerEntity) {
                return activeGhost;
            }
            else {
                return true;
            }
        }
        return false;
    }
}
