package net.pawjwp.varkin_system.datagen.loot;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.RegistryObject;
import net.pawjwp.varkin_system.block.VarkinSystemBlocks;
import net.pawjwp.varkin_system.block.VarkinSystemBlocks.CrystalSet;

import java.util.Set;

public class VarkinSystemBlockLootTables extends BlockLootSubProvider {
    public VarkinSystemBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        for (CrystalSet set : VarkinSystemBlocks.CRYSTAL_SETS) {
            // Buds drops self with silk touch, otherwise nothing
            this.dropWhenSilkTouch(set.small().get());
            this.dropWhenSilkTouch(set.medium().get());
            this.dropWhenSilkTouch(set.large().get());

            // Cluster drops self with silk touch, otherwise 4 shards with fortune bonus
            this.add(set.cluster().get(), createCrystalClusterDrops(set.cluster().get(), set.shard().get()));

            // Budding block drops nothing
            this.add(set.budding().get(), noDrop());

            // Storage block drops self
            this.dropSelf(set.storageBlock().get());
        }

        // Plasteel-derived blocks drop themselves
        // Bookshelves and cabinets will already drop inventory on removal
        for (RegistryObject<Block> block : VarkinSystemBlocks.PLASTEEL_DERIVED_BLOCKS) {
            // Slabs which drop two when doubled.
            if (block.get() instanceof SlabBlock) {
                this.add(block.get(), this.createSlabItemTable(block.get()));
            } else {
                this.dropSelf(block.get());
            }
        }

        // Sliding doors drop themselves from their lower half
        for (RegistryObject<Block> door : VarkinSystemBlocks.SLIDING_DOORS) {
            this.add(door.get(), this.createDoorTable(door.get()));
        }
    }

    private LootTable.Builder createCrystalClusterDrops(Block cluster, Item shard) {
        return createSilkTouchDispatchTable(cluster,
                LootItem.lootTableItem(shard)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F)))
                        .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
        );
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return VarkinSystemBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
