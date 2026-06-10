package net.pawjwp.varkin_system.datagen.recipe;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.registries.ForgeRegistries;
import net.pawjwp.varkin_system.VarkinSystem;
import net.pawjwp.varkin_system.block.VarkinSystemBlocks;
import net.pawjwp.varkin_system.block.VarkinSystemBlocks.CrystalSet;

import java.util.function.Consumer;

public class VarkinSystemCraftingRecipes {
    public static void register(Consumer<FinishedRecipe> consumer) {
        for (CrystalSet set : VarkinSystemBlocks.CRYSTAL_SETS) {
            // 9 shards -> 1 block
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, set.storageBlockItem().get(), 1)
                    .pattern("###")
                    .pattern("###")
                    .pattern("###")
                    .define('#', set.shard().get())
                    .unlockedBy("has_" + set.name() + "_crystal_shard",
                            InventoryChangeTrigger.TriggerInstance.hasItems(set.shard().get()))
                    .save(consumer);

            // 1 block -> 9 shards
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, set.shard().get(), 9)
                    .requires(set.storageBlockItem().get())
                    .unlockedBy("has_" + set.name() + "_block",
                            InventoryChangeTrigger.TriggerInstance.hasItems(set.storageBlockItem().get()))
                    .save(consumer, ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID,
                            set.name() + "_crystal_shard_from_" + set.name() + "_block"));
        }



        // Sliding doors
        ConditionalRecipe.builder()
                .addCondition(new ModLoadedCondition("create"))
                .addRecipe(c -> {
                    var plating = ForgeRegistries.ITEMS.getValue(ResourceLocation.parse("ad_astra:iron_plating"));
                    ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE,
                                    ForgeRegistries.ITEMS.getValue(ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, "iron_sliding_door")), 2)
                            .pattern("PP")
                            .pattern("GG")
                            .pattern("PP")
                            .define('G', Items.GLASS)
                            .define('P', plating)
                            .unlockedBy("has_iron_plating", InventoryChangeTrigger.TriggerInstance.hasItems(plating))
                            .save(c);
                })
                .build(consumer, ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, "iron_sliding_door"));

        ConditionalRecipe.builder()
                .addCondition(new ModLoadedCondition("create"))
                .addRecipe(c -> {
                    var plating = ForgeRegistries.ITEMS.getValue(ResourceLocation.parse("ad_astra:steel_plating"));
                    ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE,
                                    ForgeRegistries.ITEMS.getValue(ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, "steel_sliding_door")), 2)
                            .pattern("PP")
                            .pattern("GG")
                            .pattern("PP")
                            .define('G', Items.GLASS)
                            .define('P', plating)
                            .unlockedBy("has_steel_plating", InventoryChangeTrigger.TriggerInstance.hasItems(plating))
                            .save(c);
                })
                .build(consumer, ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, "steel_sliding_door"));

        ConditionalRecipe.builder()
                .addCondition(new ModLoadedCondition("create"))
                .addRecipe(c -> {
                    var plating = ForgeRegistries.ITEMS.getValue(ResourceLocation.parse("ad_astra:desh_plating"));
                    ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE,
                                    ForgeRegistries.ITEMS.getValue(ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, "desh_sliding_door")), 2)
                            .pattern("PP")
                            .pattern("GG")
                            .pattern("PP")
                            .define('G', Items.GLASS)
                            .define('P', plating)
                            .unlockedBy("has_desh_plating", InventoryChangeTrigger.TriggerInstance.hasItems(plating))
                            .save(c);
                })
                .build(consumer, ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, "desh_sliding_door"));

        ConditionalRecipe.builder()
                .addCondition(new ModLoadedCondition("create"))
                .addRecipe(c -> {
                    var plating = ForgeRegistries.ITEMS.getValue(ResourceLocation.parse("ad_astra:ostrum_plating"));
                    ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE,
                                    ForgeRegistries.ITEMS.getValue(ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, "ostrum_sliding_door")), 2)
                            .pattern("PP")
                            .pattern("GG")
                            .pattern("PP")
                            .define('G', Items.GLASS)
                            .define('P', plating)
                            .unlockedBy("has_ostrum_plating", InventoryChangeTrigger.TriggerInstance.hasItems(plating))
                            .save(c);
                })
                .build(consumer, ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, "ostrum_sliding_door"));

        ConditionalRecipe.builder()
                .addCondition(new ModLoadedCondition("create"))
                .addRecipe(c -> {
                    var plating = ForgeRegistries.ITEMS.getValue(ResourceLocation.parse("ad_astra:calorite_plating"));
                    ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE,
                                    ForgeRegistries.ITEMS.getValue(ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, "calorite_sliding_door")), 2)
                            .pattern("PP")
                            .pattern("GG")
                            .pattern("PP")
                            .define('G', Items.GLASS)
                            .define('P', plating)
                            .unlockedBy("has_calorite_plating", InventoryChangeTrigger.TriggerInstance.hasItems(plating))
                            .save(c);
                })
                .build(consumer, ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, "calorite_sliding_door"));
    }
}
