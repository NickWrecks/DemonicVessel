package nickwrecks.demonicvessel.client;


import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import nickwrecks.demonicvessel.DemonicVessel;
import nickwrecks.demonicvessel.client.screen.BatteryScreen;
import nickwrecks.demonicvessel.client.screen.ModScreens;

@Mod.EventBusSubscriber(modid = DemonicVessel.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {

    @SubscribeEvent
    public static void onModelRegistryEvent(ModelEvent.RegisterGeometryLoaders event) {
        event.register(BatteryModelLoader.BATTERY_LOADER.getPath(), new BatteryModelLoader());
    }

    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ModScreens.BATTERY_CONTAINER.get(), BatteryScreen::new);
        });
    }
}
