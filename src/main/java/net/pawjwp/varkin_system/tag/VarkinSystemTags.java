package net.pawjwp.varkin_system.tag;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.pawjwp.varkin_system.VarkinSystem;

public class VarkinSystemTags {

    // Block tags
    public static final TagKey<Block> CRYSTAL_BLOCKS =
            BlockTags.create(ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, "crystal_blocks"));
    public static final TagKey<Block> CRYSTAL_CLUSTERS =
            BlockTags.create(ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, "crystal_clusters"));
    public static final TagKey<Block> BUDDING_CRYSTALS =
            BlockTags.create(ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, "budding_crystals"));
    public static final TagKey<Block> BASE_STONE_ICARUS =
            BlockTags.create(ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, "base_stone_icarus"));
    public static final TagKey<Block> PLASTEEL_BLOCK =
            BlockTags.create(ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, "plasteel_blocks"));

    // Item tags
    public static final TagKey<Item> CRYSTAL_SHARDS =
            ItemTags.create(ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, "crystal_shards"));
    public static final TagKey<Item> SHIP_CHAIRS =
            ItemTags.create(ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, "ship_chairs"));
    public static final TagKey<Item> PLASTEEL_BLOCKS =
            ItemTags.create(ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, "plasteel_blocks"));
    public static final TagKey<Item> PLASTEEL_SLABS =
            ItemTags.create(ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, "plasteel_slabs"));
    public static final TagKey<Item> PLASTEEL_STAIRS =
            ItemTags.create(ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, "plasteel_stairs"));
    public static final TagKey<Item> PLASTEEL_BOOKSHELVES =
            ItemTags.create(ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, "plasteel_bookshelves"));
    public static final TagKey<Item> PLASTEEL_CABINETS =
            ItemTags.create(ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, "plasteel_cabinets"));
}
