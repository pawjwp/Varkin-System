package net.pawjwp.varkin_system.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChiseledBookShelfBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
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
import net.pawjwp.varkin_system.compat.CreateCompat;
import net.pawjwp.varkin_system.compat.SophisticatedStorageCompat;
import net.pawjwp.varkin_system.item.VarkinSystemItems;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class VarkinSystemBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, VarkinSystem.MOD_ID);
    public static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, VarkinSystem.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, VarkinSystem.MOD_ID);

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
    public static final CrystalSet PETROVSKAITE = registerCrystalSet("petrovskaite", MapColor.SAND, 1.5F);
    public static final CrystalSet PHLOGISTITE = registerCrystalSet("phlogistite", MapColor.TERRACOTTA_GRAY, 2.5F);

    // All plasteel-derived blocks
    public static final List<RegistryObject<Block>> PLASTEEL_DERIVED_BLOCKS = new ArrayList<>();
    // Individual arrays for each plasteel-derived block
    public static final List<RegistryObject<Block>> PLASTEEL_BLOCKS = new ArrayList<>();
    public static final List<RegistryObject<Block>> PLASTEEL_STAIRS = new ArrayList<>();
    public static final List<RegistryObject<Block>> PLASTEEL_SLABS = new ArrayList<>();
    public static final List<RegistryObject<Block>> PLASTEEL_BOOKSHELVES = new ArrayList<>();
    public static final List<RegistryObject<Block>> PLASTEEL_CABINETS = new ArrayList<>();
    public static final List<RegistryObject<Block>> SHIP_CHAIRS = new ArrayList<>();

    public static final Map<DyeColor, RegistryObject<Block>> SHIP_CHAIRS_BY_COLOR = new EnumMap<>(DyeColor.class);

    public static RegistryObject<? extends BlockEntityType<?>> PLASTEEL_CABINET_BE;

    // Ensure plasteel blocks are registered in aesthetic color order
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

    // Properties shared by all plasteel-derived blocks
    public static BlockBehaviour.Properties plasteelProperties(DyeColor color) {
        return BlockBehaviour.Properties.of()
                .mapColor(color.getMapColor())
                .sound(SoundType.METAL)
                .strength(1.8F, 6.0F)
                .requiresCorrectToolForDrops();
    }

    static {
        boolean SophisticatedStorage = ModList.get().isLoaded("sophisticatedstorage");
        boolean Create = ModList.get().isLoaded("create");

        // Register all blocks for each color
        // Items and creative tabs are handled afterward to ensure ordering by block type
        for (DyeColor color : ORDERED_DYE_COLORS) {
            String prefix = color.getName();

            // Main block
            RegistryObject<Block> block = BLOCKS.register(prefix + "_plasteel_block", () -> new Block(plasteelProperties(color)));
            PLASTEEL_BLOCKS.add(block);
            // Derivatives
            PLASTEEL_STAIRS.add(BLOCKS.register(prefix + "_plasteel_stairs", () -> new StairBlock(() -> block.get().defaultBlockState(), plasteelProperties(color))));
            PLASTEEL_SLABS.add(BLOCKS.register(prefix + "_plasteel_slab", () -> new VerticalSlabBlock(plasteelProperties(color))));
            PLASTEEL_BOOKSHELVES.add(BLOCKS.register(prefix + "_plasteel_bookshelf", () -> new ChiseledBookShelfBlock(plasteelProperties(color))));
            // Conditional derivatives, registered in SophisticatedStorageCompat and CreateCompat
            if (SophisticatedStorage) {
                SophisticatedStorageCompat.registerCabinet(color);
            }
            if (Create) {
                CreateCompat.registerShipChair(color);
            }
        }

        // Assemble combined array by block type
        PLASTEEL_DERIVED_BLOCKS.addAll(PLASTEEL_BLOCKS);
        PLASTEEL_DERIVED_BLOCKS.addAll(PLASTEEL_STAIRS);
        PLASTEEL_DERIVED_BLOCKS.addAll(PLASTEEL_SLABS);
        PLASTEEL_DERIVED_BLOCKS.addAll(PLASTEEL_BOOKSHELVES);
        PLASTEEL_DERIVED_BLOCKS.addAll(PLASTEEL_CABINETS);
        PLASTEEL_DERIVED_BLOCKS.addAll(SHIP_CHAIRS);

        // Register a BlockItem and add to creative tabs for each
        for (RegistryObject<Block> derived : PLASTEEL_DERIVED_BLOCKS) {
            RegistryObject<Item> item = BLOCK_ITEMS.register(derived.getId().getPath(), () -> new BlockItem(derived.get(), new Item.Properties()));
            VarkinSystemItems.CREATIVE_TAB_ITEMS.add(item);
        }

        // Register cabinet block entity
        if (SophisticatedStorage) {
            SophisticatedStorageCompat.registerBlockEntity();
        }
    }

    // Create-style sliding doors, registered in CreateCompat
    public static final List<RegistryObject<Block>> SLIDING_DOORS = new ArrayList<>();
    public static RegistryObject<? extends BlockEntityType<?>> SLIDING_DOOR_BE;

    static {
        if (ModList.get().isLoaded("create") && ModList.get().isLoaded("ad_astra")) {
            CreateCompat.registerSlidingDoors();
        }
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        BLOCK_ITEMS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
    }
}
