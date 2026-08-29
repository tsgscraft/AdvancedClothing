package de.tsgscraft.advancedclothing.client;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClothingRegistry {
    private static ClothingRegistry instance;

    public static ClothingRegistry getInstance() {
        return instance;
    }

    public ClothingRegistry() {
        instance = this;
    }

    private List<ClothingElement> clothingElements;
    private List<String> clothingTypes;
    private List<String> clothingIds;
    private Map<String, List<String>> clothingTypeToIdMap;
    private Map<String, List<ClothingElement>> clothingTypeToElementMap;

    public void setClothingElements(List<ClothingElement> clothingElements) {
        this.clothingElements = clothingElements;
        this.clothingTypes = clothingElements.stream().map(ClothingElement::type).distinct().collect(Collectors.toList());
        this.clothingIds = clothingElements.stream().map(clothingElement -> clothingElement.id().toString()).collect(Collectors.toList());
        this.clothingTypeToIdMap = clothingElements.stream()
                .collect(Collectors.groupingBy(
                        ClothingElement::type,
                        Collectors.mapping(clothingElement -> clothingElement.id().toString(), Collectors.toList())
                ));
        this.clothingTypeToElementMap = clothingElements.stream()
                .collect(Collectors.groupingBy(
                        ClothingElement::type,
                        Collectors.mapping(clothingElement -> clothingElement, Collectors.toList())
                ));
    }

    public List<ClothingElement> getClothingElements() {
        return clothingElements;
    }

    public List<String> getClothingTypes() {
        return clothingTypes;
    }

    public List<String> getClothingIds() {
        return clothingIds;
    }

    public Map<String, List<String>> getClothingTypeToIdMap() {
        return clothingTypeToIdMap;
    }

    public Map<String, List<ClothingElement>> getClothingTypeToElementMap() {
        return clothingTypeToElementMap;
    }
}
