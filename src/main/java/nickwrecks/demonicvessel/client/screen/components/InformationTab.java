package nickwrecks.demonicvessel.client.screen.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import nickwrecks.demonicvessel.DemonicVessel;

public class InformationTab extends AbstractWidget {

    private final String info;
    private boolean open = false;
    private boolean fullyOpen;
    private boolean fullyClosed;
    private final int leftPos;
    private final int topPos;
    private final int imageWidth;
    private int informationHeight;
    private int informationWidth;
    private final Font font;
    private int lineIndex = 0;
    private static final int INFORMATION_WIDTH_MIN = 16;
    private static final int INFORMATION_HEIGHT_MIN = 18;
    private static final int INFORMATION_WIDTH_MAX = 84;
    private static final int INFORMATION_HEIGHT_MAX = 161;
    private int MAXIMUM_LINE_COUNT = 13;
    public ImageButton scrollDown,scrollUp;
    private static final ResourceLocation INFO = new ResourceLocation(DemonicVessel.MODID, "textures/gui/info.png");

    public InformationTab(String info, int leftPos, int topPos, int imageHeight, int imageWidth, Font font) {
        super(leftPos,topPos,imageWidth,imageHeight, CommonComponents.EMPTY);
        this.info = info;
        this.leftPos = leftPos;
        this.topPos = topPos;
        this.imageWidth = imageWidth;
        this.informationHeight = INFORMATION_HEIGHT_MIN;
        this.informationWidth = INFORMATION_WIDTH_MIN;
        this.font = font;
        this.setX(leftPos+imageWidth);
        this.m_253211_(topPos+4);
        this.width = 16;
        this.height = 16;
        MAXIMUM_LINE_COUNT = Math.min(MAXIMUM_LINE_COUNT, font.split(Component.literal(info),80).size());
        this.setTooltip(Tooltip.create(Component.literal("Information")));
         scrollUp = new ImageButton(leftPos + imageWidth + 4, topPos + 18, 15, 10, 84, 33,0, INFO, pButton -> {
            if(lineIndex>0)
                lineIndex--;
        }
        );
         scrollDown = new ImageButton(leftPos + imageWidth + 4, topPos + 30 + MAXIMUM_LINE_COUNT*font.lineHeight, 15, 10, 84, 43, 0,INFO, pButton -> {
            if(lineIndex + MAXIMUM_LINE_COUNT < font.split(Component.literal(info),80).size())
                lineIndex++;
        }
        );

    }

    public void drawInfo(PoseStack pPoseStack) {
        if(fullyOpen) {
            font.draw(pPoseStack,Component.translatable("Information"),leftPos+imageWidth+20,topPos + 10,0x000000);
            for(int i = lineIndex; i < lineIndex+MAXIMUM_LINE_COUNT; i++)
                font.draw(pPoseStack,
                        font.split(Component.literal(info), 80).get(i),
                        leftPos+imageWidth + 4,
                        topPos + 30 + (i-lineIndex) * font.lineHeight, 0x000000);
            }
        }


    @Override
    public void m_87963_(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTicks) {
        pPoseStack.pushPose();
        RenderSystem.setShaderTexture(0,INFO);
        scrollDown.visible = fullyOpen;
        scrollUp.visible = fullyOpen;
        scrollDown.active = fullyOpen;
        scrollUp.active = fullyOpen;
        if(informationWidth == INFORMATION_WIDTH_MAX && informationHeight == INFORMATION_HEIGHT_MAX) fullyOpen = true;
        else if(informationWidth == INFORMATION_WIDTH_MIN && informationHeight == INFORMATION_HEIGHT_MIN) fullyClosed = true;
        if(fullyClosed) {
            blit(pPoseStack,leftPos+imageWidth,topPos+3,85,15,16,18);
        }
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
        pPoseStack.translate(0,0,1);
        blit(pPoseStack,this.getX(),this.getY(),84,0,16,15);
        drawInfo(pPoseStack);
        pPoseStack.popPose();
    }



    @Override
    public void onClick(double pMouseX, double pMouseY) {
        open = !open;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
        this.defaultButtonNarrationText(pNarrationElementOutput);
    }
}
