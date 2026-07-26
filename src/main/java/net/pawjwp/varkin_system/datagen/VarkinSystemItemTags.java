package net.pawjwp.varkin_system.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
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
        var shipChairsTag = this.tag(VarkinSystemTags.SHIP_CHAIRS);
        for (var chair : VarkinSystemBlocks.SHIP_CHAIRS) {
            shipChairsTag.add(chair.get().asItem());
        }

        var plasteelBlocksTag = this.tag(VarkinSystemTags.PLASTEEL_BLOCKS);
        for (var block : VarkinSystemBlocks.PLASTEEL_BLOCKS) {
            plasteelBlocksTag.add(block.get().asItem());
        }

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
        for (CrystalSet set : VarkinSystemBlocks.CRYSTAL_SETS) {
            crystalShardsTag.add(set.shard().get());

            // forge:gems/CRYSTALNAME
            this.tag(ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", "gems/" + set.name())))
                    .add(set.shard().get());

            // forge:storage_blocks/CRYSTALNAME
            this.tag(ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", "storage_blocks/" + set.name())))
                    .add(set.storageBlockItem().get());

            // forge:dusts/CRYSTALNAME
            this.tag(ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", "dusts/" + set.name())))
                    .add(set.dust().get());
        }

        // forge:dusts/netherite_scrap
        this.tag(ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", "dusts/netherite_scrap")))
                .add(VarkinSystemItems.NETHERITE_SCRAP_DUST.get());
    }
}
