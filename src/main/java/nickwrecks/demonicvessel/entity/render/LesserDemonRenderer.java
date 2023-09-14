package nickwrecks.demonicvessel.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.GuardianRenderer;
import net.minecraft.client.renderer.entity.WardenRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import nickwrecks.demonicvessel.DemonicVessel;
import nickwrecks.demonicvessel.client.ClientTools;
import nickwrecks.demonicvessel.entity.custom.LesserDemonEntity;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class LesserDemonRenderer extends EntityRenderer<LesserDemonEntity> {

    public LesserDemonRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    public static final ResourceLocation LESSER_DEMON_TEXTURE_ONE = new ResourceLocation(DemonicVessel.MODID, "block/lesser_demon_texture_one");
    public static final ResourceLocation LESSER_DEMON_TEXTURE_TWO = new ResourceLocation(DemonicVessel.MODID, "block/lesser_demon_texture_two");
    public static final ResourceLocation LESSER_DEMON_TEXTURE_THREE = new ResourceLocation(DemonicVessel.MODID, "block/lesser_demon_texture_three");
    public static final ResourceLocation LESSER_DEMON_TEXTURE_FOUR = new ResourceLocation(DemonicVessel.MODID, "block/lesser_demon_texture_four");
    public static final ResourceLocation LESSER_DEMON_TEXTURE_FIVE = new ResourceLocation(DemonicVessel.MODID, "block/lesser_demon_texture_five");
    public static final ResourceLocation LESSER_DEMON_TEXTURE_SIX = new ResourceLocation(DemonicVessel.MODID, "block/lesser_demon_texture_six");

    private static final ResourceLocation LIGHTNING_BEAM_LOCATION = new ResourceLocation(DemonicVessel.MODID,"textures/block/lightning_bolt.png");
    private static final RenderType LIGHTNING_RENDER_TYPE = RenderType.entityCutoutNoCull(LIGHTNING_BEAM_LOCATION);
    private static final ResourceLocation RITUAL_CIRCLE_LOCATION = new ResourceLocation(DemonicVessel.MODID,"textures/block/lesser_circle.png");
    private static final RenderType RITUAL_CIRCLE_RENDER_TYPE = RenderType.entityCutoutNoCull(RITUAL_CIRCLE_LOCATION);

    @Override
    public void render(LesserDemonEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {


        pPoseStack.pushPose();
        pPoseStack.translate(-0.5f,-0.7f,-0.5f);
        long millis = System.currentTimeMillis();
        ClientTools.renderBillboardQuadBright(pPoseStack,pBuffer.getBuffer(RenderType.cutout()),0.5f,pEntity.getHealth()>=10 ? getTextureForTime(millis) : LESSER_DEMON_TEXTURE_ONE);
        pPoseStack.popPose();
        LivingEntity livingentity = pEntity.getActiveAttackTarget();
        if (livingentity != null) {
            float f = 80;
            /// float f1 =  pPartialTick;
            /// float f2 = f1 * 0.5F % 1.0F;
            float f3 = pEntity.getEyeHeight();
            pPoseStack.pushPose();
            pPoseStack.translate(0.0F, f3, 0.0F);
            Vec3 vec3 = this.getPosition(livingentity, (double)livingentity.getBbHeight() * 0.5D, pPartialTick);
            Vec3 vec31 = this.getPosition(pEntity, (double)f3, pPartialTick);
            Vec3 vec32 = vec3.subtract(vec31);
            float f4 = (float)(vec32.length() + 1.0D);
            vec32 = vec32.normalize();
            float f5 = (float)Math.acos(vec32.y);
            float f6 = (float)Math.atan2(vec32.z, vec32.x);
            pPoseStack.mulPose(Axis.YP.rotationDegrees((((float)Math.PI / 2F) - f6) * (180F / (float)Math.PI)));
            pPoseStack.mulPose(Axis.XP.rotationDegrees(f5 * (180F / (float)Math.PI)));
            float f7 = /*f1*/1 * 0.05F * -1.5F;
            float f8 = f * f;
            int j = 64 + (int)(f8 * 191.0F);
            int k = 32 + (int)(f8 * 191.0F);
            int l = 128 - (int)(f8 * 64.0F);
            float f19 = Mth.cos(f7 + (float)Math.PI) ;// * 0.5F;
            float f20 = Mth.sin(f7 + (float)Math.PI) ;//* 0.5F;
            float f21 = Mth.cos(f7 + 0.0F) ;//* 0.5F;
            float f22 = Mth.sin(f7 + 0.0F) ;//* 0.5F;
            float f23 = Mth.cos(f7 + ((float)Math.PI / 2F)) ;//* 0.5F;
            float f24 = Mth.sin(f7 + ((float)Math.PI / 2F)) ;//* 0.5F;
            float f25 = Mth.cos(f7 + ((float)Math.PI * 1.5F)) ;//* 0.5F;
            float f26 = Mth.sin(f7 + ((float)Math.PI * 1.5F)) ;//* 0.5F;
            float f29 = 0.0F  /* + f2*/;
            float f30 = f4 * 2.5F + f29;
            VertexConsumer vertexconsumer = pBuffer.getBuffer(LIGHTNING_RENDER_TYPE);
            PoseStack.Pose posestack$pose = pPoseStack.last();
            Matrix4f matrix4f = posestack$pose.pose();
            Matrix3f matrix3f = posestack$pose.normal();


            vertex(vertexconsumer, matrix4f, matrix3f, f19, f4, f20, 255, 255, 255, 1F, 0);
            vertex(vertexconsumer, matrix4f, matrix3f, f19, 0.0F, f20, 255, 255, 255, 1F, 1);
            vertex(vertexconsumer, matrix4f, matrix3f, f21, 0.0F, f22, 255, 255, 255, 0.0F, 1);
            vertex(vertexconsumer, matrix4f, matrix3f, f21, f4, f22, 255, 255, 255, 0.0F, 0);
  /*        Vertical quad:
           vertex(vertexconsumer, matrix4f, matrix3f, f23, f4, f24, 255, 255, 255, 1F, 0);
            vertex(vertexconsumer, matrix4f, matrix3f, f23, 0.0F, f24, 255, 255, 255, 1F, 1);
            vertex(vertexconsumer, matrix4f, matrix3f, f25, 0.0F, f26, 255, 255, 255, 0.0F, 1);
            vertex(vertexconsumer, matrix4f, matrix3f, f25, f4, f26, 255, 255, 255, 0.0F, 0);
*/
            pPoseStack.popPose();
        }
        if(pEntity.getIsChannelingSlow()) {

            pPoseStack.pushPose();
            pPoseStack.translate(0.0f,-0.1f,0.0f);


            VertexConsumer vertexConsumer = pBuffer.getBuffer(RITUAL_CIRCLE_RENDER_TYPE);
            PoseStack.Pose pose = pPoseStack.last();
            Matrix4f matrix4f = pose.pose();
            Matrix3f matrix3f = pose.normal();

            float x = pEntity.getSlowTime() + pPartialTick;

            float reducedX = x * 0.05F * -1.5F;

            float cosX= Mth.cos(reducedX + 0.0F) ;//* 0.5F;
            float sinX = Mth.sin(reducedX + 0.0F) ;//* 0.5F;
            float negCosX = Mth.cos(reducedX + (float)Math.PI) ;// * 0.5F;
            float negSinX = Mth.sin(reducedX + (float)Math.PI) ;//* 0.5F;
            float nextQuadCosX = Mth.cos(reducedX + (float)Math.PI/2f) ;// * 0.5F;
            float nextQuadSinX = Mth.sin(reducedX + (float)Math.PI/2f) ;//* 0.5F;
            float prevQuadCosX = Mth.cos(reducedX - (float)Math.PI/2f) ;// * 0.5F;
            float prevQuadSinX = Mth.sin(reducedX - (float)Math.PI/2f) ;//* 0.5F;



            vertex(vertexConsumer, matrix4f, matrix3f, cosX,0,sinX,255,255,255,1.0f,0f);
            vertex(vertexConsumer, matrix4f, matrix3f, nextQuadCosX,0,nextQuadSinX,255,255,255,1.0f,1.0f);
            vertex(vertexConsumer, matrix4f, matrix3f, negCosX,0,negSinX,255,255,255,0f,1.0f);
            vertex(vertexConsumer, matrix4f, matrix3f, prevQuadCosX,0,prevQuadSinX,255,255,255,0f,0f);

            pPoseStack.popPose();
        }
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
    }

    private static void vertex(VertexConsumer pConsumer, Matrix4f matrix4f, Matrix3f matrix3f, float pX, float pY, float pZ, int pRed, int pGreen, int pBlue, float pU, float pV) {
        pConsumer.vertex(matrix4f, pX, pY, pZ).color(pRed, pGreen, pBlue, 255).uv(pU, pV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
    }


    private ResourceLocation getTextureForTime(long millis) {
        int timeForTexture = (int) ((millis/15) % 115);
        timeForTexture = timeForTexture /10;
       switch (timeForTexture) {
           case 1, 11: return LESSER_DEMON_TEXTURE_ONE;
           case 2, 10: return LESSER_DEMON_TEXTURE_TWO;
           case 3, 9: return LESSER_DEMON_TEXTURE_THREE;
           case 4, 8: return LESSER_DEMON_TEXTURE_FOUR;
           case 5, 7: return LESSER_DEMON_TEXTURE_FIVE;
           case 6: return LESSER_DEMON_TEXTURE_SIX;
       }
        return LESSER_DEMON_TEXTURE_ONE;
    }


    @Override
    public ResourceLocation getTextureLocation(LesserDemonEntity pEntity) {
        return LESSER_DEMON_TEXTURE_ONE;
    }

    private Vec3 getPosition(LivingEntity pLivingEntity, double pYOffset, float pPartialTick) {
        double d0 = Mth.lerp((double)pPartialTick, pLivingEntity.xOld, pLivingEntity.getX());
        double d1 = Mth.lerp((double)pPartialTick, pLivingEntity.yOld, pLivingEntity.getY()) + pYOffset;
        double d2 = Mth.lerp((double)pPartialTick, pLivingEntity.zOld, pLivingEntity.getZ());
        return new Vec3(d0, d1, d2);
    }
}
