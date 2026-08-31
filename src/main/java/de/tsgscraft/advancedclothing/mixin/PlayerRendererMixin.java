package de.tsgscraft.advancedclothing.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import de.tsgscraft.advancedclothing.Config;
import de.tsgscraft.advancedclothing.REFERENCE;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public PlayerRendererMixin(EntityRendererProvider.Context context, PlayerModel<AbstractClientPlayer> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Shadow
    private void setModelProperties(AbstractClientPlayer p_117775_) {}

    @ModifyVariable(
            method = "renderHand",
            at = @At("STORE"),
            ordinal = 0
    )
    private ResourceLocation modifyHandTexture(
            ResourceLocation original,
            @Local(argsOnly = true) AbstractClientPlayer player
    ) {
        if (Config.customSkin && REFERENCE.isCurrentPlayer(player.getUUID())) {
            return REFERENCE.customSkin;
        }

        return original;
    }

    @Inject(
            method = "getTextureLocation(Lnet/minecraft/client/player/AbstractClientPlayer;)Lnet/minecraft/resources/ResourceLocation;",
            at = @At("TAIL"),
            cancellable = true
    )
    public void getTextureLocation(AbstractClientPlayer player, CallbackInfoReturnable<ResourceLocation> cir) {
        if (Config.customSkin && REFERENCE.isCurrentPlayer(player.getUUID())) {
            cir.setReturnValue(REFERENCE.customSkin);
            cir.cancel();
        }
    }
}
