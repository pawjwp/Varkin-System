package net.pawjwp.varkin_system.datagen.recipe;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
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
    }
}
