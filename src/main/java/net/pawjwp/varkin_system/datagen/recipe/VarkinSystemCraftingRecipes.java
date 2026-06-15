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
import net.minecraftforge.common.Tags;
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



        // Sliding doors cost 4 plating and 2 glass to make 2 doors
        // All require Ad Astra and Create
        for (String material : new String[]{"iron", "steel", "desh", "ostrum", "calorite"}) {
            String door = material + "_sliding_door";
            ConditionalRecipe.builder()
                    .addCondition(new ModLoadedCondition("create"))
                    .addCondition(new ModLoadedCondition("ad_astra"))
                    .addRecipe(c -> {
                        var plating = ForgeRegistries.ITEMS.getValue(ResourceLocation.parse("ad_astra:" + material + "_plating"));
                        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE,
                                        ForgeRegistries.ITEMS.getValue(ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, door)), 2)
                                .pattern("PP")
                                .pattern("GG")
                                .pattern("PP")
                                .define('G', Items.GLASS)
                                .define('P', plating)
                                .unlockedBy("has_" + material + "_plating", InventoryChangeTrigger.TriggerInstance.hasItems(plating))
                                .save(c);
                    })
                    .build(consumer, ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, door));
        }

        // Construction recipes for each plasteel block type
        for (DyeColor color : DyeColor.values()) {
            Item block = item(color.getName() + "_plasteel_block");
            Item slab = item(color.getName() + "_plasteel_slab");
            Item stairs = item(color.getName() + "_plasteel_stairs");
            Item dyeItem = DyeItem.byColor(color);

            var hasBlock = InventoryChangeTrigger.TriggerInstance.hasItems(block);
            var hasDye = InventoryChangeTrigger.TriggerInstance.hasItems(dyeItem);

            // ------------------------------
            // Plasteel derived block recipes
            // ------------------------------

            // Slabs
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, slab, 6)
                    .pattern("###")
                    .define('#', block)
                    .unlockedBy("has_plasteel_block", hasBlock)
                    .save(consumer);
            SingleItemRecipeBuilder.stonecutting(Ingredient.of(block), RecipeCategory.BUILDING_BLOCKS, slab, 2)
                    .unlockedBy("has_plasteel_block", hasBlock)
                    .save(consumer, id(color.getName() + "_plasteel_slab_from_stonecutting"));

            // Stairs
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, stairs, 4)
                    .pattern("#  ")
                    .pattern("## ")
                    .pattern("###")
                    .define('#', block)
                    .unlockedBy("has_plasteel_block", hasBlock)
                    .save(consumer);
            SingleItemRecipeBuilder.stonecutting(Ingredient.of(block), RecipeCategory.BUILDING_BLOCKS, stairs, 1)
                    .unlockedBy("has_plasteel_block", hasBlock)
                    .save(consumer, id(color.getName() + "_plasteel_stairs_from_stonecutting"));
            
            // Bookshelf (crafted from 6 plasteel and 3 plasteel slabs)
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, item(color.getName() + "_plasteel_bookshelf"))
                    .pattern("PPP")
                    .pattern("SSS")
                    .pattern("PPP")
                    .define('P', block)
                    .define('S', slab)
                    .unlockedBy("has_plasteel_block", hasBlock)
                    .save(consumer);

            // Cabinet (crafted from a chest and 8 plasteel, only if Sophisticated Storage is present)
            ConditionalRecipe.builder()
                    .addCondition(new ModLoadedCondition("sophisticatedstorage"))
                    .addRecipe(cc -> ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, item(color.getName() + "_plasteel_cabinet"))
                            .pattern("PPP")
                            .pattern("PCP")
                            .pattern("PPP")
                            .define('P', block)
                            .define('C', Tags.Items.CHESTS_WOODEN)
                            .unlockedBy("has_plasteel_block", hasBlock)
                            .save(cc))
                    .build(consumer, id(color.getName() + "_plasteel_cabinet"));

            // Ship chair (crafted from 4 white plasteel and 2 wool or a Create seat, only if Create is present)
            ConditionalRecipe.builder()
                    .addCondition(new ModLoadedCondition("create"))
                    .addRecipe(cc -> {
                        Item whitePlasteel = item("white_plasteel_block");
                        Item wool = ForgeRegistries.ITEMS.getValue(ResourceLocation.fromNamespaceAndPath("minecraft", color.getName() + "_wool"));
                        Item seat = ForgeRegistries.ITEMS.getValue(ResourceLocation.fromNamespaceAndPath("create", color.getName() + "_seat"));
                        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, item(color.getName() + "_ship_chair"))
                                .pattern("W  ")
                                .pattern("PWP")
                                .pattern("PP ")
                                .define('W', Ingredient.of(wool, seat))
                                .define('P', whitePlasteel)
                                .unlockedBy("has_white_plasteel_block", InventoryChangeTrigger.TriggerInstance.hasItems(whitePlasteel))
                                .save(cc);
                    })
                    .build(consumer, id(color.getName() + "_ship_chair"));

            // ---------------------------------
            // Plasteel derived block recoloring
            // ---------------------------------

            ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, item(color.getName() + "_plasteel_block"))
                    .requires(dyeItem)
                    .requires(VarkinSystemTags.PLASTEEL_BLOCKS)
                    .unlockedBy("has_dye", hasDye)
                    .save(consumer, id(color.getName() + "_plasteel_block_from_dye"));
            ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, item(color.getName() + "_plasteel_slab"))
                    .requires(dyeItem)
                    .requires(VarkinSystemTags.PLASTEEL_SLABS)
                    .unlockedBy("has_dye", hasDye)
                    .save(consumer, id(color.getName() + "_plasteel_slab_from_dye"));
            ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, item(color.getName() + "_plasteel_stairs"))
                    .requires(dyeItem)
                    .requires(VarkinSystemTags.PLASTEEL_STAIRS)
                    .unlockedBy("has_dye", hasDye)
                    .save(consumer, id(color.getName() + "_plasteel_stairs_from_dye"));
            ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, item(color.getName() + "_plasteel_bookshelf"))
                    .requires(dyeItem)
                    .requires(VarkinSystemTags.PLASTEEL_BOOKSHELVES)
                    .unlockedBy("has_dye", hasDye)
                    .save(consumer, id(color.getName() + "_plasteel_bookshelf_from_dye"));

            // Cabinet recolor (if Sophisticated Storage is present)
            ConditionalRecipe.builder()
                    .addCondition(new ModLoadedCondition("sophisticatedstorage"))
                    .addRecipe(cc -> ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, item(color.getName() + "_plasteel_cabinet"))
                            .requires(dyeItem)
                            .requires(VarkinSystemTags.PLASTEEL_CABINETS)
                            .unlockedBy("has_dye", hasDye)
                            .save(cc, id(color.getName() + "_plasteel_cabinet_from_dye")))
                    .build(consumer, id(color.getName() + "_plasteel_cabinet_from_dye"));

            // Ship chair recolor (if Create is present)
            ConditionalRecipe.builder()
                    .addCondition(new ModLoadedCondition("create"))
                    .addRecipe(cc -> ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, item(color.getName() + "_ship_chair"))
                            .requires(dyeItem)
                            .requires(VarkinSystemTags.SHIP_CHAIRS)
                            .unlockedBy("has_dye", hasDye)
                            .save(cc, id(color.getName() + "_ship_chair_from_dye")))
                    .build(consumer, id(color.getName() + "_ship_chair_from_dye"));
        }
    }

    private static Item item(String path) {
        return ForgeRegistries.ITEMS.getValue(ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, path));
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, path);
    }
}
