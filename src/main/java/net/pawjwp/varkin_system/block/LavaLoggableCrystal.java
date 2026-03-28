package net.pawjwp.varkin_system.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * A directional crystal-like block that attaches to a surface and breaks when support is removed.
 * Extends LavaLoggableBlock for water/lava logging support.
 * Used by KubeJS via createCustom — this class is not registered directly by the mod.
 */
public class LavaLoggableCrystal extends LavaLoggableBlock {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    private final Map<Direction, VoxelShape> shapeMap;

    /**
     * @param pixWidth  Width of the crystal cross-section in pixels (1-16)
     * @param pixHeight Height of the crystal along its facing axis in pixels (1-16)
     * @param properties Block properties (e.g. Properties.copy(Blocks.MAGMA_BLOCK).noOcclusion())
     */
    public LavaLoggableCrystal(int pixWidth, int pixHeight, Properties properties) {
        super(properties.pushReaction(PushReaction.DESTROY));
        this.registerDefaultState(this.defaultBlockState().setValue(LIQUID_LOGGED, 0).setValue(FACING, Direction.UP));
        this.shapeMap = buildShapeMap(pixWidth, pixHeight);
    }

    // Block State Definition

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    // Shape

    private static Map<Direction, VoxelShape> buildShapeMap(int pixWidth, int pixHeight) {
        Map<Direction, VoxelShape> map = new HashMap<>();
        double offset = (16.0 - pixWidth) / 2.0;
        map.put(Direction.UP,    Block.box(offset, 0,              offset, 16 - offset, pixHeight,      16 - offset));
        map.put(Direction.DOWN,  Block.box(offset, 16 - pixHeight, offset, 16 - offset, 16,             16 - offset));
        map.put(Direction.NORTH, Block.box(offset, offset, 16 - pixHeight, 16 - offset, 16 - offset, 16));
        map.put(Direction.SOUTH, Block.box(offset, offset, 0,              16 - offset, 16 - offset, pixHeight));
        map.put(Direction.EAST,  Block.box(0,              offset, offset, pixHeight,      16 - offset, 16 - offset));
        map.put(Direction.WEST,  Block.box(16 - pixHeight, offset, offset, 16,             16 - offset, 16 - offset));
        return map;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return shapeMap.get(state.getValue(FACING));
    }

    // Placement & Survival

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction face = context.getClickedFace();

        BlockState state = this.defaultBlockState()
                .setValue(FACING, face)
                .setValue(LIQUID_LOGGED, getLiquidType(level.getFluidState(pos)));

        return state.canSurvive(level, pos) ? state : null;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        BlockPos supportPos = pos.relative(facing.getOpposite());
        return level.getBlockState(supportPos).isFaceSturdy(level, supportPos, facing);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        // Break if support block was removed
        if (direction == state.getValue(FACING).getOpposite() && !state.canSurvive(level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }

        // Parent handles fluid tick scheduling
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    // Rotation & Mirroring

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }
}