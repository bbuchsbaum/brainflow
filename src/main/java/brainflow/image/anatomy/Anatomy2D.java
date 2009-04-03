package brainflow.image.anatomy;

import brainflow.image.anatomy.AnatomicalAxis;
import brainflow.image.anatomy.Anatomy;
import brainflow.image.IndexConverter2D;
import brainflow.image.IndexConverter1D;
import brainflow.math.Matrix4f;
import brainflow.math.Matrix3f;
import brainflow.math.Vector3f;

/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: Jun 30, 2003
 * Time: 12:35:21 PM
 * To change this template use Options | File Templates.
 */
public class Anatomy2D implements Anatomy {

    private static Anatomy2D[] instances = new Anatomy2D[24];

    public static final Anatomy2D SAGITTAL_AI = new Anatomy2D(AnatomicalOrientation.SAGITTAL, AnatomicalAxis.ANTERIOR_POSTERIOR, AnatomicalAxis.INFERIOR_SUPERIOR);
    public static final Anatomy2D SAGITTAL_PI = new Anatomy2D(AnatomicalOrientation.SAGITTAL, AnatomicalAxis.POSTERIOR_ANTERIOR, AnatomicalAxis.INFERIOR_SUPERIOR);
    public static final Anatomy2D SAGITTAL_PS = new Anatomy2D(AnatomicalOrientation.SAGITTAL, AnatomicalAxis.POSTERIOR_ANTERIOR, AnatomicalAxis.SUPERIOR_INFERIOR);
    public static final Anatomy2D SAGITTAL_AS = new Anatomy2D(AnatomicalOrientation.SAGITTAL, AnatomicalAxis.ANTERIOR_POSTERIOR, AnatomicalAxis.SUPERIOR_INFERIOR);
    public static final Anatomy2D SAGITTAL_IA = new Anatomy2D(AnatomicalOrientation.SAGITTAL, AnatomicalAxis.INFERIOR_SUPERIOR, AnatomicalAxis.ANTERIOR_POSTERIOR);
    public static final Anatomy2D SAGITTAL_IP = new Anatomy2D(AnatomicalOrientation.SAGITTAL, AnatomicalAxis.INFERIOR_SUPERIOR, AnatomicalAxis.POSTERIOR_ANTERIOR);
    public static final Anatomy2D SAGITTAL_SP = new Anatomy2D(AnatomicalOrientation.SAGITTAL, AnatomicalAxis.SUPERIOR_INFERIOR, AnatomicalAxis.POSTERIOR_ANTERIOR);
    public static final Anatomy2D SAGITTAL_SA = new Anatomy2D(AnatomicalOrientation.SAGITTAL, AnatomicalAxis.SUPERIOR_INFERIOR, AnatomicalAxis.ANTERIOR_POSTERIOR);

    public static final Anatomy2D CORONAL_LI = new Anatomy2D(AnatomicalOrientation.CORONAL, AnatomicalAxis.LEFT_RIGHT, AnatomicalAxis.INFERIOR_SUPERIOR);
    public static final Anatomy2D CORONAL_RI = new Anatomy2D(AnatomicalOrientation.CORONAL, AnatomicalAxis.RIGHT_LEFT, AnatomicalAxis.INFERIOR_SUPERIOR);
    public static final Anatomy2D CORONAL_RS = new Anatomy2D(AnatomicalOrientation.CORONAL, AnatomicalAxis.RIGHT_LEFT, AnatomicalAxis.SUPERIOR_INFERIOR);
    public static final Anatomy2D CORONAL_LS = new Anatomy2D(AnatomicalOrientation.CORONAL, AnatomicalAxis.LEFT_RIGHT, AnatomicalAxis.SUPERIOR_INFERIOR);
    public static final Anatomy2D CORONAL_IL = new Anatomy2D(AnatomicalOrientation.CORONAL, AnatomicalAxis.INFERIOR_SUPERIOR, AnatomicalAxis.LEFT_RIGHT);
    public static final Anatomy2D CORONAL_IR = new Anatomy2D(AnatomicalOrientation.CORONAL, AnatomicalAxis.INFERIOR_SUPERIOR, AnatomicalAxis.RIGHT_LEFT);
    public static final Anatomy2D CORONAL_SR = new Anatomy2D(AnatomicalOrientation.CORONAL, AnatomicalAxis.SUPERIOR_INFERIOR, AnatomicalAxis.RIGHT_LEFT);
    public static final Anatomy2D CORONAL_SL = new Anatomy2D(AnatomicalOrientation.CORONAL, AnatomicalAxis.SUPERIOR_INFERIOR, AnatomicalAxis.LEFT_RIGHT);


    public static final Anatomy2D AXIAL_LA = new Anatomy2D(AnatomicalOrientation.AXIAL, AnatomicalAxis.LEFT_RIGHT, AnatomicalAxis.ANTERIOR_POSTERIOR);
    public static final Anatomy2D AXIAL_RA = new Anatomy2D(AnatomicalOrientation.AXIAL, AnatomicalAxis.RIGHT_LEFT, AnatomicalAxis.ANTERIOR_POSTERIOR);
    public static final Anatomy2D AXIAL_RP = new Anatomy2D(AnatomicalOrientation.AXIAL, AnatomicalAxis.RIGHT_LEFT, AnatomicalAxis.POSTERIOR_ANTERIOR);
    public static final Anatomy2D AXIAL_LP = new Anatomy2D(AnatomicalOrientation.AXIAL, AnatomicalAxis.LEFT_RIGHT, AnatomicalAxis.POSTERIOR_ANTERIOR);
    public static final Anatomy2D AXIAL_AL = new Anatomy2D(AnatomicalOrientation.AXIAL, AnatomicalAxis.ANTERIOR_POSTERIOR, AnatomicalAxis.LEFT_RIGHT);
    public static final Anatomy2D AXIAL_AR = new Anatomy2D(AnatomicalOrientation.AXIAL, AnatomicalAxis.ANTERIOR_POSTERIOR, AnatomicalAxis.RIGHT_LEFT);
    public static final Anatomy2D AXIAL_PL = new Anatomy2D(AnatomicalOrientation.AXIAL, AnatomicalAxis.POSTERIOR_ANTERIOR, AnatomicalAxis.LEFT_RIGHT);
    public static final Anatomy2D AXIAL_PR = new Anatomy2D(AnatomicalOrientation.AXIAL, AnatomicalAxis.POSTERIOR_ANTERIOR, AnatomicalAxis.RIGHT_LEFT);



    private static int count=0;

    public final AnatomicalAxis XAXIS;
    public final AnatomicalAxis YAXIS;

    AnatomicalOrientation orientation;

    private final Matrix3f referenceTransform;

    private Anatomy2D(AnatomicalOrientation _orientation, AnatomicalAxis xAxis, AnatomicalAxis yAxis) {
        orientation = _orientation;
        this.XAXIS = xAxis;
        this.YAXIS = yAxis;

        Vector3f v1 = XAXIS.getDirectionVector();
        Vector3f v2 = YAXIS.getDirectionVector();


        referenceTransform = new Matrix3f(v1.getX(), v1.getY(), 0, v2.getX(), v2.getY(), 0, 0, 0, 1);


        instances[count] = this;
        count++;

    }

    public static Anatomy2D matchAnatomy(AnatomicalDirection a1, AnatomicalDirection a2) {

        for (int i = 0; i < instances.length; i++) {
            Anatomy2D tmp = instances[i];
            if ((tmp.XAXIS.getMinDirection() == a1) && (tmp.YAXIS.getMinDirection() == a2)) {
                return tmp;
            }

        }
        throw new IllegalArgumentException("Axes do not correspond to valid anatomical volume ");

    }


    public static Anatomy2D matchAnatomy(AnatomicalAxis xaxis, AnatomicalAxis yaxis) {

        for (int i=0; i<instances.length; i++) {
            if ( (instances[i].XAXIS == xaxis) && (instances[i].YAXIS == yaxis) )
                return instances[i];
        }

        throw new IllegalArgumentException("Axes do not correspond to valid anatomical plane ");
    }

    public IndexConverter2D getIndexConverter(Anatomy2D other, int xdim, int ydim) {
        IndexConverter1D xcon = null;
        IndexConverter1D ycon = null;

        boolean swap = false;



        if (other.XAXIS == XAXIS) {
            xcon = new AnatomicalAxis.IndexDoNothing();
        }
        else if (other.XAXIS == XAXIS.getFlippedAxis()) {
            xcon = new AnatomicalAxis.IndexFlipper(xdim);
        }

        else if ( other.XAXIS == YAXIS) {
            ycon = new AnatomicalAxis.IndexDoNothing();
            swap=true;
        }

        else if ( other.XAXIS == YAXIS.getFlippedAxis()) {
            ycon = new AnatomicalAxis.IndexFlipper(ydim);
            swap=true;
        }

        else { throw new IllegalArgumentException("Cannot convert between orthogonal planes");  }

        if (other.YAXIS == YAXIS) {
            ycon = new AnatomicalAxis.IndexDoNothing();
        }

        else if (other.YAXIS == YAXIS.getFlippedAxis()) {
            ycon = new AnatomicalAxis.IndexFlipper(ydim);
        }

        else if (other.YAXIS == XAXIS) {
            xcon = new AnatomicalAxis.IndexDoNothing();
            swap=true;
        }

        else if ( other.YAXIS == XAXIS.getFlippedAxis()) {
            xcon = new AnatomicalAxis.IndexFlipper(xdim);
            swap=true;
        }

        else { throw new IllegalArgumentException("Cannot convert between orthogonal planes"); };

        if (swap) return new Anatomy2D.SwappingIndexConverter2D(xcon, ycon);
        else return new Anatomy2D.StandardIndexConverter2D(xcon, ycon);


    }

    public Anatomy2D getAxisSwappedPlane() {
        return matchAnatomy(YAXIS, XAXIS);
    }

    public boolean isSwappedVersion(Anatomy2D other) {
        if (other.XAXIS.sameAxis(YAXIS) && (other.YAXIS.sameAxis(XAXIS)) ) {
            return true;
        }

        return false;

    }

    public Matrix3f getReferenceTransform() {
        return referenceTransform;
    }


    public AnatomicalAxis[] getAnatomicalAxes() {
        return new AnatomicalAxis[] { XAXIS, YAXIS };
    }

    public AnatomicalOrientation getOrientation() {
        return orientation;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Axis 1: " + XAXIS + " ");
        sb.append("Axis 2: " + YAXIS);

        return sb.toString();

    }


    public static class StandardIndexConverter2D implements IndexConverter2D {

        IndexConverter1D one;
        IndexConverter1D two;

        public StandardIndexConverter2D(IndexConverter1D _one, IndexConverter1D _two) {
            one = _one;
            two = _two;
        }

        public final int[] convertXY(int x, int y) {
            return new int[] { one.convert(x), two.convert(y) };
        }

         public final int[] convertXY(int x, int y, int[] out) {
            out[0] = one.convert(x);
            out[1] = two.convert(y);
            return out;
        }


    }

    public static class SwappingIndexConverter2D implements IndexConverter2D {

        IndexConverter1D one;
        IndexConverter1D two;

        public SwappingIndexConverter2D(IndexConverter1D _one, IndexConverter1D _two) {
            one = _one;
            two = _two;
        }

        public final int[] convertXY(int x, int y) {
            return new int[] { one.convert(y), two.convert(x) };

        }

        public final int[] convertXY(int x, int y, int[] out) {
            out[0] = one.convert(y);
            out[1] = two.convert(x);
            return out;
        }


    }


}
