package net.pawjwp.varkin_system.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.pawjwp.varkin_system.datagen.loot.VarkinSystemBlockLootTables;

import java.util.List;
import java.util.Set;

public class VarkinSystemLootTables {
    public static LootTableProvider create(PackOutput output) {
        return new LootTableProvider(output, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(VarkinSystemBlockLootTables::new, LootContextParamSets.BLOCK)
        ));
    }
}
