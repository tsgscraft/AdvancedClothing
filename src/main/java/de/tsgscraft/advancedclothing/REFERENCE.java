package de.tsgscraft.advancedclothing;

import de.tsgscraft.advancedclothing.compat.FirstPersonMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModList;

import java.util.UUID;

public class REFERENCE {

    // Main mod (advancedclothing)
    public static final String MODID = "advancedclothing";
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


    // Utility method to check if the given UUID is the current player's UUID
    public static boolean isCurrentPlayer(UUID uuid) {
        return uuid.equals(Minecraft.getInstance().player.getUUID());
    }
}
