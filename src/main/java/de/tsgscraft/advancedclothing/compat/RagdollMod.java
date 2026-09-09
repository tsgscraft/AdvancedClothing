package de.tsgscraft.advancedclothing.compat;

import dev.leo.sableplayerragdoll.api.RagdollAPI;
import net.minecraft.server.level.ServerPlayer;

public class RagdollMod {
    public static boolean isPlayerRagdoll(ServerPlayer player) {
        return RagdollAPI.isRagdolled(player);
    }
}
