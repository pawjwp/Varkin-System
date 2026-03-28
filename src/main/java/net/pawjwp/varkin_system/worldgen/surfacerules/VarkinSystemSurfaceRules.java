package net.pawjwp.varkin_system.worldgen.surfacerules;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.pawjwp.varkin_system.VarkinSystem;

public class VarkinSystemSurfaceRules {
    public static final DeferredRegister<Codec<? extends SurfaceRules.RuleSource>> SURFACE_RULES =
            DeferredRegister.create(Registries.MATERIAL_RULE, VarkinSystem.MOD_ID);

    public static final RegistryObject<Codec<? extends SurfaceRules.RuleSource>> VOLCANIC_BANDS =
            SURFACE_RULES.register("volcanic_bands", () -> VolcanicBandlands.CODEC.codec());

    public static void register(IEventBus eventBus) {
        SURFACE_RULES.register(eventBus);
    }
}