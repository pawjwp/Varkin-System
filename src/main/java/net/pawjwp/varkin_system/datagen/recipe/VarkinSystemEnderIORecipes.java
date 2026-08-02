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

public class VarkinSystemEnderIORecipes implements DataProvider {

    private final PackOutput output;

    public VarkinSystemEnderIORecipes(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        for (CrystalSet set : VarkinSystemBlocks.CRYSTAL_SETS) {
            String name = set.name();
            futures.add(saveRecipe(cache, buildSagMillRecipe("tag", "forge:gems/" + name,
                            "varkin_system:" + name + "_dust", 0.5f),
                    "compat/enderio/" + name + "_crystal_sag_milling"));
        }

        // Netherite scrap crushes into dust
        futures.add(saveRecipe(cache, buildSagMillRecipe("item", "minecraft:netherite_scrap",
                        "varkin_system:netherite_scrap_dust", 0f),
                "compat/enderio/netherite_scrap_sag_milling"));

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    // Input key is either a tag or item
    // Bonus chance of zero skips the second output
    private JsonObject buildSagMillRecipe(String inputKey, String inputId, String resultItem, float bonusChance) {
        JsonObject json = new JsonObject();

        // Conditions
        JsonArray conditions = new JsonArray();
        JsonObject modLoaded = new JsonObject();
        modLoaded.addProperty("type", "forge:mod_loaded");
        modLoaded.addProperty("modid", "enderio");
        conditions.add(modLoaded);
        json.add("conditions", conditions);

        json.addProperty("type", "enderio:sag_milling");
        json.addProperty("energy", 2400);

        JsonObject input = new JsonObject();
        input.addProperty(inputKey, inputId);
        json.add("input", input);

        JsonArray outputs = new JsonArray();

        // Guaranteed 1 dust
        JsonObject primaryOutput = new JsonObject();
        JsonObject primaryItem = new JsonObject();
        primaryItem.addProperty("item", resultItem);
        primaryOutput.add("item", primaryItem);
        primaryOutput.addProperty("chance", 1.0);
        primaryOutput.addProperty("optional", false);
        outputs.add(primaryOutput);

        // Bonus result, boostable by grinding balls
        if (bonusChance > 0) {
            JsonObject bonusOutput = new JsonObject();
            JsonObject bonusItem = new JsonObject();
            bonusItem.addProperty("item", resultItem);
            bonusOutput.add("item", bonusItem);
            bonusOutput.addProperty("chance", bonusChance);
            bonusOutput.addProperty("optional", false);
            outputs.add(bonusOutput);
        }

        json.add("outputs", outputs);

        return json;
    }

    private CompletableFuture<?> saveRecipe(CachedOutput cache, JsonObject json, String path) {
        Path outputPath = this.output.getOutputFolder(PackOutput.Target.DATA_PACK)
                .resolve(VarkinSystem.MOD_ID).resolve("recipes").resolve(path + ".json");
        return DataProvider.saveStable(cache, json, outputPath);
    }

    @Override
    public String getName() {
        return "Varkin System EnderIO Recipes";
    }
}
