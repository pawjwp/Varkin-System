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

    // Item tags
    public static final TagKey<Item> CRYSTAL_SHARDS =
            ItemTags.create(ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, "crystal_shards"));
}
