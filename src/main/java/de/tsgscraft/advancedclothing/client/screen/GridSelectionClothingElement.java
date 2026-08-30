package de.tsgscraft.advancedclothing.client.screen;

import de.tsgscraft.advancedclothing.attachments.ClothingAttachments;
import de.tsgscraft.advancedclothing.client.ClothingElement;
import de.tsgscraft.advancedclothing.network.SetClothingPayload;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

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

        clothingElement.renderInfo().renderEntityInInventoryFollowsMouse(guiGraphics, itemX + 2, itemY + 16, itemX + itemWidth - 4, itemY + itemHeight - 4, 30, 0, mouseX, mouseY);

        Component name = Component.translatable(clothingElement.name());

        if (player.getData(ClothingAttachments.CLOTHING_DATA).containsValue(clothingElement.id().toString())) {
            name = name.copy().withStyle(ChatFormatting.UNDERLINE);
        }

        AbstractWidget.renderScrollingString(guiGraphics, Minecraft.getInstance().font, name, itemX+2, itemY+2, itemX + itemWidth - 2, itemY + 14, 0xFFFFFFFF);
    }

    @Override
    public List<GridSelectionClothingElement> sortItems(List<GridSelectionClothingElement> items) {
        return items;
    }

    public ClothingElement clothingElement() {
        return clothingElement;
    }
}
