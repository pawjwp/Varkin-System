package net.pawjwp.varkin_system.datagen;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.pawjwp.varkin_system.VarkinSystem;
import net.pawjwp.varkin_system.block.LavaLoggableBlock;
import net.pawjwp.varkin_system.block.LavaLoggableCrystal;
import net.pawjwp.varkin_system.block.VarkinSystemBlocks;
import net.pawjwp.varkin_system.block.VarkinSystemBlocks.CrystalSet;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public class VarkinSystemBlockStates extends BlockStateProvider {

    public VarkinSystemBlockStates(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, VarkinSystem.MOD_ID, exFileHelper);
    }

    private String blockName(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block).getPath();
    }

    public ResourceLocation resourceBlock(String path) {
        return fromNamespaceAndPath(VarkinSystem.MOD_ID, "block/" + path);
    }

    @Override
    protected void registerStatesAndModels() {
        for (CrystalSet set : VarkinSystemBlocks.CRYSTAL_SETS) {
            directionalCrystalBlock(set.small().get());
            directionalCrystalBlock(set.medium().get());
            directionalCrystalBlock(set.large().get());
            directionalCrystalBlock(set.cluster().get());

            simpleBlock(set.budding().get(),
                    models().cubeAll(set.name() + "_crystal_budding",
                            resourceBlock(set.name() + "_crystal_budding")));

            simpleBlock(set.storageBlock().get(),
                    models().cubeAll(set.name() + "_block",
                            resourceBlock(set.name() + "_block")));
        }
    }

    private void directionalCrystalBlock(Block block) {
        String name = blockName(block);
        ModelFile model = models().cross(name, resourceBlock(name)).renderType("cutout");

        getVariantBuilder(block)
                .forAllStatesExcept(state -> {
                    Direction facing = state.getValue(LavaLoggableCrystal.FACING);
                    int xRot = 0;
                    int yRot = 0;

                    switch (facing) {
                        case UP -> { xRot = 0; yRot = 0; }
                        case DOWN -> { xRot = 180; yRot = 0; }
                        case NORTH -> { xRot = 90; yRot = 0; }
                        case SOUTH -> { xRot = 90; yRot = 180; }
                        case EAST -> { xRot = 90; yRot = 90; }
                        case WEST -> { xRot = 90; yRot = 270; }
                    }

                    return ConfiguredModel.builder()
                            .modelFile(model)
                            .rotationX(xRot)
                            .rotationY(yRot)
                            .build();
                }, LavaLoggableBlock.LIQUID_LOGGED);
    }
}
