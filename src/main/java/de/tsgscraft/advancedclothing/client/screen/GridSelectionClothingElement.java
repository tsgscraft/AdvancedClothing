package de.tsgscraft.advancedclothing.client.screen;

import de.tsgscraft.advancedclothing.client.ClothingElement;
import de.tsgscraft.advancedclothing.network.SetClothingPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

public class GridSelectionClothingElement extends GridSelectionItem<GridSelectionClothingElement> {
    
    public ClothingElement clothingElement;
    
    public GridSelectionClothingElement(ClothingElement clothingElement) {
        this.clothingElement = clothingElement;
    }

    @Override
    public void onClick(double x, double y, int button, ClothingSelectionScreen screen) {
        PacketDistributor.sendToServer(
                new SetClothingPayload(clothingElement.type(), clothingElement().id().toString())
        );
    }

    @Override
    public void renderItem(int i, int itemsAlongX, int itemWidth, int itemHeight, int x, int y, int padding, int scrollOffset, int mouseX, int mouseY, GuiGraphics guiGraphics, GridSelectionWidget<GridSelectionClothingElement> gridSelectionWidget) {
        int row = i / itemsAlongX;
        int col = i % itemsAlongX;

        int itemX = x + col * (itemWidth + padding) + padding;
        int itemY = y + row * (itemHeight + padding) + padding - scrollOffset;

        guiGraphics.fill(
                itemX,
                itemY,
                itemX + itemWidth,
                itemY + itemHeight,
                gridSelectionWidget.isOverItem(mouseX, mouseY, itemX, itemY) ? 0x44000000 : 0x77000000
        );

        clothingElement.renderInfo().renderEntityInInventoryFollowsMouse(guiGraphics, itemX + 2, itemY + 16, itemX + itemWidth - 4, itemY + itemHeight - 4, 30, 0, mouseX, mouseY);

        AbstractWidget.renderScrollingString(guiGraphics, Minecraft.getInstance().font, Component.translatable(clothingElement.name()), itemX+2, itemY+2, itemX + itemWidth - 2, itemY + 14, 0xFFFFFFFF);
    }

    public ClothingElement clothingElement() {
        return clothingElement;
    }
}
