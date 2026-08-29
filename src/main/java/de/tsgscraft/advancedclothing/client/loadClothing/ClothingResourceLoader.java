package de.tsgscraft.advancedclothing.client.loadClothing;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.tsgscraft.advancedclothing.AdvancedClothing;
import de.tsgscraft.advancedclothing.REFERENCE;
import de.tsgscraft.advancedclothing.client.ClothingElement;
import de.tsgscraft.advancedclothing.client.ClothingRendering;
import de.tsgscraft.advancedclothing.client.anchor.ClothingAnchorInfo;
import de.tsgscraft.advancedclothing.client.modifiers.ClothingModifiers;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static com.mojang.text2speech.Narrator.LOGGER;

public class ClothingResourceLoader implements PreparableReloadListener {

    @Override
    public CompletableFuture<Void> reload(
            PreparationBarrier barrier,
            ResourceManager resourceManager,
            ProfilerFiller preparationsProfiler,
            ProfilerFiller reloadProfiler,
            Executor backgroundExecutor,
            Executor gameExecutor
    ) {
        return CompletableFuture
                .supplyAsync(
                        () -> loadClothing(resourceManager),
                        backgroundExecutor
                )
                .thenCompose(barrier::wait)
                .thenAcceptAsync(
                        loadedClothing -> {
                            for (ClothingElement clothing : loadedClothing) {
                                clothing.renderInfo().bake();
                            }
                            AdvancedClothing.getClothingRegistry().setClothingElements(loadedClothing);
                        },
                        gameExecutor
                );
    }

    private List<ClothingElement> loadClothing(ResourceManager resourceManager) {
        List<ClothingElement> result = new ArrayList<>();

        ResourceLocation location =
                ResourceLocation.fromNamespaceAndPath(
                        REFERENCE.MODID,
                        "clothing.json"
                );

        List<Resource> resources =
                resourceManager.getResourceStack(location);

        for (Resource resource : resources) {
            try (Reader reader = resource.openAsReader()) {

                JsonObject json =
                        JsonParser.parseReader(reader).getAsJsonObject();

                JsonArray clothing =
                        json.getAsJsonArray("clothing");

                for (JsonElement element : clothing) {
                    ResourceLocation clothingDataLocation = ResourceLocation.parse(element.getAsString());
                    Resource data = resourceManager.getResource(clothingDataLocation).orElse(null);
                    if (data == null) {
                        LOGGER.warn("Failed to find clothing data for location: {}", clothingDataLocation);
                        continue;
                    }
                    try (Reader infoReader = data.openAsReader()) {
                        JsonObject clothingJson = JsonParser.parseReader(infoReader).getAsJsonObject();

                        String clothingName = clothingJson.get("name").getAsString();

                        ResourceLocation clothingType = ResourceLocation.parse(clothingJson.get("type").getAsString());

                        Map<String, TextureData> textureDataMap = new HashMap<>();
                        JsonArray textures = clothingJson.getAsJsonArray("textures");
                        for (JsonElement textureElement : textures) {
                            JsonObject textureObject = textureElement.getAsJsonObject();
                            String textureId = textureObject.get("id").getAsString();
                            ResourceLocation textureLocation = ResourceLocation.parse(textureObject.get("location").getAsString());
                            ResourceLocation debugTextureLocation = null;
                            if (textureObject.has("debug")) {
                                debugTextureLocation = ResourceLocation.parse(textureObject.get("debug").getAsString());
                            }
                            int width = textureObject.get("width").getAsInt();
                            int height = textureObject.get("height").getAsInt();
                            TextureData textureData = new TextureData(textureLocation, width, height, debugTextureLocation);
                            textureDataMap.put(textureId, textureData);
                        }

                        Map<ClothingAnchorInfo, List<CubeDefinition>> slimModelData = null;
                        if (clothingJson.has("slim_model")) {
                            slimModelData = loadModel(resourceManager.getResource(ResourceLocation.parse(clothingJson.get("slim_model").getAsString())).get(), textureDataMap);
                        }
                        Map<ClothingAnchorInfo, List<CubeDefinition>> modelData = null;
                        if (clothingJson.has("model")) {
                            modelData = loadModel(resourceManager.getResource(ResourceLocation.parse(clothingJson.get("model").getAsString())).get(), textureDataMap);
                        }

                        result.add(new ClothingElement(new ClothingRendering(modelData, slimModelData), new ClothingModifiers(clothingJson.has("modifiers") ? clothingJson.get("modifiers").getAsJsonObject() : new JsonObject()), clothingName, clothingType.toString(), clothingDataLocation));
                    } catch (Exception e) {
                        LOGGER.error(
                                "Failed to load model {} from resource pack {}",
                                clothingDataLocation,
                                data.sourcePackId(),
                                e
                        );
                    }
                }
            } catch (Exception e) {
                LOGGER.error(
                        "Failed to load {} from resource pack {}",
                        location,
                        resource.sourcePackId(),
                        e
                );
            }
        }

        return result;
    }

    private void customLoader() {
        /*
        if (clothingJson.get("model").isJsonObject()) {
            renderInfo = loadModel(clothingJson.get("model").getAsJsonObject());
        } else {
            Optional<Resource> resource1 = resourceManager.getResource(ResourceLocation.parse(clothingJson.get("model").getAsString()));
            renderInfo = loadModel(resource1.get());
        }
         */
    }

    private Map<ClothingAnchorInfo, List<CubeDefinition>> loadModel(Resource key, Map<String, TextureData> texture) {
        try (Reader reader = key.openAsReader()) {

            JsonObject json =
                    JsonParser.parseReader(reader).getAsJsonObject();

            return loadModel(json, texture);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<ClothingAnchorInfo, List<CubeDefinition>> loadModel(JsonObject model, Map<String, TextureData> texture) {
        Map<ClothingAnchorInfo, List<CubeDefinition>> cubes = new HashMap<>();
        JsonArray anchors = model.getAsJsonArray("anchors");
        for (int i = 0; i < anchors.size(); i++) {
            JsonObject anchorObject = anchors.get(i).getAsJsonObject();
            String anchorName = anchorObject.get("anchor").getAsString();
            JsonArray offsetArray = anchorObject.getAsJsonArray("offset");
            float offsetX = offsetArray.get(0).getAsFloat();
            float offsetY = offsetArray.get(1).getAsFloat();
            float offsetZ = offsetArray.get(2).getAsFloat();
            ClothingAnchorInfo anchorInfo = new ClothingAnchorInfo(
                    ResourceLocation.parse(anchorName),
                    offsetX,
                    offsetY,
                    offsetZ
            );
            JsonArray elements = anchorObject.getAsJsonArray("elements");
            CubeListBuilder cubeListBuilder = CubeListBuilder.create(anchorInfo);
            for (int j = 0; j < elements.size(); j++) {
                JsonObject elementObject = elements.get(j).getAsJsonObject();

                JsonArray fromArray = elementObject.getAsJsonArray("from");
                float fromX = fromArray.get(0).getAsFloat();
                float fromY = fromArray.get(1).getAsFloat();
                float fromZ = fromArray.get(2).getAsFloat();

                JsonArray toArray = elementObject.getAsJsonArray("to");
                float toX = toArray.get(0).getAsFloat();
                float toY = toArray.get(1).getAsFloat();
                float toZ = toArray.get(2).getAsFloat();

                CubeRotation rotation = new CubeRotation(0, 0, 0, 0, 0, 0);
                if (elementObject.has("rotation")) {
                    JsonObject rotationObject = elementObject.getAsJsonObject("rotation");
                    if (rotationObject.has("axis")) {
                        String axis = rotationObject.get("axis").getAsString();
                        float angle = rotationObject.get("angle").getAsFloat();
                        JsonArray pivotArray = rotationObject.getAsJsonArray("origin");
                        float pivotX = pivotArray.get(0).getAsFloat();
                        float pivotY = pivotArray.get(1).getAsFloat();
                        float pivotZ = pivotArray.get(2).getAsFloat();
                        rotation = switch (axis) {
                            case "x" -> new CubeRotation(angle, 0, 0, pivotX, pivotY, pivotZ);
                            case "y" -> new CubeRotation(0, angle, 0, pivotX, pivotY, pivotZ);
                            case "z" -> new CubeRotation(0, 0, angle, pivotX, pivotY, pivotZ);
                            default -> rotation;
                        };
                    }else {
                        float rotationX = rotationObject.get("x").getAsFloat();
                        float rotationY = rotationObject.get("y").getAsFloat();
                        float rotationZ = rotationObject.get("z").getAsFloat();

                        JsonArray pivotArray = rotationObject.getAsJsonArray("origin");
                        float pivotX = pivotArray.get(0).getAsFloat();
                        float pivotY = pivotArray.get(1).getAsFloat();
                        float pivotZ = pivotArray.get(2).getAsFloat();
                        rotation = new CubeRotation(rotationX, rotationY, rotationZ, pivotX, pivotY, pivotZ);
                    }
                }

                cubeListBuilder.texOffs(getUvData(elementObject.getAsJsonObject("faces"), texture));
                cubeListBuilder.addBox(fromX, fromY, fromZ, toX - fromX, toY - fromY, toZ - fromZ, rotation, PartPose.offset(offsetX, offsetY, offsetZ));
            }
            cubes.put(anchorInfo, cubeListBuilder.getCubes());
        }
        return cubes;
    }

    private ModelCube.UVData getUvData(JsonObject elementObject, Map<String, TextureData> textureDataMap) {
        ModelCube.FaceData north = null;
        if (elementObject.has("north"))
            north = getFaceData(elementObject.getAsJsonObject("north"), textureDataMap);
        ModelCube.FaceData south = null;
        if (elementObject.has("south"))
            south = getFaceData(elementObject.getAsJsonObject("south"), textureDataMap);
        ModelCube.FaceData east = null;
        if (elementObject.has("east"))
            east = getFaceData(elementObject.getAsJsonObject("east"), textureDataMap);
        ModelCube.FaceData west = null;
        if (elementObject.has("west"))
            west = getFaceData(elementObject.getAsJsonObject("west"), textureDataMap);
        ModelCube.FaceData up = null;
        if (elementObject.has("up"))
            up = getFaceData(elementObject.getAsJsonObject("up"), textureDataMap);
        ModelCube.FaceData down = null;
        if (elementObject.has("down"))
            down = getFaceData(elementObject.getAsJsonObject("down"), textureDataMap);
        return new ModelCube.UVData(
                up,
                down,
                west,
                north,
                east,
                south
        );
    }

    private ModelCube.FaceData getFaceData(JsonObject faceObject, Map<String, TextureData> textureDataMap) {
        JsonArray uvArray = faceObject.getAsJsonArray("uv");
        float u1 = uvArray.get(0).getAsFloat();
        float v1 = uvArray.get(1).getAsFloat();
        float u2 = uvArray.get(2).getAsFloat();
        float v2 = uvArray.get(3).getAsFloat();
        String textureId = faceObject.get("texture").getAsString();
        TextureData textureData = textureDataMap.get(textureId);
        if (textureData == null) {
            textureData = TextureData.DEFAULT;
        }
        return new ModelCube.FaceData(u1, v1, u2, v2, textureData);
    }
}