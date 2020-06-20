package com.alan19.astral.items.tools;

import com.alan19.astral.items.AstralItems;
import net.minecraft.item.Item;
import net.minecraft.item.ShearsItem;

public class PhantasmalShears extends ShearsItem {
    public PhantasmalShears() {
        super(new Item.Properties().group(AstralItems.ASTRAL_ITEMS).maxDamage(120));
    }
}
