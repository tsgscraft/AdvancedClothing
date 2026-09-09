package de.tsgscraft.advancedclothing.client.modifiers;

import com.google.gson.JsonObject;
import de.tsgscraft.advancedclothing.attachments.ClothingAttachments;
import de.tsgscraft.advancedclothing.client.ClothingElement;
import de.tsgscraft.advancedclothing.client.ClothingRegistry;
import dev.leo.sableplayerragdoll.block.entity.RagdollPartBlockEntity;
import dev.leo.sableplayerragdoll.entity.RagdollDollEntity;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ClothingModifiers {

    private ModelPartModifiers secondLayerHeadModifier;
    private ModelPartModifiers secondLayerBodyModifier;
    private ModelPartModifiers secondLayerLeftArmModifier;
    private ModelPartModifiers secondLayerRightArmModifier;
    private ModelPartModifiers secondLayerLeftLegModifier;
    private ModelPartModifiers secondLayerRightLegModifier;
    private ModelPartModifiers secondLayerLboobModifier;
    private ModelPartModifiers secondLayerRboobModifier;

    public static Map<UUID, Map<Integer, ModelPartModifiers>> playerModifiers = new HashMap<>();

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

    public static void applyModifiersToPlayer(Player player) {
        Map<String, String> clothingData = player.getData(ClothingAttachments.CLOTHING_DATA);

        List<ClothingElement> clothingElements = ClothingRegistry.getInstance().getClothingElements().stream()
                .filter(clothingElement -> clothingData.containsValue(clothingElement.id().toString()))
                .toList();

        applyModifiersToPlayer(player, clothingElements);
    }

    public static void applyModifiersToPlayer(Player player, List<ClothingElement> clothingElements) {
        ModelPartModifiers combinedHeadModifiers = new ModelPartModifiers();
        ModelPartModifiers combinedBodyModifiers = new ModelPartModifiers();
        ModelPartModifiers combinedLeftArmModifiers = new ModelPartModifiers();
        ModelPartModifiers combinedRightArmModifiers = new ModelPartModifiers();
        ModelPartModifiers combinedLeftLegModifiers = new ModelPartModifiers();
        ModelPartModifiers combinedRightLegModifiers = new ModelPartModifiers();

        for (ClothingElement clothingElement : clothingElements) {
            if (clothingElement.modifiers() != null) {
                if (clothingElement.modifiers().secondLayerHeadModifier != null) {
                    combinedHeadModifiers.combine(clothingElement.modifiers().secondLayerHeadModifier);
                }
                if (clothingElement.modifiers().secondLayerBodyModifier != null) {
                    combinedBodyModifiers.combine(clothingElement.modifiers().secondLayerBodyModifier);
                }
                if (clothingElement.modifiers().secondLayerLeftArmModifier != null) {
                    combinedLeftArmModifiers.combine(clothingElement.modifiers().secondLayerLeftArmModifier);
                }
                if (clothingElement.modifiers().secondLayerRightArmModifier != null) {
                    combinedRightArmModifiers.combine(clothingElement.modifiers().secondLayerRightArmModifier);
                }
                if (clothingElement.modifiers().secondLayerLeftLegModifier != null) {
                    combinedLeftLegModifiers.combine(clothingElement.modifiers().secondLayerLeftLegModifier);
                }
                if (clothingElement.modifiers().secondLayerRightLegModifier != null) {
                    combinedRightLegModifiers.combine(clothingElement.modifiers().secondLayerRightLegModifier);
                }
            }
        }

        System.out.println("Combined Modifiers for player " + player.getName().getString() + ":");
        System.out.println("Head: " + combinedHeadModifiers);
        System.out.println("Body: " + combinedBodyModifiers);
        System.out.println("Left Arm: " + combinedLeftArmModifiers);
        System.out.println("Right Arm: " + combinedRightArmModifiers);
        System.out.println("Left Leg: " + combinedLeftLegModifiers);
        System.out.println("Right Leg: " + combinedRightLegModifiers);

        Map<Integer, ModelPartModifiers> combinedModifiers = new HashMap<>();
        combinedModifiers.put(0, combinedHeadModifiers);
        combinedModifiers.put(1, combinedBodyModifiers);
        combinedModifiers.put(2, combinedLeftArmModifiers);
        combinedModifiers.put(3, combinedRightArmModifiers);
        combinedModifiers.put(4, combinedLeftLegModifiers);
        combinedModifiers.put(5, combinedRightLegModifiers);

        playerModifiers.put(player.getUUID(), combinedModifiers);
    }

    public static void configureDefaultSecondLayer(PlayerModel<AbstractClientPlayer> layer, UUID uuid) {
        Map<Integer, ModelPartModifiers> map = playerModifiers.getOrDefault(uuid, new HashMap<>());
        if (map.getOrDefault(0, null) != null && map.get(0).isModifying()) {
            layer.hat.visible = false;
        }
        if (map.getOrDefault(1, null) != null && map.get(1).isModifying()) {
            layer.jacket.visible = false;
        }
        if (map.getOrDefault(2, null) != null && map.get(2).isModifying()) {
            layer.leftSleeve.visible = false;
        }
        if (map.getOrDefault(3, null) != null && map.get(3).isModifying()) {
            layer.rightSleeve.visible = false;
        }
        if (map.getOrDefault(4, null) != null && map.get(4).isModifying()) {
            layer.leftPants.visible = false;
        }
        if (map.getOrDefault(5, null) != null && map.get(5).isModifying()) {
            layer.rightPants.visible = false;
        }
    }

    public static void configureRagdollSecondLayer(PlayerModel<RagdollDollEntity> model, UUID uuid, RagdollPartBlockEntity.BodyPart part) {
        Map<Integer, ModelPartModifiers> map = playerModifiers.getOrDefault(uuid, new HashMap<>());
        if (map.getOrDefault(0, null) != null && map.get(0).isModifying() && part == RagdollPartBlockEntity.BodyPart.HEAD) {
            model.hat.visible = false;
        }
        if (map.getOrDefault(1, null) != null && map.get(1).isModifying() && part == RagdollPartBlockEntity.BodyPart.TORSO) {
            model.jacket.visible = false;
        }
        if (map.getOrDefault(2, null) != null && map.get(2).isModifying() && part == RagdollPartBlockEntity.BodyPart.LEFT_ARM) {
            model.leftSleeve.visible = false;
        }
        if (map.getOrDefault(3, null) != null && map.get(3).isModifying() && part == RagdollPartBlockEntity.BodyPart.RIGHT_ARM) {
            model.rightSleeve.visible = false;
        }
        if (map.getOrDefault(4, null) != null && map.get(4).isModifying() && part == RagdollPartBlockEntity.BodyPart.LEFT_LEG) {
            model.leftPants.visible = false;
        }
        if (map.getOrDefault(5, null) != null && map.get(5).isModifying() && part == RagdollPartBlockEntity.BodyPart.RIGHT_LEG) {
            model.rightPants.visible = false;
        }
    }

    public static void configureSecondLayer(PlayerModel<?> layer, UUID uuid) {
        Map<Integer, ModelPartModifiers> map = playerModifiers.getOrDefault(uuid, new HashMap<>());
        if (map.getOrDefault(0, null) != null) {
            map.get(0).applyTo(layer.hat);
        }
        if (map.getOrDefault(1, null) != null) {
            map.get(1).applyTo(layer.jacket);
        }
        if (map.getOrDefault(2, null) != null) {
            map.get(2).applyTo(layer.leftSleeve);
        }
        if (map.getOrDefault(3, null) != null) {
            map.get(3).applyTo(layer.rightSleeve);
        }
        if (map.getOrDefault(4, null) != null) {
            map.get(4).applyTo(layer.leftPants);
        }
        if (map.getOrDefault(5, null) != null) {
            map.get(5).applyTo(layer.rightPants);
        }
    }

    @Override
    public String toString() {
        return "ClothingModifiers{" +
                "secondLayerHeadModifier=" + secondLayerHeadModifier +
                ", secondLayerBodyModifier=" + secondLayerBodyModifier +
                ", secondLayerLeftArmModifier=" + secondLayerLeftArmModifier +
                ", secondLayerRightArmModifier=" + secondLayerRightArmModifier +
                ", secondLayerLeftLegModifier=" + secondLayerLeftLegModifier +
                ", secondLayerRightLegModifier=" + secondLayerRightLegModifier +
                ", secondLayerLboobModifier=" + secondLayerLboobModifier +
                ", secondLayerRboobModifier=" + secondLayerRboobModifier +
                '}';
    }
}
