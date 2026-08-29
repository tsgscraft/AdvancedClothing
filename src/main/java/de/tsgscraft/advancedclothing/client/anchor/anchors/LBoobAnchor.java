package de.tsgscraft.advancedclothing.client.anchor.anchors;

import com.mojang.blaze3d.vertex.PoseStack;
import de.tsgscraft.advancedclothing.client.anchor.ClothingAnchor;
import de.tsgscraft.advancedclothing.client.anchor.ClothingAnchorInfo;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

// FemaleGenderMod Anchor for the left boob
public class LBoobAnchor extends ClothingAnchor {
    @Override
    public ResourceLocation getAnchor() {
        return ResourceLocation.parse("wildfire_gender:lboob");
    }

    @Override
    public String renderKey() {
        return "lboob";
    }

    @Override
    public void transform(PoseStack poseStack, HumanoidModel<?> model, Player player, ClothingAnchorInfo info) {

    }

    @Override
    public void transformForInventory(PoseStack poseStack, HumanoidModel<?> model, Player player, ClothingAnchorInfo info) {

    }
}
