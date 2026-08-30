package de.tsgscraft.advancedclothing.client.screen;

import de.tsgscraft.advancedclothing.REFERENCE;
import de.tsgscraft.advancedclothing.attachments.ClothingAttachments;
import de.tsgscraft.advancedclothing.client.ClothingElement;
import de.tsgscraft.advancedclothing.client.ClothingRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        screen.setActive(clothingType);
    }

    @Override
    public void renderItem(int i, int itemsAlongX, int itemWidth, int itemHeight, int x, int y, int padding, int scrollOffset, int mouseX, int mouseY, GuiGraphics guiGraphics, GridSelectionWidget<GridSelectionClothingType> gridSelectionWidget) {
        int row = i / itemsAlongX;
        int col = i % itemsAlongX;

        int itemX = x + col * (itemWidth + padding) + padding;
        int itemY = y + row * (itemHeight + padding) + padding - scrollOffset;

        int bgColor = 0x44000000; // Default background color
        if (gridSelectionWidget.isOverItem(mouseX, mouseY, itemX, itemY)) {
            bgColor += 0x44888888; // Highlight color when hovered
        }
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        guiGraphics.fill(
                itemX,
                itemY,
                itemX + itemWidth,
                itemY + itemHeight,
                bgColor
        );

        int d0 = Math.toIntExact(Math.round((double) Util.getMillis() / (double) 2000.0F));
        List<ClothingElement> clothingElements = ClothingRegistry.getInstance().getClothingTypeToElementMap().get(clothingType);
        ClothingElement clothingElement = clothingElements.get(d0 % clothingElements.size());

        clothingElement.renderInfo().renderEntityInInventoryFollowsMouse(guiGraphics, itemX + 2, itemY + 16, itemX + itemWidth - 4, itemY + itemHeight - 4, 30, 0, mouseX, mouseY);

        Component typeName = Component.translatable(REFERENCE.MODID + ".type." + clothingType.split(":")[0] + "." + clothingType.split(":")[1]);
        if (player.getData(ClothingAttachments.CLOTHING_DATA).containsKey(clothingType)) {
            typeName = typeName.copy().withStyle(ChatFormatting.UNDERLINE);
        }
        AbstractWidget.renderScrollingString(guiGraphics, Minecraft.getInstance().font, typeName, itemX+2, itemY+2, itemX + itemWidth - 2, itemY + 14, 0xFFFFFFFF);
    }

    @Override
    public List<GridSelectionClothingType> sortItems(List<GridSelectionClothingType> oldItems) {
        List<GridSelectionClothingType> items = new ArrayList<>(oldItems);
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return items;
        Map<String, String> types = player.getData(ClothingAttachments.CLOTHING_DATA);
        List<String> activeTypes = types.keySet().stream().toList();
        items.sort((item1, item2) -> {
            boolean item1Active = activeTypes.contains(item1.clothingType());
            boolean item2Active = activeTypes.contains(item2.clothingType());

            if (item1Active && !item2Active) {
                return -1; // item1 is active, item2 is not, so item1 comes first
            } else if (!item1Active && item2Active) {
                return 1; // item2 is active, item1 is not, so item2 comes first
            } else {
                return item1.clothingType().split(":")[1].compareTo(item2.clothingType().split(":")[1]); // both are either active or inactive, sort alphabetically
            }
        });
        return items;
    }

    public String clothingType() {
        return clothingType;
    }
}
