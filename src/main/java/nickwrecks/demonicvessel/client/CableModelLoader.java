package nickwrecks.demonicvessel.client;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Transformation;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.IDynamicBakedModel;
import net.minecraftforge.client.model.IQuadTransformer;
import net.minecraftforge.client.model.QuadTransformers;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;
import nickwrecks.demonicvessel.DemonicVessel;
import nickwrecks.demonicvessel.block.custom.CableBlock;
import nickwrecks.demonicvessel.block.custom.ConnectorType;
import nickwrecks.demonicvessel.block.entity.BatteryBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class CableModelLoader implements IGeometryLoader<CableModelLoader.CableModelGeometry> {

    public static final ResourceLocation CABLE_LOADER = new ResourceLocation(DemonicVessel.MODID, "cable_loader");
    @Override
    public CableModelLoader.CableModelGeometry read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException {
        return new CableModelGeometry();
    }


    public static class CableModelGeometry implements IUnbakedGeometry<CableModelGeometry> {

        @Override
        public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
            return new CableModel(modelState,spriteGetter,context.getTransforms());
        }
    }

    private static class CableModel implements IDynamicBakedModel {
        private final static Map<Direction, Map<ConnectorType, Supplier<BakedModel>>> FACE_QUAD_CACHE = new HashMap<>();

        private static final RandomSource RANDOM = RandomSource.create();
        private final Function<Material, TextureAtlasSprite> spriteGetter;

        private final Map<Direction, List<BakedQuad>> ITEM_QUAD_CACHE = new EnumMap<>(Direction.class);

        private final ItemTransforms itemTransforms;
        private Transformation shiftToCenter = new Transformation(new Vector3f(0.5f),null,null,null);
        private IQuadTransformer quadTransformers = QuadTransformers.applying(shiftToCenter);
        public CableModel(ModelState modelState,Function<Material, TextureAtlasSprite> spriteGetter, ItemTransforms itemTransforms) {
            this.spriteGetter =spriteGetter;
            this.itemTransforms = itemTransforms;
            generateCache();
        }

        @Override
        public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData extraData, @Nullable RenderType renderType) {
            List<BakedQuad> quads = new ArrayList<>();
            if (side == null && renderType==RenderType.solid()) {
                ConnectorType[] connectorTypes = new ConnectorType[6];
                if (state != null) {
                    connectorTypes[0] = state.getValue(CableBlock.DOWN);
                    connectorTypes[1] = state.getValue(CableBlock.UP);
                    connectorTypes[2] = state.getValue(CableBlock.NORTH);
                    connectorTypes[3] = state.getValue(CableBlock.SOUTH);
                    connectorTypes[4] = state.getValue(CableBlock.WEST);
                    connectorTypes[5] = state.getValue(CableBlock.EAST);
                    for (int i = 0; i <= 5; i++) {
                        quads.addAll(FACE_QUAD_CACHE.get(Direction.from3DDataValue(i)).get(connectorTypes[i]).get().getQuads(null,null,RANDOM,ModelData.EMPTY,null));
                        quadTransformers.process(quads);
                    }
                }
            }
            return quads;
        }
        private static void generateCache() {
            Supplier<BakedModel> downNone = () -> Minecraft.getInstance().getModelManager().getModel(new ResourceLocation(DemonicVessel.MODID, "block/cable/cable_down_none"));
            Supplier<BakedModel>  downCabled = () -> Minecraft.getInstance().getModelManager().getModel(new ResourceLocation(DemonicVessel.MODID, "block/cable/cable_down_cabled"));
            Supplier<BakedModel>  downConnected = () -> Minecraft.getInstance().getModelManager().getModel(new ResourceLocation(DemonicVessel.MODID, "block/cable/cable_down_connected"));
            Supplier<BakedModel>  upNone = () -> Minecraft.getInstance().getModelManager().getModel(new ResourceLocation(DemonicVessel.MODID, "block/cable/cable_up_none"));
            Supplier<BakedModel>  upCabled = () -> Minecraft.getInstance().getModelManager().getModel(new ResourceLocation(DemonicVessel.MODID, "block/cable/cable_up_cabled"));
            Supplier<BakedModel>  upConnected = () -> Minecraft.getInstance().getModelManager().getModel(new ResourceLocation(DemonicVessel.MODID, "block/cable/cable_up_connected"));
            Supplier<BakedModel>  northNone = () -> Minecraft.getInstance().getModelManager().getModel(new ResourceLocation(DemonicVessel.MODID, "block/cable/cable_north_none"));
            Supplier<BakedModel>  northCabled = () -> Minecraft.getInstance().getModelManager().getModel(new ResourceLocation(DemonicVessel.MODID, "block/cable/cable_north_cabled"));
            Supplier<BakedModel>  northConnected = () -> Minecraft.getInstance().getModelManager().getModel(new ResourceLocation(DemonicVessel.MODID, "block/cable/cable_north_connected"));
            Supplier<BakedModel>  southNone = () -> Minecraft.getInstance().getModelManager().getModel(new ResourceLocation(DemonicVessel.MODID, "block/cable/cable_south_none"));
            Supplier<BakedModel>  southCabled = () -> Minecraft.getInstance().getModelManager().getModel(new ResourceLocation(DemonicVessel.MODID, "block/cable/cable_south_cabled"));
            Supplier<BakedModel>  southConnected = () -> Minecraft.getInstance().getModelManager().getModel(new ResourceLocation(DemonicVessel.MODID, "block/cable/cable_south_connected"));
            Supplier<BakedModel>  westNone = () -> Minecraft.getInstance().getModelManager().getModel(new ResourceLocation(DemonicVessel.MODID, "block/cable/cable_west_none"));
            Supplier<BakedModel>  westCabled = () -> Minecraft.getInstance().getModelManager().getModel(new ResourceLocation(DemonicVessel.MODID, "block/cable/cable_west_cabled"));
            Supplier<BakedModel>  westConnected = () -> Minecraft.getInstance().getModelManager().getModel(new ResourceLocation(DemonicVessel.MODID, "block/cable/cable_west_connected"));
            Supplier<BakedModel>  eastNone = () -> Minecraft.getInstance().getModelManager().getModel(new ResourceLocation(DemonicVessel.MODID, "block/cable/cable_east_none"));
            Supplier<BakedModel>  eastCabled = () -> Minecraft.getInstance().getModelManager().getModel(new ResourceLocation(DemonicVessel.MODID, "block/cable/cable_east_cabled"));
            Supplier<BakedModel>  eastConnected = () -> Minecraft.getInstance().getModelManager().getModel(new ResourceLocation(DemonicVessel.MODID, "block/cable/cable_east_connected"));

            Map<ConnectorType,Supplier<BakedModel>> down = new HashMap<>();
            Map<ConnectorType,Supplier<BakedModel>> up = new HashMap<>();
            Map<ConnectorType,Supplier<BakedModel>> north = new HashMap<>();
            Map<ConnectorType,Supplier<BakedModel>> south = new HashMap<>();
            Map<ConnectorType,Supplier<BakedModel>> west = new HashMap<>();
            Map<ConnectorType,Supplier<BakedModel>> east = new HashMap<>();


            down.put(ConnectorType.NONE,downNone);
            down.put(ConnectorType.CABLED,downCabled);
            down.put(ConnectorType.CONNECTED,downConnected);
            up.put(ConnectorType.NONE,upNone);
            up.put(ConnectorType.CABLED,upCabled);
            up.put(ConnectorType.CONNECTED,upConnected);
            north.put(ConnectorType.NONE,northNone);
            north.put(ConnectorType.CABLED,northCabled);
            north.put(ConnectorType.CONNECTED,northConnected);
            south.put(ConnectorType.NONE,southNone);
            south.put(ConnectorType.CABLED,southCabled);
            south.put(ConnectorType.CONNECTED,southConnected);
            west.put(ConnectorType.NONE,westNone);
            west.put(ConnectorType.CABLED,westCabled);
            west.put(ConnectorType.CONNECTED,westConnected);
            east.put(ConnectorType.NONE,eastNone);
            east.put(ConnectorType.CABLED,eastCabled);
            east.put(ConnectorType.CONNECTED,eastConnected);

            FACE_QUAD_CACHE.put(Direction.DOWN,down);
            FACE_QUAD_CACHE.put(Direction.UP,up);
            FACE_QUAD_CACHE.put(Direction.NORTH,north);
            FACE_QUAD_CACHE.put(Direction.SOUTH,south);
            FACE_QUAD_CACHE.put(Direction.WEST,west);
            FACE_QUAD_CACHE.put(Direction.EAST,east);
        }



        @Override
        public BakedModel applyTransform(ItemDisplayContext cameraTransformType, PoseStack poseStack, boolean applyLeftHandTransform) {
            getTransforms().m_269404_(cameraTransformType).apply(applyLeftHandTransform,poseStack);
            return this;
        }
        private final ItemOverrides overrides = new ItemOverrides() {
            @Nullable
            @Override
            public BakedModel resolve(BakedModel pModel, ItemStack pStack, @Nullable ClientLevel pLevel, @Nullable LivingEntity pEntity, int pSeed) {
                ITEM_QUAD_CACHE.put(Direction.DOWN,FACE_QUAD_CACHE.get(Direction.DOWN).get(ConnectorType.NONE).get().getQuads(null,null,RANDOM,ModelData.EMPTY,null));
                ITEM_QUAD_CACHE.put(Direction.UP,FACE_QUAD_CACHE.get(Direction.UP).get(ConnectorType.NONE).get().getQuads(null,null,RANDOM,ModelData.EMPTY,null));
                ITEM_QUAD_CACHE.put(Direction.NORTH,FACE_QUAD_CACHE.get(Direction.NORTH).get(ConnectorType.CABLED).get().getQuads(null,null,RANDOM,ModelData.EMPTY,null));
                ITEM_QUAD_CACHE.put(Direction.SOUTH,FACE_QUAD_CACHE.get(Direction.SOUTH).get(ConnectorType.CABLED).get().getQuads(null,null,RANDOM,ModelData.EMPTY,null));
                ITEM_QUAD_CACHE.put(Direction.WEST,FACE_QUAD_CACHE.get(Direction.WEST).get(ConnectorType.NONE).get().getQuads(null,null,RANDOM,ModelData.EMPTY,null));
                ITEM_QUAD_CACHE.put(Direction.EAST,FACE_QUAD_CACHE.get(Direction.SOUTH).get(ConnectorType.NONE).get().getQuads(null,null,RANDOM,ModelData.EMPTY,null));
                return new SimpleBakedModel(List.of(), ITEM_QUAD_CACHE, useAmbientOcclusion(),usesBlockLight(),isGui3d(),getParticleIcon(),itemTransforms,overrides);
            }
        };
        @Override
        public boolean useAmbientOcclusion() {
            return true;
        }

        @Override
        public boolean isGui3d() {
            return false;
        }

        @Override
        public boolean usesBlockLight() {
            return false;
        }

        @Override
        public boolean isCustomRenderer() {
            return false;
        }

        @Override
        public TextureAtlasSprite getParticleIcon() {
            return spriteGetter.apply(BatteryModelLoader.MATERIAL_BATTERY_NONE);
        }

        @Override
        public ItemOverrides getOverrides() {
            return overrides;
        }

        @Override
        public ItemTransforms getTransforms() {
            return itemTransforms;
        }
    }
}


