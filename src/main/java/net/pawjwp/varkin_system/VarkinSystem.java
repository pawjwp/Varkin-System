package net.pawjwp.varkin_system;

import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.pawjwp.varkin_system.item.VarkinSystemCreativeTabs;
import net.pawjwp.varkin_system.item.VarkinSystemItems;
import net.pawjwp.varkin_system.block.VarkinSystemBlocks;
import net.pawjwp.varkin_system.worldgen.feature.VarkinSystemFeatures;
import net.pawjwp.varkin_system.worldgen.surfacerules.VarkinSystemSurfaceRules;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(VarkinSystem.MOD_ID)
public class VarkinSystem
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "varkin_system";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public VarkinSystem(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();


        VarkinSystemCreativeTabs.register(modEventBus);
        VarkinSystemItems.register(modEventBus);
        VarkinSystemBlocks.register(modEventBus);
        VarkinSystemFeatures.register(modEventBus);
        VarkinSystemSurfaceRules.register(modEventBus);

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register config
        // modEventBus.addListener(VarkinSystemConfig::onLoad);
        // modEventBus.addListener(VarkinSystemConfig::onReload);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if(event.getTabKey() == VarkinSystemCreativeTabs.VARKIN_SYSTEM_TAB.getKey()) {
            VarkinSystemItems.CREATIVE_TAB_ITEMS.forEach(event::accept);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
        }
    }
}
