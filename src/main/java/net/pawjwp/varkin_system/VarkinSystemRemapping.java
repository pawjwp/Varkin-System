package net.pawjwp.varkin_system;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.MissingMappingsEvent;

import java.util.Map;
import java.util.Optional;

// Remaps blocks and items from their old IDs to new ones
@Mod.EventBusSubscriber(modid = VarkinSystem.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VarkinSystemRemapping {

    // old_namespace:old_id, new_namespace:new_id
    private static final Map<String, String> REMAPS = Map.ofEntries(
            Map.entry("kubejs:light_plasteel_block", "varkin_system:white_plasteel_block"),
            Map.entry("kubejs:dark_plasteel_block", "varkin_system:black_plasteel_block"),

            Map.entry("kubejs:white_ship_chair", "varkin_system:white_ship_chair"),
            Map.entry("kubejs:light_gray_ship_chair", "varkin_system:light_gray_ship_chair"),
            Map.entry("kubejs:gray_ship_chair", "varkin_system:gray_ship_chair"),
            Map.entry("kubejs:black_ship_chair", "varkin_system:black_ship_chair"),
            Map.entry("kubejs:brown_ship_chair", "varkin_system:brown_ship_chair"),
            Map.entry("kubejs:red_ship_chair", "varkin_system:red_ship_chair"),
            Map.entry("kubejs:orange_ship_chair", "varkin_system:orange_ship_chair"),
            Map.entry("kubejs:yellow_ship_chair", "varkin_system:yellow_ship_chair"),
            Map.entry("kubejs:lime_ship_chair", "varkin_system:lime_ship_chair"),
            Map.entry("kubejs:green_ship_chair", "varkin_system:green_ship_chair"),
            Map.entry("kubejs:cyan_ship_chair", "varkin_system:cyan_ship_chair"),
            Map.entry("kubejs:light_blue_ship_chair", "varkin_system:light_blue_ship_chair"),
            Map.entry("kubejs:blue_ship_chair", "varkin_system:blue_ship_chair"),
            Map.entry("kubejs:purple_ship_chair", "varkin_system:purple_ship_chair"),
            Map.entry("kubejs:magenta_ship_chair", "varkin_system:magenta_ship_chair"),
            Map.entry("kubejs:pink_ship_chair", "varkin_system:pink_ship_chair")
    );

    // returns the remapped ID from the old ID
    public static ResourceLocation remap(ResourceLocation oldId) {
        String to = REMAPS.get(oldId.toString());
        return to == null ? null : ResourceLocation.parse(to);
    }

    // Looks up a block, falls back to remapped ID when unknown
    // Used by the NbtUtils and BlockStateParser mixins
    public static Optional<Holder.Reference<Block>> getOrRemap(HolderGetter<Block> getter, ResourceKey<Block> key) {
        Optional<Holder.Reference<Block>> result = getter.get(key);
        if (result.isEmpty()) {
            ResourceLocation remapped = remap(key.location());
            if (remapped != null) {
                return getter.get(ResourceKey.create(Registries.BLOCK, remapped));
            }
        }
        return result;
    }

    @SubscribeEvent
    public static void onMissingMappings(MissingMappingsEvent event) {
        remap(event, Registries.BLOCK, ForgeRegistries.BLOCKS);
        remap(event, Registries.ITEM, ForgeRegistries.ITEMS);
    }

    private static <T> void remap(MissingMappingsEvent event, ResourceKey<? extends Registry<T>> registry,
                                  IForgeRegistry<T> forgeRegistry) {
        REMAPS.forEach((from, to) -> {
            ResourceLocation oldId = ResourceLocation.parse(from);
            ResourceLocation newId = ResourceLocation.parse(to);
            for (MissingMappingsEvent.Mapping<T> mapping : event.getMappings(registry, oldId.getNamespace())) {
                if (mapping.getKey().equals(oldId)) {
                    mapping.remap(forgeRegistry.getValue(newId));
                }
            }
        });
    }
}
