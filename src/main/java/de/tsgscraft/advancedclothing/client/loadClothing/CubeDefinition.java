package de.tsgscraft.advancedclothing.client.loadClothing;

import net.minecraft.client.model.geom.PartPose;
import org.joml.Vector3f;

public class CubeDefinition {
    private final Vector3f origin;
    private final Vector3f dimensions;
    private final CubeDeformation grow;
    private final ModelCube.UVData uvData;
    private final CubeRotation rotation;
    private final PartPose partPose;

    protected CubeDefinition(ModelCube.UVData uvData, float originX, float originY, float originZ, float dimensionX, float dimensionY, float dimensionZ, CubeDeformation grow, CubeRotation rotation, PartPose partPose) {
        this.uvData = uvData;
        this.origin = new Vector3f(originX, originY, originZ);
        this.dimensions = new Vector3f(dimensionX, dimensionY, dimensionZ);
        this.grow = grow;
        this.rotation = rotation;
        this.partPose = partPose;
    }

    public ModelCube bake() {
        return new ModelCube(uvData, origin.x(), origin.y(), origin.z(), dimensions.x(), dimensions.y(), dimensions.z(), this.grow.growX, this.grow.growY, this.grow.growZ, rotation, partPose);
    }
}
