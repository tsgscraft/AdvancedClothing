package de.tsgscraft.advancedclothing.client.loadClothing;

public class CubeDeformation {
    public static final CubeDeformation NONE = new CubeDeformation(0.0F);
    final float growX;
    final float growY;
    final float growZ;

    public CubeDeformation(float growX, float growY, float growZ) {
        this.growX = growX;
        this.growY = growY;
        this.growZ = growZ;
    }

    public CubeDeformation(float grow) {
        this(grow, grow, grow);
    }

    public CubeDeformation extend(float grow) {
        return new CubeDeformation(this.growX + grow, this.growY + grow, this.growZ + grow);
    }

    public CubeDeformation extend(float growX, float growY, float growZ) {
        return new CubeDeformation(this.growX + growX, this.growY + growY, this.growZ + growZ);
    }
}
