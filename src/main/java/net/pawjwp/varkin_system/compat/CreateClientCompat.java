package net.pawjwp.varkin_system.compat;

import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlockEntity;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorRenderer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.pawjwp.varkin_system.block.VarkinSystemBlocks;

// Registers sliding door renderer, only loads if Create and Ad Astra are present
public class CreateClientCompat {

    @SuppressWarnings("unchecked")
    public static void registerSlidingDoorRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(
                (BlockEntityType<SlidingDoorBlockEntity>) VarkinSystemBlocks.SLIDING_DOOR_BE.get(),
                SlidingDoorRenderer::new);
    }
}
