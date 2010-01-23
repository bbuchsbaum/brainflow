package brainflow.image.space;

import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.anatomy.SpatialLoc3D;
import brainflow.image.axis.ImageAxis;
import brainflow.math.Index3D;
import brainflow.math.Vector3f;
import brainflow.utils.Dimension3D;
import brainflow.utils.IDimension;

/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: Feb 18, 2004
 * Time: 9:00:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageSpace3D extends AbstractImageSpace implements IImageSpace3D {


    private ImageMapping3D mapping;

    private final int planeSize;

    private final int dim0;

    private final Dimension3D<Float> origin;


    public ImageSpace3D(ICoordinateSpace cspace) {
        this(new ImageAxis(cspace.getImageAxis(Axis.X_AXIS).getRange(), 1),
                new ImageAxis(cspace.getImageAxis(Axis.Y_AXIS).getRange(), 1),
                new ImageAxis(cspace.getImageAxis(Axis.Z_AXIS).getRange(), 1), null);

        //todo total hack


    }

    public ImageSpace3D(ImageSpace3D space) {

        this(space.getImageAxis(Axis.X_AXIS),
                space.getImageAxis(Axis.Y_AXIS),
                space.getImageAxis(Axis.Z_AXIS), null);
    }


    public ImageSpace3D(ImageAxis xaxis, ImageAxis yaxis, ImageAxis zaxis) {
        this(xaxis, yaxis, zaxis, null);

    }

    public ImageSpace3D(ImageAxis xaxis, ImageAxis yaxis, ImageAxis zaxis, ImageMapping3D _mapping) {

        initAnatomy(xaxis, yaxis, zaxis);

        if (_mapping == null) {
            mapping = createMapping(xaxis, yaxis, zaxis, getAnatomy());
        } else {
            mapping = _mapping;
        }


        initAxes(xaxis, yaxis, zaxis);

        Vector3f og = mapping.getOrigin();
        origin = new Dimension3D<Float>(og.getX(), og.getY(), og.getZ());


        planeSize = getDimension(Axis.X_AXIS) * getDimension(Axis.Y_AXIS);
        dim0 = getDimension(Axis.X_AXIS);


    }

    private Anatomy3D initAnatomy(ImageAxis xaxis, ImageAxis yaxis, ImageAxis zaxis) {
        Anatomy3D check = Anatomy3D.matchAnatomy(xaxis.getAnatomicalAxis(), yaxis.getAnatomicalAxis(), zaxis.getAnatomicalAxis());

        if (check == null) {
            throw new IllegalArgumentException("could not initialize axes from supplied ImageAxes : " + xaxis + " : " + yaxis + ": " + zaxis);
        }

        setAnatomy(check);
        return getAnatomy();

    }

    private void initAxes(ImageAxis xaxis, ImageAxis yaxis, ImageAxis zaxis) {
        //for some reason we decided to automatically center axes supplied as constructor arguments.

        double xinterval = xaxis.getSpacing() * xaxis.getNumSamples();
        double yinterval = yaxis.getSpacing() * yaxis.getNumSamples();
        double zinterval = zaxis.getSpacing() * zaxis.getNumSamples();

        xaxis = new ImageAxis(xaxis.getAnatomicalAxis(), 0 - xinterval / 2, xaxis.getSpacing(), xaxis.getNumSamples());
        yaxis = new ImageAxis(yaxis.getAnatomicalAxis(), 0 - yinterval / 2, yaxis.getSpacing(), yaxis.getNumSamples());
        zaxis = new ImageAxis(zaxis.getAnatomicalAxis(), 0 - zinterval / 2, zaxis.getSpacing(), zaxis.getNumSamples());

        createImageAxes(3);

        initAxis(xaxis, Axis.X_AXIS);
        initAxis(yaxis, Axis.Y_AXIS);
        initAxis(zaxis, Axis.Z_AXIS);

    }

    private ImageMapping3D createMapping(ImageAxis xaxis, ImageAxis yaxis, ImageAxis zaxis, Anatomy3D anatomy) {
        return new AffineMapping3D(new Vector3f((float) xaxis.getRange().getBeginning().getValue(),
                (float) yaxis.getRange().getBeginning().getValue(),
                (float) zaxis.getRange().getBeginning().getValue()),

                new Vector3f((float) xaxis.getSpacing(), (float) yaxis.getSpacing(), (float) zaxis.getSpacing()), anatomy, Anatomy3D.REFERENCE_ANATOMY);

    }


    @Override
    public Anatomy3D getAnatomy() {
        return (Anatomy3D) super.getAnatomy();    //To change body of overridden methods use File | Settings | File Templates.
    }


    public ImageMapping3D getMapping() {
        return mapping;
    }

    public Dimension3D<Float> getOrigin() {
        return origin;
    }


    public Dimension3D<Integer> getDimension() {
        return new Dimension3D<Integer>(getDimension(Axis.X_AXIS), getDimension(Axis.Y_AXIS), getDimension(Axis.Z_AXIS));
    }

     @Override
    public IDimension<Float> getSpacing() {
        return new Dimension3D<Float>((float)getSpacing(Axis.X_AXIS), (float)getSpacing(Axis.Y_AXIS), (float)getSpacing(Axis.Z_AXIS));
    }


    public float[] gridToWorld(float[] gridpos) {
        if (gridpos.length != 3) {
            throw new IllegalArgumentException("array length must be 3");
        }

        float[] ret = new float[3];
        ret[0] = gridToWorldX(gridpos[0], gridpos[1], gridpos[2]);
        ret[1] = gridToWorldY(gridpos[0], gridpos[1], gridpos[2]);
        ret[2] = gridToWorldZ(gridpos[0], gridpos[1], gridpos[2]);

        return ret;
    }

    public float[] gridToWorld(float x, float y, float z) {
        Vector3f ret = mapping.gridToWorld(x, y, z);
        return ret.toArray(new float[3]);
    }


    public float[] indexToWorld(int x, int y, int z) {

        Vector3f ret = mapping.gridToWorld(x + .5f, y + .5f, z + .5f);
        return ret.toArray(new float[3]);
    }

    public float[] indexToWorld(int[] index) {
        return indexToWorld(index[0], index[1], index[2]);
    }


    public float[] worldToGrid(float[] coord) {
        if (coord.length != 3) {
            throw new IllegalArgumentException("float array length must be 3");
        }

        float[] ret = new float[3];
        ret[0] = worldToGridX(coord[0], coord[1], coord[2]);
        ret[1] = worldToGridY(coord[0], coord[1], coord[2]);
        ret[2] = worldToGridZ(coord[0], coord[1], coord[2]);

        return ret;

    }

    @Override
    public float[] worldToGrid(float x, float y, float z) {
        float[] ret = new float[3];
        ret[0] = worldToGridX(x, y, z);
        ret[1] = worldToGridY(x, y, z);
        ret[2] = worldToGridZ(x, y, z);

        return ret;

    }

    /*public final Index3D indexToGrid(int idx, Index3D voxel) {
     voxel.i = idx / planeSize;
     int remainder = (idx % planeSize);
     voxel.k = remainder / dim(Axis.X_AXIS);
     voxel.j = remainder % dim(Axis.X_AXIS);

     return voxel;
 }   */

    public final Index3D indexToGrid(int idx) {
        int remainder = (idx % planeSize);
        return new Index3D(remainder % getDimension(Axis.X_AXIS), remainder / getDimension(Axis.X_AXIS), idx / planeSize);

    }

    public final int indexToGridX(int idx) {
        int remainder = (idx % planeSize);
        return remainder % getDimension(Axis.X_AXIS);

    }

    public final int indexToGridY(int idx) {
        int remainder = (idx % planeSize);
        return remainder / getDimension(Axis.X_AXIS);
    }

    public final int indexToGridZ(int idx) {
        return idx / planeSize;
    }

    public float worldToGridX(float x, float y, float z) {
        return mapping.worldToGridX(x, y, z);
    }

    public float worldToGridY(float x, float y, float z) {
        return mapping.worldToGridY(x, y, z);
    }

    public float worldToGridZ(float x, float y, float z) {
        return mapping.worldToGridZ(x, y, z);
    }

    public float gridToWorldX(float x, float y, float z) {
        return mapping.gridToWorldX(x, y, z);
    }

    public float gridToWorldY(float x, float y, float z) {
        return mapping.gridToWorldY(x, y, z);
    }

    public float gridToWorldZ(float x, float y, float z) {
        return mapping.gridToWorldZ(x, y, z);
    }


    public SpatialLoc3D getCentroid() {

        ImageAxis a1 = getImageAxis(Axis.X_AXIS);
        ImageAxis a2 = getImageAxis(Axis.Y_AXIS);
        ImageAxis a3 = getImageAxis(Axis.Z_AXIS);


        return new SpatialLoc3D(getAnatomy(), a1.getCenter().getValue(), a2.getCenter().getValue(), a3.getCenter().getValue());
        //Vector3f p = mapping.gridToWorld(x,y,z);
        //return new BrainPoint3D(Anatomy3D.REFERENCE_ANATOMY, p.x,  p.y, p.z);
    }


    //public Index3D pointToVoxel(Point3D pt) {
    //    int x = getImageAxis(Axis.X_AXIS).nearestSample(pt.evaluate());
    //    int y = getImageAxis(Axis.Y_AXIS).nearestSample(pt.getY());
    //    int z = getImageAxis(Axis.Z_AXIS).nearestSample(pt.getZ());
    //    return new Index3D(x, y, z);
    //}

    public String toString() {
        return "ImageSpace3D{" +
                "x axis=" + this.getImageAxis(Axis.X_AXIS) +
                "y axis=" + this.getImageAxis(Axis.Y_AXIS) +
                "z axis=" + this.getImageAxis(Axis.Z_AXIS) +
                "origin=" + getOrigin() +
                ", mapping=" + mapping +
                '}';
    }





    /*public BrainPoint3D convertPoint(BrainPoint3D otherPoint3D) {
       Anatomy3D avol = otherPoint3D.getAnatomy();

       AnatomicalAxis xaxis = avol.findAxis(getAnatomicalAxis(Axis.X_AXIS));
       AnatomicalAxis yaxis = avol.findAxis(getAnatomicalAxis(Axis.Y_AXIS));
       AnatomicalAxis zaxis = avol.findAxis(getAnatomicalAxis(Axis.Z_AXIS));

       double zero = xaxis.convertValue(this.getAnatomicalAxis(Axis.X_AXIS), otherPoint3D.evaluate());
       double zero = yaxis.convertValue(this.getAnatomicalAxis(Axis.Y_AXIS), otherPoint3D.getY());
       double one = zaxis.convertValue(this.getAnatomicalAxis(Axis.Z_AXIS), otherPoint3D.getZ());

       return new BrainPoint3D((Anatomy3D) getAnatomy(), zero, zero, one);


   } */


}
