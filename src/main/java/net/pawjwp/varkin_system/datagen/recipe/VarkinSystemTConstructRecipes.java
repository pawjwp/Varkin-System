package net.pawjwp.varkin_system.datagen.recipe;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.pawjwp.varkin_system.VarkinSystem;
import slimeknights.tconstruct.library.recipe.melting.MeltingRecipeBuilder;

import java.util.function.Consumer;

public class VarkinSystemTConstructRecipes {

    private static final int INGOT_SIZE = 90;
    private static final int NUGGET_SIZE = 10;

    public static void register(Consumer<FinishedRecipe> consumer) {
        Consumer<FinishedRecipe> tconConsumer = withCondition(consumer, new ModLoadedCondition("tconstruct"));

        // Chalcopyrite melts into molten copper, byproduct of iron
        crystalMelting(tconConsumer, "chalcopyrite", "tconstruct:molten_copper", INGOT_SIZE,
                "tconstruct:molten_iron", NUGGET_SIZE * 3);

        // Sphalerite melts into molten iron, byproduct of zinc
        crystalMelting(tconConsumer, "sphalerite", "tconstruct:molten_iron", INGOT_SIZE,
                "tconstruct:molten_zinc", NUGGET_SIZE * 3);

        // Pentlandite melts into molten invar, byproduct of nickel
        crystalMelting(tconConsumer, "pentlandite", "tconstruct:molten_invar", INGOT_SIZE,
                "tconstruct:molten_nickel", NUGGET_SIZE * 3);

        // Stannite melts into molten bronze, byproduct of iron
        crystalMelting(tconConsumer, "stannite", "tconstruct:molten_bronze", INGOT_SIZE,
                "tconstruct:molten_iron", NUGGET_SIZE * 3);

        // Galena melts into molten lead, byproduct of silver
        crystalMelting(tconConsumer, "galena", "tconstruct:molten_lead", INGOT_SIZE,
                "tconstruct:molten_silver", NUGGET_SIZE * 3);

        // Electrum melts into molten electrum, no byproduct
        crystalMelting(tconConsumer, "electrum", "tconstruct:molten_electrum", INGOT_SIZE,
                null, 0);
    }

    private static void crystalMelting(Consumer<FinishedRecipe> consumer, String crystalName,
            String fluidId, int amount, String byproductFluidId, int byproductAmount) {
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(ResourceLocation.parse(fluidId));
        String folder = "compat/tconstruct/";

        // Gem melting gives x2 output
        var gemBuilder = MeltingRecipeBuilder.melting(
                Ingredient.of(ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", "gems/" + crystalName))),
                new FluidStack(fluid, amount * 2), 1.0f);
        if (byproductFluidId != null) {
            Fluid byproductFluid = ForgeRegistries.FLUIDS.getValue(ResourceLocation.parse(byproductFluidId));
            gemBuilder.addByproduct(new FluidStack(byproductFluid, byproductAmount * 2));
        }
        gemBuilder.save(consumer, location(folder + crystalName + "/gem"));

        // Dust melting gives normal output
        var dustBuilder = MeltingRecipeBuilder.melting(
                Ingredient.of(ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", "dusts/" + crystalName))),
                new FluidStack(fluid, amount), 1.0f);
        if (byproductFluidId != null) {
            Fluid byproductFluid = ForgeRegistries.FLUIDS.getValue(ResourceLocation.parse(byproductFluidId));
            dustBuilder.addByproduct(new FluidStack(byproductFluid, byproductAmount));
        }
        dustBuilder.save(consumer, location(folder + crystalName + "/dust"));

        // Block melting gives x18 output
        var blockBuilder = MeltingRecipeBuilder.melting(
                Ingredient.of(ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", "storage_blocks/" + crystalName))),
                new FluidStack(fluid, amount * 18), 3.0f);
        if (byproductFluidId != null) {
            Fluid byproductFluid = ForgeRegistries.FLUIDS.getValue(ResourceLocation.parse(byproductFluidId));
            blockBuilder.addByproduct(new FluidStack(byproductFluid, byproductAmount * 18));
        }
        blockBuilder.save(consumer, location(folder + crystalName + "/block"));
    }

    private static ResourceLocation location(String path) {
        return ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, path);
    }

    private static Consumer<FinishedRecipe> withCondition(Consumer<FinishedRecipe> consumer,
            ModLoadedCondition condition) {
        return recipe -> ConditionalRecipe.builder()
                .addCondition(condition)
                .addRecipe(recipe)
                .generateAdvancement()
                .build(consumer, recipe.getId());
    }
}
