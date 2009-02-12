package brainflow.image.space;

import brainflow.image.anatomy.Anatomy3D;
import brainflow.math.Matrix4f;
import brainflow.math.Vector3f;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Apr 19, 2008
 * Time: 11:53:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class AffineMapping3D implements ImageMapping3D {

    private final Matrix4f mat;

    private final Matrix4f invMat;

    public AffineMapping3D(Matrix4f mat) {
        this.mat = mat;
        invMat = mat.invert();
    }

    public AffineMapping3D(Vector3f offset, Vector3f spacing, Anatomy3D anatomy) {
        offset.x = anatomy.XAXIS.getDirectionVector().getX() * offset.x;
        offset.y = anatomy.YAXIS.getDirectionVector().getY() * offset.y;
        offset.z = anatomy.ZAXIS.getDirectionVector().getZ() * offset.z;

        Matrix4f tmp = new Matrix4f(spacing.x, 0, 0, offset.x, 0, spacing.y, 0, offset.y, 0, 0, spacing.z, offset.z, 0, 0, 0, 1);
        Matrix4f ref = anatomy.getReferenceTransform();


        mat = tmp.scale(new Vector3f(ref.m00, ref.m11, ref.m22));
        invMat = mat.invert();
    }

    public Matrix4f getMatrix() {
        return new Matrix4f(mat);
    }

   
    public Vector3f getOrigin() {
        return mat.toTranslationVector();
    }


    public Vector3f gridToWorld(int i, int j, int k, Vector3f out) {
        return mat.mult3f(i,j,k, out);
    }


    public Vector3f gridToWorld(Vector3f in) {
        return mat.mult(in, new Vector3f());
    }


    public Vector3f gridToWorld(float i, float j, float k) {
        return mat.mult3f(i,j,k, new Vector3f());
    }


    public Vector3f gridToWorld(float i, float j, float k, Vector3f out) {
        return mat.mult3f(i,j,k, out);
    }


    public Vector3f gridToWorld(int i, int j, int k) {
        return mat.mult3f(i,j,k, new Vector3f());
    }


    public Vector3f worldToGrid(Vector3f in, Vector3f out) {
        return invMat.mult3f(in.x, in.y, in.z, out);
    }


    public Vector3f worldToGrid(Vector3f in) {
        return invMat.mult(in, new Vector3f());
    }


    public final float gridToWorldX(float x, float y, float z) {
        return mat.multX(x,y,z);
    }


    public final float gridToWorldY(float x, float y, float z) {
        return mat.multY(x,y,z);
    }


    public final float gridToWorldZ(float x, float y, float z) {
        return mat.multZ(x,y,z);
    }


    public final float worldToGridX(float x, float y, float z) {
        return invMat.multX(x,y,z);
    }


    public final float worldToGridY(float x, float y, float z) {
        return invMat.multY(x,y,z);
    }

   
    public final float worldToGridZ(float x, float y, float z) {
        return invMat.multZ(x,y,z);
    }

    public String toString() {
        return mat.toString();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AffineMapping3D)) return false;

        AffineMapping3D that = (AffineMapping3D) o;

        if (mat != null ? !mat.equals(that.mat) : that.mat != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (mat != null ? mat.hashCode() : 0);
        result = 31 * result + (invMat != null ? invMat.hashCode() : 0);
        return result;
    }
}
