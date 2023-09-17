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
import net.minecraft.util.Mth;
import nickwrecks.demonicvessel.DemonicVessel;

public class FamishedGeneratorJawModel extends Model {

	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(DemonicVessel.MODID, "famished_generator_jaw"), "main");
	private final ModelPart FamishedGenerator;

	public FamishedGeneratorJawModel(ModelPart root) {
		super(RenderType::entityCutout);
		this.FamishedGenerator = root.getChild("FamishedGenerator");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition FamishedGenerator = partdefinition.addOrReplaceChild("FamishedGenerator", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, 0.0F, -7.0F, 7.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(15, 20).addBox(-7.0F, 2.0F, -7.0F, 7.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(15, 17).addBox(-7.0F, 2.0F, 7.0F, 7.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(0, 17).addBox(-7.0F, 2.0F, -7.0F, 0.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);


	}

	public void setupAnim(float pTime) {
		float f = Math.abs(Mth.sin(pTime * 0.05F)*0.25f);
		this.FamishedGenerator.zRot = -f-0.01f;
	}
	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		FamishedGenerator.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}


}