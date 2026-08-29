package de.tsgscraft.advancedclothing.client.modifiers;

import com.google.gson.JsonObject;
import de.tsgscraft.advancedclothing.client.render.AnchorLayer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;

import java.util.Map;

public class ClothingModifiers {

    private ModelPartModifiers secondLayerHeadModifier;
    private ModelPartModifiers secondLayerBodyModifier;
    private ModelPartModifiers secondLayerLeftArmModifier;
    private ModelPartModifiers secondLayerRightArmModifier;
    private ModelPartModifiers secondLayerLeftLegModifier;
    private ModelPartModifiers secondLayerRightLegModifier;
    private ModelPartModifiers secondLayerLboobModifier;
    private ModelPartModifiers secondLayerRboobModifier;

    public ClothingModifiers(JsonObject jsonObject) {
        // Initialize the ClothingModifiers with the provided JSON object
        JsonObject secondLayerJson = jsonObject.getAsJsonObject("second_layer");
        if (secondLayerJson != null) {
            this.secondLayerHeadModifier = new ModelPartModifiers(secondLayerJson.has("head") ? secondLayerJson.getAsJsonObject("head") : null);
            this.secondLayerBodyModifier = new ModelPartModifiers(secondLayerJson.has("body") ? secondLayerJson.getAsJsonObject("body") : null);
            this.secondLayerLeftArmModifier = new ModelPartModifiers(secondLayerJson.has("left_arm") ? secondLayerJson.getAsJsonObject("left_arm") : null);
            this.secondLayerRightArmModifier = new ModelPartModifiers(secondLayerJson.has("right_arm") ? secondLayerJson.getAsJsonObject("right_arm") : null);
            this.secondLayerLeftLegModifier = new ModelPartModifiers(secondLayerJson.has("left_leg") ? secondLayerJson.getAsJsonObject("left_leg") : null);
            this.secondLayerRightLegModifier = new ModelPartModifiers(secondLayerJson.has("right_leg") ? secondLayerJson.getAsJsonObject("right_leg") : null);
            this.secondLayerLboobModifier = new ModelPartModifiers(secondLayerJson.has("lboob") ? secondLayerJson.getAsJsonObject("lboob") : null);
            this.secondLayerRboobModifier = new ModelPartModifiers(secondLayerJson.has("rboob") ? secondLayerJson.getAsJsonObject("rboob") : null);
        }
    }

    public void configureSecondLayer(AnchorLayer layer, Map<String, ModelPartModifiers> extraModifiers) {
        if (secondLayerHeadModifier != null) {
            secondLayerHeadModifier.applyTo(layer.hat);
        }
        if (secondLayerBodyModifier != null) {
            secondLayerBodyModifier.applyTo(layer.jacket);
        }
        if (secondLayerLeftArmModifier != null) {
            secondLayerLeftArmModifier.applyTo(layer.leftSleeve);
        }
        if (secondLayerRightArmModifier != null) {
            secondLayerRightArmModifier.applyTo(layer.rightSleeve);
        }
        if (secondLayerLeftLegModifier != null) {
            secondLayerLeftLegModifier.applyTo(layer.leftPants);
        }
        if (secondLayerRightLegModifier != null) {
            secondLayerRightLegModifier.applyTo(layer.rightPants);
        }
        if (secondLayerLboobModifier != null) {
            extraModifiers.put("lboob", secondLayerLboobModifier);
        }
        if (secondLayerRboobModifier != null) {
            extraModifiers.put("rboob", secondLayerRboobModifier);
        }
    }

    public void configureDefaultSecondLayer(PlayerModel<AbstractClientPlayer> layer) {
        if (secondLayerHeadModifier != null && secondLayerHeadModifier.isModifying())
            layer.hat.visible = false;
        if (secondLayerBodyModifier != null && secondLayerBodyModifier.isModifying())
            layer.jacket.visible = false;
        if (secondLayerLeftArmModifier != null && secondLayerLeftArmModifier.isModifying())
            layer.leftSleeve.visible = false;
        if (secondLayerRightArmModifier != null && secondLayerRightArmModifier.isModifying())
            layer.rightSleeve.visible = false;
        if (secondLayerLeftLegModifier != null && secondLayerLeftLegModifier.isModifying())
            layer.leftPants.visible = false;
        if (secondLayerRightLegModifier != null && secondLayerRightLegModifier.isModifying())
            layer.rightPants.visible = false;
    }
}
