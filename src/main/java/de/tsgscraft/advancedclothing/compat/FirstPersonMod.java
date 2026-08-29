package de.tsgscraft.advancedclothing.compat;

import dev.tr7zw.firstperson.FirstPersonModelCore;

public class FirstPersonMod {
    public static boolean shouldSkipHead() {
        return FirstPersonModelCore.instance.isRenderingPlayer();
    }
}