package nickwrecks.demonicvessel.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import nickwrecks.demonicvessel.DemonicVessel;
import nickwrecks.demonicvessel.block.entity.FamishedGeneratorBlockEntity;


public class FamishedGeneratorRenderer implements BlockEntityRenderer<FamishedGeneratorBlockEntity> {

    public static Material JAW_LOCATION = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(DemonicVessel.MODID, "block/famished_generator_jaw"));
    private final FamishedGeneratorJawModel jawModel1;
    private final FamishedGeneratorJawModel jawModel2;

    public FamishedGeneratorRenderer(BlockEntityRendererProvider.Context pContext) {
        this.jawModel1 = new FamishedGeneratorJawModel(pContext.bakeLayer(FamishedGeneratorJawModel.LAYER_LOCATION));
        this.jawModel2 = new FamishedGeneratorJawModel(pContext.bakeLayer(FamishedGeneratorJawModel.LAYER_LOCATION));
    }

    @Override
    public void render(FamishedGeneratorBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        pPoseStack.pushPose();

        if(pBlockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING) == Direction.WEST || pBlockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING) == Direction.EAST) {
            pPoseStack.translate(0.5f,0.44f,0.55f);
            pPoseStack.mulPose(Axis.YP.rotationDegrees(90));}
        else
            pPoseStack.translate(0.45f,0.44f,0.5f);
        this.jawModel1.setupAnim(pBlockEntity.time);
        this.jawModel1.renderToBuffer(pPoseStack,JAW_LOCATION.buffer(pBufferSource,RenderType::entityCutoutNoCull), LightTexture.FULL_BRIGHT,pPackedOverlay,1.0f,1.0f,1.0f,1.0f);

        pPoseStack.mulPose(Axis.YP.rotationDegrees(180));
        pPoseStack.translate(-0.10f,0.0f,0.0f);
        this.jawModel2.setupAnim(pBlockEntity.time);
        this.jawModel2.renderToBuffer(pPoseStack,JAW_LOCATION.buffer(pBufferSource,RenderType::entityCutoutNoCull), LightTexture.FULL_BRIGHT,pPackedOverlay,1.0f,1.0f,1.0f,1.0f);

        pPoseStack.popPose();

    }
}
