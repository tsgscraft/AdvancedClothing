package de.tsgscraft.advancedclothing.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import java.util.List;

public class GridSelectionWidget<T extends GridSelectionItem<T>> extends AbstractWidget {

    public int itemsAlongX;
    private List<T> items;
    private final int itemWidth;
    private final int itemHeight;

    private final int padding = 3;

    private final int scrollSpeed = 20; // Adjust this value to control the scroll speed
    private int scrollOffset = 0;

    private final ClothingSelectionScreen parentScreen;

    public GridSelectionWidget(int x, int y, int width, int height, int itemsAlongX, Component message, ClothingSelectionScreen parentScreen) {
        super(x, y, width, height, message);
        this.itemsAlongX = itemsAlongX;
        this.itemWidth = ((width-padding)/itemsAlongX) - padding;
        this.itemHeight = (int) (itemWidth * 2.5);
        this.parentScreen = parentScreen;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0x77000000);
        renderItems(guiGraphics, mouseX, mouseY, partialTick);
    }

    private void renderItems(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (items == null || items.isEmpty()) return;

        int clipX = this.getX();
        int clipY = this.getY();
        int clipWidth = this.getWidth();
        int clipHeight = this.getHeight();

        guiGraphics.enableScissor(
                clipX,
                clipY,
                clipX + clipWidth,
                clipY + clipHeight
        );

        for (int i = 0; i < items.size(); i++) {
            T item = items.get(i);
            item.renderItem(i, itemsAlongX, itemWidth, itemHeight, getX(), getY(), padding, scrollOffset, mouseX, mouseY, guiGraphics, this);
        }

        guiGraphics.disableScissor();
    }

    public T getHoveredItem(double mouseX, double mouseY) {
        if (items == null || items.isEmpty()) return null;

        for (int i = 0; i < items.size(); i++) {
            int row = i / itemsAlongX;
            int col = i % itemsAlongX;

            int itemX = this.getX() + col * (itemWidth + padding) + padding;
            int itemY = this.getY() + row * (itemHeight + padding) + padding - scrollOffset;

            if (isOverItem(mouseX, mouseY, itemX, itemY)) {
                return items.get(i);
            }
        }
        return null;
    }

    public boolean isOverItem(double mouseX, double mouseY, int itemX, int itemY) {
        return mouseX >= itemX && mouseX <= itemX + itemWidth && mouseY >= itemY && mouseY <= itemY + itemHeight;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }

    public void setItems(List<T> items) {
        if (!items.isEmpty()) {
            this.items = items.getFirst().sortItems(items);
        } else {
            this.items = items;
        }
    }

    public List<T> getItems() {
        return this.items;
    }

    public void sort() {
        if (items != null && !items.isEmpty()) {
            this.items = items.getFirst().sortItems(items);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.active && this.visible) {
            if (this.isValidClickButton(button)) {
                boolean flag = this.clicked(mouseX, mouseY);
                if (flag && getHoveredItem(mouseX, mouseY) != null) {
                    this.playDownSound(Minecraft.getInstance().getSoundManager());
                    this.onClick(mouseX, mouseY, button);
                    getHoveredItem(mouseX, mouseY).onClick(mouseX, mouseY, button, parentScreen);
                    return true;
                }
            }
        }
        return false;
    }

    /*
        Smooth scrolling for the grid selection widget. This method handles mouse scroll events and adjusts the scroll offset accordingly. It ensures that the scroll offset stays within valid bounds, preventing scrolling beyond the available items.
         */
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (this.active && this.visible) {
            if (scrollY > 0) {
                scrollOffset = Math.max(scrollOffset - scrollSpeed, 0);
            } else if (scrollY < 0) {
                int maxScroll = Math.max(0, (((items.size()-1) / itemsAlongX) + 1) * (itemHeight + padding) - this.getHeight() + padding);
                scrollOffset = Math.min(scrollOffset + scrollSpeed, maxScroll);
            }
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }
}
