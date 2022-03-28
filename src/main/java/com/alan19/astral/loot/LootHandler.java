package com.alan19.astral.loot;

import com.alan19.astral.Astral;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Astral.MOD_ID)
public class LootHandler {
    @SubscribeEvent
    public static void lootLoad(LootTableLoadEvent event) {

        if (event.getName().equals(EntityType.PHANTOM.getDefaultLootTable())) {
            event.getTable().addPool(getInjectPool("phantom"));
        }
        if (event.getName().equals(new ResourceLocation("chests/shipwreck_supply"))) {
            event.getTable().addPool(getInjectPool("chests/shipwreck_supply"));
        }
    }

    private static LootPool getInjectPool(String pool) {
        return LootPool.lootPool()
                .add(getInjectEntry(pool, 1))
                .name("astral_inject")
                .build();
    }

    private static LootPoolEntryContainer.Builder<?> getInjectEntry(String name, int weight) {
        ResourceLocation injectedFolder = new ResourceLocation(Astral.MOD_ID, "inject/" + name);
        return LootTableReference.lootTableReference(injectedFolder).setWeight(weight);
    }
}
