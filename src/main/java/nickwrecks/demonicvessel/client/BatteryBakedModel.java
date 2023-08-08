package nickwrecks.demonicvessel.client;



import com.mojang.math.Transformation;
import it.unimi.dsi.fastutil.ints.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.IDynamicBakedModel;
import net.minecraftforge.client.model.data.ModelData;
import nickwrecks.demonicvessel.block.entity.BatteryBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

import static nickwrecks.demonicvessel.client.ClientTools.*;


public class BatteryBakedModel implements IDynamicBakedModel {



    private final Function<Material, TextureAtlasSprite> spriteGetter;
    private final static Map<Direction, Int2ObjectMap<List<BakedQuad>>> FACE_QUAD_CACHE = new HashMap<>();
    private final Map<Direction, List<BakedQuad>> ITEM_QUAD_CACHE = new EnumMap<>(Direction.class);


    private final ItemTransforms itemTransforms;

    private final ModelState modelState;


    public BatteryBakedModel(ModelState modelState,Function<Material, TextureAtlasSprite> spriteGetter, ItemTransforms itemTransforms) {
        this.modelState = modelState;
        this.spriteGetter = spriteGetter;
        this.itemTransforms = itemTransforms;
        generateQuadCache();
    }
    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData extraData, @Nullable RenderType renderType) {

        if(side==null || (renderType!=null && !renderType.equals(RenderType.solid()))) {
            return Collections.EMPTY_LIST;
        }
        int[] inputStatus = extraData.get(BatteryBlockEntity.FACES_INPUT_STATUS);
        return FACE_QUAD_CACHE.get(side).get(inputStatus[side.get3DDataValue()]);

    }

    private void generateQuadCache() {

        TextureAtlasSprite batteryNone = spriteGetter.apply(BatteryModelLoader.MATERIAL_BATTERY_NONE);
        TextureAtlasSprite batteryInput = spriteGetter.apply(BatteryModelLoader.MATERIAL_BATTERY_INPUT);
        Transformation rotation = modelState.getRotation();
        Int2ObjectMap<List<BakedQuad>> down = new Int2ObjectOpenHashMap<List<BakedQuad>>();
        Int2ObjectMap<List<BakedQuad>> up = new Int2ObjectOpenHashMap<List<BakedQuad>>();
        Int2ObjectMap<List<BakedQuad>> north = new Int2ObjectOpenHashMap<List<BakedQuad>>();
        Int2ObjectMap<List<BakedQuad>> south = new Int2ObjectOpenHashMap<List<BakedQuad>>();
        Int2ObjectMap<List<BakedQuad>> west = new Int2ObjectOpenHashMap<List<BakedQuad>>();
        Int2ObjectMap<List<BakedQuad>> east = new Int2ObjectOpenHashMap<List<BakedQuad>>();
        List<BakedQuad> downNone = new ArrayList<BakedQuad>();
        downNone.add(directionalUnrotatedFaceQuad(Direction.DOWN,batteryNone));
        List<BakedQuad> downInput = new ArrayList<BakedQuad>();
        downInput.add(directionalUnrotatedFaceQuad(Direction.DOWN,batteryInput));
        List<BakedQuad> downOutput = new ArrayList<BakedQuad>();
        List<BakedQuad> downBoth = new ArrayList<BakedQuad>();
        List<BakedQuad> upNone = new ArrayList<BakedQuad>();
        upNone.add(directionalUnrotatedFaceQuad(Direction.UP,batteryNone));
        List<BakedQuad> upInput = new ArrayList<BakedQuad>();
        upInput.add(directionalUnrotatedFaceQuad(Direction.UP,batteryInput));
        List<BakedQuad> upOutput = new ArrayList<BakedQuad>();
        List<BakedQuad> upBoth = new ArrayList<BakedQuad>();
        List<BakedQuad> northNone = new ArrayList<BakedQuad>();
        northNone.add(directionalUnrotatedFaceQuad(Direction.NORTH,batteryNone));
        List<BakedQuad> northInput = new ArrayList<BakedQuad>();
        northInput.add(directionalUnrotatedFaceQuad(Direction.NORTH,batteryInput));
        List<BakedQuad> northOutput = new ArrayList<BakedQuad>();
        List<BakedQuad> northBoth = new ArrayList<BakedQuad>();
        List<BakedQuad> southNone = new ArrayList<BakedQuad>();
        southNone.add(directionalUnrotatedFaceQuad(Direction.SOUTH,batteryNone));
        List<BakedQuad> southInput = new ArrayList<BakedQuad>();
        southInput.add(directionalUnrotatedFaceQuad(Direction.SOUTH,batteryInput));
        List<BakedQuad> southOutput = new ArrayList<BakedQuad>();
        List<BakedQuad> southBoth = new ArrayList<BakedQuad>();
        List<BakedQuad> eastNone = new ArrayList<BakedQuad>();
        eastNone.add(directionalUnrotatedFaceQuad(Direction.EAST,batteryNone));
        List<BakedQuad> eastInput = new ArrayList<BakedQuad>();
        eastInput.add(directionalUnrotatedFaceQuad(Direction.EAST,batteryInput));
        List<BakedQuad> eastOutput = new ArrayList<BakedQuad>();
        List<BakedQuad> eastBoth = new ArrayList<BakedQuad>();
        List<BakedQuad> westNone = new ArrayList<BakedQuad>();
        westNone.add(directionalUnrotatedFaceQuad(Direction.WEST,batteryNone));
        List<BakedQuad> westInput = new ArrayList<BakedQuad>();
        westInput.add(directionalUnrotatedFaceQuad(Direction.WEST,batteryInput));
        List<BakedQuad> westOutput = new ArrayList<BakedQuad>();
        List<BakedQuad> westBoth = new ArrayList<BakedQuad>();

        ///Down face
        down.put(0, downNone);
        down.put(1,downInput);
        down.put(2,downOutput);
        down.put(3,downBoth);
        ///Up face
        up.put(0,upNone);
        up.put(1,upInput);
        up.put(2,upOutput);
        up.put(3,upBoth);
        ///North Face
        north.put(0,northNone);
        north.put(1,northInput);
        north.put(2,northOutput);
        north.put(3,northBoth);
        ///South Face
        south.put(0,southNone);
        south.put(1,southInput);
        south.put(2,southOutput);
        south.put(3,southBoth);
        ///West Face
        west.put(0,westNone);
        west.put(1,westInput);
        west.put(2,westOutput);
        west.put(3,westBoth);
        ///East Face
        east.put(0, eastNone);
        east.put(1, eastInput);
        east.put(2, eastOutput);
        east.put(3, eastBoth);




        FACE_QUAD_CACHE.put(Direction.DOWN, down);
        FACE_QUAD_CACHE.put(Direction.UP, up);
        FACE_QUAD_CACHE.put(Direction.EAST, east);
        FACE_QUAD_CACHE.put(Direction.WEST,west);
        FACE_QUAD_CACHE.put(Direction.SOUTH,south);
        FACE_QUAD_CACHE.put(Direction.NORTH,north);
    }


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
    public ItemTransforms getTransforms() {
        return itemTransforms;
    }
    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return spriteGetter.apply(BatteryModelLoader.MATERIAL_BATTERY_NONE);
    }

    private final ItemOverrides overrides = new ItemOverrides() {
        @Nullable
        @Override
        public BakedModel resolve(BakedModel pModel, ItemStack pStack, @Nullable ClientLevel pLevel, @Nullable LivingEntity pEntity, int pSeed) {
            int[] inputStatus;
            if(pStack.getTag() == null || pStack.getTag().getIntArray("InputStatus").length==0)
                inputStatus = new int[]{0,0,0,0,0,0};
            else inputStatus = pStack.getTag().getIntArray("InputStatus");
            ITEM_QUAD_CACHE.put(Direction.DOWN, FACE_QUAD_CACHE.get(Direction.DOWN).get(inputStatus[Direction.DOWN.get3DDataValue()]));
            ITEM_QUAD_CACHE.put(Direction.UP, FACE_QUAD_CACHE.get(Direction.UP).get(inputStatus[Direction.UP.get3DDataValue()]));
            ITEM_QUAD_CACHE.put(Direction.NORTH, FACE_QUAD_CACHE.get(Direction.NORTH).get(inputStatus[Direction.NORTH.get3DDataValue()]));
            ITEM_QUAD_CACHE.put(Direction.SOUTH,FACE_QUAD_CACHE.get(Direction.SOUTH).get(inputStatus[Direction.SOUTH.get3DDataValue()]));
            ITEM_QUAD_CACHE.put(Direction.WEST, FACE_QUAD_CACHE.get(Direction.WEST).get(inputStatus[Direction.WEST.get3DDataValue()]));
            ITEM_QUAD_CACHE.put(Direction.EAST, FACE_QUAD_CACHE.get(Direction.EAST).get(inputStatus[Direction.EAST.get3DDataValue()]));
            return new SimpleBakedModel(List.of(), ITEM_QUAD_CACHE, useAmbientOcclusion(),usesBlockLight(),isGui3d(),getParticleIcon(),itemTransforms,overrides);
        }
    };

    @Override
    public ItemOverrides getOverrides() {
        return overrides;
    }
}
