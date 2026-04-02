package net.pawjwp.varkin_system.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
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
            this.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath("forge", "storage_blocks/" + set.name())))
                    .add(set.storageBlock().get());
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
    }
}
