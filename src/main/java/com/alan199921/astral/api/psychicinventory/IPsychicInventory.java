package com.alan199921.astral.api.psychicinventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.UUID;

public interface IPsychicInventory extends INBTSerializable<CompoundNBT> {
    PsychicInventoryInstance getInventoryOfPlayer(UUID player);

    InventoryType getInventoryType();

    void setInventoryType(InventoryType inventoryType, PlayerInventory playerInventory);

    enum InventoryType {
        ASTRAL("astral"), INNER_REALM("inner_realm"), PHYSICAL("physical");

        private final String name;

        InventoryType(String inventoryName) {
            name = inventoryName;
        }

        public boolean equalsName(String otherName) {
            // (otherName == null) check is not needed because name.equals(null) returns false
            return name.equals(otherName);
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    enum InventorySection {
        MAIN, ARMOR, HANDS
    }
}
