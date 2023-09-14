package nickwrecks.demonicvessel.client.particle;


import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

import static nickwrecks.demonicvessel.DemonicVessel.PARTICLE_TYPES;

public class ModParticleTypes {

    public static final RegistryObject<SimpleParticleType> PURPLE_STAR_PARTICLE_TYPE = PARTICLE_TYPES.register("purple_star_particle", () -> new SimpleParticleType(true));

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
