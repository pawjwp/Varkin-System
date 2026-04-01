package net.pawjwp.varkin_system.worldgen.feature;

import java.util.ArrayList;
import java.util.List;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.pawjwp.varkin_system.block.LavaLoggableBlock;

public class CrystalPatchFeature extends Feature<CrystalPatchConfiguration> {

    private static final Direction[] DIRECTIONS = Direction.values();

    public CrystalPatchFeature(Codec<CrystalPatchConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<CrystalPatchConfiguration> context) {
        CrystalPatchConfiguration config = context.config();
        RandomSource random = context.random();
        BlockPos origin = context.origin();
        WorldGenLevel level = context.level();

        int placed = 0;

        // Place budding blocks with growing crystals on their faces
        placed += placeBuddingBlocks(level, origin, random, config);

        // Place standalone crystal clusters
        placed += placeCrystalClusters(level, origin, random, config);

        return placed > 0;
    }

    private int placeBuddingBlocks(WorldGenLevel level, BlockPos origin, RandomSource random,
            CrystalPatchConfiguration config) {
        int placed = 0;

        for (int i = 0; i < config.buddingSize(); i++) {
            BlockPos pos = randomOffset(origin, random, config.xzSpread(), config.ySpread());
            BlockState existing = level.getBlockState(pos);

            // Find a matching target for this block
            BlockState buddingState = null;
            for (OreConfiguration.TargetBlockState target : config.targets()) {
                if (target.target.test(existing, random)) {
                    buddingState = target.state;
                    break;
                }
            }
            if (buddingState == null)
                continue;

            // Budding blocks must be adjacent to at least one open space
            List<Direction> openFaces = getOpenFaces(level, pos);
            if (openFaces.isEmpty())
                continue;

            // Place the budding block
            level.setBlock(pos, buddingState, 2);
            placed++;

            // Place 1-3 random growing crystals on adjacent open faces
            placeBuddingCrystals(level, pos, openFaces, random, config);
        }

        return placed;
    }

    private void placeBuddingCrystals(WorldGenLevel level, BlockPos buddingPos,
            List<Direction> openFaces, RandomSource random,
            CrystalPatchConfiguration config) {
        List<Block> crystalOptions = new ArrayList<>();
        config.smallCrystal().ifPresent(crystalOptions::add);
        config.mediumCrystal().ifPresent(crystalOptions::add);
        config.largeCrystal().ifPresent(crystalOptions::add);
        if (crystalOptions.isEmpty())
            return;

        // Pick 1-3 random faces via partial Fisher-Yates shuffle
        List<Direction> faces = new ArrayList<>(openFaces);
        int count = Math.min(1 + random.nextInt(3), faces.size());

        for (int i = 0; i < count; i++) {
            int idx = i + random.nextInt(faces.size() - i);
            Direction temp = faces.get(idx);
            faces.set(idx, faces.get(i));
            faces.set(i, temp);

            Direction face = faces.get(i);
            BlockPos crystalPos = buddingPos.relative(face);
            Block crystalBlock = crystalOptions.get(random.nextInt(crystalOptions.size()));
            // Crystal faces the same direction as face (pointing away from the budding
            // block)
            placeCrystal(level, crystalPos, crystalBlock, face);
        }
    }

    private int placeCrystalClusters(WorldGenLevel level, BlockPos origin, RandomSource random,
            CrystalPatchConfiguration config) {
        if (config.crystalCluster().isEmpty())
            return 0;
        Block clusterBlock = config.crystalCluster().get();
        int placed = 0;

        for (int i = 0; i < config.crystalSize(); i++) {
            BlockPos pos = randomOffset(origin, random, config.xzSpread(), config.ySpread());

            if (!isOpenBlock(level.getBlockState(pos)))
                continue;

            // Find all solid faces this crystal could attach to
            List<Direction> validFacings = getValidCrystalFacings(level, pos);
            if (validFacings.isEmpty())
                continue;

            Direction facing = validFacings.get(random.nextInt(validFacings.size()));
            placeCrystal(level, pos, clusterBlock, facing);
            placed++;
        }

        return placed;
    }

    private void placeCrystal(WorldGenLevel level, BlockPos pos, Block block, Direction facing) {
        BlockState state = block.defaultBlockState();

        if (state.hasProperty(BlockStateProperties.FACING)) {
            state = state.setValue(BlockStateProperties.FACING, facing);
        }

        // Handle liquid logging
        if (state.hasProperty(LavaLoggableBlock.LIQUID_LOGGED)) {
            int liquidType = LavaLoggableBlock.getLiquidType(level.getFluidState(pos));
            state = state.setValue(LavaLoggableBlock.LIQUID_LOGGED, liquidType);
        } else if (state.hasProperty(BlockStateProperties.WATERLOGGED)) {
            boolean waterlogged = level.getFluidState(pos).getType() == net.minecraft.world.level.material.Fluids.WATER;
            state = state.setValue(BlockStateProperties.WATERLOGGED, waterlogged);
        }

        level.setBlock(pos, state, 2);
    }

    private static BlockPos randomOffset(BlockPos origin, RandomSource random, int xzSpread, int ySpread) {
        int x = random.nextInt(2 * xzSpread + 1) - xzSpread;
        int y = random.nextInt(2 * ySpread + 1) - ySpread;
        int z = random.nextInt(2 * xzSpread + 1) - xzSpread;
        return origin.offset(x, y, z);
    }

    private static boolean isOpenBlock(BlockState state) {
        return state.isAir()
                || state.is(Blocks.WATER)
                || state.is(Blocks.LAVA);
    }

    /**
     * Returns directions from {@code pos} that have an adjacent air/water/lava
     * block.
     */
    private static List<Direction> getOpenFaces(WorldGenLevel level, BlockPos pos) {
        List<Direction> open = new ArrayList<>();
        for (Direction dir : DIRECTIONS) {
            if (isOpenBlock(level.getBlockState(pos.relative(dir)))) {
                open.add(dir);
            }
        }
        return open;
    }

    /**
     * Returns valid crystal facing directions for a crystal placed at
     * {@code crystalPos}.
     * A crystal faces away from its support block, so we check each adjacent block
     * for a sturdy face pointing toward the crystal.
     */
    private static List<Direction> getValidCrystalFacings(WorldGenLevel level, BlockPos crystalPos) {
        List<Direction> validFacings = new ArrayList<>();
        for (Direction dir : DIRECTIONS) {
            BlockPos supportPos = crystalPos.relative(dir);
            BlockState supportState = level.getBlockState(supportPos);
            // Crystal faces away from support: facing = dir.getOpposite()
            // canSurvive checks isFaceSturdy(supportPos, facing)
            Direction crystalFacing = dir.getOpposite();
            if (supportState.isFaceSturdy(level, supportPos, crystalFacing)) {
                validFacings.add(crystalFacing);
            }
        }
        return validFacings;
    }
}
