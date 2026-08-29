package de.tsgscraft.advancedclothing.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class SimpleImageButton extends AbstractWidget {

    private final ResourceLocation texture;
    private final Runnable onPress;

    public SimpleImageButton(int x, int y, int width, int height, Component message, ResourceLocation sprite, Runnable onPress) {
        super(x, y, width, height, message);
        this.texture = sprite;
        this.onPress = onPress;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fill(
                getX(),
                getY(),
                getX() + getWidth(),
                getY() + getHeight(),
                isOverItem(mouseX, mouseY, getX(), getY()) ? 0x44000000 : 0x77000000
        );

        int size = Math.min(getWidth(), getHeight());

        guiGraphics.blitSprite(
                texture,
                getX() + (getWidth() - size) / 2,
                getY() + (getHeight() - size) / 2,
                size,
                size
        );
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        onPress.run();
        super.onClick(mouseX, mouseY, button);
    }

    private boolean isOverItem(int mouseX, int mouseY, int itemX, int itemY) {
        return mouseX >= itemX && mouseX <= itemX + getWidth() && mouseY >= itemY && mouseY <= itemY + getHeight();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
