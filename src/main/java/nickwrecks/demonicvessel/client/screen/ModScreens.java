package nickwrecks.demonicvessel.client.screen;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;


import static nickwrecks.demonicvessel.DemonicVessel.MENU_TYPES;

public class ModScreens {
    public static final RegistryObject<MenuType<BatteryMenu>> BATTERY_CONTAINER  = MENU_TYPES.register("battery_menu",
            () -> IForgeMenuType.create((windowId, inv, data) -> new BatteryMenu(windowId, inv.player,data.readBlockPos())));
    public static void register(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }
}
