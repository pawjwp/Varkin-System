package net.pawjwp.varkin_system.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

// Slab that be placed vertically, inspired by Copycats+ slabs
public class VerticalSlabBlock extends SlabBlock {
    public static final EnumProperty<Axis> AXIS = BlockStateProperties.AXIS;

    private static final VoxelShape X_BOTTOM = Block.box(0.0D, 0.0D, 0.0D, 8.0D, 16.0D, 16.0D);
    private static final VoxelShape X_TOP = Block.box(8.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape Z_BOTTOM = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 8.0D);
    private static final VoxelShape Z_TOP = Block.box(0.0D, 0.0D, 8.0D, 16.0D, 16.0D, 16.0D);

    public VerticalSlabBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState().setValue(AXIS, Axis.Y));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(AXIS);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        SlabType type = pState.getValue(TYPE);
        if (type == SlabType.DOUBLE) {
            return Shapes.block();
        }
        boolean top = type == SlabType.TOP;
        return switch (pState.getValue(AXIS)) {
            case X -> top ? X_TOP : X_BOTTOM;
            case Y -> top ? TOP_AABB : BOTTOM_AABB;
            case Z -> top ? Z_TOP : Z_BOTTOM;
        };
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockPos blockpos = pContext.getClickedPos();
        BlockState blockstate = pContext.getLevel().getBlockState(blockpos);
        if (blockstate.is(this)) {
            return blockstate.setValue(TYPE, SlabType.DOUBLE).setValue(WATERLOGGED, Boolean.valueOf(false));
        }

        Axis axis = pContext.getNearestLookingDirection().getAxis();
        Direction direction = pContext.getClickedFace();
        boolean lowerHalf = !(relativeHit(pContext, axis) > 0.5D);
        SlabType type = direction != Direction.fromAxisAndDirection(axis, AxisDirection.NEGATIVE)
                && (direction == Direction.fromAxisAndDirection(axis, AxisDirection.POSITIVE) || lowerHalf)
                ? SlabType.BOTTOM : SlabType.TOP;
        boolean waterlogged = pContext.getLevel().getFluidState(blockpos).getType() == Fluids.WATER;
        return this.defaultBlockState().setValue(AXIS, axis).setValue(TYPE, type).setValue(WATERLOGGED, waterlogged);
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return setFacing(pState, pRotation.rotate(getFacing(pState)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return setFacing(pState, pMirror.mirror(getFacing(pState)));
    }

    private static Direction getFacing(BlockState pState) {
        return Direction.fromAxisAndDirection(pState.getValue(AXIS),
                pState.getValue(TYPE) == SlabType.BOTTOM ? AxisDirection.NEGATIVE : AxisDirection.POSITIVE);
    }

    private static BlockState setFacing(BlockState pState, Direction direction) {
        pState = pState.setValue(AXIS, direction.getAxis());
        if (pState.getValue(TYPE) == SlabType.DOUBLE) {
            return pState;
        }
        return pState.setValue(TYPE,
                direction.getAxisDirection() == AxisDirection.POSITIVE ? SlabType.TOP : SlabType.BOTTOM);
    }

    @Override
    public boolean canBeReplaced(BlockState pState, BlockPlaceContext pUseContext) {
        SlabType slabtype = pState.getValue(TYPE);
        if (slabtype == SlabType.DOUBLE || !pUseContext.getItemInHand().is(this.asItem())) {
            return false;
        }
        if (!pUseContext.replacingClickedOnBlock()) {
            return true;
        }
        Axis axis = pState.getValue(AXIS);
        Direction direction = pUseContext.getClickedFace();
        boolean upperHalf = relativeHit(pUseContext, axis) > 0.5D;
        if (slabtype == SlabType.BOTTOM) {
            return direction == Direction.fromAxisAndDirection(axis, AxisDirection.POSITIVE)
                    || (upperHalf && direction.getAxis() != axis);
        }
        return direction == Direction.fromAxisAndDirection(axis, AxisDirection.NEGATIVE)
                || (!upperHalf && direction.getAxis() != axis);
    }

    private static double relativeHit(BlockPlaceContext pContext, Axis axis) {
        BlockPos pos = pContext.getClickedPos();
        return switch (axis) {
            case X -> pContext.getClickLocation().x - pos.getX();
            case Y -> pContext.getClickLocation().y - pos.getY();
            case Z -> pContext.getClickLocation().z - pos.getZ();
        };
    }
}
