package nickwrecks.demonicvessel.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import nickwrecks.demonicvessel.DemonicVessel;
import nickwrecks.demonicvessel.entity.custom.LesserDemonEntity;

import static nickwrecks.demonicvessel.DemonicVessel.ENTITY_TYPES;

public class ModEntityTypes {
    public static final RegistryObject<EntityType<LesserDemonEntity>> LESSER_DEMON = ENTITY_TYPES.register("lesser_demon",
            ()-> EntityType.Builder.of(LesserDemonEntity::new, MobCategory.MONSTER)
                    .sized(0.5f, 0.5f)
                    .build(new ResourceLocation(DemonicVessel.MODID, "lesser_demon").toString()));
    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
