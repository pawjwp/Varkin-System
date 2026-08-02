package net.pawjwp.varkin_system.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.pawjwp.varkin_system.VarkinSystem;
import net.pawjwp.varkin_system.block.VarkinSystemBlocks;
import net.pawjwp.varkin_system.block.VarkinSystemBlocks.CrystalSet;
import net.pawjwp.varkin_system.item.VarkinSystemItems;
import net.pawjwp.varkin_system.tag.VarkinSystemTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class VarkinSystemItemTags extends ItemTagsProvider {

    public VarkinSystemItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, VarkinSystem.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        var storageBlocksTag = this.tag(Tags.Items.STORAGE_BLOCKS);

        var shipChairsTag = this.tag(VarkinSystemTags.SHIP_CHAIRS);
        for (var chair : VarkinSystemBlocks.SHIP_CHAIRS) {
            shipChairsTag.add(chair.get().asItem());
        }

        var doorsTag = this.tag(ItemTags.DOORS);
        for (var door : VarkinSystemBlocks.SLIDING_DOORS) {
            doorsTag.add(door.get().asItem());
        }

        var plasteelBlocksTag = this.tag(VarkinSystemTags.PLASTEEL_BLOCKS);
        var plasteelStorageTag = this.tag(VarkinSystemTags.FORGE_STORAGE_BLOCKS_PLASTEEL_ITEM);
        for (var block : VarkinSystemBlocks.PLASTEEL_BLOCKS) {
            plasteelBlocksTag.add(block.get().asItem());
            plasteelStorageTag.add(block.get().asItem());
        }
        storageBlocksTag.addTag(VarkinSystemTags.FORGE_STORAGE_BLOCKS_PLASTEEL_ITEM);

        var slabsTag = this.tag(ItemTags.SLABS);
        var plasteelSlabsTag = this.tag(VarkinSystemTags.PLASTEEL_SLABS);
        for (var slab : VarkinSystemBlocks.PLASTEEL_SLABS) {
            slabsTag.add(slab.get().asItem());
            plasteelSlabsTag.add(slab.get().asItem());
        }

        var stairsTag = this.tag(ItemTags.STAIRS);
        var plasteelStairsTag = this.tag(VarkinSystemTags.PLASTEEL_STAIRS);
        for (var stairs : VarkinSystemBlocks.PLASTEEL_STAIRS) {
            stairsTag.add(stairs.get().asItem());
            plasteelStairsTag.add(stairs.get().asItem());
        }

        var bookshelvesTag = this.tag(VarkinSystemTags.PLASTEEL_BOOKSHELVES);
        for (var bookshelf : VarkinSystemBlocks.PLASTEEL_BOOKSHELVES) {
            bookshelvesTag.add(bookshelf.get().asItem());
        }

        var cabinetsTag = this.tag(VarkinSystemTags.PLASTEEL_CABINETS);
        for (var cabinet : VarkinSystemBlocks.PLASTEEL_CABINETS) {
            cabinetsTag.add(cabinet.get().asItem());
        }

        var crystalShardsTag = this.tag(VarkinSystemTags.CRYSTAL_SHARDS);
        var gemsTag = this.tag(Tags.Items.GEMS);
        var dustsTag = this.tag(Tags.Items.DUSTS);
        for (CrystalSet set : VarkinSystemBlocks.CRYSTAL_SETS) {
            crystalShardsTag.add(set.shard().get());

            // forge:gems/CRYSTALNAME
            TagKey<Item> gemTag = ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", "gems/" + set.name()));
            this.tag(gemTag).add(set.shard().get());
            gemsTag.addTag(gemTag);

            // forge:storage_blocks/CRYSTALNAME
            TagKey<Item> storageBlockTag = ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", "storage_blocks/" + set.name()));
            this.tag(storageBlockTag).add(set.storageBlockItem().get());
            storageBlocksTag.addTag(storageBlockTag);

            // forge:dusts/CRYSTALNAME
            TagKey<Item> dustTag = ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", "dusts/" + set.name()));
            this.tag(dustTag).add(set.dust().get());
            dustsTag.addTag(dustTag);
        }

        // forge:dusts/netherite_scrap
        this.tag(VarkinSystemTags.FORGE_DUSTS_NETHERITE_SCRAP).add(VarkinSystemItems.NETHERITE_SCRAP_DUST.get());
        dustsTag.addTag(VarkinSystemTags.FORGE_DUSTS_NETHERITE_SCRAP);
    }
}
