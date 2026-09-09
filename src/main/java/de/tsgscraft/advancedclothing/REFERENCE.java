package de.tsgscraft.advancedclothing;

import de.tsgscraft.advancedclothing.compat.FirstPersonMod;
import de.tsgscraft.advancedclothing.compat.RagdollMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class REFERENCE {

    // Main mod (advancedclothing)
    public static final String MODID = "advancedclothing";
    public static ResourceLocation debugSkin = ResourceLocation.parse("advancedclothing:textures/entity/player_debug.png");
    public static ResourceLocation customSkin = ResourceLocation.parse("advancedclothing:textures/entity/player.png");





    // Female Gender Mod (fmg)
    public static final String fmg_MODID = "wildfire_gender";





    // First Person Mod (firstperson)
    public static final String fp_MODID = "firstperson";

    public static boolean skipHead = false;
    public static void updateFirstPerson() {
        skipHead = false;
        LocalPlayer player = Minecraft.getInstance().player;

        if (ModList.get().isLoaded(fp_MODID)) {
            skipHead = FirstPersonMod.shouldSkipHead();
            if (Config.debugFirstPerson)
                player.sendSystemMessage(Component.literal("First Person Mod detected. Skip head: " + skipHead));
        }else if (Config.debugFirstPerson)
            player.sendSystemMessage(Component.literal("First Person Mod not found."));

    }





    // Ragdoll (sable_player_ragdoll)
    public static final String rd_MODID = "sable_player_ragdoll";
    public static final Map<UUID, Boolean> playerRagdollStates = new HashMap<>();

    public static boolean isPlayerRagdoll(ServerPlayer player) {
        if (ModList.get().isLoaded(rd_MODID)) {
            return RagdollMod.isPlayerRagdoll(player);
        }
        return false;
    }

    public static boolean isPlayerRagdoll(Player player) {
        if (ModList.get().isLoaded(rd_MODID)) {
            return playerRagdollStates.getOrDefault(player.getUUID(), false);
        }
        return false;
    }





    // Utility method to check if the given UUID is the current player's UUID
    public static boolean isCurrentPlayer(UUID uuid) {
        return uuid.equals(Minecraft.getInstance().player.getUUID());
    }

    public static void updateRagdollPlayers(MinecraftServer server) {
        if (ModList.get().isLoaded(rd_MODID)) {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                boolean isRagdoll = RagdollMod.isPlayerRagdoll(player);
                playerRagdollStates.put(player.getUUID(), isRagdoll);
            }
        }
    }
}
