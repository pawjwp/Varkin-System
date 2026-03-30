package net.pawjwp.varkin_system.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
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
                    set.budding().get()
            );

            crystalBlocksTag.add(
                    set.small().get(), set.medium().get(),
                    set.large().get(), set.cluster().get()
            );

            clusterTag.add(set.cluster().get());
            buddingTag.add(set.budding().get());
        }
    }
}
