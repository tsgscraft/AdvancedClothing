package de.tsgscraft.advancedclothing.client.screen;

import de.tsgscraft.advancedclothing.client.ClothingRegistry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class ClothingSelectionScreen extends Screen {

    //TODO: Add a back button to go back to the previous screen
    //TODO: Add a button to clear the current clothing type
    //TODO: Highlight the selected clothing items
    //TODO: Add a keybinding to open the clothing selection screen
    //TODO: Sort the active clothing types at the top of the list

    private GridSelectionWidget<GridSelectionClothingType> gridSelectionTypeWidget;
    GridSelectionWidget<GridSelectionClothingElement> gridSelectionItemWidget;

    private List<GridSelectionWidget<?>> gridSelectionWidgets;

    public boolean selectedType = false;

    public ClothingSelectionScreen() {
        super(Component.literal("Clothing Selection"));
    }

    @Override
    protected void init() {
        this.gridSelectionTypeWidget = new GridSelectionWidget<>(this.width / 2 - 25, this.height / 2 - 100, 150, 200, 3, Component.literal("Clothing Selection"), this);
        this.gridSelectionItemWidget = new GridSelectionWidget<>(this.width / 2 - 25, this.height / 2 - 100, 150, 200, 3, Component.literal("Clothing Items"), this);
        this.gridSelectionWidgets = List.of(this.gridSelectionTypeWidget, this.gridSelectionItemWidget);
        List<String> type = ClothingRegistry.getInstance().getClothingTypes();
        List<GridSelectionClothingType> clothingTypes = type.stream().map(GridSelectionClothingType::new).toList();
        this.gridSelectionTypeWidget.setItems(clothingTypes);
        this.addRenderableWidget(this.gridSelectionTypeWidget);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        int x = this.width / 2;
        int y = this.height / 2;

        if (minecraft != null && minecraft.level != null) {
            Player ent = minecraft.level.getPlayerByUUID(minecraft.player.getUUID());
            if (ent != null) {
                InventoryScreen.renderEntityInInventoryFollowsMouse(guiGraphics, x - 164, y - 80, x, y + 80, 45, 0, mouseX, mouseY, ent);
            }
        }
    }

    public void setActive(GridSelectionWidget<GridSelectionClothingElement> gridSelectionItemWidget) {
        gridSelectionWidgets.forEach(this::removeWidget);
        addRenderableWidget(gridSelectionItemWidget);
        this.gridSelectionItemWidget = gridSelectionItemWidget;
    }
}
