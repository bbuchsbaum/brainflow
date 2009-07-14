package brainflow.image.anatomy;

import brainflow.math.Matrix4f;
import brainflow.math.Vector3f;
import brainflow.image.space.PermutationMatrix3D;

import java.util.Arrays;
import java.util.List;


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
