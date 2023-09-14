package nickwrecks.demonicvessel.entity;


import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nickwrecks.demonicvessel.DemonicVessel;
import nickwrecks.demonicvessel.entity.custom.LesserDemonEntity;

@Mod.EventBusSubscriber(modid = DemonicVessel.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityEventHandler {
    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.LESSER_DEMON.get(), LesserDemonEntity.setAttributes());
    }
}
