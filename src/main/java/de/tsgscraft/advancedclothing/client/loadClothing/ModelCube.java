package de.tsgscraft.advancedclothing.client.loadClothing;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.tsgscraft.advancedclothing.AdvancedClothing;
import de.tsgscraft.advancedclothing.Config;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ModelCube {
    private final Polygon[] polygons;
    private final Vector3f origin;
    private final Vector3f dimensions;
    private final CubeDeformation grow;
    private final CubeRotation rotation;
    private final PartPose partPose;

    public ModelCube(UVData uvData, float originX, float originY, float originZ, float dimensionX, float dimensionY, float dimensionZ, float growX, float growY, float growZ, CubeRotation rotation, PartPose partPose) {
        this.origin = new Vector3f(originX, originY, originZ);
        this.dimensions = new Vector3f(dimensionX, dimensionY, dimensionZ);
        this.grow = new CubeDeformation(growX, growY, growZ);
        this.rotation = rotation;
        this.partPose = partPose;
        this.polygons = createPolygons(uvData);
        AdvancedClothing.LOGGER.debug("ModelCube created with origin: {}, dimensions: {}, grow: {}, rotation: {}", origin, dimensions, grow, rotation);
    }

    private Polygon[] createPolygons(UVData uvData) {
        float f = origin.x() + dimensions.x() + grow.growX;
        float f1 = origin.y() + dimensions.y() + grow.growY;
        float f2 = origin.z() + dimensions.z() + grow.growZ;
        Polygon[] polygons = new Polygon[6];
        Vertex modelpart$vertex7 = new Vertex(origin.x(), origin.y(), origin.z(), 0.0F, 0.0F).rotateWithPivot(rotation);
        Vertex modelpart$vertex = new Vertex(f, origin.y(), origin.z(), 0.0F, 8.0F).rotateWithPivot(rotation);
        Vertex modelpart$vertex1 = new Vertex(f, f1, origin.z(), 8.0F, 8.0F).rotateWithPivot(rotation);
        Vertex modelpart$vertex2 = new Vertex(origin.x(), f1, origin.z(), 8.0F, 0.0F).rotateWithPivot(rotation);
        Vertex modelpart$vertex3 = new Vertex(origin.x(), origin.y(), f2, 0.0F, 0.0F).rotateWithPivot(rotation);
        Vertex modelpart$vertex4 = new Vertex(f, origin.y(), f2, 0.0F, 8.0F).rotateWithPivot(rotation);
        Vertex modelpart$vertex5 = new Vertex(f, f1, f2, 8.0F, 8.0F).rotateWithPivot(rotation);
        Vertex modelpart$vertex6 = new Vertex(origin.x(), f1, f2, 8.0F, 0.0F).rotateWithPivot(rotation);
        int i = 0;

        if (uvData.UP() != null)
            polygons[i++] = new Polygon(new Vertex[]{modelpart$vertex2, modelpart$vertex1, modelpart$vertex5, modelpart$vertex6}, uvData.UP().u1, uvData.UP().v1, uvData.UP().u2, uvData.UP().v2, uvData.UP().texture().width(), uvData.UP().texture().height(), false, Direction.UP, uvData.UP().texture().location(), uvData.UP().texture().debugTexture());
        if (uvData.DOWN() != null)
            polygons[i++] = new Polygon(new Vertex[]{modelpart$vertex3, modelpart$vertex4, modelpart$vertex, modelpart$vertex7}, uvData.DOWN().u1, uvData.DOWN().v1, uvData.DOWN().u2, uvData.DOWN().v2, uvData.DOWN().texture().width(), uvData.DOWN().texture().height(), false, Direction.DOWN, uvData.DOWN().texture().location(), uvData.DOWN().texture().debugTexture());
        if (uvData.WEST() != null)
            polygons[i++] = new Polygon(new Vertex[]{modelpart$vertex5, modelpart$vertex1, modelpart$vertex, modelpart$vertex4}, uvData.WEST().u1, uvData.WEST().v1, uvData.WEST().u2, uvData.WEST().v2, uvData.WEST().texture().width(), uvData.WEST().texture().height(), false, Direction.WEST, uvData.WEST().texture().location(), uvData.WEST().texture().debugTexture());
        if (uvData.NORTH() != null)
            polygons[i++] = new Polygon(new Vertex[]{modelpart$vertex1, modelpart$vertex2, modelpart$vertex7, modelpart$vertex}, uvData.NORTH().u1, uvData.NORTH().v1, uvData.NORTH().u2, uvData.NORTH().v2, uvData.NORTH().texture().width(), uvData.NORTH().texture().height(), false, Direction.NORTH, uvData.NORTH().texture().location(), uvData.NORTH().texture().debugTexture());
        if (uvData.EAST() != null)
            polygons[i++] = new Polygon(new Vertex[]{modelpart$vertex2, modelpart$vertex6, modelpart$vertex3, modelpart$vertex7}, uvData.EAST().u1, uvData.EAST().v1, uvData.EAST().u2, uvData.EAST().v2, uvData.EAST().texture().width(), uvData.EAST().texture().height(), false, Direction.EAST, uvData.EAST().texture().location(), uvData.EAST().texture().debugTexture());
        if (uvData.SOUTH() != null)
            polygons[i] = new Polygon(new Vertex[]{modelpart$vertex6, modelpart$vertex5, modelpart$vertex4, modelpart$vertex3}, uvData.SOUTH().u1, uvData.SOUTH().v1, uvData.SOUTH().u2,	uvData.SOUTH().v2,	uvData.SOUTH().texture().width(),	uvData.SOUTH().texture().height(), false, Direction.SOUTH, uvData.SOUTH().texture().location(), uvData.SOUTH().texture().debugTexture());

        /*
        polygons[i++] = new Polygon(new Vertex[]{modelpart$vertex4, modelpart$vertex3, modelpart$vertex7, modelpart$vertex}, f5, f10, f6, f11, texScaleU, texScaleV, false, Direction.DOWN);
        polygons[i++] = new Polygon(new Vertex[]{modelpart$vertex1, modelpart$vertex2, modelpart$vertex6, modelpart$vertex5}, f6, f11, f7, f10, texScaleU, texScaleV, false, Direction.UP);
        polygons[i++] = new Polygon(new Vertex[]{modelpart$vertex7, modelpart$vertex3, modelpart$vertex6, modelpart$vertex2}, f4, f11, f5, f12, texScaleU, texScaleV, false, Direction.WEST);
        polygons[i++] = new Polygon(new Vertex[]{modelpart$vertex, modelpart$vertex7, modelpart$vertex2, modelpart$vertex1}, f5, f11, f6, f12, texScaleU, texScaleV, false, Direction.NORTH);
        polygons[i++] = new Polygon(new Vertex[]{modelpart$vertex4, modelpart$vertex, modelpart$vertex1, modelpart$vertex5}, f6, f11, f8, f12, texScaleU, texScaleV, false, Direction.EAST);
        polygons[i] = new Polygon(new Vertex[]{modelpart$vertex3, modelpart$vertex4, modelpart$vertex5, modelpart$vertex6}, f8, f11, f9, f12, texScaleU, texScaleV, false, Direction.SOUTH);
         */
        return polygons;
    }

    public void compile(PoseStack.Pose pose, MultiBufferSource buffer, int packedLight, int packedOverlay, int color) {
        Matrix4f matrix4f = pose.pose();
        Vector3f vector3f = new Vector3f();

        for(Polygon modelpart$polygon : this.polygons) {
            if (modelpart$polygon == null) {
                continue;
            }
            Vector3f vector3f1 = pose.transformNormal(modelpart$polygon.normal, vector3f);
            float f = vector3f1.x();
            float f1 = vector3f1.y();
            float f2 = vector3f1.z();
            VertexConsumer vertexConsumer = (Config.debug && modelpart$polygon.debugTextureId != null) ? buffer.getBuffer(RenderType.entityTranslucent(modelpart$polygon.debugTextureId)) : buffer.getBuffer(RenderType.entityTranslucent(modelpart$polygon.textureId));
            for(Vertex modelpart$vertex : modelpart$polygon.vertices) {
                float f3 = modelpart$vertex.pos.x() / 16.0F;
                float f4 = modelpart$vertex.pos.y() / 16.0F;
                float f5 = modelpart$vertex.pos.z() / 16.0F;
                Vector3f vector3f2 = matrix4f.transformPosition(f3, f4, f5, vector3f);
                vertexConsumer.addVertex(vector3f2.x(), vector3f2.y(), vector3f2.z(), color, modelpart$vertex.u, modelpart$vertex.v, packedOverlay, packedLight, f, f1, f2);
            }
        }
    }

    static class Polygon {
        public final Vertex[] vertices;
        public final Vector3f normal;
        public final ResourceLocation textureId;
        public final ResourceLocation debugTextureId;

        public Polygon(Vertex[] vertices, float u1, float v1, float u2, float v2, float textureWidth, float textureHeight, boolean mirror, Direction direction, ResourceLocation textureId, ResourceLocation debugTextureId) {
            this.vertices = vertices;
            this.textureId = textureId;
            this.debugTextureId = debugTextureId;
            float f = 0.0F / textureWidth;
            float f1 = 0.0F / textureHeight;
            float u = u2 / textureWidth * (textureWidth / 16) - f;
            float u3 = u1 / textureWidth * (textureWidth / 16) + f;
            float v = v1 / textureHeight * (textureHeight / 16) + f1;
            float v3 = v2 / textureHeight * (textureHeight / 16) - f1;
            vertices[0] = vertices[0].remap(u, v);
            vertices[1] = vertices[1].remap(u3, v);
            vertices[2] = vertices[2].remap(u3, v3);
            vertices[3] = vertices[3].remap(u, v3);
            if (mirror) {
                int i = vertices.length;

                for(int j = 0; j < i / 2; ++j) {
                    Vertex modelpart$vertex = vertices[j];
                    vertices[j] = vertices[i - 1 - j];
                    vertices[i - 1 - j] = modelpart$vertex;
                }
            }

            this.normal = direction.step();
            if (mirror) {
                this.normal.mul(-1.0F, 1.0F, 1.0F);
            }
        }
    }

    static class Vertex {
        public Vector3f pos;
        public final float u;
        public final float v;

        public Vertex(float x, float y, float z, float u, float v) {
            this(new Vector3f(x, y, z), u, v);
        }

        public Vertex remap(float u, float v) {
            return new Vertex(this.pos, u, v);
        }

        public Vertex(Vector3f pos, float u, float v) {
            this.pos = pos;
            this.u = u;
            this.v = v;
        }

        public Vertex rotateWithPivot(CubeRotation cubeRotation) {
            Quaternionf quaternion = cubeRotation.toQuaternion();
            Vector3f pivot = cubeRotation.getPivot();
            pos = new Vector3f(pos).sub(pivot).rotate(quaternion).add(pivot);
            return this;
        }
    }

    public record UVData(FaceData UP, FaceData DOWN, FaceData WEST, FaceData NORTH, FaceData EAST, FaceData SOUTH) {
    }

    public record FaceData(float u1, float v1, float u2, float v2, TextureData texture) {

    }
}
