package de.tsgscraft.advancedclothing;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@SuppressWarnings("removal")
@EventBusSubscriber(modid = REFERENCE.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue CUSTOM_SKIN = BUILDER.comment("Whether to use a custom skin for the player (use a resource pack ('/textures/skin/own.png'))").define("customSkin", false);
    private static final ModConfigSpec.BooleanValue DEBUG = BUILDER.comment("Whether to enable debug mode").define("debug", false);
    private static final ModConfigSpec.BooleanValue DEBUG_SKIN = BUILDER.comment("Whether to hide the base").define("debugSkin", false);
    private static final ModConfigSpec.BooleanValue ONLY_BREASTS = BUILDER.comment("Hide everything but the Female Gender Mod Breast model").define("onlyBreasts", false);
    private static final ModConfigSpec.BooleanValue HIDE_LEFT_BOOB = BUILDER.comment("Hide the left breast").define("hideLeftBoob", false);
    private static final ModConfigSpec.BooleanValue HIDE_RIGHT_BOOB = BUILDER.comment("Hide the right breast").define("hideRightBoob", false);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean customSkin = false;
    public static boolean debug = false;
    public static boolean debugSkin = false;
    public static boolean onlyBreasts = false;
    public static boolean hideLeftBoob = false;
    public static boolean hideRightBoob = false;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        customSkin = CUSTOM_SKIN.get();
        debug = DEBUG.get();
        debugSkin = DEBUG_SKIN.get();
        onlyBreasts = ONLY_BREASTS.get();
        hideLeftBoob = HIDE_LEFT_BOOB.get();
        hideRightBoob = HIDE_RIGHT_BOOB.get();
    }
}
