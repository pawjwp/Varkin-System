package net.pawjwp.varkin_system.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

// reference: https://cadiboo.github.io/tutorials/1.15.2/forge/3.3-config/

public class VarkinSystemConfig {

    public static final CommonConfig COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        final Pair<CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    // blocks
    public static int cabinetInventorySize = 27;
    public static int cabinetUpgradeSlots = 3;

    // ad astra
    public static boolean enableSolarSystemRenderer = true;
    public static boolean enableNoGravityFix = true;

    public static void onLoad(final ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == COMMON_SPEC) {
            bakeConfig();
        }
    }

    public static void onReload(final ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == COMMON_SPEC) {
            bakeConfig();
        }
    }

    public static void bakeConfig() {
        // blocks
        cabinetInventorySize = COMMON.cabinetInventorySize.get();
        cabinetUpgradeSlots = COMMON.cabinetUpgradeSlots.get();

        // ad astra
        enableSolarSystemRenderer = COMMON.enableSolarSystemRenderer.get();
        enableNoGravityFix = COMMON.enableNoGravityFix.get();
    }

    public static class CommonConfig {
        // blocks
        public final ForgeConfigSpec.IntValue cabinetInventorySize;
        public final ForgeConfigSpec.IntValue cabinetUpgradeSlots;

        // ad astra
        public final ForgeConfigSpec.BooleanValue enableSolarSystemRenderer;
        public final ForgeConfigSpec.BooleanValue enableNoGravityFix;

        public CommonConfig(ForgeConfigSpec.Builder builder) {
            builder.push("blocks");

            cabinetInventorySize = builder
                    .comment("Number of item slots in each plasteel cabinet.")
                    .defineInRange("cabinet_inventory_size", 27, 1, 256);

            cabinetUpgradeSlots = builder
                    .comment("Number of upgrade slots in each plasteel cabinet.")
                    .defineInRange("cabinet_upgrade_slots", 3, 1, 9);

            builder.pop(); // blocks

            builder.push("ad_astra");

            enableSolarSystemRenderer = builder
                    .comment("Enable the custom Varkin solar system renderer on the planet selection screen.")
                    .define("enable_solar_system_renderer", true);

            enableNoGravityFix = builder
                    .comment("Fix entities without gravity floating away on low-gravity planets.")
                    .define("enable_no_gravity_fix", true);

            builder.pop(); // ad_astra
        }
    }
}
