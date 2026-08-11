package net.pawjwp.varkin_system.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.p3pp3rf1y.sophisticatedstorage.block.SophisticatedOpenersCounter;
import net.p3pp3rf1y.sophisticatedstorage.block.StorageBlockEntity;
import net.p3pp3rf1y.sophisticatedstorage.common.gui.StorageContainerMenu;

// Block entity for PlasteelCabinetBlock using Sophisticated Storage's StorageBlockEntity
public class PlasteelCabinetBlockEntity extends StorageBlockEntity {
    public static final String STORAGE_TYPE = "plasteel_cabinet";

    private final SophisticatedOpenersCounter openersCounter = new SophisticatedOpenersCounter() {
        @Override
        protected void onOpen(Level level, BlockPos pos, BlockState state) {
            playSound(level, getBlockPos(), SoundEvents.CHEST_OPEN);
            updateOpenBlockState(state, true);
        }

        @Override
        protected void onClose(Level level, BlockPos pos, BlockState state) {
            playSound(level, getBlockPos(), SoundEvents.CHEST_CLOSE);
            updateOpenBlockState(state, false);
        }

        @Override
        protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int previousOpenerCount, int newOpenerCount) {
        }

        @Override
        protected boolean isOwnContainer(Player player) {
            return player.containerMenu instanceof StorageContainerMenu menu && menu.getStorageBlockEntity() == PlasteelCabinetBlockEntity.this;
        }
    };

    @SuppressWarnings("unchecked")
    public PlasteelCabinetBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state, (BlockEntityType<? extends StorageBlockEntity>) VarkinSystemBlocks.PLASTEEL_CABINET_BE.get());
    }

    private void updateOpenBlockState(BlockState state, boolean open) {
        if (level != null) {
            level.setBlock(getBlockPos(), state.setValue(PlasteelCabinetBlock.OPEN, open), 3);
        }
    }

    private void playSound(Level level, BlockPos pos, SoundEvent sound) {
        level.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, sound, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public SophisticatedOpenersCounter getOpenersCounter() {
        return openersCounter;
    }

    @Override
    protected String getStorageType() {
        return STORAGE_TYPE;
    }
}
