package nickwrecks.demonicvessel.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import nickwrecks.demonicvessel.DemonicVessel;

import static nickwrecks.demonicvessel.DemonicVessel.SOUND_EVENTS;

public class ModSounds {

    public static final RegistryObject<SoundEvent> LESSER_WHISPER = SOUND_EVENTS.register("lesser_whisper",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(DemonicVessel.MODID, "lesser_whisper")));

    public static final RegistryObject<SoundEvent> LESSER_IMPACT = SOUND_EVENTS.register("lesser_impact",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(DemonicVessel.MODID, "lesser_impact")));

    public static final RegistryObject<SoundEvent> LESSER_SLOW_CASTING = SOUND_EVENTS.register("lesser_slow_casting",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(DemonicVessel.MODID, "lesser_slow_casting")));

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
