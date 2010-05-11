package brainflow.image.io;


import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.anatomy.AnatomicalAxis;
import brainflow.math.Matrix4f;
import brainflow.math.Vector3f;
import brainflow.math.Matrix3f;
import brainflow.utils.DataType;

import static java.lang.Math.*;
import java.util.*;

import org.apache.commons.vfs.FileObject;

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

    private List<Extension> extensionList = new ArrayList<Extension>();

    public static final String[] validHeaderExtensions = { ".nii", ".nii.gz", ".hdr", ".hdr.gz"};

    public static final String[] validImageExtensions = { ".nii", ".nii.gz", ".img", ".img.gz" };

    public static final Map<String, String> validExtensionPairs = new HashMap<String, String>();



    static {
        validExtensionPairs.put(".hdr", ".img");
        validExtensionPairs.put(".hdr.gz", ".img.gz");
        validExtensionPairs.put(".nii", ".nii");
        validExtensionPairs.put(".nii.gz", ".nii.gz");
    }


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
        if (index < 0 || index >= getNumVolumes()) {
            throw new IllegalArgumentException("illegal selection index for image info with " + getNumVolumes() + " sub images");
        }

        String name = getHeaderFile().getName().getBaseName();

        int idx = name.indexOf(".");

        if (idx > 0) {
            name = name.substring(0, name.indexOf("."));
        }

        return new NiftiImageInfo(this, name + ":" + index, index);

    }

    public NiftiImageInfo copy(FileObject newHeaderFile, FileObject newDataFile) {
        if (!NiftiImageInfo.areCompatible(newHeaderFile, newDataFile)) {
            throw new IllegalArgumentException("header " + newHeaderFile + " and image" + newDataFile + " are not compatible");
        }


        //try {
            //if (newHeaderFile.exists()) {
            //    throw new IllegalArgumentException("header file " + newHeaderFile + " already exists");
            //}
            //if (newDataFile.exists()) {
            //    throw new IllegalArgumentException("image file " + newDataFile + " already exists");
            //}

                     
            NiftiImageInfo.Builder builder = new NiftiImageInfo.Builder(this);
            builder.headerFile(newHeaderFile);
            builder.dataFile(newDataFile);
            builder.qfac(qfac);
            builder.qform(qform);
            builder.sform(sform);
            builder.quaternion(quaternion);
            builder.extensions(extensionList);

            return builder.build();

        //} catch(IOException e) {
        //    throw new IllegalArgumentException(e);
        //}
    }

    @Override
    public ImageReader createImageReader() {
        return new NiftiImageReader(this);
    }

    public boolean hasExtensions() {
        return extensionList.size() > 0;
    }

    public List<Extension> getExtensionList() {
        return extensionList;
    }

    public static boolean isHeaderFile(String name) {
        if (ImageInfo.isValidExtension(name, validHeaderExtensions)) {
            return true;
        }

        return false;
    }

    public static boolean isImageFile(String name) {
        if (ImageInfo.isValidExtension(name, validImageExtensions)) {
            return true;
        }

        return false;
    }

    public static String getHeaderName(String name, String defaultExtension) {
        Set<String> keyset = validExtensionPairs.keySet();
        for (String headerExt : keyset) {
            String imageExt = validExtensionPairs.get(headerExt);
            if (name.endsWith(headerExt)) {
                return name;
            } else if (name.endsWith(imageExt)) {
                return name.substring(0, name.length() - imageExt.length()) + headerExt;
            }
        }

        return name + defaultExtension;
    }

    public static String getImageName(String name, String defaultExtension) {
        Set<String> keySet = validExtensionPairs.keySet();
        for (String headerExt : keySet) {
            String imageExt = validExtensionPairs.get(headerExt);
            if (name.endsWith(headerExt)) {
                return name.substring(0, name.length() - headerExt.length()) + imageExt;
            } else if (name.endsWith(imageExt)) {
                return name;
            }
        }

        return name + defaultExtension;

    }

    public static boolean areCompatible(String headerName, String imageName) {

        if (!(isHeaderFile(headerName) && isImageFile(imageName))) {
            return false;
        }

        Set<String> keySet = validExtensionPairs.keySet();
        for (String headerExt : keySet) {
            String imageExt = validExtensionPairs.get(headerExt);
            if (headerName.endsWith(headerExt) && imageName.endsWith(imageExt)) {
                return true;
            }
        }

        return true;
    }


    public static boolean areCompatible(FileObject header, FileObject image) {
        String headerName = header.getName().getPath();
        String imageName = image.getName().getPath();

        return NiftiImageInfo.areCompatible(headerName, imageName);
    }

    public static short getDataTypeCode(DataType datatype) {
        switch (datatype) {
            case BOOLEAN:
                return Nifti1Dataset.NIFTI_TYPE_INT8;
            case BYTE:
                return Nifti1Dataset.NIFTI_TYPE_INT8;
            case DOUBLE:
                return Nifti1Dataset.NIFTI_TYPE_FLOAT64;
            case FLOAT:
                return Nifti1Dataset.NIFTI_TYPE_FLOAT32;
            case INTEGER:
                return Nifti1Dataset.NIFTI_TYPE_INT32;
            case LONG:
                return Nifti1Dataset.NIFTI_TYPE_INT64;
            case SHORT:
                return Nifti1Dataset.NIFTI_TYPE_INT16;
            case UBYTE:
                return Nifti1Dataset.NIFTI_TYPE_UINT8;
            default:
                throw new IllegalArgumentException("cannot encode data type: " + datatype);
        }

    }

    public static DataType getDataType(short datatype) {
        DataType dtype;

        switch (datatype) {
            case Nifti1Dataset.NIFTI_TYPE_UINT8:
                dtype = DataType.UBYTE;
                break;
            case Nifti1Dataset.NIFTI_TYPE_INT8:
                dtype = DataType.BYTE;
                break;
            case Nifti1Dataset.NIFTI_TYPE_INT16:
                dtype = DataType.SHORT;
                break;
            case Nifti1Dataset.NIFTI_TYPE_INT32:
                dtype = DataType.INTEGER;
                break;
            case Nifti1Dataset.NIFTI_TYPE_FLOAT32:
                dtype = DataType.FLOAT;
                break;
            case Nifti1Dataset.NIFTI_TYPE_FLOAT64:
                dtype = DataType.DOUBLE;
                break;
            case Nifti1Dataset.NIFTI_TYPE_INT64:
                dtype = DataType.LONG;
                break;
            case Nifti1Dataset.NIFTI_TYPE_UINT16:
                throw new IllegalArgumentException("Do not support NIFTI_TYPE_UINT16 dataType");
            case Nifti1Dataset.NIFTI_TYPE_UINT32:
                throw new IllegalArgumentException("Do not support NIFTI_TYPE_UINT32 dataType");
            case Nifti1Dataset.NIFTI_TYPE_UINT64:
                throw new IllegalArgumentException("Do not support NIFTI_TYPE_UINT64 dataType");
            case Nifti1Dataset.NIFTI_TYPE_RGB24:
                throw new IllegalArgumentException("Do not support NIFTI_TYPE_RGB24 dataType");
            default:
                throw new IllegalArgumentException("Do not support NIFTI_TYPE " + datatype);

        }

        return dtype;

    }

    public static class Builder extends ImageInfo.Builder {

        public Builder() {
            super(new NiftiImageInfo());
        }

        public Builder(NiftiImageInfo info) {
            super(new NiftiImageInfo(info));
            qform(info.qform);
            sform(info.sform);
            quaternion(info.quaternion);
            qfac(info.qfac);
            qoffset(info.qoffset);
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

        public Builder extensions(List<Extension> extlist) {
            info().extensionList = extlist;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NiftiImageInfo that = (NiftiImageInfo) o;

        if (qfac != that.qfac) return false;
        if (!qform.equals(that.qform)) return false;
        if (!qoffset.equals(that.qoffset)) return false;
        if (!quaternion.equals(that.quaternion)) return false;
        if (!sform.equals(that.sform)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = qfac;
        result = 31 * result + qform.hashCode();
        result = 31 * result + sform.hashCode();
        result = 31 * result + quaternion.hashCode();
        result = 31 * result + qoffset.hashCode();
        return result;
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


    public static class Extension {
        public static final int ECODE_UNKNOWN = 0;
        public static final int ECODE_DICOM = 2;
        public static final int ECODE_AFNI = 4;

        public Extension(int esize, int ecode, byte[] blob) {
            this.esize = esize;
            this.ecode = ecode;
            this.blob = blob;
        }

        int esize;

        int ecode;

        byte[] blob;
        
    }
}



