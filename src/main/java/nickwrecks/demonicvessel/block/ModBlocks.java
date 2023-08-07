package nickwrecks.demonicvessel.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import nickwrecks.demonicvessel.block.custom.BatteryBlock;
import nickwrecks.demonicvessel.block.custom.CreativeGeneratorBlock;

import static nickwrecks.demonicvessel.DemonicVessel.BLOCKS;

public class ModBlocks {


    public static final RegistryObject<Block> CREATIVE_GENERATOR_BLOCK = BLOCKS.register("creative_generator_block",
            ()-> new CreativeGeneratorBlock(BlockBehaviour.Properties.of(Material.METAL).strength(6.0f)));
    public static final RegistryObject<Block> BATTERY_BLOCK = BLOCKS.register("battery_block",
            ()-> new BatteryBlock(BlockBehaviour.Properties.of(Material.METAL).strength(6.0f)));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}