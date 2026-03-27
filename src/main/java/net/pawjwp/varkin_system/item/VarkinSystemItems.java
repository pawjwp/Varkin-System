package net.pawjwp.varkin_system.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.pawjwp.varkin_system.VarkinSystem;

import java.util.LinkedHashSet;
import java.util.function.Supplier;

public class VarkinSystemItems {
    public static LinkedHashSet<RegistryObject<Item>> CREATIVE_TAB_ITEMS = new LinkedHashSet<>();;

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, VarkinSystem.MOD_ID);

    public static RegistryObject<Item> registerWithTab(String name, Supplier<Item> supplier) {
        RegistryObject<Item> item = ITEMS.register(name, supplier);
        CREATIVE_TAB_ITEMS.add(item);
        return item;
    }


    // Item registry


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
