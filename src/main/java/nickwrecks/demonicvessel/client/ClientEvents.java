package nickwrecks.demonicvessel.client;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import nickwrecks.demonicvessel.DemonicVessel;
import nickwrecks.demonicvessel.client.particle.PurpleStarParticle;
import nickwrecks.demonicvessel.client.particle.ModParticleTypes;
import nickwrecks.demonicvessel.client.screen.BatteryScreen;
import nickwrecks.demonicvessel.client.screen.ModScreens;
import nickwrecks.demonicvessel.entity.ModEntityTypes;
import nickwrecks.demonicvessel.entity.render.LesserDemonRenderer;

@Mod.EventBusSubscriber(modid = DemonicVessel.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {

    @SubscribeEvent
    public static void onModelRegistryEvent(ModelEvent.RegisterGeometryLoaders event) {
        event.register(BatteryModelLoader.BATTERY_LOADER.getPath(), new BatteryModelLoader());
    }

    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ModScreens.BATTERY_CONTAINER.get(), BatteryScreen::new);
        });
    }

    @SubscribeEvent
    public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.LESSER_DEMON.get(), LesserDemonRenderer::new);
    }
    @SubscribeEvent
    public static void onRegisterSpriteParticle(RegisterParticleProvidersEvent event) {
       Minecraft.getInstance().particleEngine.register(ModParticleTypes.PURPLE_STAR_PARTICLE_TYPE.get(), PurpleStarParticle.Provider::new);

    }

}
