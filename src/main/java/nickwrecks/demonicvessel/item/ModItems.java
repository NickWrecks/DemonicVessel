package nickwrecks.demonicvessel.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import nickwrecks.demonicvessel.item.custom.BatteryBlockItem;

import static nickwrecks.demonicvessel.DemonicVessel.ITEMS;
import static nickwrecks.demonicvessel.block.ModBlocks.BATTERY_BLOCK;
import static nickwrecks.demonicvessel.block.ModBlocks.CREATIVE_GENERATOR_BLOCK;

public class ModItems {

    public static final RegistryObject<Item> GENERATOR_BLOCK_ITEM = ITEMS.register("creative_generator_block",
            ()-> new BlockItem(CREATIVE_GENERATOR_BLOCK.get(),new Item.Properties()));
    public static final RegistryObject<Item> BATTERY_BLOCK_ITEM = ITEMS.register("battery_block",
            ()-> new BatteryBlockItem(BATTERY_BLOCK.get(),new Item.Properties()));
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
