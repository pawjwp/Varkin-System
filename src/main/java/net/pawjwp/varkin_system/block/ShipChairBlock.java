package net.pawjwp.varkin_system.block;

import com.simibubi.create.content.contraptions.actors.seat.SeatBlock;
import com.simibubi.create.foundation.utility.BlockHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

// Extends Create's SeatBlock to inherit sitting and some other behaviors
public class ShipChairBlock extends SeatBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    private static final Map<Direction, VoxelShape> SHAPES = rotatedShapes(baseShape());

    public ShipChairBlock(Properties properties, DyeColor color) {
        super(properties, color);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return SHAPES.get(state.getValue(FACING));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return SHAPES.get(state.getValue(FACING));
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand,
                                 BlockHitResult hit) {
        if (!player.isShiftKeyDown()) {
            DyeColor dye = DyeColor.getColor(player.getItemInHand(hand));
            if (dye != null && dye != getColor()) {
                var target = VarkinSystemBlocks.SHIP_CHAIRS_BY_COLOR.get(dye);
                if (target != null) {
                    if (!world.isClientSide)
                        world.setBlockAndUpdate(pos, BlockHelper.copyProperties(state, target.get().defaultBlockState()));
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.use(state, world, pos, player, hand, hit);
    }

    // Chair voxel shape
    private static VoxelShape baseShape() {
        return Stream.of(
                Block.box(0, 17, 15, 16, 23, 21),
                Block.box(0, 12, 13, 16, 18, 19),
                Block.box(0, 7, 11, 16, 13, 17),
                Block.box(1, 0, 1, 15, 3, 15),
                Block.box(2, 3, 2, 14, 5, 14),
                Block.box(14, 10, 3, 18, 14, 15),
                Block.box(-2, 10, 3, 2, 14, 15),
                Block.box(0, 5, 0, 16, 11, 16)
        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    }

    // Rotate VoxelShape as needed
    private static Map<Direction, VoxelShape> rotatedShapes(VoxelShape south) {
        VoxelShape west = rotateClockwise(south);
        VoxelShape north = rotateClockwise(west);
        VoxelShape east = rotateClockwise(north);
        Map<Direction, VoxelShape> shapes = new EnumMap<>(Direction.class);
        shapes.put(Direction.SOUTH, south);
        shapes.put(Direction.WEST, west);
        shapes.put(Direction.NORTH, north);
        shapes.put(Direction.EAST, east);
        return shapes;
    }

    // Helper to rotate 90 degrees
    private static VoxelShape rotateClockwise(VoxelShape shape) {
        List<VoxelShape> parts = new ArrayList<>();
        shape.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) ->
                parts.add(Shapes.box(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
        return parts.stream().reduce(Shapes.empty(), (a, b) -> Shapes.join(a, b, BooleanOp.OR));
    }
}
