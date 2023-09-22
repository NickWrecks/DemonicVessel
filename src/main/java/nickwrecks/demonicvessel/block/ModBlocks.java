package nickwrecks.demonicvessel.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import nickwrecks.demonicvessel.block.custom.BatteryBlock;
import nickwrecks.demonicvessel.block.custom.CreativeGeneratorBlock;
import nickwrecks.demonicvessel.block.custom.DistillationFeederBlock;
import nickwrecks.demonicvessel.block.custom.FamishedGeneratorBlock;

import static nickwrecks.demonicvessel.DemonicVessel.BLOCKS;

public class ModBlocks {


    public static final RegistryObject<Block> CREATIVE_GENERATOR_BLOCK = BLOCKS.register("creative_generator_block",
            ()-> new CreativeGeneratorBlock(BlockBehaviour.Properties.of(Material.METAL).strength(6.0f)));
    public static final RegistryObject<Block> BATTERY_BLOCK = BLOCKS.register("battery_block",
            ()-> new BatteryBlock(BlockBehaviour.Properties.of(Material.METAL).strength(6.0f)));
    public static final RegistryObject<Block> ABBADONIUM_BLOCK = BLOCKS.register("abbadonium_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.METAL).strength(6.0f)));
    public static final RegistryObject<Block> FAMISHED_GENERATOR_BLOCK = BLOCKS.register("famished_generator_block",
            () -> new FamishedGeneratorBlock(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(6.0f)));
    public static final RegistryObject<Block> DISTILLATION_FEEDER_BLOCK = BLOCKS.register("distillation_feeder_block",
            () -> new DistillationFeederBlock(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(2.0f)));


    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
