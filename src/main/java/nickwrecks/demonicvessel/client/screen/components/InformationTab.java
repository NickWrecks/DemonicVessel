package nickwrecks.demonicvessel.client.screen.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import nickwrecks.demonicvessel.DemonicVessel;
import nickwrecks.demonicvessel.block.entity.BatteryBlockEntity;

public class InformationTab extends GuiComponent {

    private String info;
    private boolean open;
    private boolean fullyOpen;
    private boolean fullyClosed;
    private int leftPos;
    private int topPos;
    private int imageWidth;
    private int informationHeight;
    private int informationWidth;

    private static final int INFORMATION_WIDTH_MIN = 16;
    private static final int INFORMATION_HEIGHT_MIN = 18;
    private static final int INFORMATION_WIDTH_MAX = 84;
    private static final int INFORMATION_HEIGHT_MAX = 161;
    public ImageButton button;
    private static final ResourceLocation INFO = new ResourceLocation(DemonicVessel.MODID, "textures/gui/info.png");

    public InformationTab(String info, int leftPos, int topPos, int imageWidth) {
        this.info = info;
        this.leftPos = leftPos;
        this.topPos = topPos;
        this.imageWidth = imageWidth;
        this.informationHeight = INFORMATION_HEIGHT_MIN;
        this.informationWidth = INFORMATION_WIDTH_MIN;
        fullyClosed = true;
        button = new ImageButton(leftPos + imageWidth, topPos + 4, 16, 15, 84, 0,0, INFO, new Button.OnPress() {
            @Override
            public void onPress(Button pButton) {
                open = !open;
            }
        });
    }

    public void draw(PoseStack pPoseStack) {
        RenderSystem.setShaderTexture(0,INFO);
        if(informationWidth == INFORMATION_WIDTH_MAX && informationHeight == INFORMATION_HEIGHT_MAX) fullyOpen = true;
        else if(informationWidth == INFORMATION_WIDTH_MIN && informationHeight == INFORMATION_HEIGHT_MIN) fullyClosed = true;
        if(fullyClosed && !open) {
            blit(pPoseStack,leftPos+imageWidth,topPos+3,85,15,15,17); }
        else
            blit(pPoseStack,leftPos+imageWidth,topPos+3,0,0,informationWidth,informationHeight);
        if(open) {
            fullyClosed = false;
            if(informationHeight < INFORMATION_HEIGHT_MAX) informationHeight += 12;
            if(informationHeight > INFORMATION_HEIGHT_MAX) informationHeight = INFORMATION_HEIGHT_MAX;
            if(informationWidth < INFORMATION_WIDTH_MAX && informationHeight == INFORMATION_HEIGHT_MAX) informationWidth += 8;
            if(informationWidth > INFORMATION_WIDTH_MAX) informationWidth = INFORMATION_WIDTH_MAX;
        }
        else {
            fullyOpen = false;
            if(informationHeight > INFORMATION_HEIGHT_MIN && informationWidth == INFORMATION_WIDTH_MIN) informationHeight -= 12;
            if(informationHeight < INFORMATION_HEIGHT_MIN) informationHeight = INFORMATION_HEIGHT_MIN;
            if(informationWidth > INFORMATION_WIDTH_MIN) informationWidth -= 8;
            if(informationWidth < INFORMATION_WIDTH_MIN) informationWidth = INFORMATION_WIDTH_MIN;
        }
    }

    public void drawInfo(PoseStack pPoseStack, Font font) {
        int numberOfLines = font.split(Component.literal(info), 80).size();
        if(fullyOpen) {
            font.draw(pPoseStack,Component.translatable("Information"),this.imageWidth+35,10,0x000000);
            for(int i = 0; i < numberOfLines; i++)
                font.draw(pPoseStack, font.split(Component.literal(info), 80).get(i), this.imageWidth+INFORMATION_WIDTH_MIN*2, 32+ i * font.lineHeight, 0x000000);
            }
        }
}
