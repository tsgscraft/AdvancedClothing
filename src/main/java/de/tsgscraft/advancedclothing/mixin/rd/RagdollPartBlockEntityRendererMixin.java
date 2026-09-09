package de.tsgscraft.advancedclothing.mixin.rd;

import com.mojang.blaze3d.vertex.PoseStack;
import de.tsgscraft.advancedclothing.Config;
import de.tsgscraft.advancedclothing.REFERENCE;
import de.tsgscraft.advancedclothing.client.modifiers.ClothingModifiers;
import de.tsgscraft.advancedclothing.client.render.AnchorLayerRender;
import dev.leo.sableplayerragdoll.block.entity.RagdollPartBlockEntity;
import dev.leo.sableplayerragdoll.entity.RagdollDollEntity;
import dev.leo.sableplayerragdoll.neoforge.mixin.LivingEntityRendererAccessor;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.UUID;

@Mixin(targets = "dev/leo/sableplayerragdoll/neoforge/client/RagdollPartBlockEntityRenderer")
public abstract class RagdollPartBlockEntityRendererMixin {

    @Shadow
    private PlayerModel<RagdollDollEntity> model;

    @Shadow
    protected abstract LivingEntity getRenderEntity(RagdollPartBlockEntity blockEntity);

    @Unique
    private UUID advancedClothing$currentPlayerUUID;

    @Inject(
            method = "render(Ldev/leo/sableplayerragdoll/block/entity/RagdollPartBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",
            at = @At("HEAD")
    )
    private void captureUuid(
            RagdollPartBlockEntity blockEntity,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            int packedOverlay,
            CallbackInfo ci
    ) {
        advancedClothing$currentPlayerUUID = blockEntity.skinProfile().getId();
    }

    @ModifyArg(
            method = "renderLayers",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/RenderType;entityTranslucent(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;"
            ),
            index = 0
    )
    private ResourceLocation modifyTexture(
            ResourceLocation original
    ) {
        if (Config.debugSkinTexture) {
            return REFERENCE.debugSkin;
        }
        if (REFERENCE.isCurrentPlayer(advancedClothing$currentPlayerUUID) && Config.customSkin) {
            return REFERENCE.customSkin;
        }
        return original;
    }

    @Redirect(
            method = "renderLayers",
            at = @At(
                    value = "INVOKE",
                    target = "Ldev/leo/sableplayerragdoll/neoforge/mixin/LivingEntityRendererAccessor;getLayers()Ljava/util/List;"
            )
    )
    private List<RenderLayer<?, ?>> redirectLayerRender(
            LivingEntityRendererAccessor instance,
            RagdollPartBlockEntity blockEntity,
            RagdollPartBlockEntity.BodyPart bodyPart
    ) {
        List<RenderLayer<?, ?>> renderLayers = instance.getLayers();
        for (RenderLayer<?, ?> layer : instance.getLayers()) {
            if (layer.getClass().getName().equals("de.tsgscraft.advancedclothing.client.render.AnchorLayerRender")) {
                break;
            }
        }
        return renderLayers;
    }

    @Inject(
            method = "renderLayers",
            at = @At("HEAD")
    )
    private void layerStart(
            RagdollPartBlockEntity blockEntity, RagdollPartBlockEntity.BodyPart bodyPart, LivingEntity entity, PoseStack poseStack, MultiBufferSource buffer, int packedLight, float partialTick, CallbackInfo ci
    ) {
        ClothingModifiers.configureRagdollSecondLayer(model, advancedClothing$currentPlayerUUID, bodyPart);
        AnchorLayerRender.notRagdoll = false;
        AnchorLayerRender.currentRagdollPart = bodyPart;
    }

    @Inject(
            method = "renderLayers",
            at = @At("RETURN")
    )
    private void layerEnd(
            RagdollPartBlockEntity blockEntity, RagdollPartBlockEntity.BodyPart bodyPart, LivingEntity entity, PoseStack poseStack, MultiBufferSource buffer, int packedLight, float partialTick, CallbackInfo ci
    ) {
        AnchorLayerRender.notRagdoll = true;
    }
}
