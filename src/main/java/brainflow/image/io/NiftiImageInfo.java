package brainflow.image.io;

import brainflow.image.io.ImageInfo;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.anatomy.AnatomicalAxis;
import brainflow.math.Matrix4f;
import brainflow.math.Vector3f;
import brainflow.math.Matrix3f;

import static java.lang.Math.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Aug 15, 2006
 * Time: 10:22:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class NiftiImageInfo extends ImageInfo {

    private int qfac = 1;



    private Matrix4f qform = new Matrix4f();

    private Matrix4f sform = new Matrix4f();

    private Vector3f quaternion = new Vector3f();

    private Vector3f qoffset = new Vector3f();

    //todo provide reasonable defaults?


    private NiftiImageInfo() {
    }

    public NiftiImageInfo(NiftiImageInfo info) {
        super(info);
        qfac = info.qfac;
        qform = info.qform;
        sform = info.sform;
        quaternion = info.quaternion;
        qoffset = info.qoffset;
    }

    public NiftiImageInfo(NiftiImageInfo info, String _imageLabel, int index) {
        super(info, _imageLabel, index);
        qfac = info.qfac;
        qform = info.qform;
        sform = info.sform;
        quaternion = info.quaternion;
        qoffset = info.qoffset;
    }

    public NiftiImageInfo selectInfo(int index) {
        if (index < 0 || index >= getNumImages()) {
            throw new IllegalArgumentException("illegal selection index for image info with " + getNumImages() + " sub images");
        }

        String name = getHeaderFile().getName().getBaseName();

        int idx = name.indexOf(".");

        if (idx > 0) {
            name = name.substring(0, name.indexOf("."));
        }

        return new NiftiImageInfo(this, name + ":" + index, index);

    }

    @Override
    public ImageReader createImageReader() {
        return new NiftiImageReader(this);
    }

    public static class Builder extends ImageInfo.Builder {

        public Builder() {
            super(new NiftiImageInfo());
        }

        private NiftiImageInfo info() {
            return (NiftiImageInfo) super.info;
        }

        public Builder qform(Matrix4f qform) {
            info().qform = qform;
            return this;
        }

        public Builder sform(Matrix4f sform) {
            info().sform = sform;
            return this;
        }

        public Builder qfac(int qfac) {
            info().qfac = qfac;
            return this;
        }

        public Builder quaternion(Vector3f quaternion) {
            info().quaternion = quaternion;
            return this;
        }

        public Builder qoffset(Vector3f qoffset) {
            info().qoffset = qoffset;
            return this;
        }

        @Override
        public NiftiImageInfo build() {
            this.checkBuilt();
            this.isBuilt = true;
            return info();
        }
    }

    public int getQfac() {
        return qfac;
    }

    public Matrix4f getQform() {
        return qform;
    }

    public Matrix4f getSform() {
        return sform;
    }

    public Vector3f getQuaternion() {
        return quaternion;
    }

    public Vector3f getQoffset() {
        return qoffset;
    }

    public static Matrix4f quaternionToMatrix(float qb, float qc, float qd,
                                              float qx, float qy, float qz,
                                              float dx, float dy, float dz, float qfac) {

        Matrix4f R = new Matrix4f();
        double a, b = qb, c = qc, d = qd, xd, yd, zd;

        /* last row is always [ 0 0 0 1 ] */

        //R.m[3][0]=R.m[3][1]=R.m[3][2] = 0.0 ; R.m[3][3]= 1.0 ;
        R.m30 = R.m31 = R.m32 = 0;
        R.m33 = 1f;

        /* compute a parameter from b,c,d */

        a = 1.0 - (b * b + c * c + d * d);
        if (a < 1.e-7) {                   /* special case */
            a = 1.0 / Math.sqrt(b * b + c * c + d * d);
            b *= a;
            c *= a;
            d *= a;        /* normalize (b,c,d) vector */
            a = 0.0;                        /* a = 0 ==> 180 degree rotation */
        } else {
            a = Math.sqrt(a);                     /* angle = 2*arccos(a) */
        }

        /* load rotation matrix, including scaling factors for voxel sizes */

        xd = (dx > 0.0) ? dx : 1.0;       /* make sure are positive */
        yd = (dy > 0.0) ? dy : 1.0;
        zd = (dz > 0.0) ? dz : 1.0;

        if (qfac < 0.0) zd = -zd;         /* left handedness? */

        R.m00 = (float) ((a * a + b * b - c * c - d * d) * xd);
        R.m01 = (float) (2.0 * (b * c - a * d) * yd);
        R.m02 = (float) (2.0 * (b * d + a * c) * zd);
        R.m10 = (float) (2.0 * (b * c + a * d) * xd);
        R.m11 = (float) ((a * a + c * c - b * b - d * d) * yd);
        R.m12 = (float) (2.0 * (c * d - a * b) * zd);
        R.m20 = (float) (2.0 * (b * d - a * c) * xd);
        R.m21 = (float) (2.0 * (c * d + a * b) * yd);
        R.m22 = (float) ((a * a + d * d - c * c - b * b) * zd);

        /* load offsets */

        R.m03 = qx;
        R.m13 = qy;
        R.m23 = qz;

        return R;
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
           only 3*3*3*2*2*2 = 216 passes, which will run very quickly. */

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
        return super.toString() + "\n" + "NiftiImageInfo{" +
                "qfac=" + qfac +
                ", qform=" + qform +
                ", sform=" + sform +
                ", quaternion=" + quaternion +
                ", qoffset=" + qoffset +
                '}';
    }
}



