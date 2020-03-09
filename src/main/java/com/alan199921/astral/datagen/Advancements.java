package com.alan199921.astral.datagen;

import com.alan199921.astral.Astral;
import com.alan199921.astral.dimensions.AstralDimensions;
import com.alan199921.astral.effects.AstralEffects;
import com.alan199921.astral.items.AstralItems;
import com.alan199921.astral.potions.AstralPotions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.criterion.*;
import net.minecraft.data.AdvancementProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;

public class Advancements extends AdvancementProvider {
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    private DataGenerator dataGenerator;

    public Advancements(DataGenerator dataGenerator) {
        super(dataGenerator);
        this.dataGenerator = dataGenerator;
    }

    private void registerAdvancements(Consumer<Advancement> consumer) {
        final Advancement root = Advancement.Builder.builder()
                .withDisplay(new DisplayBuilder(AstralItems.FEVERWEED, "root")
                        .hidden(true)
                        .showToast(false)
                        .announceToChat(false)
                        .background(new ResourceLocation("astral:textures/block/ether_dirt.png"))
                        .build())
                .withCriterion("tick", new TickTrigger.Instance())
                .register(consumer, "astral:root");

        final Advancement craftTravelingMedicine = Advancement.Builder.builder()
                .withParent(root)
                .withCriterion("craft_travelling_medicine", InventoryChangeTrigger.Instance.forItems(AstralItems.TRAVELLING_MEDICINE))
                .withDisplay(new DisplayBuilder(AstralItems.TRAVELLING_MEDICINE, "craft_travelling_medicine").build())
                .register(consumer, new ResourceLocation(Astral.MOD_ID, "craft_travelling_medicine").toString());

        final Advancement becomeAstral = Advancement.Builder.builder()
                .withParent(craftTravelingMedicine)
                .withCriterion("get_astral_travel", EffectsChangedTrigger.Instance.forEffect(MobEffectsPredicate.any().addEffect(AstralEffects.ASTRAL_TRAVEL)))
                .withDisplay(new DisplayBuilder(AstralItems.TRAVELLING_MEDICINE, "get_astral_travel").build())
                .register(consumer, new ResourceLocation(Astral.MOD_ID, "get_astral_travel").toString());

        final String brewStrongAstralPotionName = "brew_strong_astral_potion";
        final Advancement brewStrongAstralPotion = Advancement.Builder.builder()
                .withParent(craftTravelingMedicine)
                .withCriterion(brewStrongAstralPotionName, new BrewedPotionTrigger.Instance(AstralPotions.STRONG_ASTRAL_TRAVEL_POTION))
                .withDisplay(new DisplayBuilder(Items.POTION, brewStrongAstralPotionName).build())
                .register(consumer, new ResourceLocation(Astral.MOD_ID, brewStrongAstralPotionName).toString());

        final Advancement craftIntrospectionMedicine = Advancement.Builder.builder()
                .withParent(root)
                .withCriterion("craft_introspection_medicine", InventoryChangeTrigger.Instance.forItems(AstralItems.INTROSPECTION_MEDICINE))
                .withDisplay(new DisplayBuilder(AstralItems.INTROSPECTION_MEDICINE, "craft_introspection_medicine").build())
                .register(consumer, new ResourceLocation(Astral.MOD_ID, "craft_introspection_medicine").toString());

        final Advancement innerRealm = Advancement.Builder.builder()
                .withParent(craftIntrospectionMedicine)
                .withCriterion("inner_realm", ChangeDimensionTrigger.Instance.changedDimensionTo(DimensionType.byName(AstralDimensions.INNER_REALM)))
                .withDisplay(new DisplayBuilder(AstralItems.ENLIGHTENMENT_KEY, "inner_realm").build())
                .register(consumer, new ResourceLocation(Astral.MOD_ID, "inner_realm").toString());

        final Advancement magicalPuissance = Advancement.Builder.builder()
                .withParent(innerRealm)
                .withCriterion("none", new TickTrigger.Instance())
                .withDisplay(new DisplayBuilder(Items.ENDER_PEARL, "magical_puissance")
                        .hidden(true)
                        .showToast(false)
                        .announceToChat(false)
                        .build())
                .register(consumer, new ResourceLocation(Astral.MOD_ID, "magical_puissance").toString());

        final Advancement enchantingInsight = Advancement.Builder.builder()
                .withParent(magicalPuissance)
                .withCriterion("enchanting_insight", EnchantedItemTrigger.Instance.any())
                .withDisplay(new DisplayBuilder(Items.ENCHANTED_BOOK, "enchanting_insight").build())
                .register(consumer, new ResourceLocation(Astral.MOD_ID, "enchanting_insight").toString());

        final Advancement brewingInsight = Advancement.Builder.builder()
                .withParent(magicalPuissance)
                .withCriterion("brewing_insight", BrewedPotionTrigger.Instance.brewedPotion())
                .withDisplay(new DisplayBuilder(Items.BREWING_STAND, "brewing_insight").build())
                .register(consumer, new ResourceLocation(Astral.MOD_ID, "brewing_insight").toString());

        final Advancement autonomousInsight = Advancement.Builder.builder()
                .withParent(magicalPuissance)
                .withCriterion("autonomous_insight", new SummonedEntityTrigger.Instance(EntityPredicate.Builder.create().type(EntityType.IRON_GOLEM).build()))
                .withDisplay(new DisplayBuilder(Items.CARVED_PUMPKIN, "autonomous_insight").build())
                .register(consumer, new ResourceLocation(Astral.MOD_ID, "autonomous_insight").toString());

        final Advancement medicalInsight = Advancement.Builder.builder()
                .withParent(magicalPuissance)
                .withCriterion("medical_insight", CuredZombieVillagerTrigger.Instance.any())
                .withDisplay(new DisplayBuilder(Items.GOLDEN_APPLE, "medical_insight").build())
                .register(consumer, new ResourceLocation(Astral.MOD_ID, "medical_insight").toString());
    }

    @Override
    public void act(@Nonnull DirectoryCache cache) {
        Path outputFolder = this.dataGenerator.getOutputFolder();
        Consumer<Advancement> consumer = advancement -> {
            Path path = outputFolder.resolve("data/" + advancement.getId().getNamespace() + "/advancements/" + advancement.getId().getPath() + ".json");
            try {
                IDataProvider.save(GSON, cache, advancement.copy().serialize(), path);
            } catch (IOException e) {
                System.out.println(e);
            }
        };
        registerAdvancements(consumer);
    }

    @Override
    public String getName() {
        return "Astral Advancements";
    }
}
