package nickwrecks.demonicvessel.client.screen;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;


import static nickwrecks.demonicvessel.DemonicVessel.MENU_TYPES;

public class ModScreens {
    public static final RegistryObject<MenuType<BatteryMenu>> BATTERY_CONTAINER  = MENU_TYPES.register("battery_menu",
            () -> IForgeMenuType.create((windowId, inv, data) -> new BatteryMenu(windowId, inv.player,data.readBlockPos())));
    public static final RegistryObject<MenuType<FamishedGeneratorMenu>> FAMISHED_GENERATOR_CONTAINER  = MENU_TYPES.register("famished_generator_menu",
            () -> IForgeMenuType.create((windowId, inv, data) -> new FamishedGeneratorMenu(windowId, inv.player, data.readBlockPos())));
    public static final RegistryObject<MenuType<DistillationFeederMenu>> DISTILLATION_FEEDER_CONTAINER = MENU_TYPES.register("distillation_feeder_menu",
            () -> IForgeMenuType.create((windowId, inv, data) -> new DistillationFeederMenu(windowId, inv.player, data.readBlockPos())));
    public static void register(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }
}
