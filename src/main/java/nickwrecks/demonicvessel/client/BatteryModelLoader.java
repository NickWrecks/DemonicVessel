package nickwrecks.demonicvessel.client;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;
import nickwrecks.demonicvessel.DemonicVessel;

import java.util.function.Function;

public class BatteryModelLoader implements IGeometryLoader<BatteryModelLoader.BatteryModelGeometry> {

    public static final ResourceLocation BATTERY_LOADER = new ResourceLocation(DemonicVessel.MODID, "battery_loader");

    public static final ResourceLocation BATTERY_NONE = new ResourceLocation(DemonicVessel.MODID, "block/battery_none");

    public static final Material MATERIAL_BATTERY_NONE = ForgeHooksClient.getBlockMaterial(BATTERY_NONE);

    @Override
    public BatteryModelGeometry read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException {
        return new BatteryModelGeometry();
    }

    public static class BatteryModelGeometry implements IUnbakedGeometry<BatteryModelGeometry> {


        @Override
        public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
            return new BatteryBakedModel(modelState,spriteGetter, context.getTransforms());
        }


    }
}
