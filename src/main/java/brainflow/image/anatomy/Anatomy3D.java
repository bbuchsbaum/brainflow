package brainflow.image.anatomy;

import brainflow.math.Matrix3f;
import brainflow.math.Matrix4f;
import brainflow.math.Vector3f;
import brainflow.image.space.PermutationMatrix3D;

import java.util.Arrays;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;


/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: Jul 1, 2003
 * Time: 9:16:40 AM
 * To change this template use Options | File Templates.
 */
public class Anatomy3D implements Anatomy {

    private static final Anatomy3D[] instances = new Anatomy3D[24];


    public static final Anatomy3D AXIAL_LPI = new Anatomy3D(AnatomicalOrientation.AXIAL, AnatomicalAxis.LEFT_RIGHT, AnatomicalAxis.POSTERIOR_ANTERIOR, AnatomicalAxis.INFERIOR_SUPERIOR);
    public static final Anatomy3D AXIAL_RPI = new Anatomy3D(AnatomicalOrientation.AXIAL, AnatomicalAxis.RIGHT_LEFT, AnatomicalAxis.POSTERIOR_ANTERIOR, AnatomicalAxis.INFERIOR_SUPERIOR);
    public static final Anatomy3D AXIAL_LAI = new Anatomy3D(AnatomicalOrientation.AXIAL, AnatomicalAxis.LEFT_RIGHT, AnatomicalAxis.ANTERIOR_POSTERIOR, AnatomicalAxis.INFERIOR_SUPERIOR);
    public static final Anatomy3D AXIAL_RAI = new Anatomy3D(AnatomicalOrientation.AXIAL, AnatomicalAxis.RIGHT_LEFT, AnatomicalAxis.ANTERIOR_POSTERIOR, AnatomicalAxis.INFERIOR_SUPERIOR);
    public static final Anatomy3D AXIAL_LAS = new Anatomy3D(AnatomicalOrientation.AXIAL, AnatomicalAxis.LEFT_RIGHT, AnatomicalAxis.ANTERIOR_POSTERIOR, AnatomicalAxis.SUPERIOR_INFERIOR);
    public static final Anatomy3D AXIAL_RAS = new Anatomy3D(AnatomicalOrientation.AXIAL, AnatomicalAxis.RIGHT_LEFT, AnatomicalAxis.ANTERIOR_POSTERIOR, AnatomicalAxis.SUPERIOR_INFERIOR);
    public static final Anatomy3D AXIAL_LPS = new Anatomy3D(AnatomicalOrientation.AXIAL, AnatomicalAxis.LEFT_RIGHT, AnatomicalAxis.POSTERIOR_ANTERIOR, AnatomicalAxis.SUPERIOR_INFERIOR);
    public static final Anatomy3D AXIAL_RPS = new Anatomy3D(AnatomicalOrientation.AXIAL, AnatomicalAxis.RIGHT_LEFT, AnatomicalAxis.POSTERIOR_ANTERIOR, AnatomicalAxis.SUPERIOR_INFERIOR);

    public static final Anatomy3D SAGITTAL_AIL = new Anatomy3D(AnatomicalOrientation.SAGITTAL, AnatomicalAxis.ANTERIOR_POSTERIOR, AnatomicalAxis.INFERIOR_SUPERIOR, AnatomicalAxis.LEFT_RIGHT);
    public static final Anatomy3D SAGITTAL_PIL = new Anatomy3D(AnatomicalOrientation.SAGITTAL, AnatomicalAxis.POSTERIOR_ANTERIOR, AnatomicalAxis.INFERIOR_SUPERIOR, AnatomicalAxis.LEFT_RIGHT);
    public static final Anatomy3D SAGITTAL_ASL = new Anatomy3D(AnatomicalOrientation.SAGITTAL, AnatomicalAxis.ANTERIOR_POSTERIOR, AnatomicalAxis.SUPERIOR_INFERIOR, AnatomicalAxis.LEFT_RIGHT);
    public static final Anatomy3D SAGITTAL_PSL = new Anatomy3D(AnatomicalOrientation.SAGITTAL, AnatomicalAxis.POSTERIOR_ANTERIOR, AnatomicalAxis.SUPERIOR_INFERIOR, AnatomicalAxis.LEFT_RIGHT);
    public static final Anatomy3D SAGITTAL_AIR = new Anatomy3D(AnatomicalOrientation.SAGITTAL, AnatomicalAxis.ANTERIOR_POSTERIOR, AnatomicalAxis.INFERIOR_SUPERIOR, AnatomicalAxis.RIGHT_LEFT);
    public static final Anatomy3D SAGITTAL_PIR = new Anatomy3D(AnatomicalOrientation.SAGITTAL, AnatomicalAxis.POSTERIOR_ANTERIOR, AnatomicalAxis.INFERIOR_SUPERIOR, AnatomicalAxis.RIGHT_LEFT);
    public static final Anatomy3D SAGITTAL_ASR = new Anatomy3D(AnatomicalOrientation.SAGITTAL, AnatomicalAxis.ANTERIOR_POSTERIOR, AnatomicalAxis.SUPERIOR_INFERIOR, AnatomicalAxis.RIGHT_LEFT);
    public static final Anatomy3D SAGITTAL_PSR = new Anatomy3D(AnatomicalOrientation.SAGITTAL, AnatomicalAxis.POSTERIOR_ANTERIOR, AnatomicalAxis.SUPERIOR_INFERIOR, AnatomicalAxis.RIGHT_LEFT);

    public static final Anatomy3D CORONAL_LIA = new Anatomy3D(AnatomicalOrientation.CORONAL, AnatomicalAxis.LEFT_RIGHT, AnatomicalAxis.INFERIOR_SUPERIOR, AnatomicalAxis.ANTERIOR_POSTERIOR);
    public static final Anatomy3D CORONAL_RIA = new Anatomy3D(AnatomicalOrientation.CORONAL, AnatomicalAxis.RIGHT_LEFT, AnatomicalAxis.INFERIOR_SUPERIOR, AnatomicalAxis.ANTERIOR_POSTERIOR);
    public static final Anatomy3D CORONAL_LSA = new Anatomy3D(AnatomicalOrientation.CORONAL, AnatomicalAxis.LEFT_RIGHT, AnatomicalAxis.SUPERIOR_INFERIOR, AnatomicalAxis.ANTERIOR_POSTERIOR);
    public static final Anatomy3D CORONAL_RSA = new Anatomy3D(AnatomicalOrientation.CORONAL, AnatomicalAxis.RIGHT_LEFT, AnatomicalAxis.SUPERIOR_INFERIOR, AnatomicalAxis.ANTERIOR_POSTERIOR);
    public static final Anatomy3D CORONAL_LIP = new Anatomy3D(AnatomicalOrientation.CORONAL, AnatomicalAxis.LEFT_RIGHT, AnatomicalAxis.INFERIOR_SUPERIOR, AnatomicalAxis.POSTERIOR_ANTERIOR);
    public static final Anatomy3D CORONAL_RIP = new Anatomy3D(AnatomicalOrientation.CORONAL, AnatomicalAxis.RIGHT_LEFT, AnatomicalAxis.INFERIOR_SUPERIOR, AnatomicalAxis.POSTERIOR_ANTERIOR);
    public static final Anatomy3D CORONAL_LSP = new Anatomy3D(AnatomicalOrientation.CORONAL, AnatomicalAxis.LEFT_RIGHT, AnatomicalAxis.SUPERIOR_INFERIOR, AnatomicalAxis.POSTERIOR_ANTERIOR);
    public static final Anatomy3D CORONAL_RSP = new Anatomy3D(AnatomicalOrientation.CORONAL, AnatomicalAxis.RIGHT_LEFT, AnatomicalAxis.SUPERIOR_INFERIOR, AnatomicalAxis.POSTERIOR_ANTERIOR);


    public static final Anatomy3D REFERENCE_ANATOMY = AXIAL_LPI;


    private AnatomicalOrientation orientation;

    public final AnatomicalAxis XAXIS;
    public final AnatomicalAxis YAXIS;
    public final AnatomicalAxis ZAXIS;

    public final Anatomy2D XY_PLANE;
    public final Anatomy2D XZ_PLANE;
    public final Anatomy2D YZ_PLANE;

    private static int count = 0;

    private final Matrix4f referenceTransform;


    private Anatomy3D(AnatomicalOrientation _orientation, AnatomicalAxis _xAxis, AnatomicalAxis _yAxis, AnatomicalAxis _zAxis) {
        orientation = _orientation;
        this.XAXIS = _xAxis;
        this.YAXIS = _yAxis;
        this.ZAXIS = _zAxis;

        XY_PLANE = Anatomy2D.matchAnatomy(XAXIS, YAXIS);
        XZ_PLANE = Anatomy2D.matchAnatomy(XAXIS, ZAXIS);
        YZ_PLANE = Anatomy2D.matchAnatomy(YAXIS, ZAXIS);

        Vector3f v1 = XAXIS.getDirectionVector();
        Vector3f v2 = YAXIS.getDirectionVector();
        Vector3f v3 = ZAXIS.getDirectionVector();

        referenceTransform = new Matrix4f(v1.getX(), v1.getY(), v1.getZ(), 0, v2.getX(), v2.getY(), v2.getZ(), 0, v3.getX(), v3.getY(), v3.getZ(), 0, 0, 0, 0, 1);

        instances[count] = this;
        count++;
    }

    public AnatomicalAxis[] getAnatomicalAxes() {
        return new AnatomicalAxis[]{XAXIS, YAXIS, ZAXIS};
    }


    public AnatomicalAxis matchAxis(AnatomicalAxis axis) {
        if (axis.sameAxis(XAXIS)) return XAXIS;
        if (axis.sameAxis(YAXIS)) return YAXIS;
        if (axis.sameAxis(ZAXIS)) return ZAXIS;

        return null;

    }

    public static Anatomy3D getCanonicalSagittal() {
        return SAGITTAL_ASL;
    }

    public static Anatomy3D getCanonicalCoronal() {
        return CORONAL_LSA;
    }

    public static Anatomy3D getCanonicalAxial() {
        return AXIAL_LAI;
    }

    public Matrix4f getReferenceTransform() {
        return referenceTransform;
    }

    public static List<Anatomy3D> getInstanceList() {
        return Arrays.asList(instances);
    }


    public Anatomy3D[] getCanonicalOrthogonal() {
        if (isAxial()) {
            return new Anatomy3D[]{this, SAGITTAL_ASL, CORONAL_LSA};
        }
        if (isSagittal()) {
            return new Anatomy3D[]{this, AXIAL_LAI, CORONAL_LSA};
        }
        if (isCoronal()) {
            return new Anatomy3D[]{this, AXIAL_LAI, SAGITTAL_ASL};
        } else {
            // never here
            throw new RuntimeException("Anatomy3D.getCanonicalOrthogonal(...) : reached ureachable else");
        }

    }

    public boolean isAxial() {
        if (orientation == AnatomicalOrientation.AXIAL)
            return true;
        return false;
    }

    public boolean isSagittal() {
        if (orientation == AnatomicalOrientation.SAGITTAL)
            return true;
        return false;
    }

    public boolean isCoronal() {
        if (orientation == AnatomicalOrientation.CORONAL)
            return true;
        return false;
    }


    public AnatomicalOrientation getOrientation() {
        return orientation;
    }

    public static Anatomy3D matchAnatomy(AnatomicalDirection a1, AnatomicalDirection a2, AnatomicalDirection a3) {

        for (int i = 0; i < instances.length; i++) {
            Anatomy3D tmp = instances[i];
            if ((tmp.XAXIS.getMinDirection() == a1) && (tmp.YAXIS.getMinDirection() == a2) && (tmp.ZAXIS.getMinDirection() == a3)) {
                return tmp;
            }

        }


        throw new IllegalArgumentException("Axes do not correspond to valid anatomical volume ");

    }

    public static Anatomy3D matchAnatomy(AnatomicalAxis a1, AnatomicalAxis a2, AnatomicalAxis a3) {

        for (int i = 0; i < instances.length; i++) {
            Anatomy3D tmp = instances[i];
            if ((tmp.XAXIS == a1) && (tmp.YAXIS == a2) && (tmp.ZAXIS == a3)) {
                return tmp;
            }

        }

        throw new IllegalArgumentException("Axes do not correspond to valid anatomical volume ");

    }


    

    public static Anatomy3D[] getAxialFamily() {
        Anatomy3D[] rets = new Anatomy3D[8];
        int count = 0;
        for (int i = 0; i < instances.length; i++) {
            if (instances[i].getOrientation() == AnatomicalOrientation.AXIAL) {
                rets[count] = instances[i];
                count++;
            }
        }

        return rets;
    }

    public static Anatomy3D[] getSagittalFamily() {
        Anatomy3D[] rets = new Anatomy3D[8];
        int count = 0;
        for (int i = 0; i < instances.length; i++) {
            if (instances[i].getOrientation() == AnatomicalOrientation.SAGITTAL) {
                rets[count] = instances[i];
                count++;
            }
        }

        return rets;
    }

    public static Anatomy3D[] getCoronalFamily() {
        Anatomy3D[] rets = new Anatomy3D[8];
        int count = 0;
        for (int i = 0; i < instances.length; i++) {
            if (instances[i].getOrientation() == AnatomicalOrientation.CORONAL) {
                rets[count] = instances[i];
                count++;
            }
        }

        return rets;
    }

public static Anatomy3D nearestAnatomy(Matrix4f R) {
        Matrix3f P = new Matrix3f();
        Matrix3f Q = new Matrix3f();


        float xi = R.m00;
        float xj = R.m01;
        float xk = R.m02;
        float yi = R.m10;
        float yj = R.m11;
        float yk = R.m12;
        float zi = R.m20;
        float zj = R.m21;
        float zk = R.m22;

        /* normalize column vectors to get unit vectors along each ijk-axis */

        /* normalize i axis */

        float val = (float) sqrt(xi * xi + yi * yi + zi * zi);
        if (val == 0.0) throw new IllegalArgumentException("invalid Matrix input");           /* stupid input */
        xi /= val;
        yi /= val;
        zi /= val;

        /* normalize j axis */

        val = (float) sqrt(xj * xj + yj * yj + zj * zj);
        if (val == 0.0) throw new IllegalArgumentException("invalid Matrix input");
        xj /= val;
        yj /= val;
        zj /= val;

        /* orthogonalize j axis to i axis, if needed */

        val = xi * xj + yi * yj + zi * zj;    /* dot product between i and j */
        if (abs(val) > 1.e-4) {
            xj -= val * xi;
            yj -= val * yi;
            zj -= val * zi;
            val = (float) sqrt(xj * xj + yj * yj + zj * zj);  /* must renormalize */
            if (val == 0.0) throw new IllegalArgumentException("invalid Matrix input");
            xj /= val;
            yj /= val;
            zj /= val;
        }

        /* normalize k axis; if it is zero, make it the cross product i x j */

        val = (float) sqrt(xk * xk + yk * yk + zk * zk);
        if (val == 0.0) {
            xk = yi * zj - zi * yj;
            yk = zi * xj - zj * xi;
            zk = xi * yj - yi * xj;
        } else {
            xk /= val;
            yk /= val;
            zk /= val;
        }

        /* orthogonalize k to i */

        val = xi * xk + yi * yk + zi * zk;    /* dot product between i and k */
        if (abs(val) > 1.e-4) {
            xk -= val * xi;
            yk -= val * yi;
            zk -= val * zi;
            val = (float) sqrt(xk * xk + yk * yk + zk * zk);
            if (val == 0.0) throw new IllegalArgumentException("invalid Matrix input");
            xk /= val;
            yk /= val;
            zk /= val;
        }

        /* orthogonalize k to j */

        val = xj * xk + yj * yk + zj * zk;    /* dot product between j and k */
        if (abs(val) > 1.e-4) {
            xk -= val * xj;
            yk -= val * yj;
            zk -= val * zj;
            val = (float) sqrt(xk * xk + yk * yk + zk * zk);
            if (val == 0.0) throw new IllegalArgumentException("invalid Matrix input");
            xk /= val;
            yk /= val;
            zk /= val;
        }

        Q.m00 = xi;
        Q.m01 = xj;
        Q.m02 = xk;
        Q.m10 = yi;
        Q.m11 = yj;
        Q.m12 = yk;
        Q.m20 = zi;
        Q.m21 = zj;
        Q.m22 = zk;

        /* at this point, Q is the rotation matrix from the (i,j,k) to (x,y,z) axes */

        float detQ = Q.determinant();
        if (detQ == 0.0) throw new IllegalArgumentException("invalid Matrix input");

        /* Build and test all possible +1/-1 coordinate permutation matrices P;
           then find the P such that the rotation matrix M=PQ is closest to the
           identity, in the sense of M having the smallest total rotation angle. */

        /* Despite the formidable looking 6 nested loops, there are
           only 3*3*3*2*2*2 = 216 passes, which will label very quickly. */

        float vbest = -666f;
        float ibest = 1;
        float pbest = 1;
        float qbest = 1;
        float rbest = 1;

        float jbest = 2;
        float kbest = 3;
        for (int i = 1; i <= 3; i++) {     /* i = column number to use for row #1 */
            for (int j = 1; j <= 3; j++) {    /* j = column number to use for row #2 */
                if (i == j) continue;
                for (int k = 1; k <= 3; k++) {  /* k = column number to use for row #3 */
                    if (i == k || j == k) continue;
                    P.m00 = P.m01 = P.m02 =
                            P.m10 = P.m11 = P.m12 =
                                    P.m20 = P.m21 = P.m22 = 0f;
                    for (int p = -1; p <= 1; p += 2) {    /* p,q,r are -1 or +1      */
                        for (int q = -1; q <= 1; q += 2) {   /* and go into rows #1,2,3 */
                            for (int r = -1; r <= 1; r += 2) {
                                P.set(0, i - 1, p);
                                P.set(1, j - 1, q);
                                P.set(2, k - 1, r);

                                float detP = P.determinant();           /* sign of permutation */
                                if (detP * detQ <= 0.0) continue;  /* doesn't match sign of Q */
                                Matrix3f M = P.mult(Q);
                                //M = nifti_mat33_mul(P,Q) ;

                                /* angle of M rotation = 2.0*acos(0.5*sqrt(1.0+trace(M)))       */
                                /* we want largest trace(M) == smallest angle == M nearest to I */

                                val = M.m00 + M.m11 + M.m22; /* trace */
                                if (val > vbest) {
                                    vbest = val;
                                    ibest = i;
                                    jbest = j;
                                    kbest = k;
                                    pbest = p;
                                    qbest = q;
                                    rbest = r;
                                }
                            }
                        }
                    }
                }
            }
        }

        /** At this point ibest is 1 or 2 or 3; pbest is -1 or +1; etc.
         The matrix P that corresponds is the best permutation approximation
         to Q-inverse; that is, P (approximately) takes (x,y,z) coordinates
         to the (i,j,k) axes.

         For example, the first row of P (which contains pbest in column ibest)
         determines the way the i axis points relative to the anatomical
         (x,y,z) axes.  If ibest is 2, then the i axis is along the y axis,
         which is direction P2A (if pbest > 0) or A2P (if pbest < 0).

         So, using ibest and pbest, we can assign the output code for
         the i axis.  Mutatis mutandis for the j and k axes, of course. **/

        AnatomicalAxis i = null;
        AnatomicalAxis j = null;
        AnatomicalAxis k = null;


        switch ((int) (ibest * pbest)) {
            case 1:
                i = AnatomicalAxis.LEFT_RIGHT;
                break;
            case -1:
                i = AnatomicalAxis.RIGHT_LEFT;
                break;
            case 2:
                i = AnatomicalAxis.POSTERIOR_ANTERIOR;
                break;
            case -2:
                i = AnatomicalAxis.ANTERIOR_POSTERIOR;
                break;
            case 3:
                i = AnatomicalAxis.INFERIOR_SUPERIOR;
                break;
            case -3:
                i = AnatomicalAxis.SUPERIOR_INFERIOR;
                break;
        }

        switch ((int) (jbest * qbest)) {
            case 1:
                j = AnatomicalAxis.LEFT_RIGHT;
                break;
            case -1:
                j = AnatomicalAxis.RIGHT_LEFT;
                break;
            case 2:
                j = AnatomicalAxis.POSTERIOR_ANTERIOR;
                break;
            case -2:
                j = AnatomicalAxis.ANTERIOR_POSTERIOR;
                break;
            case 3:
                j = AnatomicalAxis.INFERIOR_SUPERIOR;
                break;
            case -3:
                j = AnatomicalAxis.SUPERIOR_INFERIOR;
                break;
        }

        switch ((int) (kbest * rbest)) {
            case 1:
                k = AnatomicalAxis.LEFT_RIGHT;
                break;
            case -1:
                k = AnatomicalAxis.RIGHT_LEFT;
                break;
            case 2:
                k = AnatomicalAxis.POSTERIOR_ANTERIOR;
                break;
            case -2:
                k = AnatomicalAxis.ANTERIOR_POSTERIOR;
                break;
            case 3:
                k = AnatomicalAxis.INFERIOR_SUPERIOR;
                break;
            case -3:
                k = AnatomicalAxis.SUPERIOR_INFERIOR;
                break;
        }

        return Anatomy3D.matchAnatomy(i, j, k);

    }
    


    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(XAXIS.getMinDirection().toString());
        sb.append("-");
        sb.append(YAXIS.getMinDirection().toString());
        sb.append("-");
        sb.append(ZAXIS.getMinDirection().toString());
        //sb.append("matrix : " + referenceTransform);

        return sb.toString();

    }


}
