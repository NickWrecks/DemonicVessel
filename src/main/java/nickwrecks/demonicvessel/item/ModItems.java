package nickwrecks.demonicvessel.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import nickwrecks.demonicvessel.block.ModBlocks;
import nickwrecks.demonicvessel.entity.ModEntityTypes;
import nickwrecks.demonicvessel.item.custom.BatteryBlockItem;
import nickwrecks.demonicvessel.item.custom.EmptySoulSyringeItem;
import nickwrecks.demonicvessel.item.custom.LesserSoulSyringeItem;

import static nickwrecks.demonicvessel.DemonicVessel.ITEMS;
import static nickwrecks.demonicvessel.block.ModBlocks.BATTERY_BLOCK;
import static nickwrecks.demonicvessel.block.ModBlocks.CREATIVE_GENERATOR_BLOCK;

public class ModItems {

    public static final RegistryObject<Item> GENERATOR_BLOCK_ITEM = ITEMS.register("creative_generator_block",
            ()-> new BlockItem(CREATIVE_GENERATOR_BLOCK.get(),new Item.Properties()));

    public static final RegistryObject<Item> FAMISHED_GENERATOR_ITEM = ITEMS.register("famished_generator_item",
            ()-> new BlockItem(ModBlocks.FAMISHED_GENERATOR_BLOCK.get(),new Item.Properties()));
    public static final RegistryObject<Item> BATTERY_BLOCK_ITEM = ITEMS.register("battery_block",
            ()-> new BatteryBlockItem(BATTERY_BLOCK.get(),new Item.Properties()));
    public static final RegistryObject<Item> ABBADONIUM_INGOT = ITEMS.register("abbadonium_ingot",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ABBADONIUM_GEAR = ITEMS.register("abbadonium_gear",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ABBADONIUM_JACKET = ITEMS.register("abbadonium_jacket",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ABBADONIUM_BLOCK = ITEMS.register("abbadonium_block",
            ()-> new BlockItem(ModBlocks.ABBADONIUM_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Item> EMPTY_SOUL_SYRINGE = ITEMS.register("empty_soul_syringe",
            () -> new EmptySoulSyringeItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> LESSER_SOUL_SYRINGE = ITEMS.register("lesser_soul_syringe",
            () -> new LesserSoulSyringeItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> LESSER_DEMON_SPAWN_EGG = ITEMS.register("lesser_demon_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.LESSER_DEMON,0x871394,0x000000,new Item.Properties()));

    public static final RegistryObject<Item> EXPERIENCE_GEM = ITEMS.register("experience_gem",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DISTILLATION_FEEDER = ITEMS.register("distillation_feeder",
            ()-> new BlockItem(ModBlocks.DISTILLATION_FEEDER_BLOCK.get(), new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
