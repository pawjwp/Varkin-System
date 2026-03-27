package net.pawjwp.varkin_system.item;

import com.google.errorprone.annotations.Var;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.pawjwp.varkin_system.VarkinSystem;

public class VarkinSystemCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, VarkinSystem.MOD_ID);

    public static final RegistryObject<CreativeModeTab> SCARCITY_TAB = CREATIVE_MODE_TABS.register("varkin_system_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(Items.FIREWORK_STAR/*.get()*/))
                    .title(Component.translatable("creativetab.varkin_system_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        // pOutput.accept(VarkinSystem.OAK_SEED.get());
                        // List more items for the creative tab as needed
                    })
                    .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
