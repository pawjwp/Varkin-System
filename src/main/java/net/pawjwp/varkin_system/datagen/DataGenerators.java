package net.pawjwp.varkin_system.datagen;

import net.pawjwp.varkin_system.VarkinSystem;
import net.minecraft.data.DataGenerator;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = VarkinSystem.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // Server-side
        generator.addProvider(event.includeServer(), VarkinSystemLootTables.create(packOutput));

        // Client-side
        generator.addProvider(event.includeClient(), new VarkinSystemBlockStates(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new VarkinSystemItemModels(packOutput, existingFileHelper));

        // Tags
        VarkinSystemBlockTags blockTagGen = generator.addProvider(event.includeServer(),
                new VarkinSystemBlockTags(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(),
                new VarkinSystemItemTags(packOutput, lookupProvider, blockTagGen.contentsGetter(), existingFileHelper));
    }
}
