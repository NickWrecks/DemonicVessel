package nickwrecks.demonicvessel.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import nickwrecks.demonicvessel.DemonicVessel;
import nickwrecks.demonicvessel.client.screen.components.InformationTab;

public class DistillationFeederScreen extends AbstractContainerScreen<DistillationFeederMenu> {
    public DistillationFeederScreen(DistillationFeederMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }
    public boolean isOverFire = false;

    private static InformationTab informationTab;
    private static final ResourceLocation GUI = new ResourceLocation(DemonicVessel.MODID, "textures/gui/distillation_feeder.png");

    private String info = new String("Feeds the Famished Generator it is attached to, without feeding it unecessarily. Requires a campfire beneath it, to distill the food for the generator.");
    @Override
    protected void init() {
        super.init();
        informationTab = new InformationTab(info,leftPos,topPos,imageWidth);
        addRenderableWidget(informationTab.button);
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        this.renderBackground(pPoseStack);
        RenderSystem.setShaderTexture(0,GUI);
        blit(pPoseStack,leftPos,topPos,0,0,this.imageWidth,this.imageHeight);
        if(isOverFire)
        blit(pPoseStack,leftPos+81,topPos+61,177,1,13,13);
        informationTab.draw(pPoseStack);
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        super.renderLabels(pPoseStack, pMouseX, pMouseY);
        pPoseStack.scale(0.9f,0.9f,1.0f);
        informationTab.drawInfo(pPoseStack,this.font);
    }
}
