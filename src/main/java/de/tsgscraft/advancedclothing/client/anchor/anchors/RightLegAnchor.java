package de.tsgscraft.advancedclothing.client.anchor.anchors;

import com.mojang.blaze3d.vertex.PoseStack;
import de.tsgscraft.advancedclothing.client.anchor.ClothingAnchor;
import de.tsgscraft.advancedclothing.client.anchor.ClothingAnchorInfo;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.joml.Quaternionf;

public class RightLegAnchor extends ClothingAnchor {
    @Override
    public ResourceLocation getAnchor() {
        return ResourceLocation.parse("minecraft:right_leg");
    }

    @Override
    public String renderKey() {
        return "generic";
    }

    @Override
    public void transform(PoseStack poseStack, HumanoidModel<?> model, Player player, ClothingAnchorInfo info) {
        ModelPart rightLeg = model.rightLeg;
        rightLeg.translateAndRotate(poseStack);
        poseStack.translate(0, 0.75f, 0);
    }

    @Override
    public void transformForInventory(PoseStack poseStack, HumanoidModel<?> model, Player player, ClothingAnchorInfo info) {
        poseStack.mulPose(new Quaternionf().rotateXYZ(0, (float) Math.toRadians(player.yBodyRot), 0));
        ModelPart rightLeg = model.rightLeg;
        rightLeg.translateAndRotate(poseStack);
        poseStack.translate(0, 0.75f, 0);
    }
}
