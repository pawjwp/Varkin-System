package net.pawjwp.varkin_system.block;

import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlockEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.pawjwp.varkin_system.VarkinSystem;
import net.pawjwp.varkin_system.item.VarkinSystemItems;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class VarkinSystemBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, VarkinSystem.MOD_ID);
    public static final DeferredRegister<Item> BLOCK_ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, VarkinSystem.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, VarkinSystem.MOD_ID);

    public static final List<CrystalSet> CRYSTAL_SETS = new ArrayList<>();

    public record CrystalSet(
            String name,
            RegistryObject<Block> small,
            RegistryObject<Block> medium,
            RegistryObject<Block> large,
            RegistryObject<Block> cluster,
            RegistryObject<Block> budding,
            RegistryObject<Block> storageBlock,
            RegistryObject<Item> smallItem,
            RegistryObject<Item> mediumItem,
            RegistryObject<Item> largeItem,
            RegistryObject<Item> clusterItem,
            RegistryObject<Item> buddingItem,
            RegistryObject<Item> storageBlockItem,
            RegistryObject<Item> shard,
            RegistryObject<Item> dust
    ) {}

    private static final Supplier<BlockBehaviour.Properties> CRYSTAL_BASE =
            () -> BlockBehaviour.Properties.of()
                    .randomTicks().pushReaction(PushReaction.DESTROY).noOcclusion();

    private static CrystalSet registerCrystalSet(String name, MapColor color, float strength) {
        RegistryObject<Block> small = BLOCKS.register(name + "_crystal_small",
                () -> new LavaLoggableCrystal(4, 6, CRYSTAL_BASE.get()
                        .mapColor(color)
                        .strength(strength)
                        .forceSolidOn()
                        .sound(SoundType.SMALL_AMETHYST_BUD)
                        .lightLevel(state -> 1)
                )
        );
        RegistryObject<Block> medium = BLOCKS.register(name + "_crystal_medium",
                () -> new LavaLoggableCrystal(6, 10, CRYSTAL_BASE.get()
                        .mapColor(color)
                        .strength(strength)
                        .forceSolidOn()
                        .sound(SoundType.LARGE_AMETHYST_BUD)
                        .lightLevel(state -> 2)
                )
        );
        RegistryObject<Block> large = BLOCKS.register(name + "_crystal_large",
                () -> new LavaLoggableCrystal(7, 12, CRYSTAL_BASE.get()
                        .mapColor(color)
                        .strength(strength)
                        .forceSolidOn()
                        .sound(SoundType.MEDIUM_AMETHYST_BUD)
                        .lightLevel(state -> 4)
                )
        );
        RegistryObject<Block> cluster = BLOCKS.register(name + "_crystal_cluster",
                () -> new LavaLoggableCrystal(8, 14, CRYSTAL_BASE.get()
                        .mapColor(color)
                        .strength(strength)
                        .forceSolidOn()
                        .sound(SoundType.AMETHYST_CLUSTER)
                        .lightLevel(state -> 5)
                )
        );

        RegistryObject<Block> budding = BLOCKS.register(name + "_crystal_budding",
                () -> new BuddingCrystalBlock(List.of(small, medium, large, cluster),5, true, BlockBehaviour.Properties.of()
                        .mapColor(color)
                        .strength(strength)
                        .sound(SoundType.AMETHYST)
                        .requiresCorrectToolForDrops()
                )
        );

        RegistryObject<Block> storageBlock = BLOCKS.register(name + "_crystal_block",
                () -> new Block(BlockBehaviour.Properties.of()
                        .mapColor(color)
                        .strength(strength + 1.0F, 6.0F)
                        .sound(SoundType.AMETHYST)
                        .requiresCorrectToolForDrops()
                )
        );

        RegistryObject<Item> smallItem = BLOCK_ITEMS.register(name + "_crystal_small",
                () -> new BlockItem(small.get(), new Item.Properties().fireResistant()));
        RegistryObject<Item> mediumItem = BLOCK_ITEMS.register(name + "_crystal_medium",
                () -> new BlockItem(medium.get(), new Item.Properties().fireResistant()));
        RegistryObject<Item> largeItem = BLOCK_ITEMS.register(name + "_crystal_large",
                () -> new BlockItem(large.get(), new Item.Properties().fireResistant()));
        RegistryObject<Item> clusterItem = BLOCK_ITEMS.register(name + "_crystal_cluster",
                () -> new BlockItem(cluster.get(), new Item.Properties().fireResistant()));
        RegistryObject<Item> buddingItem = BLOCK_ITEMS.register(name + "_crystal_budding",
                () -> new BlockItem(budding.get(), new Item.Properties().fireResistant()));
        RegistryObject<Item> storageBlockItem = BLOCK_ITEMS.register(name + "_crystal_block",
                () -> new BlockItem(storageBlock.get(), new Item.Properties().fireResistant()));

        RegistryObject<Item> shard = VarkinSystemItems.registerWithTab(
                name + "_crystal_shard", () -> new Item(new Item.Properties().fireResistant()));
        RegistryObject<Item> dust = VarkinSystemItems.registerWithTab(
                name + "_dust", () -> new Item(new Item.Properties().fireResistant()));

        VarkinSystemItems.CREATIVE_TAB_ITEMS.add(smallItem);
        VarkinSystemItems.CREATIVE_TAB_ITEMS.add(mediumItem);
        VarkinSystemItems.CREATIVE_TAB_ITEMS.add(largeItem);
        VarkinSystemItems.CREATIVE_TAB_ITEMS.add(clusterItem);
        VarkinSystemItems.CREATIVE_TAB_ITEMS.add(buddingItem);
        VarkinSystemItems.CREATIVE_TAB_ITEMS.add(storageBlockItem);

        CrystalSet set = new CrystalSet(name, small, medium, large, cluster, budding, storageBlock,
                smallItem, mediumItem, largeItem, clusterItem, buddingItem, storageBlockItem, shard, dust);
        CRYSTAL_SETS.add(set);
        return set;
    }

    // Crystal sets
    public static final CrystalSet CHALCOPYRITE = registerCrystalSet("chalcopyrite", MapColor.GOLD, 1.5F);
    public static final CrystalSet SPHALERITE = registerCrystalSet("sphalerite", MapColor.TERRACOTTA_GRAY, 1.5F);
    public static final CrystalSet PENTLANDITE = registerCrystalSet("pentlandite", MapColor.WOOD, 1.5F);
    public static final CrystalSet STANNITE = registerCrystalSet("stannite", MapColor.COLOR_GRAY, 1.5F);
    public static final CrystalSet GALENA = registerCrystalSet("galena", MapColor.COLOR_GRAY, 1.5F);
    public static final CrystalSet ELECTRUM = registerCrystalSet("electrum", MapColor.SAND, 1.5F);

    public static final List<RegistryObject<Block>> PLASTEEL_BLOCKS = new ArrayList<>();

    // Register plasteel blocks in aesthetic color order
    private static final DyeColor[] ORDERED_DYE_COLORS = {
            DyeColor.WHITE,
            DyeColor.LIGHT_GRAY,
            DyeColor.GRAY,
            DyeColor.BLACK,
            DyeColor.BROWN,
            DyeColor.RED,
            DyeColor.ORANGE,
            DyeColor.YELLOW,
            DyeColor.LIME,
            DyeColor.GREEN,
            DyeColor.CYAN,
            DyeColor.LIGHT_BLUE,
            DyeColor.BLUE,
            DyeColor.PURPLE,
            DyeColor.MAGENTA,
            DyeColor.PINK
    };

    private static void registerPlasteel(DyeColor color) {
        String name = color.getName() + "_plasteel_block";
        RegistryObject<Block> block = BLOCKS.register(name,
                () -> new Block(BlockBehaviour.Properties.of()
                        .mapColor(color.getMapColor())
                        .sound(SoundType.METAL)
                        .strength(1.8F, 6.0F)
                        .requiresCorrectToolForDrops()));
        RegistryObject<Item> item = BLOCK_ITEMS.register(name,
                () -> new BlockItem(block.get(), new Item.Properties()));
        VarkinSystemItems.CREATIVE_TAB_ITEMS.add(item);
        PLASTEEL_BLOCKS.add(block);
    }

    static {
        for (DyeColor color : ORDERED_DYE_COLORS) {
            registerPlasteel(color);
        }
    }

    // Create seat-based ship chairs for each dye color
    public static final List<RegistryObject<Block>> SHIP_CHAIRS = new ArrayList<>();
    public static final Map<DyeColor, RegistryObject<Block>> SHIP_CHAIRS_BY_COLOR = new EnumMap<>(DyeColor.class);

    private static void registerShipChair(DyeColor color) {
        String name = color.getName() + "_ship_chair";
        RegistryObject<Block> chair = BLOCKS.register(name,
                () -> new ShipChairBlock(BlockBehaviour.Properties.of()
                        .mapColor(color.getMapColor())
                        .sound(SoundType.METAL)
                        .strength(1.8F, 6.0F)
                        .requiresCorrectToolForDrops(), color));
        RegistryObject<Item> item = BLOCK_ITEMS.register(name,
                () -> new BlockItem(chair.get(), new Item.Properties()));
        VarkinSystemItems.CREATIVE_TAB_ITEMS.add(item);
        SHIP_CHAIRS.add(chair);
        SHIP_CHAIRS_BY_COLOR.put(color, chair);
    }

    static {
        if (ModList.get().isLoaded("create")) {
            for (DyeColor color : ORDERED_DYE_COLORS) {
                registerShipChair(color);
            }
        }
    }

    // Create-style sliding doors
    public static final List<RegistryObject<Block>> SLIDING_DOORS = new ArrayList<>();
    public static RegistryObject<BlockEntityType<SlidingDoorBlockEntity>> SLIDING_DOOR_BE;

    // Properties mirror Ad Astra's sliding doors: iron door behaviour with per-material blast resistance and colour.
    private static void registerSlidingDoor(String name, float explosionResistance, MapColor color) {
        RegistryObject<Block> door = BLOCKS.register(name,
                () -> new SlidingDoorBlock(BlockBehaviour.Properties.copy(Blocks.IRON_DOOR)
                        .explosionResistance(explosionResistance)
                        .mapColor(color),
                        com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlock.TRAIN_SET_TYPE.get(), false));
        RegistryObject<Item> item = BLOCK_ITEMS.register(name,
                () -> new BlockItem(door.get(), new Item.Properties()));
        VarkinSystemItems.CREATIVE_TAB_ITEMS.add(item);
        SLIDING_DOORS.add(door);
    }

    static {
        if (ModList.get().isLoaded("create") && ModList.get().isLoaded("ad_astra")) {
            registerSlidingDoor("iron_sliding_door", 6, MapColor.METAL);
            registerSlidingDoor("steel_sliding_door", 12, MapColor.COLOR_GRAY);
            registerSlidingDoor("desh_sliding_door", 9, MapColor.COLOR_ORANGE);
            registerSlidingDoor("ostrum_sliding_door", 16, MapColor.COLOR_PURPLE);
            registerSlidingDoor("calorite_sliding_door", 22, MapColor.COLOR_RED);

            SLIDING_DOOR_BE = BLOCK_ENTITIES.register("sliding_door",
                    () -> BlockEntityType.Builder.of(
                            (pos, state) -> new SlidingDoorBlockEntity(SLIDING_DOOR_BE.get(), pos, state),
                            SLIDING_DOORS.stream().map(RegistryObject::get).toArray(Block[]::new)
                    ).build(null));
        }
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        BLOCK_ITEMS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
    }
}
