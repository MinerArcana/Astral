package com.alan19.astral.data.providers;

import com.alan19.astral.Astral;
import com.alan19.astral.dimensions.AstralDimensions;
import com.alan19.astral.effects.AstralEffects;
import com.alan19.astral.items.AstralItems;
import com.alan19.astral.potions.AstralPotions;
import com.alan19.astral.tags.AstralTags;
import com.alan19.astral.util.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.*;
import net.minecraft.commands.CommandFunction;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.worldgen.StructureFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;

public class Advancements extends AdvancementProvider {
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    private final DataGenerator dataGenerator;
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
    private Advancement dimensionalTravel;
    private Advancement spectralWorld;
    private Advancement relativeDistance;
    private Advancement infiniteExpanse;
    private Advancement voidSubstance;
    private Advancement yourWings;
    private Advancement enterStronghold;
    private Advancement radiantPower;
    private Advancement reaperCreeper;
    private Advancement etherealHunter;

    public Advancements(@Nonnull DataGenerator dataGenerator) {
        super(dataGenerator);
        this.dataGenerator = dataGenerator;
    }

    public Advancement getEnterStronghold() {
        return enterStronghold;
    }

    public Advancement getRadiantPower() {
        return radiantPower;
    }

    public Advancement getDimensionalTravel() {
        return dimensionalTravel;
    }

    public Advancement getSpectralWorld() {
        return spectralWorld;
    }

    public Advancement getRelativeDistance() {
        return relativeDistance;
    }

    public Advancement getInfiniteExpanse() {
        return infiniteExpanse;
    }

    public Advancement getVoidSubstance() {
        return voidSubstance;
    }

    public Advancement getYourWings() {
        return yourWings;
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

    public Advancement getReaperCreeper() {
        return reaperCreeper;
    }

    public Advancement getEtherealHunter() {
        return etherealHunter;
    }

    private void registerAdvancements(Consumer<Advancement> consumer) {
        root = Advancement.Builder.advancement()
                .display(new DisplayBuilder(Constants.getAstronomicon(), "root")
                        .hidden(true)
                        .showToast(false)
                        .announceToChat(false)
                        .background(new ResourceLocation("astral:textures/block/ether_dirt.png"))
                        .build())
                .addCriterion("tick", new TickTrigger.TriggerInstance(EntityPredicate.Composite.ANY))
                .save(consumer, "astral:root");

        craftTravelingMedicine = Advancement.Builder.advancement()
                .parent(root)
                .addCriterion("craft_traveling_medicine", InventoryChangeTrigger.TriggerInstance.hasItems(AstralItems.TRAVELING_MEDICINE.get()))
                .display(new DisplayBuilder(AstralItems.TRAVELING_MEDICINE.get(), "are_you_experienced").build())
                .save(consumer, new ResourceLocation(Astral.MOD_ID, "are_you_experienced").toString());

        final AdvancementRewards enlightenmentKeyReward = new AdvancementRewards(5, new ResourceLocation[]{}, new ResourceLocation[]{}, new CommandFunction.CacheableFunction(new ResourceLocation(Astral.MOD_ID, "give_key")));

        becomeAstral = Advancement.Builder.advancement()
                .parent(craftTravelingMedicine)
                .addCriterion("get_astral_travel", EffectsChangedTrigger.TriggerInstance.hasEffects(MobEffectsPredicate.effects().and(AstralEffects.ASTRAL_TRAVEL.get())))
                .display(new DisplayBuilder(Items.PHANTOM_MEMBRANE, "steps_on_the_wind").build())
                .rewards(enlightenmentKeyReward)
                .save(consumer, new ResourceLocation(Astral.MOD_ID, "steps_on_the_wind").toString());

        final String brewStrongAstralPotionName = "mile_stride";
        brewStrongAstralPotion = Advancement.Builder.advancement()
                .parent(craftTravelingMedicine)
                .addCriterion(brewStrongAstralPotionName, new BrewedPotionTrigger.TriggerInstance(EntityPredicate.Composite.ANY, AstralPotions.ASTRAL_TRAVEL_POTION.getStrongPotion().get()))
                .display(new DisplayBuilder(PotionUtils.setPotion(new ItemStack(Items.POTION), AstralPotions.ASTRAL_TRAVEL_POTION.getStrongPotion().get()), brewStrongAstralPotionName).build())
                .rewards(enlightenmentKeyReward)
                .save(consumer, new ResourceLocation(Astral.MOD_ID, brewStrongAstralPotionName).toString());

        craftIntrospectionMedicine = Advancement.Builder.advancement()
                .parent(root)
                .addCriterion("craft_introspection_medicine", InventoryChangeTrigger.TriggerInstance.hasItems(AstralItems.INTROSPECTION_MEDICINE.get()))
                .display(new DisplayBuilder(AstralItems.INTROSPECTION_MEDICINE.get(), "compartmentalization").build())
                .save(consumer, new ResourceLocation(Astral.MOD_ID, "compartmentalization").toString());

        innerRealm = Advancement.Builder.advancement()
                .parent(craftIntrospectionMedicine)
                .addCriterion("withdrawal", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(AstralDimensions.INNER_REALM))
                .display(new DisplayBuilder(AstralItems.ENLIGHTENMENT_KEY.get(), "withdrawal").build())
                .rewards(enlightenmentKeyReward)
                .save(consumer, new ResourceLocation(Astral.MOD_ID, "withdrawal").toString());

        magicalPuissance = Advancement.Builder.advancement()
                .parent(innerRealm)
                .addCriterion("none", new TickTrigger.TriggerInstance(EntityPredicate.Composite.ANY))
                .display(new DisplayBuilder(Items.EXPERIENCE_BOTTLE, "magical_puissance")
                        .hidden(true)
                        .showToast(false)
                        .announceToChat(false)
                        .build())
                .save(consumer, new ResourceLocation(Astral.MOD_ID, "magical_puissance").toString());

        enchantingInsight = Advancement.Builder.advancement()
                .parent(magicalPuissance)
                .addCriterion("imparting_the_spirit", EnchantedItemTrigger.TriggerInstance.enchantedItem())
                .display(new DisplayBuilder(Items.ENCHANTED_BOOK, "imparting_the_spirit").build())
                .rewards(enlightenmentKeyReward)
                .save(consumer, new ResourceLocation(Astral.MOD_ID, "imparting_the_spirit").toString());

        brewingInsight = Advancement.Builder.advancement()
                .parent(magicalPuissance)
                .addCriterion("improved_medicines", BrewedPotionTrigger.TriggerInstance.brewedPotion())
                .display(new DisplayBuilder(Items.BREWING_STAND, "improved_medicines").build())
                .rewards(enlightenmentKeyReward)
                .save(consumer, new ResourceLocation(Astral.MOD_ID, "improved_medicines").toString());

        autonomousInsight = Advancement.Builder.advancement()
                .parent(magicalPuissance)
                .addCriterion("questioning_intelligence", SummonedEntityTrigger.TriggerInstance.summonedEntity(EntityPredicate.Builder.entity().of(EntityType.IRON_GOLEM)))
                .display(new DisplayBuilder(Items.CARVED_PUMPKIN, "questioning_intelligence").build())
                .rewards(enlightenmentKeyReward)
                .save(consumer, new ResourceLocation(Astral.MOD_ID, "questioning_intelligence").toString());

        medicalInsight = Advancement.Builder.advancement()
                .parent(magicalPuissance)
                .addCriterion("minds_renewed", CuredZombieVillagerTrigger.TriggerInstance.curedZombieVillager())
                .display(new DisplayBuilder(Items.GOLDEN_APPLE, "minds_renewed").build())
                .rewards(enlightenmentKeyReward)
                .save(consumer, new ResourceLocation(Astral.MOD_ID, "minds_renewed").toString());

        enterStronghold = Advancement.Builder.advancement()
                .parent(magicalPuissance)
                .addCriterion("seeing_through_other_eyes", LocationTrigger.TriggerInstance.located(LocationPredicate.inFeature(StructureFeatures.STRONGHOLD.unwrapKey().get())))
                .display(new DisplayBuilder(Items.ENDER_EYE, "seeing_through_other_eyes").build())
                .rewards(enlightenmentKeyReward)
                .save(consumer, new ResourceLocation(Astral.MOD_ID, "seeing_through_other_eyes").toString());

        radiantPower = Advancement.Builder.advancement()
                .parent(magicalPuissance)
                .addCriterion("beacon", ConstructBeaconTrigger.TriggerInstance.constructedBeacon(MinMaxBounds.Ints.atLeast(4)))
                .display(new DisplayBuilder(Items.BEACON, "radiant_power").build())
                .rewards(enlightenmentKeyReward)
                .save(consumer, new ResourceLocation(Astral.MOD_ID, "radiant_power").toString());

        dimensionalTravel = Advancement.Builder.advancement()
                .parent(innerRealm)
                .addCriterion("none", new TickTrigger.TriggerInstance(EntityPredicate.Composite.ANY))
                .display(new DisplayBuilder(Items.END_PORTAL_FRAME, "dimensional_travel")
                        .announceToChat(false)
                        .showToast(false)
                        .hidden(true)
                        .build())
                .save(consumer, new ResourceLocation(Astral.MOD_ID, "dimensional_travel").toString());

        spectralWorld = Advancement.Builder.advancement()
                .parent(dimensionalTravel)
                .addCriterion("go_to_nether", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(Level.NETHER))
                .display(new DisplayBuilder(Items.FLINT_AND_STEEL, "spectral_world").build())
                .rewards(enlightenmentKeyReward)
                .save(consumer, new ResourceLocation(Astral.MOD_ID, "spectral_world").toString());

        relativeDistance = Advancement.Builder.advancement()
                .parent(dimensionalTravel)
                .addCriterion("nether_travel", DistanceTrigger.TriggerInstance.travelledThroughNether(DistancePredicate.horizontal(MinMaxBounds.Doubles.atLeast(7000))))
                .display(new DisplayBuilder(Items.MAP, "relative_distance").build())
                .rewards(enlightenmentKeyReward)
                .save(consumer, new ResourceLocation(Astral.MOD_ID, "relative_distance").toString());

        infiniteExpanse = Advancement.Builder.advancement()
                .parent(dimensionalTravel)
                .addCriterion("enter_the_end", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(Level.END))
                .display(new DisplayBuilder(Items.END_STONE, "infinite_expanse").build())
                .rewards(enlightenmentKeyReward)
                .save(consumer, new ResourceLocation(Astral.MOD_ID, "infinite_expanse").toString());

        voidSubstance = Advancement.Builder.advancement()
                .parent(dimensionalTravel)
                .addCriterion("end_gateway", EnterBlockTrigger.TriggerInstance.entersBlock(Blocks.END_GATEWAY))
                .display(new DisplayBuilder(Items.ENDER_PEARL, "substance_of_the_void").build())
                .rewards(enlightenmentKeyReward)
                .save(consumer, new ResourceLocation(Astral.MOD_ID, "substance_of_the_void").toString());

        yourWings = Advancement.Builder.advancement()
                .parent(dimensionalTravel)
                .addCriterion("wings_of_your_own", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ELYTRA))
                .display(new DisplayBuilder(Items.ELYTRA, "wings_of_your_own").build())
                .rewards(enlightenmentKeyReward)
                .save(consumer, new ResourceLocation(Astral.MOD_ID, "wings_of_your_own").toString());

        reaperCreeper = Advancement.Builder.advancement()
                .parent(becomeAstral)
                .addCriterion("use_etheric_powder", InventoryChangeTrigger.TriggerInstance.hasItems(AstralItems.ETHERIC_POWDER_ITEM.get()))
                .display(new DisplayBuilder(AstralItems.ETHERIC_POWDER_ITEM.get(), "reaper_creeper").build())
                .save(consumer, new ResourceLocation(Astral.MOD_ID, "reaper_creeper").toString());

        etherealHunter = Advancement.Builder.advancement()
                .parent(becomeAstral)
                .addCriterion("have_astral_travel", EffectsChangedTrigger.TriggerInstance.hasEffects(MobEffectsPredicate.effects().and(AstralEffects.ASTRAL_TRAVEL.get())))
                .addCriterion("kill_spiritual", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(AstralTags.ATTUNED_ENTITIES)))
                .display(new DisplayBuilder(AstralItems.SLEEPLESS_EYE.get(), "ethereal_hunter").build())
                .rewards(enlightenmentKeyReward)
                .save(consumer, new ResourceLocation(Astral.MOD_ID, "ethereal_hunter").toString());
    }

    @Override
    public void run(@Nonnull HashCache cache) {
        Path outputFolder = this.dataGenerator.getOutputFolder();
        Consumer<Advancement> consumer = advancement -> {
            Path path = outputFolder.resolve("data/" + advancement.getId().getNamespace() + "/advancements/" + advancement.getId().getPath() + ".json");
            try {
                DataProvider.save(GSON, cache, advancement.deconstruct().serializeToJson(), path);
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
