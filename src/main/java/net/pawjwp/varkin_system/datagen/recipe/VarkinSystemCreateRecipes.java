package net.pawjwp.varkin_system.datagen.recipe;

import com.simibubi.create.content.kinetics.millstone.MillingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.pawjwp.varkin_system.VarkinSystem;
import net.pawjwp.varkin_system.block.VarkinSystemBlocks;
import net.pawjwp.varkin_system.block.VarkinSystemBlocks.CrystalSet;

import java.util.function.Consumer;

public class VarkinSystemCreateRecipes {

    public static void register(Consumer<FinishedRecipe> consumer) {
        for (CrystalSet set : VarkinSystemBlocks.CRYSTAL_SETS) {
            new ProcessingRecipeBuilder<>(MillingRecipe::new,
                    ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID,
                            set.name() + "_crystal_milling"))
                .require(ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", "gems/" + set.name())))
                .output(set.dust().get())
                .output(0.5f, set.dust().get(), 1)
                .duration(200)
                .whenModLoaded("create")
                .build(consumer);
        }
    }
}
