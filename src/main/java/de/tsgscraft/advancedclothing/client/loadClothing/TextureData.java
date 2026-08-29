package de.tsgscraft.advancedclothing.client.loadClothing;

import net.minecraft.resources.ResourceLocation;

public record TextureData(ResourceLocation location, int width, int height, ResourceLocation debugTexture) {
    public static final TextureData DEFAULT = new TextureData(ResourceLocation.fromNamespaceAndPath("minecraft", "textures/block/stone.png"), 8, 8, ResourceLocation.fromNamespaceAndPath("minecraft", "textures/block/redstone_block.png"));
}
