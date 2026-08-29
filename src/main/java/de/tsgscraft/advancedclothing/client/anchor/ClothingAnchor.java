package de.tsgscraft.advancedclothing.client.anchor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public abstract class ClothingAnchor {

    public abstract ResourceLocation getAnchor();

    public abstract String renderKey();

    public abstract void transform(
            PoseStack poseStack,
            HumanoidModel<?> model,
            Player player,
            ClothingAnchorInfo info
    );

    public abstract void transformForInventory(
            PoseStack poseStack,
            HumanoidModel<?> model,
            Player player,
            ClothingAnchorInfo info
    );
}
