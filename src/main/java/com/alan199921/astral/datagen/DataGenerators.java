package com.alan199921.astral.datagen;

import com.alan199921.astral.Astral;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Astral.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
//        generator.addProvider(new Recipes(generator));
//        generator.addProvider(new LootTables(generator));
        generator.addProvider(new Advancements(generator));
    }
}