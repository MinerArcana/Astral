package com.alan199921.astral.api.psychicinventory;

public enum InventoryType {
    ASTRAL("astral"), INNER_REALM("inner_realm"), PHYSICAL("physical");

    private final String name;

    InventoryType(String inventoryName) {
        name = inventoryName;
    }

    @Override
    public String toString() {
        return this.name;
    }
}