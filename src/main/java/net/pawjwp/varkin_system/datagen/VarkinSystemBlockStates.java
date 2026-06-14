package net.pawjwp.varkin_system.datagen;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChiseledBookShelfBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.p3pp3rf1y.sophisticatedstorage.block.StorageBlockBase;
import net.pawjwp.varkin_system.VarkinSystem;
import net.pawjwp.varkin_system.block.LavaLoggableBlock;
import net.pawjwp.varkin_system.block.LavaLoggableCrystal;
import net.pawjwp.varkin_system.block.PlasteelCabinetBlock;
import net.pawjwp.varkin_system.block.ShipChairBlock;
import net.pawjwp.varkin_system.block.VarkinSystemBlocks;
import net.pawjwp.varkin_system.block.VarkinSystemBlocks.CrystalSet;
import net.pawjwp.varkin_system.block.VerticalSlabBlock;

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
                    models().cubeAll(set.name() + "_crystal_block",
                            resourceBlock(set.name() + "_crystal_block")));
        }

        for (var block : VarkinSystemBlocks.PLASTEEL_BLOCKS) {
            String name = blockName(block.get());
            simpleBlock(block.get(), models().cubeAll(name, resourceBlock(name)));
        }

        for (var slab : VarkinSystemBlocks.PLASTEEL_SLABS) {
            String name = blockName(slab.get());
            ResourceLocation tex = resourceBlock(name.replace("_slab", "_block"));
            ModelFile bottom = models().slab(name, tex, tex, tex);
            ModelFile doubleSlab = models().getExistingFile(resourceBlock(name.replace("_slab", "_block")));
            getVariantBuilder(slab.get()).forAllStatesExcept(state -> {
                SlabType type = state.getValue(SlabBlock.TYPE);
                if (type == SlabType.DOUBLE) {
                    return ConfiguredModel.builder().modelFile(doubleSlab).build();
                }
                boolean top = type == SlabType.TOP;
                int xRot = 0;
                int yRot = 0;
                switch (state.getValue(VerticalSlabBlock.AXIS)) {
                    case Y -> xRot = top ? 180 : 0;
                    case Z -> xRot = top ? 90 : 270;
                    case X -> { xRot = 90; yRot = top ? 270 : 90; }
                }
                var builder = ConfiguredModel.builder()
                        .modelFile(bottom).rotationX(xRot).rotationY(yRot);
                if (xRot != 0 || yRot != 0) {
                    builder.uvLock(true);
                }
                return builder.build();
            }, SlabBlock.WATERLOGGED);
        }

        for (var stairs : VarkinSystemBlocks.PLASTEEL_STAIRS) {
            String name = blockName(stairs.get());
            ResourceLocation tex = resourceBlock(name.replace("_stairs", "_block"));
            stairsBlock((StairBlock) stairs.get(), tex);
        }

        // Ship chairs: hand-authored model per colour, authored facing south (no rotation),
        // so the rotations match the chair's VoxelShape (south=0, west=90, north=180, east=270).
        for (var chair : VarkinSystemBlocks.SHIP_CHAIRS) {
            ModelFile model = models().getExistingFile(resourceBlock("ship_chair/" + chairColor(chair.get())));
            getVariantBuilder(chair.get()).forAllStatesExcept(state -> {
                int rot = switch (state.getValue(ShipChairBlock.FACING)) {
                    case WEST -> 90;
                    case NORTH -> 180;
                    case EAST -> 270;
                    default -> 0; // SOUTH
                };
                return ConfiguredModel.builder().modelFile(model).rotationY(rot).build();
            }, BlockStateProperties.WATERLOGGED);
        }

        for (var cabinet : VarkinSystemBlocks.PLASTEEL_CABINETS) {
            cabinetBlock((PlasteelCabinetBlock) cabinet.get());
        }
    }

    // Cabinet doors
    private void cabinetBlock(PlasteelCabinetBlock block) {
        String name = blockName(block);
        ResourceLocation tex = resourceBlock(name);
        ModelFile closedLeft = cabinetModel(name + "_closed_hinge_left", "cabinet_closed_hinge_left", tex);
        ModelFile closedRight = cabinetModel(name + "_closed_hinge_right", "cabinet_closed_hinge_right", tex);
        ModelFile openLeft = cabinetModel(name + "_open_hinge_left", "cabinet_open_hinge_left", tex);
        ModelFile openRight = cabinetModel(name + "_open_hinge_right", "cabinet_open_hinge_right", tex);

        getVariantBuilder(block).forAllStatesExcept(state -> {
            boolean open = state.getValue(PlasteelCabinetBlock.OPEN);
            boolean right = state.getValue(PlasteelCabinetBlock.HINGE) == DoorHingeSide.RIGHT;
            ModelFile model = open ? (right ? openRight : openLeft) : (right ? closedRight : closedLeft);
            return ConfiguredModel.builder()
                    .modelFile(model)
                    .rotationY(horizontalYRot(state.getValue(PlasteelCabinetBlock.DIRECTION)))
                    .build();
        }, StorageBlockBase.TICKING);
    }

    private ModelFile cabinetModel(String name, String parent, ResourceLocation tex) {
        return models().withExistingParent(name, resourceBlock(parent))
                .texture("texture", tex).texture("particle", tex);
    }

    private int horizontalYRot(Direction dir) {
        return switch (dir) {
            case EAST -> 90;
            case SOUTH -> 180;
            case WEST -> 270;
            default -> 0;
        };
    }

    private String chairColor(Block chair) {
        return blockName(chair).replace("_ship_chair", "");
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
