package net.pawjwp.varkin_system.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;

import java.util.List;
import java.util.function.Supplier;

/**
 * A budding block that grows LavaLoggableCrystal stages on its faces via random ticks.
 * Growth stages are provided as an ordered list of block suppliers. On a random tick,
 * the block picks a random direction and attempts to advance the crystal on that face
 */
public class BuddingCrystalBlock extends Block {

    private static final Direction[] DIRECTIONS = Direction.values();

    private final List<Supplier<Block>> growthStages;
    private final int growthChance;
    private final boolean enableGrowth;

    /**
     * @param growthStages Ordered list of crystal stage suppliers (e.g. [small, medium, large, cluster]).
     *                     Each must produce a block with a FACING and LIQUID_LOGGED property
     *                     (i.e. a LavaLoggableCrystal or compatible block).
     * @param growthChance 1-in-N chance per random tick to attempt growth (vanilla amethyst uses 5)
     * @param enableGrowth If false, the block never grows crystals (but still receives random ticks)
     * @param properties   Block properties — randomTicks() is applied automatically
     */
    public BuddingCrystalBlock(List<Supplier<Block>> growthStages, int growthChance, boolean enableGrowth,
                               Properties properties) {
        super(properties.randomTicks().pushReaction(PushReaction.DESTROY));
        this.growthStages = growthStages;
        this.growthChance = growthChance;
        this.enableGrowth = enableGrowth;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!enableGrowth) return;
        if (random.nextInt(growthChance) != 0) return;

        Direction direction = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
        BlockPos targetPos = pos.relative(direction);
        BlockState targetState = level.getBlockState(targetPos);

        Block nextBlock = getNextStage(targetState, direction);
        if (nextBlock == null) return;

        int liquidType = LavaLoggableBlock.getLiquidType(targetState.getFluidState());
        BlockState newState = nextBlock.defaultBlockState()
                .setValue(LavaLoggableCrystal.FACING, direction)
                .setValue(LavaLoggableBlock.LIQUID_LOGGED, liquidType);
        level.setBlockAndUpdate(targetPos, newState);
    }

    /**
     * Determines the next growth stage for the block at the target position.
     * Returns null if no growth should occur.
     */
    private Block getNextStage(BlockState targetState, Direction direction) {
        // Place the first stage if empty adjacent block
        if (canClusterGrowAtState(targetState)) {
            return growthStages.isEmpty() ? null : growthStages.get(0).get();
        }

        // Check if the target is an existing stage that can be advanced
        for (int i = 0; i < growthStages.size() - 1; i++) {
            Block stageBlock = growthStages.get(i).get();
            if (targetState.is(stageBlock) && targetState.getValue(LavaLoggableCrystal.FACING) == direction) {
                return growthStages.get(i + 1).get();
            }
        }

        return null;
    }

    /**
     * Whether a crystal can grow into this position.
     * Allows air, water source blocks, and lava source blocks.
     */
    public static boolean canClusterGrowAtState(BlockState state) {
        return state.isAir()
                || (state.is(Blocks.WATER) && state.getFluidState().getAmount() == 8)
                || (state.is(Blocks.LAVA) && state.getFluidState().getAmount() == 8);
    }
}
