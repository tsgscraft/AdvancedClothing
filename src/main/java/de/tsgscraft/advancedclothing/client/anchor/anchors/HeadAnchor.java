package de.tsgscraft.advancedclothing.client.anchor.anchors;

import com.mojang.blaze3d.vertex.PoseStack;
import de.tsgscraft.advancedclothing.client.anchor.ClothingAnchor;
import de.tsgscraft.advancedclothing.client.anchor.ClothingAnchorInfo;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.joml.Quaternionf;

public class HeadAnchor extends ClothingAnchor {
    @Override
    public ResourceLocation getAnchor() {
        return ResourceLocation.parse("minecraft:head");
    }

    @Override
    public String renderKey() {
        return "generic";
    }

    @Override
    public void transform(PoseStack poseStack, HumanoidModel<?> model, Player player, ClothingAnchorInfo info) {
        ModelPart head = model.head;
        head.translateAndRotate(poseStack);
    }

    @Override
    public void transformForInventory(PoseStack poseStack, HumanoidModel<?> model, Player player, ClothingAnchorInfo info) {
        poseStack.mulPose(new Quaternionf().rotateXYZ((float) Math.toRadians(-player.getXRot()), (float) Math.toRadians(player.yHeadRot), 0));
        ModelPart head = model.head;
        poseStack.translate(head.x / 16.0F, head.y / 16.0F, head.z / 16.0F);
        poseStack.scale(head.xScale, head.yScale, head.zScale);
    }
}
