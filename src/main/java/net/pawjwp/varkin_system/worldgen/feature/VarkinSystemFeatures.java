package net.pawjwp.varkin_system.worldgen.feature;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.pawjwp.varkin_system.VarkinSystem;

public class VarkinSystemFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES,
            VarkinSystem.MOD_ID);

    public static final RegistryObject<CrystalPatchFeature> CRYSTAL_PATCH = FEATURES.register("crystal_patch",
            () -> new CrystalPatchFeature(CrystalPatchConfiguration.CODEC));

    public static void register(IEventBus eventBus) {
        FEATURES.register(eventBus);
    }
}
