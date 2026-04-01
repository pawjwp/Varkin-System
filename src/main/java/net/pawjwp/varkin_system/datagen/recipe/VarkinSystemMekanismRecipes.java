package net.pawjwp.varkin_system.datagen.recipe;

import mekanism.api.datagen.recipe.builder.ItemStackToItemStackRecipeBuilder;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.pawjwp.varkin_system.VarkinSystem;
import net.pawjwp.varkin_system.block.VarkinSystemBlocks;
import net.pawjwp.varkin_system.block.VarkinSystemBlocks.CrystalSet;

import java.util.function.Consumer;

public class VarkinSystemMekanismRecipes {

    public static void register(Consumer<FinishedRecipe> consumer) {
        for (CrystalSet set : VarkinSystemBlocks.CRYSTAL_SETS) {
            TagKey<Item> gemTag = ItemTags.create(
                    ResourceLocation.fromNamespaceAndPath("forge", "gems/" + set.name()));

            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID,
                    "compat/mekanism/" + set.name() + "_crystal_crushing");

            ItemStack output = new ItemStack(set.dust().get(), 2);

            ConditionalRecipe.builder()
                .addCondition(new ModLoadedCondition("mekanism"))
                .addRecipe(c ->
                    ItemStackToItemStackRecipeBuilder.crushing(
                        IngredientCreatorAccess.item().from(gemTag),
                        output
                    ).build(c, id)
                )
                .generateAdvancement()
                .build(consumer, id);
        }
    }
}
