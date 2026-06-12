package net.pawjwp.varkin_system.datagen.recipe;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.registries.ForgeRegistries;
import net.pawjwp.varkin_system.VarkinSystem;
import net.pawjwp.varkin_system.block.VarkinSystemBlocks;
import net.pawjwp.varkin_system.block.VarkinSystemBlocks.CrystalSet;
import net.pawjwp.varkin_system.tag.VarkinSystemTags;

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

        // Ship chairs (require Create)
        var whitePlasteel = ForgeRegistries.ITEMS.getValue(
                ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, "white_plasteel_block"));
        for (DyeColor color : DyeColor.values()) {
            String name = color.getName() + "_ship_chair";

            // 4 white plasteel + 2 wool (or the matching Create seat)
            ConditionalRecipe.builder()
                    .addCondition(new ModLoadedCondition("create"))
                    .addRecipe(c -> {
                        Item chair = ForgeRegistries.ITEMS.getValue(
                                ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, name));
                        Item wool = ForgeRegistries.ITEMS.getValue(
                                ResourceLocation.fromNamespaceAndPath("minecraft", color.getName() + "_wool"));
                        Item seat = ForgeRegistries.ITEMS.getValue(
                                ResourceLocation.fromNamespaceAndPath("create", color.getName() + "_seat"));
                        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, chair)
                                .pattern("W  ")
                                .pattern("PWP")
                                .pattern("PP ")
                                .define('W', Ingredient.of(wool, seat))
                                .define('P', whitePlasteel)
                                .unlockedBy("has_white_plasteel_block",
                                        InventoryChangeTrigger.TriggerInstance.hasItems(whitePlasteel))
                                .save(c);
                    })
                    .build(consumer, ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, name));

            // Recolour any ship chair with a dye
            ConditionalRecipe.builder()
                    .addCondition(new ModLoadedCondition("create"))
                    .addRecipe(c -> {
                        Item chair = ForgeRegistries.ITEMS.getValue(
                                ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, name));
                        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, chair)
                                .requires(DyeItem.byColor(color))
                                .requires(VarkinSystemTags.SHIP_CHAIRS)
                                .unlockedBy("has_ship_chair", InventoryChangeTrigger.TriggerInstance.hasItems(
                                        DyeItem.byColor(color)))
                                .save(c);
                    })
                    .build(consumer, ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, name + "_from_dye"));
        }

        // Recolour any plasteel block with a dye
        for (DyeColor color : DyeColor.values()) {
            String name = color.getName() + "_plasteel_block";
            Item plasteelBlock = ForgeRegistries.ITEMS.getValue(
                    ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, name));
            ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, plasteelBlock)
                    .requires(DyeItem.byColor(color))
                    .requires(VarkinSystemTags.PLASTEEL_BLOCKS)
                    .unlockedBy("has_plasteel_block", InventoryChangeTrigger.TriggerInstance.hasItems(
                            DyeItem.byColor(color)))
                    .save(consumer, ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, name + "_from_dye"));
        }

        // Plasteel slabs and stairs
        for (DyeColor color : DyeColor.values()) {
            Item block = item(color.getName() + "_plasteel_block");
            Item slab = item(color.getName() + "_plasteel_slab");
            Item stairs = item(color.getName() + "_plasteel_stairs");
            var hasBlock = InventoryChangeTrigger.TriggerInstance.hasItems(block);

            // slab recipe
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, slab, 6)
                    .pattern("###")
                    .define('#', block)
                    .unlockedBy("has_plasteel_block", hasBlock)
                    .save(consumer);

            // stair recipe
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, stairs, 4)
                    .pattern("#  ")
                    .pattern("## ")
                    .pattern("###")
                    .define('#', block)
                    .unlockedBy("has_plasteel_block", hasBlock)
                    .save(consumer);

            // Stonecutting from full block
            SingleItemRecipeBuilder.stonecutting(Ingredient.of(block), RecipeCategory.BUILDING_BLOCKS, slab, 2)
                    .unlockedBy("has_plasteel_block", hasBlock)
                    .save(consumer, id(color.getName() + "_plasteel_slab_from_stonecutting"));
            SingleItemRecipeBuilder.stonecutting(Ingredient.of(block), RecipeCategory.BUILDING_BLOCKS, stairs, 1)
                    .unlockedBy("has_plasteel_block", hasBlock)
                    .save(consumer, id(color.getName() + "_plasteel_stairs_from_stonecutting"));

            // Recolor any stair/slab
            ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, slab)
                    .requires(DyeItem.byColor(color))
                    .requires(VarkinSystemTags.PLASTEEL_SLABS)
                    .unlockedBy("has_plasteel_slab", InventoryChangeTrigger.TriggerInstance.hasItems(DyeItem.byColor(color)))
                    .save(consumer, id(color.getName() + "_plasteel_slab_from_dye"));
            ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, stairs)
                    .requires(DyeItem.byColor(color))
                    .requires(VarkinSystemTags.PLASTEEL_STAIRS)
                    .unlockedBy("has_plasteel_stairs", InventoryChangeTrigger.TriggerInstance.hasItems(DyeItem.byColor(color)))
                    .save(consumer, id(color.getName() + "_plasteel_stairs_from_dye"));
        }
    }

    private static Item item(String path) {
        return ForgeRegistries.ITEMS.getValue(ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, path));
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, path);
    }
}
