package de.tsgscraft.advancedclothing.mixin.fmg;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wildfire.render.WildfireModelRenderer;
import de.tsgscraft.advancedclothing.Config;
import de.tsgscraft.advancedclothing.REFERENCE;
import de.tsgscraft.advancedclothing.attachments.ClothingAttachments;
import de.tsgscraft.advancedclothing.client.ClothingElement;
import de.tsgscraft.advancedclothing.client.ClothingRegistry;
import de.tsgscraft.advancedclothing.client.modifiers.ModelPartModifiers;
import de.tsgscraft.advancedclothing.client.render.AnchorLayerRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.neoforge.client.ClientHooks;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;

import java.util.List;
import java.util.Map;

@Mixin(targets = "com.wildfire.render.GenderLayer")
public class GenderLayerMixin<ENTITY extends LivingEntity, MODEL extends HumanoidModel<ENTITY>> {

    /**
     * @author tsgscraft
     * @reason change skin
     */
    @Overwrite
    private @Nullable ResourceLocation getBreastTexture(ENTITY entity) {
        ResourceLocation var10000;
        if (entity instanceof AbstractClientPlayer player) {
            if (player.getUUID().equals(Minecraft.getInstance().player.getUUID()) && Config.customSkin) {
                return REFERENCE.customSkin;
            }
            var10000 = player.getSkin().texture();
        } else {
            var10000 = null;
        }

        return var10000;
    }

    @Final
    @Shadow
    private TextureAtlas armorTrimAtlas;

    @Final
    @Shadow
    private static void renderBox(WildfireModelRenderer.ModelBox model, PoseStack matrixStack, VertexConsumer bufferIn, int light, int overlay, int color) {}

    @Final
    @Shadow
    private void shiftForJacket(PoseStack matrixStack) {}
    @Shadow
    private WildfireModelRenderer.BreastModelBox lBreast;
    @Shadow
    private WildfireModelRenderer.BreastModelBox rBreast;


    @Final
    @Shadow
    private static WildfireModelRenderer.OverlayModelBox lBreastWear;
    @Final
    @Shadow
    private static WildfireModelRenderer.OverlayModelBox rBreastWear;


    @Final
    @Shadow
    private static WildfireModelRenderer.BreastModelBox lBoobArmor;
    @Final
    @Shadow
    private static WildfireModelRenderer.BreastModelBox rBoobArmor;

    /**
     * @author tsgscraft
     * @reason fix armor texture
     */
    @Overwrite
    private void renderBreast(ENTITY entity, ItemStack armorStack, PoseStack matrixStack, MultiBufferSource bufferSource, @Nullable RenderType breastRenderType, int light, int overlay, float alpha, boolean left, boolean hasJacketLayer) {
        if (Config.hideLeftBoob && left) {
            return;
        }
        if (Config.hideRightBoob && !left) {
            return;
        }
        if (breastRenderType != null) {
            //Only render the breasts if we have a render type for them
            VertexConsumer vertexConsumer = bufferSource.getBuffer(breastRenderType);
            int color = FastColor.ARGB32.color(FastColor.as8BitChannel(alpha), 0xFFFFFFFF);
            renderBox(left ? lBreast : rBreast, matrixStack, vertexConsumer, light, overlay, color);
            if (hasJacketLayer) {
                shiftForJacket(matrixStack);
                // Apply any modifier values for the breast wear
                Map<String, String> clothingData = entity.getData(ClothingAttachments.CLOTHING_DATA);

                List<ClothingElement> clothingElements = ClothingRegistry.getInstance().getClothingElements().stream()
                        .filter(clothingElement -> clothingData.containsValue(clothingElement.id().toString()))
                        .toList();

                PlayerRenderer renderer = (PlayerRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity);
                clothingElements.forEach(clothingElement -> {
                    if (clothingElement.renderInfo() != null) {
                        clothingElement.renderInfo().compile(
                                matrixStack,
                                bufferSource,
                                light,
                                overlay,
                                -1,
                                renderer.getModel(),
                                (AbstractClientPlayer) entity,
                                left ? "lboob" : "rboob",
                                false
                        );
                    }
                });
                Map<String, ModelPartModifiers> modifiers = AnchorLayerRender.playerModifiers.get(entity.getUUID());
                if (modifiers != null) {
                    ModelPartModifiers modifier = modifiers.get(left ? "lboob" : "rboob");
                    if (modifier != null) {
                        modifier.applyTo(matrixStack);
                    }
                }
                vertexConsumer = bufferSource.getBuffer(breastRenderType);
                renderBox(left ? lBreastWear : rBreastWear, matrixStack, vertexConsumer, light, overlay, color);
            }
        } else if (hasJacketLayer) {//Copy exact size
            shiftForJacket(matrixStack);
        }
        //TODO: Eventually we may want to expose a way via the API for mods to be able to override rendering
        // be it because they are not an armor item or the way they render their armor item is custom
        //Render Breast Armor
        if (!armorStack.isEmpty() && armorStack.getItem() instanceof ArmorItem armorItem) {
            matrixStack.pushPose();
            matrixStack.translate(left ? 0.001f : -0.001f, 0.015f, -0.015f);
            matrixStack.scale(1.05f, 1, 1);
            WildfireModelRenderer.BreastModelBox armor = left ? lBoobArmor : rBoobArmor;

            Holder<ArmorMaterial> material = armorItem.getMaterial();
            int color = armorStack.is(ItemTags.DYEABLE) ? DyedItemColor.getOrDefault(armorStack, 0xFFA06540) : 0xFFFFFFFF;
            for (ArmorMaterial.Layer layer : material.value().layers()) {
                ResourceLocation armorTexture = ClientHooks.getArmorTexture(entity, armorStack, layer, false, EquipmentSlot.CHEST);

                RenderType armorType = RenderType.armorCutoutNoCull(armorTexture);
                VertexConsumer armorVertexConsumer = bufferSource.getBuffer(armorType);
                renderBox(armor, matrixStack, armorVertexConsumer, light, OverlayTexture.NO_OVERLAY, layer.dyeable() ? color : 0xFFFFFFFF);
            }

            ArmorTrim trim = armorStack.get(DataComponents.TRIM);
            if (trim != null) {
                TextureAtlasSprite sprite = this.armorTrimAtlas.getSprite(trim.outerTexture(material));
                VertexConsumer trimVertexConsumer = sprite.wrap(bufferSource.getBuffer(Sheets.armorTrimsSheet(trim.pattern().value().decal())));
                renderBox(armor, matrixStack, trimVertexConsumer, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
            }

            if (armorStack.hasFoil()) {
                renderBox(armor, matrixStack, bufferSource.getBuffer(RenderType.armorEntityGlint()), light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
            }

            matrixStack.popPose();
        }
    }
    @Unique
    private void advancedClothing$oldRenderBreast(ENTITY entity, ItemStack armorStack, PoseStack matrixStack, MultiBufferSource bufferSource,
                                           @Nullable RenderType breastRenderType, int light, int overlay, float alpha, boolean left, boolean hasJacketLayer) {
        if (breastRenderType != null) {
            //Only render the breasts if we have a render type for them
            VertexConsumer vertexConsumer = bufferSource.getBuffer(breastRenderType);
            int color = FastColor.ARGB32.color(FastColor.as8BitChannel(alpha), 0xFFFFFFFF);
            renderBox(left ? lBreast : rBreast, matrixStack, vertexConsumer, light, overlay, color);
            if (hasJacketLayer) {
                shiftForJacket(matrixStack);
                renderBox(left ? lBreastWear : rBreastWear, matrixStack, vertexConsumer, light, overlay, color);
            }
        } else if (hasJacketLayer) {//Copy exact size
            shiftForJacket(matrixStack);
        }
        //TODO: Eventually we may want to expose a way via the API for mods to be able to override rendering
        // be it because they are not an armor item or the way they render their armor item is custom
        //Render Breast Armor
        if (!armorStack.isEmpty() && armorStack.getItem() instanceof ArmorItem armorItem) {
            matrixStack.pushPose();
            matrixStack.translate(left ? 0.001f : -0.001f, 0.015f, -0.015f);
            matrixStack.scale(1.05f, 1, 1);
            WildfireModelRenderer.BreastModelBox armor = left ? lBoobArmor : rBoobArmor;

            Holder<ArmorMaterial> material = armorItem.getMaterial();
            int color = armorStack.is(ItemTags.DYEABLE) ? DyedItemColor.getOrDefault(armorStack, 0xFFA06540) : 0xFFFFFFFF;
            for (ArmorMaterial.Layer layer : material.value().layers()) {
                ResourceLocation armorTexture = ClientHooks.getArmorTexture(entity, armorStack, layer, false, EquipmentSlot.CHEST);

                RenderType armorType = RenderType.armorCutoutNoCull(armorTexture);
                VertexConsumer armorVertexConsumer = bufferSource.getBuffer(armorType);
                renderBox(armor, matrixStack, armorVertexConsumer, light, OverlayTexture.NO_OVERLAY, layer.dyeable() ? color : 0xFFFFFFFF);
            }

            ArmorTrim trim = armorStack.get(DataComponents.TRIM);
            if (trim != null) {
                TextureAtlasSprite sprite = this.armorTrimAtlas.getSprite(trim.outerTexture(material));
                VertexConsumer trimVertexConsumer = sprite.wrap(bufferSource.getBuffer(Sheets.armorTrimsSheet(trim.pattern().value().decal())));
                renderBox(armor, matrixStack, trimVertexConsumer, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
            }

            if (armorStack.hasFoil()) {
                renderBox(armor, matrixStack, bufferSource.getBuffer(RenderType.armorEntityGlint()), light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
            }

            matrixStack.popPose();
        }
    }
}
