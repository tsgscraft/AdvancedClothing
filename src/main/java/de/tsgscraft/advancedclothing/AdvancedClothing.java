package de.tsgscraft.advancedclothing;

import com.mojang.logging.LogUtils;
import de.tsgscraft.advancedclothing.attachments.ClothingAttachments;
import de.tsgscraft.advancedclothing.client.ClothingRegistry;
import de.tsgscraft.advancedclothing.client.anchor.Anchors;
import de.tsgscraft.advancedclothing.client.anchor.anchors.*;
import de.tsgscraft.advancedclothing.client.events.ClientGameEvents;
import de.tsgscraft.advancedclothing.client.events.NeoForgeEvents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(REFERENCE.MODID)
public class AdvancedClothing {

    public static final Logger LOGGER = LogUtils.getLogger();

    private static IEventBus modEventBus;

    private static ClothingRegistry clothingRegistry;

    public AdvancedClothing(IEventBus modEventBus, ModContainer modContainer) {
        ClothingAttachments.ATTACHMENTS.register(modEventBus);

        AdvancedClothing.modEventBus = modEventBus;

        Anchors.registerAnchor(new HeadAnchor());
        Anchors.registerAnchor(new BodyAnchor());
        Anchors.registerAnchor(new LeftArmAnchor());
        Anchors.registerAnchor(new RightArmAnchor());
        Anchors.registerAnchor(new LeftLegAnchor());
        Anchors.registerAnchor(new RightLegAnchor());
        Anchors.registerAnchor(new LBoobAnchor());
        Anchors.registerAnchor(new RBoobAnchor());

        NeoForge.EVENT_BUS.register(NeoForgeEvents.class);
        modEventBus.register(ClientGameEvents.class);

        modContainer.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
    }

    public static ClothingRegistry getClothingRegistry() {
        if (clothingRegistry == null) {
            clothingRegistry = new ClothingRegistry();
        }
        return clothingRegistry;
    }
}
