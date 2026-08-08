package net.pawjwp.varkin_system.tag;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.pawjwp.varkin_system.VarkinSystem;

public class VarkinSystemTags {

    // Varkin System block tags
    public static final TagKey<Block> CRYSTAL_BLOCKS = blockTag(VarkinSystem.MOD_ID, "crystal_blocks");
    public static final TagKey<Block> CRYSTAL_CLUSTERS = blockTag(VarkinSystem.MOD_ID, "crystal_clusters");
    public static final TagKey<Block> BUDDING_CRYSTALS = blockTag(VarkinSystem.MOD_ID, "budding_crystals");
    public static final TagKey<Block> BASE_STONE_ICARUS = blockTag(VarkinSystem.MOD_ID, "base_stone_icarus");
    public static final TagKey<Block> BASE_STONE_TALOS = blockTag(VarkinSystem.MOD_ID, "base_stone_talos");
    public static final TagKey<Block> BASE_STONE_PERDIX = blockTag(VarkinSystem.MOD_ID, "base_stone_perdix");
    public static final TagKey<Block> BASE_STONE_IAPYX = blockTag(VarkinSystem.MOD_ID, "base_stone_iapyx");
    public static final TagKey<Block> BASE_STONE_ARIAD = blockTag(VarkinSystem.MOD_ID, "base_stone_ariad");
    public static final TagKey<Block> PLASTEEL_BLOCK = blockTag(VarkinSystem.MOD_ID, "plasteel_blocks");

    // Varkin System item tags
    public static final TagKey<Item> CRYSTAL_SHARDS = itemTag(VarkinSystem.MOD_ID, "crystal_shards");
    public static final TagKey<Item> SHIP_CHAIRS = itemTag(VarkinSystem.MOD_ID, "ship_chairs");
    public static final TagKey<Item> PLASTEEL_BLOCKS = itemTag(VarkinSystem.MOD_ID, "plasteel_blocks");
    public static final TagKey<Item> PLASTEEL_SLABS = itemTag(VarkinSystem.MOD_ID, "plasteel_slabs");
    public static final TagKey<Item> PLASTEEL_STAIRS = itemTag(VarkinSystem.MOD_ID, "plasteel_stairs");
    public static final TagKey<Item> PLASTEEL_BOOKSHELVES = itemTag(VarkinSystem.MOD_ID, "plasteel_bookshelves");
    public static final TagKey<Item> PLASTEEL_CABINETS = itemTag(VarkinSystem.MOD_ID, "plasteel_cabinets");

    // Forge common tags
    public static final TagKey<Block> FORGE_STORAGE_BLOCKS_PLASTEEL = blockTag("forge", "storage_blocks/plasteel");
    public static final TagKey<Item> FORGE_STORAGE_BLOCKS_PLASTEEL_ITEM = itemTag("forge", "storage_blocks/plasteel");
    public static final TagKey<Item> FORGE_DUSTS_STEEL = itemTag("forge", "dusts/steel");
    public static final TagKey<Item> FORGE_DUSTS_IRON = itemTag("forge", "dusts/iron");
    public static final TagKey<Item> FORGE_DUSTS_NETHERITE_SCRAP = itemTag("forge", "dusts/netherite_scrap");

    // External compat tags
    public static final TagKey<Block> DESOLATE_PLANET_PLASTEEL = blockTag("desolate_planet", "plasteel_block");
    public static final TagKey<Block> QUARK_NON_DOUBLE_DOOR = blockTag("quark", "non_double_door");

    // Helper functions
    private static TagKey<Block> blockTag(String namespace, String path) {
        return BlockTags.create(ResourceLocation.fromNamespaceAndPath(namespace, path));
    }

    private static TagKey<Item> itemTag(String namespace, String path) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath(namespace, path));
    }
}
