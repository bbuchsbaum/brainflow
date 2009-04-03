package brainflow.image.space;

import brainflow.math.Vector2f;
import brainflow.math.Matrix4f;
import brainflow.math.Matrix3f;
import brainflow.math.Vector3f;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.anatomy.Anatomy2D;

import java.awt.geom.Point2D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Mar 30, 2009
 * Time: 11:08:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class AffineMapping2D implements ImageMapping2D {

    private final Matrix3f mat;

    private final Matrix3f invMat;

    public AffineMapping2D(Matrix3f mat) {
        this.mat = mat;
        invMat = mat.invert();
    }

    public AffineMapping2D(Vector2f offset, Vector2f spacing, Anatomy2D anatomy) {
        offset.x = anatomy.XAXIS.getDirectionVector().getX() * offset.x;
        offset.y = anatomy.YAXIS.getDirectionVector().getY() * offset.y;

        Matrix3f tmp = new Matrix3f(spacing.x, 0, offset.x, 0, spacing.y, offset.y, 0, 0, 1);
        Matrix3f ref = anatomy.getReferenceTransform();


        mat = tmp.scale(new Vector3f(ref.m00, ref.m11, ref.m22));
        invMat = mat.invert();
    }


    public Matrix3f getMatrix() {
        return new Matrix3f(mat);
    }

    public Vector2f getOrigin() {
        return mat.toTranslationVector();
    }


    public Vector2f gridToWorld(int i, int j, Vector2f out) {
        return mat.mult2f(i,j,out);
    }

    public Vector2f gridToWorld(int i, int j) {
       return mat.mult2f(i,j,new Vector2f());
    }

    public Vector2f gridToWorld(float i, float j) {
        return mat.mult2f(i,j,new Vector2f());
    }

    public Vector2f gridToWorld(float i, float j, Vector2f out) {
        return mat.mult2f(i,j,out);
    }

    public Vector2f gridToWorld(Vector2f in) {
        throw new UnsupportedOperationException();
    }

    public Vector2f worldToGrid(Vector2f in, Vector2f out) {
       return invMat.mult2f(in.x, in.y, out);
    }

    public Vector2f worldToGrid(Vector2f in) {
        return invMat.mult(in, new Vector2f());
    }

    public float worldToGridX(float x, float y) {
        return invMat.multX(x,y);
    }

    public float worldToGridY(float x, float y) {
        return invMat.multY(x,y);
    }

    public float gridToWorldX(float x, float y) {
        return mat.multX(x,y);
    }

    public float gridToWorldY(float x, float y) {
        return mat.multY(x,y);

    }
}
