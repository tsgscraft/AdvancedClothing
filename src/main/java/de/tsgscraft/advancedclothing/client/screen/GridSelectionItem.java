package de.tsgscraft.advancedclothing.client.screen;

import net.minecraft.client.gui.GuiGraphics;

import java.util.List;

public abstract class GridSelectionItem<T extends GridSelectionItem<T>> {
    public abstract void onClick(double x, double y, int button, ClothingSelectionScreen screen);

    public abstract void renderItem(int i, int itemsAlongX, int itemWidth, int itemHeight, int x, int y, int padding, int scrollOffset, int mouseX, int mouseY, GuiGraphics guiGraphics, GridSelectionWidget<T> gridSelectionWidget);

    public abstract List<T> sortItems(List<T> items);
}
