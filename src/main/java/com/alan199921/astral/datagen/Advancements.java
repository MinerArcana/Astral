package com.alan199921.astral.datagen;

import com.alan199921.astral.Astral;
import com.alan199921.astral.dimensions.AstralDimensions;
import com.alan199921.astral.effects.AstralEffects;
import com.alan199921.astral.items.AstralItems;
import com.alan199921.astral.potions.AstralPotions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.criterion.*;
import net.minecraft.command.FunctionObject;
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
    private Advancement root;
    private Advancement craftTravelingMedicine;
    private Advancement becomeAstral;
    private Advancement brewStrongAstralPotion;
    private Advancement craftIntrospectionMedicine;
    private Advancement innerRealm;
    private Advancement magicalPuissance;
    private Advancement enchantingInsight;
    private Advancement brewingInsight;
    private Advancement autonomousInsight;
    private Advancement medicalInsight;

    public Advancements(DataGenerator dataGenerator) {
        super(dataGenerator);
        this.dataGenerator = dataGenerator;
    }

    public Advancement getRoot() {
        return root;
    }

    public Advancement getCraftTravelingMedicine() {
        return craftTravelingMedicine;
    }

    public Advancement getBecomeAstral() {
        return becomeAstral;
    }

    public Advancement getBrewStrongAstralPotion() {
        return brewStrongAstralPotion;
    }

    public Advancement getCraftIntrospectionMedicine() {
        return craftIntrospectionMedicine;
    }

    public Advancement getInnerRealm() {
        return innerRealm;
    }

    public Advancement getMagicalPuissance() {
        return magicalPuissance;
    }

    public Advancement getEnchantingInsight() {
        return enchantingInsight;
    }

    public Advancement getBrewingInsight() {
        return brewingInsight;
    }

    public Advancement getAutonomousInsight() {
        return autonomousInsight;
    }

    public Advancement getMedicalInsight() {
        return medicalInsight;
    }

    private void registerAdvancements(Consumer<Advancement> consumer) {
        root = Advancement.Builder.builder()
                .withDisplay(new DisplayBuilder(AstralItems.FEVERWEED, "root")
                        .hidden(true)
                        .showToast(false)
                        .announceToChat(false)
                        .background(new ResourceLocation("astral:textures/block/ether_dirt.png"))
                        .build())
                .withCriterion("tick", new TickTrigger.Instance())
                .register(consumer, "astral:root");

        craftTravelingMedicine = Advancement.Builder.builder()
                .withParent(root)
                .withCriterion("craft_traveling_medicine", InventoryChangeTrigger.Instance.forItems(AstralItems.TRAVELING_MEDICINE))
                .withDisplay(new DisplayBuilder(AstralItems.TRAVELING_MEDICINE, "craft_traveling_medicine").build())
                .register(consumer, new ResourceLocation(Astral.MOD_ID, "craft_traveling_medicine").toString());

        becomeAstral = Advancement.Builder.builder()
                .withParent(craftTravelingMedicine)
                .withCriterion("get_astral_travel", EffectsChangedTrigger.Instance.forEffect(MobEffectsPredicate.any().addEffect(AstralEffects.ASTRAL_TRAVEL)))
                .withDisplay(new DisplayBuilder(AstralItems.TRAVELING_MEDICINE, "get_astral_travel").build())
                .withRewards(new AdvancementRewards(5, new ResourceLocation[]{}, new ResourceLocation[]{}, new FunctionObject.CacheableFunction(new ResourceLocation("astral:give_key"))))
                .register(consumer, new ResourceLocation(Astral.MOD_ID, "get_astral_travel").toString());

        final String brewStrongAstralPotionName = "brew_strong_astral_potion";
        brewStrongAstralPotion = Advancement.Builder.builder()
                .withParent(craftTravelingMedicine)
                .withCriterion(brewStrongAstralPotionName, new BrewedPotionTrigger.Instance(AstralPotions.STRONG_ASTRAL_TRAVEL_POTION))
                .withDisplay(new DisplayBuilder(Items.POTION, brewStrongAstralPotionName).build())
                .withRewards(new AdvancementRewards(5, new ResourceLocation[]{}, new ResourceLocation[]{}, new FunctionObject.CacheableFunction(new ResourceLocation("astral:give_key"))))
                .register(consumer, new ResourceLocation(Astral.MOD_ID, brewStrongAstralPotionName).toString());

        craftIntrospectionMedicine = Advancement.Builder.builder()
                .withParent(root)
                .withCriterion("craft_introspection_medicine", InventoryChangeTrigger.Instance.forItems(AstralItems.INTROSPECTION_MEDICINE))
                .withDisplay(new DisplayBuilder(AstralItems.INTROSPECTION_MEDICINE, "craft_introspection_medicine").build())
                .register(consumer, new ResourceLocation(Astral.MOD_ID, "craft_introspection_medicine").toString());

        innerRealm = Advancement.Builder.builder()
                .withParent(craftIntrospectionMedicine)
                .withCriterion("inner_realm", ChangeDimensionTrigger.Instance.changedDimensionTo(DimensionType.byName(AstralDimensions.INNER_REALM)))
                .withDisplay(new DisplayBuilder(AstralItems.ENLIGHTENMENT_KEY, "inner_realm").build())
                .withRewards(new AdvancementRewards(5, new ResourceLocation[]{}, new ResourceLocation[]{}, new FunctionObject.CacheableFunction(new ResourceLocation("astral:give_key"))))
                .register(consumer, new ResourceLocation(Astral.MOD_ID, "inner_realm").toString());

        magicalPuissance = Advancement.Builder.builder()
                .withParent(innerRealm)
                .withCriterion("none", new TickTrigger.Instance())
                .withDisplay(new DisplayBuilder(Items.ENDER_PEARL, "magical_puissance")
                        .hidden(true)
                        .showToast(false)
                        .announceToChat(false)
                        .build())
                .register(consumer, new ResourceLocation(Astral.MOD_ID, "magical_puissance").toString());

        enchantingInsight = Advancement.Builder.builder()
                .withParent(magicalPuissance)
                .withCriterion("enchanting_insight", EnchantedItemTrigger.Instance.any())
                .withDisplay(new DisplayBuilder(Items.ENCHANTED_BOOK, "enchanting_insight").build())
                .withRewards(new AdvancementRewards(5, new ResourceLocation[]{}, new ResourceLocation[]{}, new FunctionObject.CacheableFunction(new ResourceLocation("astral:give_key"))))
                .register(consumer, new ResourceLocation(Astral.MOD_ID, "enchanting_insight").toString());

        brewingInsight = Advancement.Builder.builder()
                .withParent(magicalPuissance)
                .withCriterion("brewing_insight", BrewedPotionTrigger.Instance.brewedPotion())
                .withDisplay(new DisplayBuilder(Items.BREWING_STAND, "brewing_insight").build())
                .withRewards(new AdvancementRewards(5, new ResourceLocation[]{}, new ResourceLocation[]{}, new FunctionObject.CacheableFunction(new ResourceLocation("astral:give_key"))))
                .register(consumer, new ResourceLocation(Astral.MOD_ID, "brewing_insight").toString());

        autonomousInsight = Advancement.Builder.builder()
                .withParent(magicalPuissance)
                .withCriterion("autonomous_insight", new SummonedEntityTrigger.Instance(EntityPredicate.Builder.create().type(EntityType.IRON_GOLEM).build()))
                .withDisplay(new DisplayBuilder(Items.CARVED_PUMPKIN, "autonomous_insight").build())
                .withRewards(new AdvancementRewards(5, new ResourceLocation[]{}, new ResourceLocation[]{}, new FunctionObject.CacheableFunction(new ResourceLocation("astral:give_key"))))
                .register(consumer, new ResourceLocation(Astral.MOD_ID, "autonomous_insight").toString());

        medicalInsight = Advancement.Builder.builder()
                .withParent(magicalPuissance)
                .withCriterion("medical_insight", CuredZombieVillagerTrigger.Instance.any())
                .withDisplay(new DisplayBuilder(Items.GOLDEN_APPLE, "medical_insight").build())
                .withRewards(new AdvancementRewards(5, new ResourceLocation[]{}, new ResourceLocation[]{}, new FunctionObject.CacheableFunction(new ResourceLocation("astral:give_key"))))
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
