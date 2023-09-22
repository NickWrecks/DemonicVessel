package nickwrecks.demonicvessel.client;// Made with Blockbench 4.8.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class GemSphereModel extends Model {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "gemspheremodel"), "main");
	private final ModelPart Gem;

	public GemSphereModel(ModelPart root) {
		super(RenderType::entityCutout);
		this.Gem = root.getChild("Gem");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();


		PartDefinition Gem = partdefinition.addOrReplaceChild("Gem", CubeListBuilder.create().texOffs(1, 1).addBox(-1.0F, -7.0F, -0.5F, 2.0F, 14.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(15, 14).addBox(-2.0F, -6.0F, -0.5F, 1.0F, 12.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(17, 1).addBox(-3.0F, -5.0F, -0.5F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(22, 12).addBox(-4.0F, -4.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(22, 23).addBox(-5.0F, -3.0F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(10, 1).addBox(1.0F, -6.0F, -0.5F, 1.0F, 12.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(8, 16).addBox(2.0F, -5.0F, -0.5F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(1, 18).addBox(3.0F, -4.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(24, 1).addBox(4.0F, -3.0F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, 0.5F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}
	public void setupAnim(float pTime) {
		this.Gem.yRot = pTime * 0.1f;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Gem.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}