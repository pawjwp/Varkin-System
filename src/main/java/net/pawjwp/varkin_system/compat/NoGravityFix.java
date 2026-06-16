package net.pawjwp.varkin_system.compat;

import earth.terrarium.adastra.api.events.AdAstraEvents;

// Prevent Ad Astra's gravity adjustment from launching NoGravity entities into the sky.
// Ad Astra adds (0.08 - 0.08*gravity) to y velocity before vanilla's, expecting vanilla to subtract 0.08 later.
// This makes NoGravity entities use vanilla gravity instead, ignoring Ad Astra's gravity adjustment and allowing them to float normally.
public class NoGravityFix {

    public static void register() {
        AdAstraEvents.EntityGravityEvent.register((entity, gravity) -> {
            if (entity.isNoGravity()) return 1.0f;
            return gravity;
        });
    }
}