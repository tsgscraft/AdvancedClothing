package de.tsgscraft.advancedclothing.client.screen;

import de.tsgscraft.advancedclothing.REFERENCE;
import de.tsgscraft.advancedclothing.client.ClothingElement;
import de.tsgscraft.advancedclothing.client.ClothingRegistry;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

import java.util.List;

public class GridSelectionClothingType extends GridSelectionItem<GridSelectionClothingType> {

    public String clothingType;

    public GridSelectionClothingType(String clothingType) {
        this.clothingType = clothingType;
    }

    @Override
    public void onClick(double x, double y, int button, ClothingSelectionScreen screen) {
        List<ClothingElement> clothingElements = ClothingRegistry.getInstance().getClothingTypeToElementMap().get(clothingType);
        List<GridSelectionClothingElement> clothingElementItems = clothingElements.stream().map(GridSelectionClothingElement::new).toList();
        screen.gridSelectionItemWidget.setItems(clothingElementItems);
        screen.setActive(screen.gridSelectionItemWidget);
    }

    @Override
    public void renderItem(int i, int itemsAlongX, int itemWidth, int itemHeight, int x, int y, int padding, int scrollOffset, int mouseX, int mouseY, GuiGraphics guiGraphics, GridSelectionWidget<GridSelectionClothingType> gridSelectionWidget) {
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

        int d0 = Math.toIntExact(Math.round((double) Util.getMillis() / (double) 2000.0F));
        List<ClothingElement> clothingElements = ClothingRegistry.getInstance().getClothingTypeToElementMap().get(clothingType);
        ClothingElement clothingElement = clothingElements.get(d0 % clothingElements.size());

        clothingElement.renderInfo().renderEntityInInventoryFollowsMouse(guiGraphics, itemX + 2, itemY + 16, itemX + itemWidth - 4, itemY + itemHeight - 4, 30, 0, mouseX, mouseY);

        AbstractWidget.renderScrollingString(guiGraphics, Minecraft.getInstance().font, Component.translatable(REFERENCE.MODID + ".type." + clothingType.split(":")[0] + "." + clothingType.split(":")[1]), itemX+2, itemY+2, itemX + itemWidth - 2, itemY + 14, 0xFFFFFFFF);
    }

    public String clothingType() {
        return clothingType;
    }
}
