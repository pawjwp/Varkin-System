package net.pawjwp.varkin_system.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.pawjwp.varkin_system.VarkinSystem;
import net.pawjwp.varkin_system.block.VarkinSystemBlocks;
import net.pawjwp.varkin_system.block.VarkinSystemBlocks.CrystalSet;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class VarkinSystemItemModels extends ItemModelProvider {
    public static final String GENERATED = "item/generated";

    public VarkinSystemItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, VarkinSystem.MOD_ID, existingFileHelper);
    }

    // Structure and functions taken from:
    // https://github.com/vectorwing/FarmersDelight/blob/1.20/src/main/java/vectorwing/farmersdelight/data/ItemModels.java

    @Override
    protected void registerModels() {
        Set<Item> items = ForgeRegistries.ITEMS.getValues().stream().filter(i -> VarkinSystem.MOD_ID.equals(ForgeRegistries.ITEMS.getKey(i).getNamespace()))
                .collect(Collectors.toSet());

        // If needed in the future, exclude specific items here
        // items.remove(VarkinSystemItems.EXAMPLE_ITEM.get());
        Set<Item> crystalStageItems = new HashSet<>();
        for (CrystalSet set : VarkinSystemBlocks.CRYSTAL_SETS) {
            crystalStageItems.add(set.smallItem().get());
            crystalStageItems.add(set.mediumItem().get());
            crystalStageItems.add(set.largeItem().get());
            crystalStageItems.add(set.clusterItem().get());
        }
        takeAll(items, crystalStageItems::contains)
                .forEach(item -> itemGeneratedModel(item, resourceBlock(itemName(item))));

        // Sliding doors use flat item model
        Set<Item> doorItems = new HashSet<>();
        for (RegistryObject<Block> door : VarkinSystemBlocks.SLIDING_DOORS) {
            doorItems.add(door.get().asItem());
        }
        if (!doorItems.isEmpty()) {
            takeAll(items, doorItems::contains)
                    .forEach(item -> itemGeneratedModel(item, resourceItem(itemName(item))));
        }

        // Ship chairs parent their (3D) block model
        Set<Item> chairItems = new HashSet<>();
        for (RegistryObject<Block> chair : VarkinSystemBlocks.SHIP_CHAIRS) {
            chairItems.add(chair.get().asItem());
        }
        if (!chairItems.isEmpty()) {
            takeAll(items, chairItems::contains).forEach(item -> {
                String color = itemName(item).replace("_ship_chair", "");
                withExistingParent(itemName(item), resourceBlock("ship_chair/" + color));
            });
        }

        // Plasteel cabinets use their closed, left-hinge model
        Set<Item> cabinetItems = new HashSet<>();
        for (RegistryObject<Block> cabinet : VarkinSystemBlocks.PLASTEEL_CABINETS) {
            cabinetItems.add(cabinet.get().asItem());
        }
        if (!cabinetItems.isEmpty()) {
            takeAll(items, cabinetItems::contains).forEach(item ->
                    withExistingParent(itemName(item), resourceBlock(itemName(item) + "_closed_hinge_left")));
        }

        // Bookshelves use their inventory model (the in-world body has no front face)
        Set<Item> bookshelfItems = new HashSet<>();
        for (RegistryObject<Block> bookshelf : VarkinSystemBlocks.PLASTEEL_BOOKSHELVES) {
            bookshelfItems.add(bookshelf.get().asItem());
        }
        if (!bookshelfItems.isEmpty()) {
            takeAll(items, bookshelfItems::contains)
                    .forEach(item -> blockBasedModel(item, "_inventory"));
        }

        // Blocks whose items look alike
        takeAll(items, i -> i instanceof BlockItem).forEach(item -> blockBasedModel(item, ""));

        // Remaining items
        items.forEach(item -> itemGeneratedModel(item, resourceItem(itemName(item))));
    }

    private String itemName(Item item) {
        return ForgeRegistries.ITEMS.getKey(item).getPath();
    }

    public void blockBasedModel(Item item, String suffix) {
        withExistingParent(itemName(item), resourceBlock(itemName(item) + suffix));
    }

    public void itemGeneratedModel(Item item, ResourceLocation texture) {
        withExistingParent(itemName(item), GENERATED).texture("layer0", texture);
    }

    public ResourceLocation resourceBlock(String path) {
        return ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, "block/" + path);
    }

    public ResourceLocation resourceItem(String path) {
        return ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, "item/" + path);
    }

    public static <T> Collection<T> takeAll(Set<T> src, Predicate<T> pred) {
        List<T> ret = new ArrayList();
        Iterator<T> iter = src.iterator();

        while(iter.hasNext()) {
            T item = (T)iter.next();
            if (pred.test(item)) {
                iter.remove();
                ret.add(item);
            }
        }

        if (ret.isEmpty()) {
            VarkinSystem.LOGGER.warn("takeAll predicate yielded nothing", new Throwable());
        }

        return ret;
    }
}
