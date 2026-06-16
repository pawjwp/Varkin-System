package net.pawjwp.varkin_system.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import net.pawjwp.varkin_system.config.VarkinSystemConfig;
import net.p3pp3rf1y.sophisticatedcore.util.WorldHelper;
import net.p3pp3rf1y.sophisticatedstorage.block.StorageBlockBase;
import net.p3pp3rf1y.sophisticatedstorage.block.StorageBlockEntity;
import net.p3pp3rf1y.sophisticatedstorage.common.gui.StorageContainerMenu;

// A cabinet block using SophisticatedStorage's StorageBlockBase
public class PlasteelCabinetBlock extends StorageBlockBase {
    public static final DirectionProperty DIRECTION = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final EnumProperty<DoorHingeSide> HINGE = BlockStateProperties.DOOR_HINGE;

    // Different voxel shapes for each rotation
    private static final VoxelShape SHAPE_NORTH = Block.box(3, 2, 0, 13, 14, 5);
    private static final VoxelShape SHAPE_EAST = Block.box(11, 2, 3, 16, 14, 13);
    private static final VoxelShape SHAPE_SOUTH = Block.box(3, 2, 11, 13, 14, 16);
    private static final VoxelShape SHAPE_WEST = Block.box(0, 2, 3, 5, 14, 13);

    public PlasteelCabinetBlock(Properties properties) {
        super(properties, () -> VarkinSystemConfig.cabinetInventorySize, () -> VarkinSystemConfig.cabinetUpgradeSlots);
        registerDefaultState(getStateDefinition().any()
                .setValue(DIRECTION, Direction.NORTH)
                .setValue(OPEN, false)
                .setValue(HINGE, DoorHingeSide.LEFT)
                .setValue(TICKING, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = defaultBlockState().setValue(DIRECTION, context.getHorizontalDirection());
        BlockPos pos = context.getClickedPos();
        Vec3 clickVec = context.getClickLocation().subtract(Vec3.atLowerCornerOf(pos));
        Direction right = context.getHorizontalDirection().getClockWise();
        double side = right.getAxis().choose(clickVec.x, 0, clickVec.z);
        side = Math.abs(Math.min(right.getAxisDirection().getStep(), 0) + side);
        return state.setValue(HINGE, side > 0.5 ? DoorHingeSide.RIGHT : DoorHingeSide.LEFT);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DIRECTION, OPEN, HINGE, TICKING);
    }

    @Override
    public Direction getFacing(BlockState state) {
        return state.getValue(DIRECTION).getOpposite();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(DIRECTION)) {
            case EAST -> SHAPE_EAST;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            default -> SHAPE_NORTH;
        };
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return WorldHelper.getBlockEntity(level, pos, PlasteelCabinetBlockEntity.class).map(be -> {
            if (level.isClientSide || hand == InteractionHand.OFF_HAND) {
                return InteractionResult.SUCCESS;
            }
            ItemStack stackInHand = player.getItemInHand(hand);
            if (tryAddUpgrade(player, hand, be, stackInHand, getFacing(state), hit)) {
                return InteractionResult.SUCCESS;
            }
            NetworkHooks.openScreen((ServerPlayer) player, new SimpleMenuProvider(
                    (w, p, pl) -> new StorageContainerMenu(w, pl, pos), be.getDisplayName()), pos);
            return InteractionResult.CONSUME;
        }).orElse(InteractionResult.PASS);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        WorldHelper.getBlockEntity(level, pos, StorageBlockEntity.class).ifPresent(StorageBlockEntity::recheckOpen);
    }

    // Skip StorageBlockBase's upgrade rendering
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(DIRECTION, rotation.rotate(state.getValue(DIRECTION)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(DIRECTION)));
    }

    @Override
    public StorageBlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PlasteelCabinetBlockEntity(pos, state);
    }

    @Override
    protected BlockEntityType<? extends StorageBlockEntity> getBlockEntityType() {
        return VarkinSystemBlocks.PLASTEEL_CABINET_BE.get();
    }
}
