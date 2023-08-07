package nickwrecks.demonicvessel.client;

import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraftforge.client.model.pipeline.QuadBakingVertexConsumer;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static net.minecraft.core.Direction.DOWN;

public class ClientTools {
    private static void putVertex(QuadBakingVertexConsumer builder, Vector3f normal, Vector4f vector,
                                  float u, float v, TextureAtlasSprite sprite) {
        builder.vertex(vector.x(), vector.y(), vector.z())
                .color(1.0f, 1.0f, 1.0f, 1.0f)
                .uv(sprite.getU(u), sprite.getV(v))
                .uv2(0, 0)
                .normal(normal.x(), normal.y(), normal.z())
                .endVertex();
    }

    public static BakedQuad createQuad(Vector3f v1, Vector3f v2, Vector3f v3, Vector3f v4, Transformation rotation, TextureAtlasSprite sprite) {
        Vector3f normal = new Vector3f(v3);
        normal.sub(v2);
        Vector3f temp = new Vector3f(v1);
        temp.sub(v2);
        normal.cross(temp);
        normal.normalize();

        int tw = sprite.contents().width();
        int th = sprite.contents().height();

        rotation = rotation.blockCenterToCorner();
        rotation.transformNormal(normal);

        Vector4f vv1 = new Vector4f(v1, 1.0f); rotation.transformPosition(vv1);
        Vector4f vv2 = new Vector4f(v2, 1.0f); rotation.transformPosition(vv2);
        Vector4f vv3 = new Vector4f(v3, 1.0f); rotation.transformPosition(vv3);
        Vector4f vv4 = new Vector4f(v4, 1.0f); rotation.transformPosition(vv4);

        BakedQuad[] quad = new BakedQuad[1];
        var builder = new QuadBakingVertexConsumer(q -> quad[0] = q);
        builder.setSprite(sprite);
        builder.setDirection(Direction.getNearest(normal.x(), normal.y(), normal.z()));
        putVertex(builder, normal, vv1, 0, 0, sprite);
        putVertex(builder, normal, vv2, 0, th, sprite);
        putVertex(builder, normal, vv3, tw, th, sprite);
        putVertex(builder, normal, vv4, tw, 0, sprite);
        return quad[0];
    }
    public static BakedQuad createQuadWithoutRotation(Vector3f v1, Vector3f v2, Vector3f v3, Vector3f v4, TextureAtlasSprite sprite) {
        Vector3f normal = new Vector3f(v3);
        normal.sub(v2);
        Vector3f temp = new Vector3f(v1);
        temp.sub(v2);
        normal.cross(temp);
        normal.normalize();

        int tw = sprite.contents().width();
        int th = sprite.contents().height();


        Vector4f vv1 = new Vector4f(v1, 1.0f);
        Vector4f vv2 = new Vector4f(v2, 1.0f);
        Vector4f vv3 = new Vector4f(v3, 1.0f);
        Vector4f vv4 = new Vector4f(v4, 1.0f);

        BakedQuad[] quad = new BakedQuad[1];
        var builder = new QuadBakingVertexConsumer(q -> quad[0] = q);
        builder.setSprite(sprite);
        builder.setDirection(Direction.getNearest(normal.x(), normal.y(), normal.z()));
        putVertex(builder, normal, vv1, 0, 0, sprite);
        putVertex(builder, normal, vv2, 0, th, sprite);
        putVertex(builder, normal, vv3, tw, th, sprite);
        putVertex(builder, normal, vv4, tw, 0, sprite);
        return quad[0];
    }

    public static BakedQuad directionalFaceQuad(Direction direction, Transformation rotation, TextureAtlasSprite sprite) {
        switch (direction) {
            case DOWN:
                return createQuad(v(0,0,0),
                        v(1,0,0), v(1,0,1), v(0,0,1),rotation,sprite);
            case UP:
                return createQuad(v(1,1,0),
                        v(0,1,0), v(0,1,1), v(1,1,1), rotation,sprite);
            case NORTH:
                return createQuad(v(1,1,0),
                        v(1,0,0), v(0,0,0), v(0,1,0),rotation,sprite);
            case SOUTH:
                return createQuad(v(0,1,1),
                        v(0,0,1), v(1,0,1), v(1,1,1),rotation,sprite);
            case EAST:
                return createQuad(v(1,1,1),
                        v(1,0,1), v(1,0,0), v(1,1,0),rotation,sprite);
        }
            return createQuad(v(0,1,0),
                    v(0,0,0), v(0,0,1), v(0,1,1),rotation,sprite);
    }


    public static Vector3f v(float x, float y, float z) {
        return new Vector3f(x, y, z);
    }

}
