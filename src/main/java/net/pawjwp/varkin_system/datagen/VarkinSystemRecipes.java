package net.pawjwp.varkin_system.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.pawjwp.varkin_system.datagen.recipe.VarkinSystemCraftingRecipes;
import net.pawjwp.varkin_system.datagen.recipe.VarkinSystemCreateRecipes;
import net.pawjwp.varkin_system.datagen.recipe.VarkinSystemMekanismRecipes;
import net.pawjwp.varkin_system.datagen.recipe.VarkinSystemTConstructRecipes;

import java.util.function.Consumer;

public class VarkinSystemRecipes extends RecipeProvider implements IConditionBuilder {
    public VarkinSystemRecipes(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        VarkinSystemCraftingRecipes.register(consumer);
        VarkinSystemCreateRecipes.register(consumer);
        VarkinSystemMekanismRecipes.register(consumer);
        VarkinSystemTConstructRecipes.register(consumer);
    }
}
