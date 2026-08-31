package de.tsgscraft.advancedclothing.client.events;

import de.tsgscraft.advancedclothing.Config;
import de.tsgscraft.advancedclothing.REFERENCE;
import de.tsgscraft.advancedclothing.attachments.ClothingAttachments;
import de.tsgscraft.advancedclothing.client.ClothingCommand;
import de.tsgscraft.advancedclothing.client.ClothingElement;
import de.tsgscraft.advancedclothing.client.ClothingRegistry;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;

import java.util.List;
import java.util.Map;

@SuppressWarnings("removal")
@EventBusSubscriber(modid = REFERENCE.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class NeoForgeEvents {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
        if (!Config.enabled) return;
        PlayerModel<AbstractClientPlayer> model = event.getRenderer().getModel();
        if (Config.debugSkin)
            model.setAllVisible(false);
        Map<String, String> clothingData = event.getEntity().getData(ClothingAttachments.CLOTHING_DATA);

        List<ClothingElement> clothingElements = ClothingRegistry.getInstance().getClothingElements().stream()
                .filter(clothingElement -> clothingData.containsValue(clothingElement.id().toString()))
                .toList();

        clothingElements.forEach(clothingElement -> {
            if (clothingElement.modifiers() != null) {
                clothingElement.modifiers().configureDefaultSecondLayer(model);
            }
        });
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRenderPlayer(RenderPlayerEvent.Post event) {
        PlayerModel<AbstractClientPlayer> model = event.getRenderer().getModel();
        model.setAllVisible(true);
    }

    @SubscribeEvent
    public static void registerCommands(RegisterClientCommandsEvent event) {
        event.getDispatcher().register(ClothingCommand.Command);
    }
}