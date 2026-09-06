package de.tsgscraft.advancedclothing.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.tsgscraft.advancedclothing.Config;
import de.tsgscraft.advancedclothing.REFERENCE;
import de.tsgscraft.advancedclothing.attachments.ClothingAttachments;
import de.tsgscraft.advancedclothing.client.ClothingElement;
import de.tsgscraft.advancedclothing.client.ClothingRegistry;
import de.tsgscraft.advancedclothing.client.events.ClientModEvents;
import de.tsgscraft.advancedclothing.client.modifiers.ModelPartModifiers;
import de.tsgscraft.advancedclothing.mixin.PlayerModelAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
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

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int light, AbstractClientPlayer entity, float limbSwing, float limbSwingAmount, float partialTicks, float age, float headYaw, float headPitch) {
        if (!Config.enabled) return;
        PlayerModel<AbstractClientPlayer> parent = this.getParentModel();
        parent.copyPropertiesTo(layer);
        layer.swimAmount = parent.swimAmount;
        layer.setupAnim(
                entity,
                limbSwing,
                limbSwingAmount,
                age,
                headYaw,
                headPitch
        );

        Map<String, String> clothingData = entity.getData(ClothingAttachments.CLOTHING_DATA);

        List<ClothingElement> clothingElements = ClothingRegistry.getInstance().getClothingElements().stream()
                .filter(clothingElement -> clothingData.containsValue(clothingElement.id().toString()))
                .toList();

        layer.setAllVisible(false);
        int overlay = LivingEntityRenderer.getOverlayCoords(entity, 0.0F);

        clothingElements.forEach(clothingElement -> {
            if (clothingElement.renderInfo() != null) {
                clothingElement.renderInfo().compile(
                        poseStack,
                        buffer,
                        light,
                        overlay,
                        -1,
                        layer,
                        entity,
                        "generic",
                        false
                );
            }
            Map<String, ModelPartModifiers> extraModifiers = new HashMap<>();
            boolean headBefore = layer.hat.visible;
            if (clothingElement.modifiers() != null) {
                clothingElement.modifiers().configureSecondLayer(layer, extraModifiers);
            }
            if (Config.debugModifiers)
                entity.sendSystemMessage(Component.literal("headBefore: " + headBefore + " | headAfter: " + layer.hat.visible));
            playerModifiers.put(entity.getUUID(), extraModifiers);
        });

        if (Config.onlyBreasts) {
            return;
        }

        // Render the anchor layer itself (as a smaller secondLayer)
        VertexConsumer vertexConsumer = buffer.getBuffer(
                RenderType.entityCutoutNoCull((Config.customSkin && REFERENCE.isCurrentPlayer(entity.getUUID())) ? REFERENCE.customSkin : entity.getSkin().texture())
        );

        if (((PlayerModelAccessor) parent).isSlim()) {
            layer.copyPropertiesTo(slimLayer);
            copyVisibility(layer, slimLayer);
            slimLayer.swimAmount = layer.swimAmount;
            slimLayer.setupAnim(
                    entity,
                    limbSwing,
                    limbSwingAmount,
                    age,
                    headYaw,
                    headPitch
            );

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
}
