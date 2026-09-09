package de.tsgscraft.advancedclothing.client.anchor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Anchors {

    private static final List<ClothingAnchor> ANCHORS = new ArrayList<>();

    public static ClothingAnchor getAnchor(ClothingAnchorInfo anchorInfo) {
        for (ClothingAnchor anchor : ANCHORS) {
            if (anchor.getAnchor().toString().equals(anchorInfo.getAnchor().toString())) {
                return anchor;
            }
        }
        return null;
    }

    public static void registerAnchor(ClothingAnchor anchor) {
        ANCHORS.add(anchor);
    }
}
