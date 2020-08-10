package com.alan19.astral.loot;

import com.alan19.astral.Astral;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.TableLootEntry;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Astral.MOD_ID)
public class LootHandler {
    @SubscribeEvent
    public static void lootLoad(LootTableLoadEvent event) {

        if (event.getName().equals(EntityType.PHANTOM.getLootTable())) {
            event.getTable().addPool(getInjectPool("phantom"));
        }
        if (event.getName().equals(new ResourceLocation("chests/shipwreck_supply"))){
            event.getTable().addPool(getInjectPool("chests/shipwreck_supply"));
        }
    }

    private static LootPool getInjectPool(String pool) {
        return LootPool.builder()
                .addEntry(getInjectEntry(pool, 1))
                .name("astral_inject")
                .build();
    }

    private static LootEntry.Builder<?> getInjectEntry(String name, int weight) {
        ResourceLocation injectedFolder = new ResourceLocation(Astral.MOD_ID, "inject/" + name);
        return TableLootEntry.builder(injectedFolder).weight(weight);
    }
}
