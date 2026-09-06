package de.tsgscraft.advancedclothing.client.anchor.anchors;

import com.mojang.blaze3d.vertex.PoseStack;
import de.tsgscraft.advancedclothing.client.anchor.ClothingAnchor;
import de.tsgscraft.advancedclothing.client.anchor.ClothingAnchorInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.joml.Quaternionf;

public class BodyAnchor extends ClothingAnchor {
    @Override
    public ResourceLocation getAnchor() {
        return ResourceLocation.parse("minecraft:body");
    }

    @Override
    public String renderKey() {
        return "generic";
    }

    @Override
    public void transform(PoseStack poseStack, HumanoidModel<?> model, Player player, ClothingAnchorInfo info) {
        ModelPart body = model.body;
        poseStack.translate(0, 0.75f, 0);
        body.translateAndRotate(poseStack);
    }

    @Override
    public void transformForInventory(PoseStack poseStack, HumanoidModel<?> model, Player player, ClothingAnchorInfo info) {
        poseStack.mulPose(new Quaternionf().rotateXYZ(0, (float) Math.toRadians(player.yBodyRot), 0));
        ModelPart body = model.body;
        poseStack.translate(0, 0.75f, 0);
        body.translateAndRotate(poseStack);
    }

    @Override
    public boolean isEnabled(Player player, HumanoidModel<?> model, ClothingAnchorInfo info) {
        if (player instanceof AbstractClientPlayer player1) {
            PlayerRenderer renderer = (PlayerRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player1);
            return renderer.getModel().body.visible;
        }
        return true;
    }
}
