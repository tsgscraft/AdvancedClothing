package de.tsgscraft.advancedclothing.client.modifiers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;

public class ModelPartModifiers {
    private boolean modified = false;

    private boolean modifiedOffset = false;
    private float xOffset = 0;
    private float yOffset = 0;
    private float zOffset = 0;

    private boolean modifiedScale = false;
    private float xScale = 1;
    private float yScale = 1;
    private float zScale = 1;

    private boolean isVisible = true;

    public ModelPartModifiers(float xOffset, float yOffset, float zOffset, float xScale, float yScale, float zScale, boolean isVisible) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
        this.xScale = xScale;
        this.yScale = yScale;
        this.zScale = zScale;
        this.isVisible = isVisible;
        this.modified = true;
    }

    public ModelPartModifiers(JsonObject jsonObject) {
        if (jsonObject == null) {
            return;
        }

        if (jsonObject.has("scale")) {
            this.modifiedScale = true;
            this.modified = true;
            JsonArray scaleArray = jsonObject.getAsJsonArray("scale");
            this.xScale = scaleArray.get(0).getAsFloat();
            this.yScale = scaleArray.get(1).getAsFloat();
            this.zScale = scaleArray.get(2).getAsFloat();
        }

        if (jsonObject.has("offset")) {
            this.modifiedOffset = true;
            this.modified = true;
            JsonArray offsetArray = jsonObject.getAsJsonArray("offset");
            this.xOffset = offsetArray.get(0).getAsFloat();
            this.yOffset = offsetArray.get(1).getAsFloat();
            this.zOffset = offsetArray.get(2).getAsFloat();
        }

        if (jsonObject.has("visible")) {
            this.modified = true;
            this.isVisible = jsonObject.get("visible").getAsBoolean();
        }
    }

    public void applyTo(ModelPart modelPart) {
        if (modified) {
            modelPart.visible = true;
        }
        if (modifiedOffset) {
            modelPart.x = modelPart.x + xOffset;
            modelPart.y = modelPart.y + yOffset;
            modelPart.z = modelPart.z + zOffset;
        }
        if (modifiedScale) {
            modelPart.xScale = xScale;
            modelPart.yScale = yScale;
            modelPart.zScale = zScale;
        }
        if (!isVisible) {
            modelPart.visible = false;
        }
    }

    public void applyTo(PoseStack stack) {
        if (modifiedOffset) {
            stack.translate(xOffset, yOffset, zOffset);
        }
        if (modifiedScale) {
            stack.scale(xScale, yScale, zScale);
        }
    }

    public boolean isModifying() {
        return modified;
    }
}
