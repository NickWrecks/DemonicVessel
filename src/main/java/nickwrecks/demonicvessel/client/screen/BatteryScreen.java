package nickwrecks.demonicvessel.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import nickwrecks.demonicvessel.DemonicVessel;
import nickwrecks.demonicvessel.block.entity.BatteryBlockEntity;
import nickwrecks.demonicvessel.network.BatteryConfigToServer;
import nickwrecks.demonicvessel.network.Channel;

import static nickwrecks.demonicvessel.client.ClientTools.playClickSound;

public class BatteryScreen extends AbstractContainerScreen<BatteryMenu> {


    public BatteryScreen(BatteryMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.titleLabelY = this.imageHeight-162;
    }

    private static final int ENERGY_LEFT = 8;
    private static final int ENERGY_WIDTH = 160;
    private static final int ENERGY_TOP = 17;
    private static final int ENERGY_HEIGHT = 52;
    private static final int INFORMATION_WIDTH_MIN = 16;
    private static final int INFORMATION_HEIGHT_MIN = 18;
    private static final int INFORMATION_WIDTH_MAX = 84;
    private static final int INFORMATION_HEIGHT_MAX = 161;

    private int informationHeight = INFORMATION_HEIGHT_MIN;
    private int informationWidth = INFORMATION_WIDTH_MIN;
    private static final int CONFIG_WIDTH_MIN = 16;
    private static final int CONFIG_HEIGHT_MIN = 18;
    private static final int CONFIG_WIDTH_MAX = 84;
    private static final int CONFIG_HEIGHT_MAX = 161;
    private int configHeight = CONFIG_HEIGHT_MIN;
    private int configWidth = CONFIG_WIDTH_MIN;
    private String info = new String("This battery is capable of storing up to " + BatteryBlockEntity.BATTERY_CAPACITY + " units of RDE. The input and output of energy on any given side can be configured via the panel on the left.");
    private boolean openInformation = false;
    private boolean openConfig = false;
    private static final ResourceLocation GUI = new ResourceLocation(DemonicVessel.MODID, "textures/gui/battery_base.png");

    private static final ResourceLocation INFO = new ResourceLocation(DemonicVessel.MODID, "textures/gui/info.png");

    private static final ResourceLocation INFO_CLOSED = new ResourceLocation(DemonicVessel.MODID, "textures/gui/info_closed.png");
    private static final ResourceLocation CONFIGURATION = new ResourceLocation(DemonicVessel.MODID, "textures/gui/configuration.png");
    private static final ResourceLocation CONFIGURATION_CLOSED = new ResourceLocation(DemonicVessel.MODID, "textures/gui/configuration_closed.png");
    public int[] config = {0,0,0,0,0,0};
    @Override
    protected void init() {
        super.init();
        addRenderableWidget(new ImageButton(leftPos + this.imageWidth, topPos + 4, 16, 16, 84, 0, 0, INFO, new Button.OnPress() {
            @Override
            public void onPress(Button pButton) {
            if(openInformation) openInformation = false;
            else openInformation = true;
            }}));
        addRenderableWidget(new ImageButton(leftPos - 15, topPos + 3, 16, 16, 84, 0, 0, CONFIGURATION, new Button.OnPress() {
            @Override
            public void onPress(Button pButton) {
                if(openConfig) openConfig = false;
                else openConfig = true;
            }
        }));
    }


    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        this.renderBackground(pPoseStack);
        RenderSystem.setShaderTexture(0, GUI);
        blit(pPoseStack,leftPos,topPos,0,0,this.imageWidth,this.imageHeight);
        int power = menu.getPower();
        int p = (int) (((power/(float) BatteryBlockEntity.BATTERY_CAPACITY)) * ENERGY_HEIGHT);
        fillGradient(pPoseStack,leftPos + ENERGY_LEFT, topPos + ENERGY_TOP + ENERGY_HEIGHT - p  , leftPos + ENERGY_LEFT + ENERGY_WIDTH, topPos + ENERGY_TOP + ENERGY_HEIGHT, 0xffff0000, 0xff000000);
        fill(pPoseStack,leftPos + ENERGY_LEFT, topPos + ENERGY_TOP, leftPos + ENERGY_LEFT + ENERGY_WIDTH, topPos + ENERGY_TOP + ENERGY_HEIGHT - p, 0xff330000);
        RenderSystem.setShaderTexture(0, (informationHeight==INFORMATION_HEIGHT_MIN && informationWidth==INFORMATION_WIDTH_MIN) ? INFO_CLOSED :INFO);
        blit(pPoseStack, leftPos+this.imageWidth, topPos+3, 0,0, informationWidth,informationHeight);
        RenderSystem.setShaderTexture(0, (configHeight==CONFIG_HEIGHT_MIN && configWidth == CONFIG_WIDTH_MIN) ? CONFIGURATION_CLOSED : CONFIGURATION);
        blit(pPoseStack, leftPos-configWidth,topPos+2,0,0, configWidth, configHeight);
        if (openInformation) {
            if(informationHeight < INFORMATION_HEIGHT_MAX) informationHeight += 12;
            if(informationHeight > INFORMATION_HEIGHT_MAX) informationHeight = INFORMATION_HEIGHT_MAX;
            if(informationWidth < INFORMATION_WIDTH_MAX && informationHeight == INFORMATION_HEIGHT_MAX) informationWidth += 8;
            if(informationWidth > INFORMATION_WIDTH_MAX) informationWidth = INFORMATION_WIDTH_MAX;
        }
        else {
            if(informationHeight > INFORMATION_HEIGHT_MIN && informationWidth == INFORMATION_WIDTH_MIN) informationHeight -= 12;
            if(informationHeight < INFORMATION_HEIGHT_MIN) informationHeight = INFORMATION_HEIGHT_MIN;
            if(informationWidth > INFORMATION_WIDTH_MIN) informationWidth -= 8;
            if(informationWidth < INFORMATION_WIDTH_MIN) informationWidth = INFORMATION_WIDTH_MIN;
        }
        if (openConfig) {
            if(configHeight < CONFIG_HEIGHT_MAX) configHeight += 12;
            if(configHeight > CONFIG_HEIGHT_MAX) configHeight = CONFIG_HEIGHT_MAX;
            if(configWidth < CONFIG_WIDTH_MAX && configHeight == CONFIG_HEIGHT_MAX) configWidth += 8;
            if(configWidth > CONFIG_WIDTH_MAX) configWidth = CONFIG_WIDTH_MAX;
        }
        else {
            if(configHeight > CONFIG_HEIGHT_MIN && configWidth == CONFIG_WIDTH_MIN) configHeight -= 12;
            if(configHeight < CONFIG_HEIGHT_MIN) configHeight = CONFIG_HEIGHT_MIN;
            if(configWidth > CONFIG_WIDTH_MIN) configWidth -= 8;
            if(configWidth < CONFIG_WIDTH_MIN) configWidth = CONFIG_WIDTH_MIN;
        }
    }
    private final static int configOffsetLeft = 49;

    private final static int configOffsetTop = 46;
    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        if(pMouseX>=leftPos + ENERGY_LEFT && pMouseX < leftPos + ENERGY_LEFT + ENERGY_WIDTH && pMouseY >= topPos+ENERGY_TOP && pMouseY<=topPos+ENERGY_TOP+ENERGY_HEIGHT) {
            int power = menu.getPower();
            renderTooltip(pPoseStack, Component.literal(power + "/" + BatteryBlockEntity.BATTERY_CAPACITY + " RDE"), pMouseX, pMouseY);
        }
        if(pMouseX<leftPos && pMouseX>=leftPos-15 && pMouseY>topPos+3 && pMouseY<=topPos+19)
            renderTooltip(pPoseStack, Component.translatable("Configuration"), pMouseX, pMouseY);

        if(pMouseX>leftPos+this.imageWidth && pMouseX<=leftPos+this.imageWidth+16 && pMouseY>topPos+3 && pMouseY<=topPos+19)
            renderTooltip(pPoseStack, Component.translatable("Information"), pMouseX, pMouseY);


        ///D-U-N-S-W-E
        if(configWidth == CONFIG_WIDTH_MAX && configHeight == CONFIG_HEIGHT_MAX) {
            RenderSystem.setShaderTexture(0, getTextureAt(2));
            blit(pPoseStack,leftPos-configOffsetLeft, topPos+configOffsetTop,1,0,0,16,16,16,16);
            RenderSystem.setShaderTexture(0,getTextureAt(5));
            blit(pPoseStack,leftPos-configOffsetLeft-18, topPos+configOffsetTop,1,0,0,16,16,16,16);
            RenderSystem.setShaderTexture(0,getTextureAt(4));
            blit(pPoseStack,leftPos-configOffsetLeft+18, topPos+configOffsetTop,1,0,0,16,16,16,16);
            RenderSystem.setShaderTexture(0,getTextureAt(0));
            blit(pPoseStack,leftPos-configOffsetLeft, topPos+configOffsetTop+18,1,0,0,16,16,16,16);
            RenderSystem.setShaderTexture(0,getTextureAt(3));
            blit(pPoseStack,leftPos-configOffsetLeft+18, topPos+configOffsetTop+18,1,0,0,16,16,16,16);
            RenderSystem.setShaderTexture(0,getTextureAt(1));
            blit(pPoseStack,leftPos-configOffsetLeft, topPos+configOffsetTop-18,1,0,0,16,16,16,16);
            if (pMouseX > leftPos - configOffsetLeft && pMouseX <= leftPos - configOffsetLeft + 16) {
                if (pMouseY > topPos + configOffsetTop && pMouseY <= topPos + configOffsetTop + 16) {
                    renderTooltip(pPoseStack, getComponentFromConfig(2), pMouseX, pMouseY);
                } else if (pMouseY > topPos + configOffsetTop - 18 && pMouseY <= topPos + configOffsetTop - 18 + 16) {
                    renderTooltip(pPoseStack, getComponentFromConfig(1), pMouseX, pMouseY);
                } else if (pMouseY > topPos + configOffsetTop + 16 && pMouseY <= topPos + configOffsetTop + 16 + 16) {
                    renderTooltip(pPoseStack, getComponentFromConfig(0), pMouseX, pMouseY);
                }
            } else if (pMouseX > leftPos - configOffsetLeft + 18 && pMouseX <= leftPos - configOffsetLeft + 18 + 16) {
                if (pMouseY > topPos + configOffsetTop && pMouseY <= topPos + configOffsetTop + 16) {
                    renderTooltip(pPoseStack, getComponentFromConfig(4), pMouseX, pMouseY);
                }
                if (pMouseY > topPos + configOffsetTop + 18 && pMouseY <= topPos + configOffsetTop + 18 + 16) {
                    renderTooltip(pPoseStack, getComponentFromConfig(3), pMouseX, pMouseY);
                }
            } else if (pMouseX > leftPos - configOffsetLeft - 18 && pMouseX <= leftPos - configOffsetLeft - 18 + 16 && pMouseY > topPos + configOffsetTop && pMouseY <= topPos + configOffsetTop + 16) {
                renderTooltip(pPoseStack, getComponentFromConfig(5), pMouseX, pMouseY);
            }
        }
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        super.renderLabels(pPoseStack, pMouseX, pMouseY);

        pPoseStack.scale(0.9f,0.9f,1);

        if(configWidth == CONFIG_WIDTH_MAX && configHeight == CONFIG_HEIGHT_MAX) {
            this.font.draw(pPoseStack, Component.translatable("Configuration"), -84, 8, 0x000000);
        }

        int numberOfLines = font.split(Component.literal(info), 80).size();
        if (informationWidth == INFORMATION_WIDTH_MAX && informationHeight == INFORMATION_HEIGHT_MAX) {
            this.font.draw(pPoseStack,Component.translatable("Information"),this.imageWidth+35,10,0x000000);
            for(int i = 0; i < numberOfLines; i++)
          this.font.draw(pPoseStack, font.split(Component.literal(info), 80).get(i), this.imageWidth+INFORMATION_WIDTH_MIN*2, 32+ i * font.lineHeight, 0x000000);
        }

    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (configWidth == CONFIG_WIDTH_MAX && configHeight == CONFIG_HEIGHT_MAX) {
            if (pMouseX > leftPos - configOffsetLeft && pMouseX <= leftPos - configOffsetLeft + 16) {
                if (pMouseY > topPos + configOffsetTop && pMouseY <= topPos + configOffsetTop + 16) {
                    configCycle(2);
                    playClickSound(0.5f);
                    Channel.sendToServer(new BatteryConfigToServer(config, getMenu().getPos()));
                } else if (pMouseY > topPos + configOffsetTop - 18 && pMouseY <= topPos + configOffsetTop - 18 + 16) {
                    configCycle(1);
                    playClickSound(0.5f);
                    Channel.sendToServer(new BatteryConfigToServer(config, getMenu().getPos()));
                } else if (pMouseY > topPos + configOffsetTop + 16 && pMouseY <= topPos + configOffsetTop + 16 + 16) {
                    configCycle(0);
                    playClickSound(0.5f);
                    Channel.sendToServer(new BatteryConfigToServer(config, getMenu().getPos()));
                }
            } else if (pMouseX > leftPos - configOffsetLeft + 18 && pMouseX <= leftPos - configOffsetLeft + 18 + 16) {
                if (pMouseY > topPos + configOffsetTop && pMouseY <= topPos + configOffsetTop + 16) {
                    configCycle(4);
                    playClickSound(0.5f);
                    Channel.sendToServer(new BatteryConfigToServer(config, getMenu().getPos()));
                }
                if (pMouseY > topPos + configOffsetTop + 18 && pMouseY <= topPos + configOffsetTop + 18 + 16) {
                    configCycle(3);
                    playClickSound(0.5f);
                    Channel.sendToServer(new BatteryConfigToServer(config, getMenu().getPos()));
                }
            } else if (pMouseX > leftPos - configOffsetLeft - 18 && pMouseX <= leftPos - configOffsetLeft - 18 + 16 && pMouseY > topPos + configOffsetTop && pMouseY <= topPos + configOffsetTop + 16) {
                configCycle(5);
                playClickSound(0.5f);
                Channel.sendToServer(new BatteryConfigToServer(config, getMenu().getPos()));
            }
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }


    private ResourceLocation getTextureAt(int index) {
        switch (config[index]) {
            case 0:
                return new ResourceLocation(DemonicVessel.MODID, "textures/block/battery_none.png");
            case 1:
                return new ResourceLocation(DemonicVessel.MODID, "textures/block/battery_input.png");
            case 2:
                return new ResourceLocation(DemonicVessel.MODID, "textures/block/battery_output.png");
            case 3:
                return new ResourceLocation(DemonicVessel.MODID, "textures/block/battery_both.png");
        }

        throw new IllegalArgumentException("Invalid index for face");
    }

    private Component getComponentFromConfig(int index) {
        switch (config[index]) {
            case 0:
                return Component.translatable("None");
            case 1:
                return Component.translatable("Input");
            case 2:
                return Component.translatable("Output");
            case 3:
                return Component.translatable("Both");
        }

        throw new IllegalArgumentException("Invalid index for face");

    }
    private void configCycle(int index) {
        if(config[index]<3) config[index]++;
        else config[index] = 0;
    }


}

