package net.pawjwp.varkin_system.block;

import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.BlockSetType;

// Extend Create's sliding doors with our own block entity
public class SlidingDoorBlock extends com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlock {

    public SlidingDoorBlock(Properties properties, BlockSetType type, boolean folds) {
        super(properties, type, folds);
    }

    @Override
    @SuppressWarnings("unchecked")
    public BlockEntityType<? extends SlidingDoorBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends SlidingDoorBlockEntity>) VarkinSystemBlocks.SLIDING_DOOR_BE.get();
    }
}
