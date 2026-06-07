package net.pawjwp.varkin_system.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import earth.terrarium.adastra.api.client.events.AdAstraClientEvents;
import earth.terrarium.adastra.client.screens.PlanetsScreen;
import net.minecraft.Util;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.pawjwp.varkin_system.VarkinSystem;

import java.util.List;

public class VarkinSystemSolarSystemRenderer {

    public static final ResourceLocation VARKIN_SYSTEM = ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, "varkin_system");

    private static final ResourceLocation STAR = ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, "textures/environment/red_sun.png");
    private static final ResourceLocation PERDIX = ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, "textures/environment/perdix.png");
    private static final List<ResourceLocation> PLANET_TEXTURES = List.of(
        ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, "textures/environment/icarus.png"),
        ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, "textures/environment/talos.png"),
        ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, "textures/environment/iapyx.png"),
        ResourceLocation.fromNamespaceAndPath(VarkinSystem.MOD_ID, "textures/environment/ariad.png")
    );

    // Talos is the 2nd planet (i=2)
    private static final int TALOS_ORBIT_INDEX = 2;
    private static final float PERDIX_ORBIT_RADIUS = 16f;

    public static void register() {
        AdAstraClientEvents.RenderSolarSystemEvent.register((graphics, solarSystem, width, height) -> {
            if (!VARKIN_SYSTEM.equals(solarSystem)) return;

            float rotation = Util.getMillis() / 100f;

            // Main orbit rings around the star
            Tesselator tessellator = Tesselator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuilder();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            bufferBuilder.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
            PlanetsScreen.drawCircles(0, 4, 0xffbf8f1f, bufferBuilder, width, height);
            tessellator.end();

            // Perdix orbit ring around Talos.
            // Compute Talos' screen position from the same angle and radius used in the pose transforms
            float talosAngleRad = (float) Math.toRadians(rotation * (5 - TALOS_ORBIT_INDEX) / 2f);
            float talosScreenX = width / 2f + 30f * TALOS_ORBIT_INDEX * (float) Math.cos(talosAngleRad);
            float talosScreenY = height / 2f + 30f * TALOS_ORBIT_INDEX * (float) Math.sin(talosAngleRad);
            bufferBuilder.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
            PlanetsScreen.drawCircle(bufferBuilder, talosScreenX, talosScreenY, PERDIX_ORBIT_RADIUS, 75, 0xffbf8f1f);
            tessellator.end();

            // Star: 64x64 texture
            graphics.pose().pushPose();
            graphics.pose().translate(width / 2f, height / 2f, 0f);
            graphics.pose().scale(0.5f, 0.5f, 1f);
            graphics.blit(STAR, -32, -32, 0, 0, 64, 64, 64, 64);
            graphics.pose().popPose();

            // Planets: 32x32 textures centered on orbit rings
            for (int i = 1; i < 5; i++) {
                graphics.pose().pushPose();
                graphics.pose().translate(width / 2f, height / 2f, 0f);
                graphics.pose().mulPose(Axis.ZP.rotationDegrees(rotation * (5 - i) / 2f));
                graphics.pose().translate(30f * i, 0f, 0f);
                graphics.pose().scale(0.5f, 0.5f, 1f);
                graphics.blit(PLANET_TEXTURES.get(i - 1), -16, -16, 0, 0, 32, 32, 32, 32);
                graphics.pose().popPose();
            }

            // Perdix orbits Talos by replicating Talos' transform chain, then adding a sub-orbit
            graphics.pose().pushPose();
            graphics.pose().translate(width / 2f, height / 2f, 0f);
            graphics.pose().mulPose(Axis.ZP.rotationDegrees(rotation * (5 - TALOS_ORBIT_INDEX) / 2f));
            graphics.pose().translate(30f * TALOS_ORBIT_INDEX, 0f, 0f);
            graphics.pose().mulPose(Axis.ZP.rotationDegrees(rotation * 3f));
            graphics.pose().translate(PERDIX_ORBIT_RADIUS, 0f, 0f);
            graphics.pose().scale(0.5f, 0.5f, 1f);
            graphics.blit(PERDIX, -16, -16, 0, 0, 32, 32, 32, 32);
            graphics.pose().popPose();
        });
    }
}
