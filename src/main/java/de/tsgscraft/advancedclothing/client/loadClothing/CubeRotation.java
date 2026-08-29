package de.tsgscraft.advancedclothing.client.loadClothing;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public class CubeRotation {
    public static final CubeRotation NONE = new CubeRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
    final float xRot;
    final float yRot;
    final float zRot;
    final float xPivot;
    final float yPivot;
    final float zPivot;

    public CubeRotation(float xRot, float yRot, float zRot, float xPivot, float yPivot, float zPivot) {
        this.xRot = xRot;
        this.yRot = yRot;
        this.zRot = zRot;
        this.xPivot = xPivot;
        this.yPivot = yPivot;
        this.zPivot = zPivot;
    }

    public CubeRotation(float xRot, float yRot, float zRot) {
        this(xRot, yRot, zRot, 0.0F, 0.0F, 0.0F);
    }

    public Quaternionf toQuaternion() {
        Quaternionf q = new Quaternionf();
        q.rotateXYZ((float) Math.toRadians(xRot), (float) Math.toRadians(yRot), (float) Math.toRadians(zRot));
        return q;
    }

    public float getXRot() {
        return xRot;
    }

    public float getYRot() {
        return yRot;
    }

    public float getZRot() {
        return zRot;
    }

    public Vector3f getPivot() {
        return new Vector3f(xPivot, yPivot, zPivot);
    }
}
