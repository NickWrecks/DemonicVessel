package nickwrecks.demonicvessel.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import nickwrecks.demonicvessel.DemonicVessel;

public class DistillationFeederScreen extends AbstractContainerScreen<DistillationFeederMenu> {
    public DistillationFeederScreen(DistillationFeederMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }
    public boolean isOverFire = false;

    private static final ResourceLocation GUI = new ResourceLocation(DemonicVessel.MODID, "textures/gui/distillation_feeder.png");
    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        this.renderBackground(pPoseStack);
        RenderSystem.setShaderTexture(0,GUI);
        blit(pPoseStack,leftPos,topPos,0,0,this.imageWidth,this.imageHeight);
        if(isOverFire)
        blit(pPoseStack,leftPos+81,topPos+61,177,1,13,13);
    }
}
