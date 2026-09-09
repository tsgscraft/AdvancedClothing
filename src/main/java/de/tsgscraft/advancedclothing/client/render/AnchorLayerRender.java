package de.tsgscraft.advancedclothing.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.tsgscraft.advancedclothing.Config;
import de.tsgscraft.advancedclothing.REFERENCE;
import de.tsgscraft.advancedclothing.attachments.ClothingAttachments;
import de.tsgscraft.advancedclothing.client.ClothingElement;
import de.tsgscraft.advancedclothing.client.ClothingRegistry;
import de.tsgscraft.advancedclothing.client.events.ClientModEvents;
import de.tsgscraft.advancedclothing.client.modifiers.ClothingModifiers;
import de.tsgscraft.advancedclothing.client.modifiers.ModelPartModifiers;
import de.tsgscraft.advancedclothing.mixin.PlayerModelAccessor;
import dev.leo.sableplayerragdoll.block.entity.RagdollPartBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AnchorLayerRender extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private final AnchorLayer layer;
    private final AnchorLayer slimLayer;

    public static final Map<UUID, Map<String, ModelPartModifiers>> playerModifiers = new HashMap<>();

    public AnchorLayerRender(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer, EntityModelSet modelSet) {
        super(renderer);

        layer = new AnchorLayer(
                modelSet.bakeLayer(ClientModEvents.ANCHOR_LAYER),
                false
        );

        slimLayer = new AnchorLayer(
                modelSet.bakeLayer(ClientModEvents.SLIM_ARMOR_LAYER),
                true
        );
    }

    public static boolean notRagdoll = true;
    public static RagdollPartBlockEntity.BodyPart currentRagdollPart = null;

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int light, AbstractClientPlayer entity, float limbSwing, float limbSwingAmount, float partialTicks, float age, float headYaw, float headPitch) {
        if (!Config.enabled) return;

        PlayerModel<AbstractClientPlayer> parent = this.getParentModel();
        layer.setAllVisible(false);
        if (notRagdoll) {
            layer.swimAmount = parent.swimAmount;
            parent.copyPropertiesTo(layer);
            layer.setupAnim(
                    entity,
                    limbSwing,
                    limbSwingAmount,
                    age,
                    headYaw,
                    headPitch
            );
            ClothingModifiers.configureSecondLayer(layer, entity.getUUID());
        }else {
            stopAnim(layer);
            ClothingModifiers.configureSecondLayer(layer, entity.getUUID());
            if (currentRagdollPart != null) {
                if (layer.hat.visible) layer.hat.visible = currentRagdollPart == RagdollPartBlockEntity.BodyPart.HEAD;
                if (layer.jacket.visible) layer.jacket.visible = currentRagdollPart == RagdollPartBlockEntity.BodyPart.TORSO;
                if (layer.leftSleeve.visible) layer.leftSleeve.visible = currentRagdollPart == RagdollPartBlockEntity.BodyPart.LEFT_ARM;
                if (layer.rightSleeve.visible) layer.rightSleeve.visible = currentRagdollPart == RagdollPartBlockEntity.BodyPart.RIGHT_ARM;
                if (layer.leftPants.visible) layer.leftPants.visible = currentRagdollPart == RagdollPartBlockEntity.BodyPart.LEFT_LEG;
                if (layer.rightPants.visible) layer.rightPants.visible = currentRagdollPart == RagdollPartBlockEntity.BodyPart.RIGHT_LEG;
            }
        }

        Map<String, String> clothingData = entity.getData(ClothingAttachments.CLOTHING_DATA);

        List<ClothingElement> clothingElements = ClothingRegistry.getInstance().getClothingElements().stream()
                .filter(clothingElement -> clothingData.containsValue(clothingElement.id().toString()))
                .toList();

        int overlay = LivingEntityRenderer.getOverlayCoords(entity, 0.0F);

        clothingElements.forEach(clothingElement -> {
            if (clothingElement.renderInfo() != null) {
                clothingElement.renderInfo().compile(
                        poseStack,
                        buffer,
                        light,
                        overlay,
                        -1,
                        parent,
                        layer,
                        entity,
                        "generic",
                        false
                );
            }
            /*
            Map<String, ModelPartModifiers> extraModifiers = new HashMap<>();
            boolean headBefore = layer.hat.visible;
            boolean bodyBefore = layer.jacket.visible;
            boolean leftArmBefore = layer.leftSleeve.visible;
            boolean rightArmBefore = layer.rightSleeve.visible;
            boolean leftLegBefore = layer.leftPants.visible;
            boolean rightLegBefore = layer.rightPants.visible;
            if (clothingElement.modifiers() != null) {
                clothingElement.modifiers().configureSecondLayer(layer, parent, extraModifiers, entity.getUUID());
            }
            if (Config.debugModifiers) {
                entity.sendSystemMessage(Component.literal("-------------------------------------------"));
                entity.sendSystemMessage(Component.literal("headBefore: " + headBefore + " | headAfter: " + layer.hat.visible));
                entity.sendSystemMessage(Component.literal("bodyBefore: " + bodyBefore + " | bodyAfter: " + layer.jacket.visible));
                entity.sendSystemMessage(Component.literal("leftArmBefore: " + leftArmBefore + " | leftArmAfter: " + layer.leftSleeve.visible));
                entity.sendSystemMessage(Component.literal("rightArmBefore: " + rightArmBefore + " | rightArmAfter: " + layer.rightSleeve.visible));
                entity.sendSystemMessage(Component.literal("leftLegBefore: " + leftLegBefore + " | leftLegAfter: " + layer.leftPants.visible));
                entity.sendSystemMessage(Component.literal("rightLegBefore: " + rightLegBefore + " | rightLegAfter: " + layer.rightPants.visible));
            }
            playerModifiers.put(entity.getUUID(), extraModifiers);
             */
        });

        if (Config.onlyBreasts) {
            return;
        }

        // Render the anchor layer itself (as a smaller secondLayer)
        VertexConsumer vertexConsumer = buffer.getBuffer(
                RenderType.entityCutoutNoCull(Config.debugSkinTexture ? REFERENCE.debugSkin : (Config.customSkin && REFERENCE.isCurrentPlayer(entity.getUUID())) ? REFERENCE.customSkin : entity.getSkin().texture())
        );

        if (((PlayerModelAccessor) parent).isSlim()) {
            copyVisibility(layer, slimLayer);
            if (notRagdoll) {
                layer.copyPropertiesTo(slimLayer);
                slimLayer.swimAmount = layer.swimAmount;
                slimLayer.setupAnim(
                        entity,
                        limbSwing,
                        limbSwingAmount,
                        age,
                        headYaw,
                        headPitch
                );
            }else {
                stopAnim(slimLayer);
            }
            checkFirstPerson(entity, slimLayer);
            slimLayer.renderToBuffer(
                    poseStack,
                    vertexConsumer,
                    light,
                    overlay
            );
        } else {
            checkFirstPerson(entity, layer);
            layer.renderToBuffer(
                    poseStack,
                    vertexConsumer,
                    light,
                    overlay
            );
        }
    }

    public static void checkFirstPerson(AbstractClientPlayer player, AnchorLayer model) {
        REFERENCE.updateFirstPerson();
        if (Config.debugFirstPerson) {
            player.sendSystemMessage(
                    Component.literal("player: " + player.getName().getString() + " | skipHead: " + REFERENCE.skipHead)
            );
        }
        if (Minecraft.getInstance().player == player) {
            if (REFERENCE.skipHead) {
                model.head.visible = false;
                model.hat.visible = false;
            }
        }
    }

    private void copyVisibility(AnchorLayer source, AnchorLayer target) {
        target.head.visible = source.head.visible;
        target.hat.visible = source.hat.visible;
        target.body.visible = source.body.visible;
        target.jacket.visible = source.jacket.visible;
        target.leftArm.visible = source.leftArm.visible;
        target.leftSleeve.visible = source.leftSleeve.visible;
        target.rightArm.visible = source.rightArm.visible;
        target.rightSleeve.visible = source.rightSleeve.visible;
        target.leftLeg.visible = source.leftLeg.visible;
        target.leftPants.visible = source.leftPants.visible;
        target.rightLeg.visible = source.rightLeg.visible;
        target.rightPants.visible = source.rightPants.visible;
    }

    private void stopAnim(AnchorLayer model) {
        stopAnim(model.head, 0);
        stopAnim(model.hat, 1);
        stopAnim(model.body, 2);
        stopAnim(model.jacket, 3);
        stopAnim(model.leftArm, 4);
        stopAnim(model.leftSleeve, 5);
        stopAnim(model.rightArm, 6);
        stopAnim(model.rightSleeve, 7);
        stopAnim(model.leftLeg, 8);
        stopAnim(model.leftPants, 9);
        stopAnim(model.rightLeg, 10);
        stopAnim(model.rightPants, 11);
    }

    private void stopAnim(ModelPart part, int partId) {
        part.xRot = 0.0F;
        part.yRot = 0.0F;
        part.zRot = 0.0F;
        part.x = 0.0F;
        part.y = 0.0F;
        part.z = 0.0F;
        switch (partId) {
            case 0, 1, 2, 3 -> part.setPos(0.0F, 0.0F, 0.0F);
            case 4, 5 -> part.setPos(5.0F, 2.5F, 0.0F);
            case 6, 7 -> part.setPos(-5.0F, 2.5F, 0.0F);
            case 8, 9 -> part.setPos(1.9F, 12.0F, 0);
            case 10, 11 -> part.setPos(-1.9F, 12.0F, 0);
        }
    }
}
