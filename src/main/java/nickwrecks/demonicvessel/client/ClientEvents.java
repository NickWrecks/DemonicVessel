package nickwrecks.demonicvessel.client;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import nickwrecks.demonicvessel.DemonicVessel;
import nickwrecks.demonicvessel.block.entity.ModBlockEntities;
import nickwrecks.demonicvessel.client.particle.ModParticleTypes;
import nickwrecks.demonicvessel.client.particle.PurpleStarParticle;
import nickwrecks.demonicvessel.client.screen.BatteryScreen;
import nickwrecks.demonicvessel.client.screen.DistillationFeederScreen;
import nickwrecks.demonicvessel.client.screen.FamishedGeneratorScreen;
import nickwrecks.demonicvessel.client.screen.ModScreens;
import nickwrecks.demonicvessel.entity.ModEntityTypes;
import nickwrecks.demonicvessel.entity.render.LesserDemonRenderer;

@Mod.EventBusSubscriber(modid = DemonicVessel.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {

    @SubscribeEvent
    public static void onModelRegistryEvent(ModelEvent.RegisterGeometryLoaders event) {
        event.register(BatteryModelLoader.BATTERY_LOADER.getPath(), new BatteryModelLoader());
        event.register(CableModelLoader.CABLE_LOADER.getPath(), new CableModelLoader());
    }
    @SubscribeEvent
    public static void onAdditionalRegistryEvent(ModelEvent.RegisterAdditional event) {
        event.register(new ResourceLocation(DemonicVessel.MODID, "block/cable/cable_down_none"));
        event.register(new ResourceLocation(DemonicVessel.MODID, "block/cable/cable_down_cabled"));
        event.register(new ResourceLocation(DemonicVessel.MODID, "block/cable/cable_down_connected"));
        event.register(new ResourceLocation(DemonicVessel.MODID, "block/cable/cable_up_none"));
        event.register(new ResourceLocation(DemonicVessel.MODID, "block/cable/cable_up_cabled"));
        event.register(new ResourceLocation(DemonicVessel.MODID, "block/cable/cable_up_connected"));
        event.register(new ResourceLocation(DemonicVessel.MODID, "block/cable/cable_north_none"));
        event.register(new ResourceLocation(DemonicVessel.MODID, "block/cable/cable_north_cabled"));
        event.register(new ResourceLocation(DemonicVessel.MODID, "block/cable/cable_north_connected"));
        event.register(new ResourceLocation(DemonicVessel.MODID, "block/cable/cable_south_none"));
        event.register(new ResourceLocation(DemonicVessel.MODID, "block/cable/cable_south_cabled"));
        event.register(new ResourceLocation(DemonicVessel.MODID, "block/cable/cable_south_connected"));
        event.register(new ResourceLocation(DemonicVessel.MODID, "block/cable/cable_west_none"));
        event.register(new ResourceLocation(DemonicVessel.MODID, "block/cable/cable_west_cabled"));
        event.register(new ResourceLocation(DemonicVessel.MODID, "block/cable/cable_west_connected"));
        event.register(new ResourceLocation(DemonicVessel.MODID, "block/cable/cable_east_none"));
        event.register(new ResourceLocation(DemonicVessel.MODID, "block/cable/cable_east_cabled"));
        event.register(new ResourceLocation(DemonicVessel.MODID, "block/cable/cable_east_connected"));


    }
    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ModScreens.BATTERY_CONTAINER.get(), BatteryScreen::new);
            MenuScreens.register(ModScreens.FAMISHED_GENERATOR_CONTAINER.get(), FamishedGeneratorScreen::new);
            MenuScreens.register(ModScreens.DISTILLATION_FEEDER_CONTAINER.get(), DistillationFeederScreen::new);
        });
    }

    @SubscribeEvent
    public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.LESSER_DEMON.get(), LesserDemonRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.FAMISHED_GENERATOR_BLOCK_ENTITY.get(), FamishedGeneratorRenderer::new);

    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(FamishedGeneratorJawModel.LAYER_LOCATION, FamishedGeneratorJawModel::createBodyLayer);
        event.registerLayerDefinition(GemSphereModel.LAYER_LOCATION,GemSphereModel::createBodyLayer);

    }
    @SubscribeEvent
    public static void onRegisterSpriteParticle(RegisterParticleProvidersEvent event) {
       Minecraft.getInstance().particleEngine.register(ModParticleTypes.PURPLE_STAR_PARTICLE_TYPE.get(), PurpleStarParticle.Provider::new);
    }

}
