package net.pawjwp.varkin_system.compat;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;
import net.pawjwp.varkin_system.block.PlasteelCabinetBlock;
import net.pawjwp.varkin_system.block.PlasteelCabinetBlockEntity;
import net.pawjwp.varkin_system.block.VarkinSystemBlocks;

// Registers the plasteel cabinet blocks based on SophisticatedStorage's StorageBlockBase
// Only loads if SophisticatedStorage is present
public class SophisticatedStorageCompat {

    // Registers a cabinet in the given color
    public static void registerCabinet(DyeColor color) {
        VarkinSystemBlocks.PLASTEEL_CABINETS.add(VarkinSystemBlocks.BLOCKS.register(color.getName() + "_plasteel_cabinet",
                () -> new PlasteelCabinetBlock(VarkinSystemBlocks.plasteelProperties(color).noOcclusion())));
    }

    // Registers the block entity used by all cabinets
    public static void registerBlockEntity() {
        VarkinSystemBlocks.PLASTEEL_CABINET_BE = VarkinSystemBlocks.BLOCK_ENTITIES.register("plasteel_cabinet",
                () -> BlockEntityType.Builder.of(PlasteelCabinetBlockEntity::new,
                        VarkinSystemBlocks.PLASTEEL_CABINETS.stream().map(RegistryObject::get).toArray(Block[]::new)).build(null));
    }
}
