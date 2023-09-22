package nickwrecks.demonicvessel.block.entity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import nickwrecks.demonicvessel.block.custom.DistillationFeederBlock;

import static nickwrecks.demonicvessel.DemonicVessel.BLOCK_ENTITIES;
import static nickwrecks.demonicvessel.block.ModBlocks.*;

public class ModBlockEntities {

    public static final RegistryObject<BlockEntityType<CreativeGeneratorBlockEntity>> CREATIVE_GENERATOR_BLOCK_ENTITY = BLOCK_ENTITIES.register("creative_generator_block_entity",
            () -> BlockEntityType.Builder.of(CreativeGeneratorBlockEntity::new, CREATIVE_GENERATOR_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<BatteryBlockEntity>> BATTERY_BLOCK_ENTITY = BLOCK_ENTITIES.register("battery_block_entity",
            () -> BlockEntityType.Builder.of(BatteryBlockEntity::new, BATTERY_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<FamishedGeneratorBlockEntity>> FAMISHED_GENERATOR_BLOCK_ENTITY = BLOCK_ENTITIES.register("famished_generator_block_entity",
            () -> BlockEntityType.Builder.of(FamishedGeneratorBlockEntity::new, FAMISHED_GENERATOR_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<DistillationFeederBlockEntity>> DISTILLATION_FEEDER_BLOCK_ENTITY = BLOCK_ENTITIES.register("distillation_feeder_block_entity",
            () -> BlockEntityType.Builder.of(DistillationFeederBlockEntity::new, DISTILLATION_FEEDER_BLOCK.get()).build(null));
    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
