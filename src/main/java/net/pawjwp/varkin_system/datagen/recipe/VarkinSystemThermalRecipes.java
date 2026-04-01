package net.pawjwp.varkin_system.datagen.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.pawjwp.varkin_system.VarkinSystem;
import net.pawjwp.varkin_system.block.VarkinSystemBlocks;
import net.pawjwp.varkin_system.block.VarkinSystemBlocks.CrystalSet;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class VarkinSystemThermalRecipes implements DataProvider {

    private final PackOutput output;

    public VarkinSystemThermalRecipes(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        for (CrystalSet set : VarkinSystemBlocks.CRYSTAL_SETS) {
            String name = set.name();

            // Pulverizer crushes gem to dust
            futures.add(saveRecipe(cache, buildPulverizerRecipe(name),
                    "compat/thermal/pulverizer/" + name + "_crystal"));

            // Crystallizer grows dust to gem with lava
            futures.add(saveRecipe(cache, buildCrystallizerRecipe(name),
                    "compat/thermal/crystallizer/" + name + "_dust"));

            // Lapidary Dynamo processes gem for 50000 RF
            futures.add(saveRecipe(cache, buildLapidaryFuelRecipe(name),
                    "compat/thermal/fuel/lapidary/" + name + "_crystal"));
        }

        // Centrifuge recipes
        futures.add(saveRecipe(cache, buildCentrifugeRecipe("chalcopyrite", 2,
                new ResultEntry[]{
                        result("thermal:copper_dust", 1, -1),
                        result("thermal:iron_dust", 1, -1),
                        result("thermal:sulfur_dust", 2, -1)
                }, 4000, modLoaded("thermal")),
                "compat/thermal/centrifuge/chalcopyrite_dust"));

        // Sphalerite WITH Create
        futures.add(saveRecipe(cache, buildCentrifugeRecipe("sphalerite", 2,
                new ResultEntry[]{
                        result("create:crushed_raw_zinc", 1, -1),
                        result("thermal:iron_dust", 1, -1),
                        result("thermal:sulfur_dust", 2, -1)
                }, 4000, modLoaded("thermal"), modLoaded("create")),
                "compat/thermal/centrifuge/sphalerite_dust_with_create"));

        // Sphalerite WITHOUT Create
        JsonArray sphaleriteNoCreateConditions = new JsonArray();
        sphaleriteNoCreateConditions.add(modLoaded("thermal"));
        JsonObject notCreate = new JsonObject();
        notCreate.addProperty("type", "forge:not");
        JsonObject createCondition = new JsonObject();
        createCondition.addProperty("type", "forge:mod_loaded");
        createCondition.addProperty("modid", "create");
        notCreate.add("value", createCondition);
        sphaleriteNoCreateConditions.add(notCreate);

        futures.add(saveRecipe(cache, buildCentrifugeRecipeRaw("sphalerite", 2,
                new ResultEntry[]{
                        result("thermal:iron_dust", 2, -1),
                        result("thermal:sulfur_dust", 2, -1)
                }, 4000, sphaleriteNoCreateConditions),
                "compat/thermal/centrifuge/sphalerite_dust_without_create"));

        futures.add(saveRecipe(cache, buildCentrifugeRecipe("pentlandite", 2,
                new ResultEntry[]{
                        result("thermal:iron_dust", 1, -1),
                        result("thermal:nickel_dust", 1, -1),
                        result("thermal:sulfur_dust", 2, -1)
                }, 4000, modLoaded("thermal")),
                "compat/thermal/centrifuge/pentlandite_dust"));

        futures.add(saveRecipe(cache, buildCentrifugeRecipe("stannite", 2,
                new ResultEntry[]{
                        result("thermal:copper_dust", 1, -1),
                        result("thermal:iron_dust", 1, 0.5f),
                        result("thermal:tin_dust", 1, 0.5f),
                        result("thermal:sulfur_dust", 2, -1)
                }, 4000, modLoaded("thermal")),
                "compat/thermal/centrifuge/stannite_dust"));

        futures.add(saveRecipe(cache, buildCentrifugeRecipe("galena", 2,
                new ResultEntry[]{
                        result("thermal:lead_dust", 1, 1.5f),
                        result("thermal:silver_dust", 1, 0.5f),
                        result("thermal:sulfur_dust", 2, -1)
                }, 4000, modLoaded("thermal")),
                "compat/thermal/centrifuge/galena_dust"));

        futures.add(saveRecipe(cache, buildCentrifugeRecipe("electrum", 2,
                new ResultEntry[]{
                        result("thermal:gold_dust", 1, -1),
                        result("thermal:silver_dust", 1, -1)
                }, 2000, modLoaded("thermal")),
                "compat/thermal/centrifuge/electrum_dust"));

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    // Recipe builders

    private JsonObject buildPulverizerRecipe(String crystalName) {
        JsonObject json = new JsonObject();
        addConditions(json, modLoaded("thermal"));
        json.addProperty("type", "thermal:pulverizer");
        JsonObject ingredient = new JsonObject();
        ingredient.addProperty("tag", "forge:gems/" + crystalName);
        json.add("ingredient", ingredient);
        JsonArray resultArray = new JsonArray();
        JsonObject resultObj = new JsonObject();
        resultObj.addProperty("item", "varkin_system:" + crystalName + "_dust");
        resultObj.addProperty("chance", 1.5);
        resultArray.add(resultObj);
        json.add("result", resultArray);
        return json;
    }

    private JsonObject buildCrystallizerRecipe(String crystalName) {
        JsonObject json = new JsonObject();
        addConditions(json, modLoaded("thermal"));
        json.addProperty("type", "thermal:crystallizer");
        JsonArray ingredients = new JsonArray();
        JsonObject fluid = new JsonObject();
        fluid.addProperty("fluid", "minecraft:lava");
        fluid.addProperty("amount", 500);
        ingredients.add(fluid);
        JsonObject itemIngredient = new JsonObject();
        itemIngredient.addProperty("tag", "forge:dusts/" + crystalName);
        ingredients.add(itemIngredient);
        json.add("ingredients", ingredients);
        JsonArray resultArray = new JsonArray();
        JsonObject resultObj = new JsonObject();
        resultObj.addProperty("item", "varkin_system:" + crystalName + "_crystal_shard");
        resultArray.add(resultObj);
        json.add("result", resultArray);
        return json;
    }

    private JsonObject buildLapidaryFuelRecipe(String crystalName) {
        JsonObject json = new JsonObject();
        addConditions(json, modLoaded("thermal"));
        json.addProperty("type", "thermal:lapidary_fuel");
        JsonObject ingredient = new JsonObject();
        ingredient.addProperty("tag", "forge:gems/" + crystalName);
        json.add("ingredient", ingredient);
        json.addProperty("energy", 50000);
        return json;
    }

    private JsonObject buildCentrifugeRecipe(String dustName, int inputCount,
            ResultEntry[] results, int energy, JsonObject... conditions) {
        JsonArray condArray = new JsonArray();
        for (JsonObject cond : conditions) {
            condArray.add(cond);
        }
        return buildCentrifugeRecipeRaw(dustName, inputCount, results, energy, condArray);
    }

    private JsonObject buildCentrifugeRecipeRaw(String dustName, int inputCount,
            ResultEntry[] results, int energy, JsonArray conditions) {
        JsonObject json = new JsonObject();
        json.add("conditions", conditions);
        json.addProperty("type", "thermal:centrifuge");
        JsonObject ingredient = new JsonObject();
        ingredient.addProperty("tag", "forge:dusts/" + dustName);
        if (inputCount > 1) ingredient.addProperty("count", inputCount);
        json.add("ingredient", ingredient);
        JsonArray resultArray = new JsonArray();
        for (ResultEntry r : results) {
            JsonObject resultObj = new JsonObject();
            resultObj.addProperty("item", r.item);
            if (r.count > 1) resultObj.addProperty("count", r.count);
            if (r.chance >= 0) resultObj.addProperty("chance", r.chance);
            resultArray.add(resultObj);
        }
        json.add("result", resultArray);
        json.addProperty("energy", energy);
        return json;
    }

    // Helpers

    private record ResultEntry(String item, int count, float chance) {}

    private static ResultEntry result(String item, int count, float chance) {
        return new ResultEntry(item, count, chance);
    }

    private static JsonObject modLoaded(String modid) {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "forge:mod_loaded");
        obj.addProperty("modid", modid);
        return obj;
    }

    private static void addConditions(JsonObject json, JsonObject... conditions) {
        JsonArray array = new JsonArray();
        for (JsonObject cond : conditions) {
            array.add(cond);
        }
        json.add("conditions", array);
    }

    private CompletableFuture<?> saveRecipe(CachedOutput cache, JsonObject json, String path) {
        Path outputPath = this.output.getOutputFolder(PackOutput.Target.DATA_PACK)
                .resolve(VarkinSystem.MOD_ID).resolve("recipes").resolve(path + ".json");
        return DataProvider.saveStable(cache, json, outputPath);
    }

    @Override
    public String getName() {
        return "Varkin System Thermal Recipes";
    }
}
