package nickwrecks.demonicvessel.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import nickwrecks.demonicvessel.DemonicVessel;
import nickwrecks.demonicvessel.block.entity.FamishedGeneratorBlockEntity;
import nickwrecks.demonicvessel.client.screen.components.InformationTab;

import java.util.Optional;

public class FamishedGeneratorScreen extends AbstractContainerScreen<FamishedGeneratorMenu> {
    public FamishedGeneratorScreen(FamishedGeneratorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);

    }
    private static final int ENERGY_HEIGHT = 34;
    private static final int ENERGY_TOP = 22;
    private static final int ENERGY_LEFT = 116;
    private static final int ENERGY_WIDTH = 16;
    private static final int EXPERIENCE_HEIGHT = 34;
    private static final int EXPERIENCE_TOP = 22;
    private static final int EXPERIENCE_LEFT = 44;
    private static final int EXPERIENCE_WIDTH = 16;
    private static final ResourceLocation GUI = new ResourceLocation(DemonicVessel.MODID, "textures/gui/famished_generator_base.png");

    private String info = "Turns EXP into RDE. Requires an Experience Gem to draw in experience from players. If left starving, will eat the gem. \n Crouch+Right Click to switch EXP collection on and off. \n A Distillation Feeder is recommended.";
    @Override
    protected void init() {
        super.init(); InformationTab informationTab = new InformationTab(info,leftPos,topPos,imageHeight,imageWidth,this.font);
        addRenderableWidget(informationTab);
        addRenderableWidget(informationTab.scrollUp);
        addRenderableWidget(informationTab.scrollDown);
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        this.renderBackground(pPoseStack);
        RenderSystem.setShaderTexture(0,GUI);
        blit(pPoseStack,leftPos,topPos,0,0,this.imageWidth,this.imageHeight);
        int power = menu.getPower();

        int p = (int) (((power/(float) FamishedGeneratorBlockEntity.FAMISHED_GEN_CAPACITY)) * ENERGY_HEIGHT);
        fillGradient(pPoseStack,leftPos + ENERGY_LEFT, topPos + ENERGY_TOP + ENERGY_HEIGHT - p  , leftPos + ENERGY_LEFT + ENERGY_WIDTH, topPos + ENERGY_TOP + ENERGY_HEIGHT, 0xffff0000, 0xff000000);
        fill(pPoseStack,leftPos + ENERGY_LEFT, topPos + ENERGY_TOP, leftPos + ENERGY_LEFT + ENERGY_WIDTH, topPos + ENERGY_TOP + ENERGY_HEIGHT - p, 0xff330000);


        int experience = menu.getExperience();
        int ratio = (int) (((experience/(float) FamishedGeneratorBlockEntity.FAMISHED_GEN_EXPERIENCE_CAPACITY)) * EXPERIENCE_HEIGHT);
        fillGradient(pPoseStack,leftPos + EXPERIENCE_LEFT, topPos + EXPERIENCE_TOP + EXPERIENCE_HEIGHT - ratio  , leftPos + EXPERIENCE_LEFT + EXPERIENCE_WIDTH, topPos + EXPERIENCE_TOP + EXPERIENCE_HEIGHT, 0xff3ae50f, 0xff1d6a09);
        fill(pPoseStack,leftPos + EXPERIENCE_LEFT, topPos + EXPERIENCE_TOP, leftPos + EXPERIENCE_LEFT + EXPERIENCE_WIDTH, topPos + EXPERIENCE_TOP + EXPERIENCE_HEIGHT - ratio, 0xff330000);

        int hunger = menu.getHunger();
        for(int i = 125; i> 125-(hunger/2)*9; i-=9 ) {
            blit(pPoseStack,leftPos+i,topPos+62,177,3,7,7);
        }
        if(hunger%2!=0) {
            blit(pPoseStack,leftPos+126-(hunger/2)*9,topPos+62,187,3,7,7);
        }
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        if(pMouseX>=leftPos + ENERGY_LEFT && pMouseX < leftPos + ENERGY_LEFT + ENERGY_WIDTH && pMouseY >= topPos+ENERGY_TOP && pMouseY<=topPos+ENERGY_TOP+ENERGY_HEIGHT) {
            int power = menu.getPower();
            renderTooltip(pPoseStack, Component.literal(power + "/" + FamishedGeneratorBlockEntity.FAMISHED_GEN_CAPACITY + " RDE"), pMouseX, pMouseY);
        }
        if(pMouseX>=leftPos + EXPERIENCE_LEFT && pMouseX < leftPos + EXPERIENCE_LEFT + EXPERIENCE_WIDTH && pMouseY >= topPos+EXPERIENCE_TOP && pMouseY<=topPos+EXPERIENCE_TOP+EXPERIENCE_HEIGHT) {
            int experience = menu.getExperience();
            renderTooltip(pPoseStack, Component.literal( experience + "/" + FamishedGeneratorBlockEntity.FAMISHED_GEN_EXPERIENCE_CAPACITY + " EXP"), pMouseX, pMouseY);
        }
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        super.renderLabels(pPoseStack, pMouseX, pMouseY);
        pPoseStack.scale(0.9f,0.9f,1.0f);
    }

    @Override
    public Optional<GuiEventListener> getChildAt(double pMouseX, double pMouseY) {
        return super.getChildAt(pMouseX, pMouseY);
    }
}
