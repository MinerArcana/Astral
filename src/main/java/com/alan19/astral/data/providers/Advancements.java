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
import net.minecraft.advancements.criterion.*;
import net.minecraft.block.Blocks;
import net.minecraft.command.FunctionObject;
import net.minecraft.data.AdvancementProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.structure.Structure;

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
        root = Advancement.Builder.builder()
                .withDisplay(new DisplayBuilder(Constants.getAstronomicon(), "root")
                        .hidden(true)
                        .showToast(false)
                        .announceToChat(false)
                        .background(new ResourceLocation("astral:textures/block/ether_dirt.png"))
                        .build())
                .withCriterion("tick", new TickTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND))
                .register(consumer, "astral:root");

        craftTravelingMedicine = Advancement.Builder.builder()
                .withParent(root)
                .withCriterion("craft_traveling_medicine", InventoryChangeTrigger.Instance.forItems(AstralItems.TRAVELING_MEDICINE.get()))
                .withDisplay(new DisplayBuilder(AstralItems.TRAVELING_MEDICINE.get(), "are_you_experienced").build())
                .register(consumer, new ResourceLocation(Astral.MOD_ID, "are_you_experienced").toString());

        final AdvancementRewards enlightenmentKeyReward = new AdvancementRewards(5, new ResourceLocation[]{}, new ResourceLocation[]{}, new FunctionObject.CacheableFunction(new ResourceLocation("astral:give_key")));

        becomeAstral = Advancement.Builder.builder()
                .withParent(craftTravelingMedicine)
                .withCriterion("get_astral_travel", EffectsChangedTrigger.Instance.forEffect(MobEffectsPredicate.any().addEffect(AstralEffects.ASTRAL_TRAVEL.get())))
                .withDisplay(new DisplayBuilder(Items.PHANTOM_MEMBRANE, "steps_on_the_wind").build())
                .withRewards(enlightenmentKeyReward)
                .register(consumer, new ResourceLocation(Astral.MOD_ID, "steps_on_the_wind").toString());

        final String brewStrongAstralPotionName = "mile_stride";
        brewStrongAstralPotion = Advancement.Builder.builder()
                .withParent(craftTravelingMedicine)
                .withCriterion(brewStrongAstralPotionName, new BrewedPotionTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, AstralPotions.ASTRAL_TRAVEL_POTION.getStrongPotion().get()))
                .withDisplay(new DisplayBuilder(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), AstralPotions.ASTRAL_TRAVEL_POTION.getStrongPotion().get()), brewStrongAstralPotionName).build())
                .withRewards(enlightenmentKeyReward)
                .register(consumer, new ResourceLocation(Astral.MOD_ID, brewStrongAstralPotionName).toString());

        craftIntrospectionMedicine = Advancement.Builder.builder()
                .withParent(root)
                .withCriterion("craft_introspection_medicine", InventoryChangeTrigger.Instance.forItems(AstralItems.INTROSPECTION_MEDICINE.get()))
                .withDisplay(new DisplayBuilder(AstralItems.INTROSPECTION_MEDICINE.get(), "compartmentalization").build())
                .register(consumer, new ResourceLocation(Astral.MOD_ID, "compartmentalization").toString());

        innerRealm = Advancement.Builder.builder()
                .withParent(craftIntrospectionMedicine)
                .withCriterion("withdrawal", ChangeDimensionTrigger.Instance.toWorld(AstralDimensions.INNER_REALM))
                .withDisplay(new DisplayBuilder(AstralItems.ENLIGHTENMENT_KEY.get(), "withdrawal").build())
                .withRewards(enlightenmentKeyReward)
                .register(consumer, new ResourceLocation(Astral.MOD_ID, "withdrawal").toString());

        magicalPuissance = Advancement.Builder.builder()
                .withParent(innerRealm)
                .withCriterion("none", new TickTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND))
                .withDisplay(new DisplayBuilder(Items.EXPERIENCE_BOTTLE, "magical_puissance")
                        .hidden(true)
                        .showToast(false)
                        .announceToChat(false)
                        .build())
                .register(consumer, new ResourceLocation(Astral.MOD_ID, "magical_puissance").toString());

        enchantingInsight = Advancement.Builder.builder()
                .withParent(magicalPuissance)
                .withCriterion("imparting_the_spirit", EnchantedItemTrigger.Instance.any())
                .withDisplay(new DisplayBuilder(Items.ENCHANTED_BOOK, "imparting_the_spirit").build())
                .withRewards(enlightenmentKeyReward)
                .register(consumer, new ResourceLocation(Astral.MOD_ID, "imparting_the_spirit").toString());

        brewingInsight = Advancement.Builder.builder()
                .withParent(magicalPuissance)
                .withCriterion("improved_medicines", BrewedPotionTrigger.Instance.brewedPotion())
                .withDisplay(new DisplayBuilder(Items.BREWING_STAND, "improved_medicines").build())
                .withRewards(enlightenmentKeyReward)
                .register(consumer, new ResourceLocation(Astral.MOD_ID, "improved_medicines").toString());

        autonomousInsight = Advancement.Builder.builder()
                .withParent(magicalPuissance)
                .withCriterion("questioning_intelligence", SummonedEntityTrigger.Instance.summonedEntity(EntityPredicate.Builder.create().type(EntityType.IRON_GOLEM)))
                .withDisplay(new DisplayBuilder(Items.CARVED_PUMPKIN, "questioning_intelligence").build())
                .withRewards(enlightenmentKeyReward)
                .register(consumer, new ResourceLocation(Astral.MOD_ID, "questioning_intelligence").toString());

        medicalInsight = Advancement.Builder.builder()
                .withParent(magicalPuissance)
                .withCriterion("minds_renewed", CuredZombieVillagerTrigger.Instance.any())
                .withDisplay(new DisplayBuilder(Items.GOLDEN_APPLE, "minds_renewed").build())
                .withRewards(enlightenmentKeyReward)
                .register(consumer, new ResourceLocation(Astral.MOD_ID, "minds_renewed").toString());

        enterStronghold = Advancement.Builder.builder()
                .withParent(magicalPuissance)
                .withCriterion("seeing_through_other_eyes", PositionTrigger.Instance.forLocation(LocationPredicate.forFeature(Structure.STRONGHOLD)))
                .withDisplay(new DisplayBuilder(Items.ENDER_EYE, "seeing_through_other_eyes").build())
                .withRewards(enlightenmentKeyReward)
                .register(consumer, new ResourceLocation(Astral.MOD_ID, "seeing_through_other_eyes").toString());

        radiantPower = Advancement.Builder.builder()
                .withParent(magicalPuissance)
                .withCriterion("beacon", ConstructBeaconTrigger.Instance.forLevel(MinMaxBounds.IntBound.atLeast(4)))
                .withDisplay(new DisplayBuilder(Items.BEACON, "radiant_power").build())
                .withRewards(enlightenmentKeyReward)
                .register(consumer, new ResourceLocation(Astral.MOD_ID, "radiant_power").toString());

        dimensionalTravel = Advancement.Builder.builder()
                .withParent(innerRealm)
                .withCriterion("none", new TickTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND))
                .withDisplay(new DisplayBuilder(Items.END_PORTAL_FRAME, "dimensional_travel")
                        .announceToChat(false)
                        .showToast(false)
                        .hidden(true)
                        .build())
                .register(consumer, new ResourceLocation(Astral.MOD_ID, "dimensional_travel").toString());

        spectralWorld = Advancement.Builder.builder()
                .withParent(dimensionalTravel)
                .withCriterion("go_to_nether", ChangeDimensionTrigger.Instance.toWorld(World.THE_NETHER))
                .withDisplay(new DisplayBuilder(Items.FLINT_AND_STEEL, "spectral_world").build())
                .withRewards(enlightenmentKeyReward)
                .register(consumer, new ResourceLocation(Astral.MOD_ID, "spectral_world").toString());

        relativeDistance = Advancement.Builder.builder()
                .withParent(dimensionalTravel)
                .withCriterion("nether_travel", NetherTravelTrigger.Instance.forDistance(DistancePredicate.forHorizontal(MinMaxBounds.FloatBound.atLeast(7000))))
                .withDisplay(new DisplayBuilder(Items.MAP, "relative_distance").build())
                .withRewards(enlightenmentKeyReward)
                .register(consumer, new ResourceLocation(Astral.MOD_ID, "relative_distance").toString());

        infiniteExpanse = Advancement.Builder.builder()
                .withParent(dimensionalTravel)
                .withCriterion("enter_the_end", ChangeDimensionTrigger.Instance.toWorld(World.THE_END))
                .withDisplay(new DisplayBuilder(Items.END_STONE, "infinite_expanse").build())
                .withRewards(enlightenmentKeyReward)
                .register(consumer, new ResourceLocation(Astral.MOD_ID, "infinite_expanse").toString());

        voidSubstance = Advancement.Builder.builder()
                .withParent(dimensionalTravel)
                .withCriterion("end_gateway", EnterBlockTrigger.Instance.forBlock(Blocks.END_GATEWAY))
                .withDisplay(new DisplayBuilder(Items.ENDER_PEARL, "substance_of_the_void").build())
                .withRewards(enlightenmentKeyReward)
                .register(consumer, new ResourceLocation(Astral.MOD_ID, "substance_of_the_void").toString());

        yourWings = Advancement.Builder.builder()
                .withParent(dimensionalTravel)
                .withCriterion("wings_of_your_own", InventoryChangeTrigger.Instance.forItems(Items.ELYTRA))
                .withDisplay(new DisplayBuilder(Items.ELYTRA, "wings_of_your_own").build())
                .withRewards(enlightenmentKeyReward)
                .register(consumer, new ResourceLocation(Astral.MOD_ID, "wings_of_your_own").toString());

        reaperCreeper = Advancement.Builder.builder()
                .withParent(becomeAstral)
                .withCriterion("use_etheric_powder", InventoryChangeTrigger.Instance.forItems(AstralItems.ETHERIC_POWDER_ITEM.get()))
                .withDisplay(new DisplayBuilder(AstralItems.ETHERIC_POWDER_ITEM.get(), "reaper_creeper").build())
                .register(consumer, new ResourceLocation(Astral.MOD_ID, "reaper_creeper").toString());

        etherealHunter = Advancement.Builder.builder()
                .withParent(becomeAstral)
                .withCriterion("have_astral_travel", EffectsChangedTrigger.Instance.forEffect(MobEffectsPredicate.any().addEffect(AstralEffects.ASTRAL_TRAVEL.get())))
                .withCriterion("kill_spiritual", KilledTrigger.Instance.playerKilledEntity(EntityPredicate.Builder.create().type(AstralTags.ATTUNED_ENTITIES)))
                .withDisplay(new DisplayBuilder(AstralItems.SLEEPLESS_EYE.get(), "ethereal_hunter").build())
                .withRewards(enlightenmentKeyReward)
                .register(consumer, new ResourceLocation(Astral.MOD_ID, "ethereal_hunter").toString());
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
