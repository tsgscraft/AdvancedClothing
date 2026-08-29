package de.tsgscraft.advancedclothing.client.anchor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Anchors {

    private static final List<ClothingAnchor> ANCHORS = new ArrayList<>();

    // TODO: Implement the other anchors (body, left_arm, right_arm, left_leg, right_leg)
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

    public static void transformAnchor(
            PoseStack poseStack,
            HumanoidModel<?> model,
            Player player,
            ClothingAnchorInfo info
    ) {
        ClothingAnchor anchor = getAnchor(info);
        if (anchor != null) {
            anchor.transform(poseStack, model, player, info);
        }
    }
}
