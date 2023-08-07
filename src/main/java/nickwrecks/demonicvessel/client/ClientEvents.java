package nickwrecks.demonicvessel.client;


import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nickwrecks.demonicvessel.DemonicVessel;

@Mod.EventBusSubscriber(modid = DemonicVessel.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {

    @SubscribeEvent
    public static void onModelRegistryEvent(ModelEvent.RegisterGeometryLoaders event) {
        event.register(BatteryModelLoader.BATTERY_LOADER.getPath(), new BatteryModelLoader());
    }
}
