package net.pawjwp.varkin_system.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * A block that supports both waterlogging and lava-logging via a custom property.
 * liquid_logged values: 0 = none, 1 = water, 2 = lava
 */
public class LavaLoggableBlock extends Block implements BucketPickup, LiquidBlockContainer {

    public static final IntegerProperty LIQUID_LOGGED = IntegerProperty.create("liquid_logged", 0, 2);

    public LavaLoggableBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(LIQUID_LOGGED, 0));
    }

    // Block State Definition

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIQUID_LOGGED);
    }

    // Placement

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState().setValue(LIQUID_LOGGED, getLiquidType(fluidState));
    }

    // Neighbor Updates

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        scheduleFluidTick(state, level, pos);
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    protected void scheduleFluidTick(BlockState state, LevelAccessor level, BlockPos pos) {
        int liquidType = state.getValue(LIQUID_LOGGED);
        if (liquidType == 1) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        } else if (liquidType == 2) {
            level.scheduleTick(pos, Fluids.LAVA, Fluids.LAVA.getTickDelay(level));
        }
    }

    // Fluid Interaction

    @Override
    public FluidState getFluidState(BlockState state) {
        int liquidType = state.getValue(LIQUID_LOGGED);
        if (liquidType == 1) return Fluids.WATER.getSource(false);
        if (liquidType == 2) return Fluids.LAVA.getSource(false);
        return Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public boolean canPlaceLiquid(BlockGetter level, BlockPos pos, BlockState state, Fluid fluid) {
        return state.getValue(LIQUID_LOGGED) == 0
                && (fluid == Fluids.WATER || fluid == Fluids.LAVA);
    }

    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
        if (state.getValue(LIQUID_LOGGED) != 0) return false;

        if (!level.isClientSide()) {
            int liquidType = getLiquidType(fluidState);
            if (liquidType == 0) return false;

            level.setBlock(pos, state.setValue(LIQUID_LOGGED, liquidType), 3);
            level.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(level));
        }
        return true;
    }

    @Override
    public ItemStack pickupBlock(LevelAccessor level, BlockPos pos, BlockState state) {
        int liquidType = state.getValue(LIQUID_LOGGED);
        if (liquidType == 0) return ItemStack.EMPTY;

        level.setBlock(pos, state.setValue(LIQUID_LOGGED, 0), 3);
        return new ItemStack(liquidType == 1 ? Items.WATER_BUCKET : Items.LAVA_BUCKET);
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Optional.of(SoundEvents.BUCKET_FILL);
    }

    @Override
    public Optional<SoundEvent> getPickupSound(BlockState state) {
        int liquidType = state.getValue(LIQUID_LOGGED);
        if (liquidType == 2) return Optional.of(SoundEvents.BUCKET_FILL_LAVA);
        return Optional.of(SoundEvents.BUCKET_FILL);
    }

    // Helpers

    public static int getLiquidType(FluidState fluidState) {
        if (fluidState.getType() == Fluids.WATER) return 1;
        if (fluidState.getType() == Fluids.LAVA) return 2;
        return 0;
    }
}