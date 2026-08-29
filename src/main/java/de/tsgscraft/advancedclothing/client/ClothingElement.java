package de.tsgscraft.advancedclothing.client;

import de.tsgscraft.advancedclothing.client.modifiers.ClothingModifiers;
import net.minecraft.resources.ResourceLocation;

public record ClothingElement(ClothingRendering renderInfo, ClothingModifiers modifiers, String name, String type, ResourceLocation id) {
}
