package de.tsgscraft.advancedclothing.client.anchor;

import net.minecraft.resources.ResourceLocation;

public class ClothingAnchorInfo {
    ResourceLocation anchor;
    float anchorOffsetX, anchorOffsetY, anchorOffsetZ;

    public ClothingAnchorInfo(ResourceLocation anchor) {
        this(anchor, 0.0f, 0.0f, 0.0f);
    }

    public ClothingAnchorInfo(ResourceLocation anchor, float anchorOffsetX, float anchorOffsetY, float anchorOffsetZ) {
        this.anchor = anchor;
        this.anchorOffsetX = anchorOffsetX;
        this.anchorOffsetY = anchorOffsetY;
        this.anchorOffsetZ = anchorOffsetZ;
    }

    public ResourceLocation getAnchor() {
        return anchor;
    }

    public float getAnchorOffsetX() {
        return anchorOffsetX;
    }

    public float getAnchorOffsetY() {
        return anchorOffsetY;
    }

    public float getAnchorOffsetZ() {
        return anchorOffsetZ;
    }
}
