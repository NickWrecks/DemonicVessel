package nickwrecks.demonicvessel.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.BlastFurnaceScreen;
import net.minecraft.client.gui.screens.inventory.FurnaceScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import nickwrecks.demonicvessel.DemonicVessel;
import nickwrecks.demonicvessel.block.entity.BatteryBlockEntity;

public class BatteryScreen extends AbstractContainerScreen<BatteryMenu> {


    public BatteryScreen(BatteryMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.titleLabelY = this.imageHeight-162;
    }

    private static final int ENERGY_LEFT = 8;
    private static final int ENERGY_WIDTH = 161;
    private static final int ENERGY_TOP = 17;
    private static final int ENERGY_HEIGHT = 53;
    private final ResourceLocation GUI = new ResourceLocation(DemonicVessel.MODID, "textures/gui/battery_base.png");

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        this.renderBackground(pPoseStack);
        RenderSystem.setShaderTexture(0, GUI);
        blit(pPoseStack,leftPos,topPos,0,0,this.imageWidth,this.imageHeight);
        int power = menu.getPower();
        int p = (int) (((power/(float) BatteryBlockEntity.BATTERY_CAPACITY)) * ENERGY_HEIGHT);
        fillGradient(pPoseStack,leftPos + ENERGY_LEFT, topPos + ENERGY_TOP + ENERGY_HEIGHT - p  , leftPos + ENERGY_LEFT + ENERGY_WIDTH, topPos + ENERGY_TOP + ENERGY_HEIGHT, 0xffff0000, 0xff000000);
        fill(pPoseStack,leftPos + ENERGY_LEFT, topPos + ENERGY_TOP, leftPos + ENERGY_LEFT + ENERGY_WIDTH, topPos + ENERGY_TOP + ENERGY_HEIGHT - p, 0xff330000);

    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        if(pMouseX>=leftPos + ENERGY_LEFT && pMouseX < leftPos + ENERGY_LEFT + ENERGY_WIDTH && pMouseY >= topPos+ENERGY_TOP && pMouseY<=topPos+ENERGY_TOP+ENERGY_HEIGHT) {
            int power = menu.getPower();
            renderTooltip(pPoseStack, Component.literal(power + " RDE"), pMouseX, pMouseY);
        }
    }
}
