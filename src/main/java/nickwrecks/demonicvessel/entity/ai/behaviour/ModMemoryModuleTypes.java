package nickwrecks.demonicvessel.entity.ai.behaviour;

import com.mojang.serialization.Codec;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

import java.util.Optional;

import static nickwrecks.demonicvessel.DemonicVessel.MEMORY_MODULE_TYPE;

public class ModMemoryModuleTypes {


    public static final RegistryObject<MemoryModuleType<Unit>> LESSER_LASER_COOLDOWN = MEMORY_MODULE_TYPE.register("lesser_laser_cooldown",()-> new MemoryModuleType<>(Optional.of(Codec.unit(Unit.INSTANCE))));
    public static final RegistryObject<MemoryModuleType<Unit>> LESSER_LASER_SOUND_COOLDOWN = MEMORY_MODULE_TYPE.register("lesser_laser_sound_cooldown",()-> new MemoryModuleType<>(Optional.of(Codec.unit(Unit.INSTANCE))));
    public static final RegistryObject<MemoryModuleType<Unit>> LESSER_LASER_SOUND_DELAY = MEMORY_MODULE_TYPE.register("lesser_laser_sound_delay",()-> new MemoryModuleType<>(Optional.of(Codec.unit(Unit.INSTANCE))));

    public static final RegistryObject<MemoryModuleType<Unit>> LESSER_SLOW_COOLDOWN = MEMORY_MODULE_TYPE.register("lesser_slow_cooldown",()-> new MemoryModuleType<>(Optional.of(Codec.unit(Unit.INSTANCE))));
    public static final RegistryObject<MemoryModuleType<Unit>> LESSER_SLOW_SOUND_COOLDOWN = MEMORY_MODULE_TYPE.register("lesser_slow_sound_cooldown",()-> new MemoryModuleType<>(Optional.of(Codec.unit(Unit.INSTANCE))));
    public static final RegistryObject<MemoryModuleType<Unit>> LESSER_SLOW_SOUND_DELAY = MEMORY_MODULE_TYPE.register("lesser_slow_sound_delay",()-> new MemoryModuleType<>(Optional.of(Codec.unit(Unit.INSTANCE))));
    public static void register(IEventBus eventBus) {
        MEMORY_MODULE_TYPE.register(eventBus);
    }
}
