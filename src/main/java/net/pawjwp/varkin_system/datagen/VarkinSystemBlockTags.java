package net.pawjwp.varkin_system.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.pawjwp.varkin_system.VarkinSystem;
import net.pawjwp.varkin_system.block.VarkinSystemBlocks;
import net.pawjwp.varkin_system.block.VarkinSystemBlocks.CrystalSet;
import net.pawjwp.varkin_system.tag.VarkinSystemTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class VarkinSystemBlockTags extends BlockTagsProvider {

    public VarkinSystemBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, VarkinSystem.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        var pickaxeTag = this.tag(BlockTags.MINEABLE_WITH_PICKAXE);
        var crystalBlocksTag = this.tag(VarkinSystemTags.CRYSTAL_BLOCKS);
        var clusterTag = this.tag(VarkinSystemTags.CRYSTAL_CLUSTERS);
        var buddingTag = this.tag(VarkinSystemTags.BUDDING_CRYSTALS);
        var storageBlocksTag = this.tag(Tags.Blocks.STORAGE_BLOCKS);

        for (CrystalSet set : VarkinSystemBlocks.CRYSTAL_SETS) {
            pickaxeTag.add(
                    set.small().get(), set.medium().get(),
                    set.large().get(), set.cluster().get(),
                    set.budding().get(), set.storageBlock().get()
            );

            crystalBlocksTag.add(
                    set.small().get(), set.medium().get(),
                    set.large().get(), set.cluster().get()
            );

            clusterTag.add(set.cluster().get());
            buddingTag.add(set.budding().get());

            // forge:storage_blocks/CRYSTALNAME (block tag)
            TagKey<Block> crystalStorageTag = BlockTags.create(
                    ResourceLocation.fromNamespaceAndPath("forge", "storage_blocks/" + set.name()));
            this.tag(crystalStorageTag).add(set.storageBlock().get());
            storageBlocksTag.addTag(crystalStorageTag);
        }

        // Stop Quark from interfering with double door opening, add pickaxe minable and door tags
        var doorsTag = this.tag(BlockTags.DOORS);
        var nonDoubleDoorTag = this.tag(VarkinSystemTags.QUARK_NON_DOUBLE_DOOR);
        for (RegistryObject<Block> door : VarkinSystemBlocks.SLIDING_DOORS) {
            pickaxeTag.add(door.get());
            doorsTag.add(door.get());
            nonDoubleDoorTag.add(door.get());
        }

        // Every plasteel-derived block is pickaxe-mineable
        for (RegistryObject<Block> block : VarkinSystemBlocks.PLASTEEL_DERIVED_BLOCKS) {
            pickaxeTag.add(block.get());
        }

        for (RegistryObject<Block> block : VarkinSystemBlocks.PLASTEEL_BLOCKS) {
            this.tag(VarkinSystemTags.PLASTEEL_BLOCK).add(block.get());
            this.tag(VarkinSystemTags.FORGE_STORAGE_BLOCKS_PLASTEEL).add(block.get());
            this.tag(VarkinSystemTags.DESOLATE_PLANET_PLASTEEL).add(block.get());
        }
        storageBlocksTag.addTag(VarkinSystemTags.FORGE_STORAGE_BLOCKS_PLASTEEL);

        var slabsTag = this.tag(BlockTags.SLABS);
        for (RegistryObject<Block> slab : VarkinSystemBlocks.PLASTEEL_SLABS) {
            slabsTag.add(slab.get());
        }

        var stairsTag = this.tag(BlockTags.STAIRS);
        for (RegistryObject<Block> stairs : VarkinSystemBlocks.PLASTEEL_STAIRS) {
            stairsTag.add(stairs.get());
        }

        this.tag(VarkinSystemTags.BASE_STONE_ICARUS)
                .add(
                        Blocks.NETHERRACK,
                        Blocks.BLACKSTONE,
                        Blocks.BASALT,
                        Blocks.SMOOTH_BASALT,
                        Blocks.TUFF,
                        Blocks.ANDESITE,
                        Blocks.GRANITE,
                        Blocks.DIORITE
                )
                .addOptional(ResourceLocation.fromNamespaceAndPath("tconstruct", "seared_stone"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("tconstruct", "scorched_stone"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("thermal", "slag_block"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("thermal", "rich_slag_block"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("create", "scoria"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("create", "scorchia"));

        this.tag(VarkinSystemTags.BASE_STONE_TALOS)
                .add(
                        Blocks.SAND,
                        Blocks.GRAVEL
                )
                .addOptional(ResourceLocation.fromNamespaceAndPath("exdeorum", "dust"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("exdeorum", "crushed_deepslate"));

        this.tag(VarkinSystemTags.BASE_STONE_PERDIX)
                .addOptional(ResourceLocation.fromNamespaceAndPath("ad_astra", "moon_stone"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("ad_astra", "moon_deepslate"));

        this.tag(VarkinSystemTags.BASE_STONE_IAPYX);

        this.tag(VarkinSystemTags.BASE_STONE_ARIAD)
                .add(
                        Blocks.ICE,
                        Blocks.PACKED_ICE
                )
                .addOptional(ResourceLocation.fromNamespaceAndPath("ad_astra", "permafrost"));
    }
}
