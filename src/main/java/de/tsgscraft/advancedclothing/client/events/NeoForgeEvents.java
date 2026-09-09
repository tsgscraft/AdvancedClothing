package de.tsgscraft.advancedclothing.client.events;

import de.tsgscraft.advancedclothing.Config;
import de.tsgscraft.advancedclothing.REFERENCE;
import de.tsgscraft.advancedclothing.attachments.ClothingAttachments;
import de.tsgscraft.advancedclothing.client.ClothingCommand;
import de.tsgscraft.advancedclothing.client.ClothingElement;
import de.tsgscraft.advancedclothing.client.ClothingRegistry;
import de.tsgscraft.advancedclothing.client.modifiers.ClothingModifiers;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

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
        ClothingModifiers.configureDefaultSecondLayer(model, event.getEntity().getUUID());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRenderPlayer(RenderPlayerEvent.Post event) {
        PlayerModel<AbstractClientPlayer> model = event.getRenderer().getModel();
        model.setAllVisible(true);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        if (!Config.enabled) return;

        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.level == null) return;

        for (Player player : minecraft.level.players()) {
            ClothingModifiers.applyModifiersToPlayer(player);
        }
    }

    @SubscribeEvent
    public static void registerCommands(RegisterClientCommandsEvent event) {
        event.getDispatcher().register(ClothingCommand.Command);
    }

    @SubscribeEvent
    public static void serverTick(ServerTickEvent.Pre event) {
        if (event.getServer().getTickCount() % 2 == 0) {
            REFERENCE.updateRagdollPlayers(event.getServer());
        }
    }
}