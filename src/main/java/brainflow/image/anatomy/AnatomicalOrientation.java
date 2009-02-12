package brainflow.image.anatomy;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class AnatomicalOrientation implements java.io.Serializable {


    private static final String __SAGITTAL = "Sagittal";
    private static final String __CORONAL = "Coronal";
    private static final String __AXIAL = "Axial";

    public static final AnatomicalOrientation SAGITTAL = AnatomicalOrientation.createSagittalOrientation();
    public static final AnatomicalOrientation CORONAL = AnatomicalOrientation.createCoronalOrientation();
    public static final AnatomicalOrientation AXIAL = AnatomicalOrientation.createAxialOrientation();

    private static AnatomicalOrientation[] instances = {SAGITTAL, CORONAL, AXIAL};


    private String orientationLabel = "NULL";


    private AnatomicalOrientation() {

    }

    public static void main(String[] args) {

    }

    public static AnatomicalOrientation[] getInstances() {
        return AnatomicalOrientation.instances;
    }

    public static AnatomicalOrientation lookupByLabel(String label) {
        for (int i = 0; i < instances.length; i++) {
            if (instances[i].orientationLabel.equalsIgnoreCase(label))
                return instances[i];
        }

        return null;
    }


    public static AnatomicalOrientation[] getOrthogonalOrientations(AnatomicalOrientation orient) {
        AnatomicalOrientation[] other = new AnatomicalOrientation[2];
        if (orient == AnatomicalOrientation.SAGITTAL) {
            other[0] = AnatomicalOrientation.AXIAL;
            other[1] = AnatomicalOrientation.CORONAL;
        } else if (orient == AnatomicalOrientation.AXIAL) {
            other[0] = AnatomicalOrientation.SAGITTAL;
            other[1] = AnatomicalOrientation.CORONAL;
        } else if (orient == AnatomicalOrientation.CORONAL) {
            other[0] = AnatomicalOrientation.AXIAL;
            other[1] = AnatomicalOrientation.SAGITTAL;
        }

        return other;


    }


    private static AnatomicalOrientation createAxialOrientation() {
        AnatomicalOrientation o = new AnatomicalOrientation();
        o.orientationLabel = __AXIAL;
        //o.anatomies = Anatomy3D.getAxialFamily();
        return o;
    }

    private static AnatomicalOrientation createCoronalOrientation() {
        AnatomicalOrientation o = new AnatomicalOrientation();
        o.orientationLabel = __CORONAL;
        //o.anatomies = Anatomy3D.getCoronalFamily();
        return o;
    }

    private static AnatomicalOrientation createSagittalOrientation() {
        AnatomicalOrientation o = new AnatomicalOrientation();
        o.orientationLabel = __SAGITTAL;
        //o.anatomies = Anatomy3D.getSagittalFamily();
        return o;

    }

    public boolean equals(Object other) {
        if (this == other)
            return true;

        if (!(other instanceof AnatomicalOrientation))
            return false;

        AnatomicalOrientation ao = (AnatomicalOrientation) other;
        if (ao.orientationLabel != orientationLabel)
            return false;
        else
            return true;
    }

    public String getOrientationLabel() {
        return orientationLabel;
    }

    public String toString() {
        return orientationLabel;
    }


}