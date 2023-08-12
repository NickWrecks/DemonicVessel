package nickwrecks.demonicvessel;

import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import nickwrecks.demonicvessel.block.ModBlocks;
import nickwrecks.demonicvessel.block.entity.ModBlockEntities;
import nickwrecks.demonicvessel.client.ClientEvents;
import nickwrecks.demonicvessel.client.screen.ModScreens;
import nickwrecks.demonicvessel.item.ModItems;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(DemonicVessel.MODID)
public class DemonicVessel
{

    private static int mainColour = 0x871394;
    private static int detailColour = 0xf2ff03;

    // Define mod id in a common place for everything to reference
    public static final String MODID = "demonicvessel";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MODID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);

    public DemonicVessel()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so things get registered
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModScreens.register(modEventBus);
        ENTITY_TYPES.register(modEventBus);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        // Register the item to a creative tab
        modEventBus.addListener(this::registerTabs);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
    }



    private void registerTabs(CreativeModeTabEvent.Register event) {
        event.registerCreativeModeTab(new ResourceLocation(MODID, "demonicvesseleverythingtab"), builder -> builder
                .title(Component.translatable("item_group."+MODID+".everything"))
                .icon(() -> new ItemStack(ModItems.BATTERY_BLOCK_ITEM.get()))
                .displayItems((featureFlags, output) -> {
                    output.accept(ModItems.BATTERY_BLOCK_ITEM.get());
                    output.accept(ModItems.GENERATOR_BLOCK_ITEM.get());
                })
        );
    }
}
