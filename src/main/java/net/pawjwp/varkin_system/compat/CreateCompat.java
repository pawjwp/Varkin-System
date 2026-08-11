package net.pawjwp.varkin_system.compat;

import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlockEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.RegistryObject;
import net.pawjwp.varkin_system.block.ShipChairBlock;
import net.pawjwp.varkin_system.block.SlidingDoorBlock;
import net.pawjwp.varkin_system.block.VarkinSystemBlocks;
import net.pawjwp.varkin_system.item.VarkinSystemItems;

// Registers ship chairs and sliding doors based on Create's SeatBlock and SlidingDoorBlock
// Only loads if Create is present
public class CreateCompat {

    public static void registerShipChair(DyeColor color) {
        RegistryObject<Block> chair = VarkinSystemBlocks.BLOCKS.register(color.getName() + "_ship_chair",
                () -> new ShipChairBlock(VarkinSystemBlocks.plasteelProperties(color), color));
        VarkinSystemBlocks.SHIP_CHAIRS.add(chair);
        VarkinSystemBlocks.SHIP_CHAIRS_BY_COLOR.put(color, chair);
    }

    public static void registerSlidingDoors() {
        registerSlidingDoor("iron_sliding_door", 6, MapColor.METAL);
        registerSlidingDoor("steel_sliding_door", 12, MapColor.COLOR_GRAY);
        registerSlidingDoor("desh_sliding_door", 9, MapColor.COLOR_ORANGE);
        registerSlidingDoor("ostrum_sliding_door", 16, MapColor.COLOR_PURPLE);
        registerSlidingDoor("calorite_sliding_door", 22, MapColor.COLOR_RED);

        VarkinSystemBlocks.SLIDING_DOOR_BE = VarkinSystemBlocks.BLOCK_ENTITIES.register("sliding_door",
                () -> BlockEntityType.Builder.of(
                        (pos, state) -> new SlidingDoorBlockEntity(VarkinSystemBlocks.SLIDING_DOOR_BE.get(), pos, state),
                        VarkinSystemBlocks.SLIDING_DOORS.stream().map(RegistryObject::get).toArray(Block[]::new)
                ).build(null));
    }

    // Properties mirror Ad Astra's sliding doors: iron door behaviour with per-material blast resistance and color.
    private static void registerSlidingDoor(String name, float explosionResistance, MapColor color) {
        RegistryObject<Block> door = VarkinSystemBlocks.BLOCKS.register(name,
                () -> new SlidingDoorBlock(BlockBehaviour.Properties.copy(Blocks.IRON_DOOR)
                        .explosionResistance(explosionResistance)
                        .mapColor(color),
                        com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlock.TRAIN_SET_TYPE.get(), false));
        RegistryObject<Item> item = VarkinSystemBlocks.BLOCK_ITEMS.register(name,
                () -> new BlockItem(door.get(), new Item.Properties()));
        VarkinSystemItems.CREATIVE_TAB_ITEMS.add(item);
        VarkinSystemBlocks.SLIDING_DOORS.add(door);
    }
}
