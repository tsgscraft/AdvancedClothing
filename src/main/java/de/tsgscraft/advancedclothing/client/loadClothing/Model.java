package de.tsgscraft.advancedclothing.client.loadClothing;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;

import java.util.List;

public class Model {
    public float x;
    public float y;
    public float z;
    public float xRot;
    public float yRot;
    public float zRot;
    public float xScale = 1.0F;
    public float yScale = 1.0F;
    public float zScale = 1.0F;
    public final List<ModelCube> cubes;

    public Model(List<ModelCube> cubes) {
        this.cubes = cubes;
    }

    public void setPosition(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setRotation(float xRot, float yRot, float zRot) {
        this.xRot = xRot;
        this.yRot = yRot;
        this.zRot = zRot;
    }

    public void setScale(float xScale, float yScale, float zScale) {
        this.xScale = xScale;
        this.yScale = yScale;
        this.zScale = zScale;
    }

    public void copyFrom(ModelPart from) {
        this.x = from.x;
        this.y = from.y;
        this.z = from.z;
        this.xRot = from.xRot;
        this.yRot = from.yRot;
        this.zRot = from.zRot;
        this.xScale = from.xScale;
        this.yScale = from.yScale;
        this.zScale = from.zScale;
    }

    public void compile(PoseStack.Pose pose, MultiBufferSource buffer, int packedLight, int packedOverlay, int color) {
        for (ModelCube cube : cubes) {
            cube.compile(pose, buffer, packedLight, packedOverlay, color);
        }
    }
}
