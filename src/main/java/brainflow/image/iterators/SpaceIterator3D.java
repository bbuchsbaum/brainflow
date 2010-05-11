package brainflow.image.iterators;

import brainflow.image.io.IImageSource;
import brainflow.math.Vector3f;
import brainflow.image.space.IImageSpace3D;
import brainflow.image.space.Axis;
import brainflow.image.data.IImageData3D;

import brainflow.core.BrainFlowException;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 12, 2008
 * Time: 9:18:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class SpaceIterator3D implements XYZIterator {

    private final IImageSpace3D space;


    private int idx;

    private int xlen;

    private int ylen;

    private int zlen;

    private int totalLen;

    private int planeLen;


    public SpaceIterator3D(IImageSpace3D _space) {
        space = _space;

        idx = 0;

        xlen = space.getDimension(Axis.X_AXIS);

        ylen = space.getDimension(Axis.Y_AXIS);

        zlen = space.getDimension(Axis.Z_AXIS);

        planeLen = xlen * ylen;

        totalLen = xlen * ylen * zlen;


    }

    public Vector3f next() {
        float[] ret = space.indexToWorld(getXIndex(), getYIndex(), getZIndex());
        idx++;
        return new Vector3f(ret[0], ret[1], ret[2]);
    }

    public Vector3f next(Vector3f holder) {
        float[] ret = space.indexToWorld(getXIndex(), getYIndex(), getZIndex());
        holder.x = ret[0];
        holder.y = ret[1];
        holder.z = ret[2];
        idx++;
        return holder;
    }

    public int nextIndex() {
        return ++idx;
    }

    public boolean hasNext() {
        return idx < totalLen - 1;
    }

    public int getXIndex() {
        return idx % xlen;
    }

    public int getYIndex() {
        return (idx % planeLen) / xlen;
    }

    public int getZIndex() {
        return idx / planeLen;
    }

    public int getIndex() {
        return idx;
    }


    public static void main(String[] args) {
        IImageSource src = null; //TestUtils.quickDataSource("BRB-20071214-09-t1_mprage-001.nii");
        try {
            IImageData3D data = (IImageData3D) src.load();
            SpaceIterator3D iter = new SpaceIterator3D(data.getImageSpace());
            double xmin = Double.POSITIVE_INFINITY;
            double ymin = Double.POSITIVE_INFINITY;
            double zmin = Double.POSITIVE_INFINITY;

            int[] xminvox = new int[3];
            int[] yminvox = new int[3];
            int[] zminvox = new int[3];

            int[] xmaxvox = new int[3];
            int[] ymaxvox = new int[3];
            int[] zmaxvox = new int[3];


            double xmax = Double.NEGATIVE_INFINITY;
            double ymax = Double.NEGATIVE_INFINITY;
            double zmax = Double.NEGATIVE_INFINITY;

            System.out.println("(0,0,0): " + Arrays.toString(data.getImageSpace().indexToWorld(0,0,0)));

            while (iter.hasNext()) {

                Vector3f coord = iter.next();
                if (coord.getX() < xmin) {
                    //System.out.println("min x at " + iter.getIndex());
                    xmin = coord.getX();
                    xminvox[0] = iter.getXIndex();
                    xminvox[1] = iter.getYIndex();
                    xminvox[2] = iter.getZIndex();
                }
                if (coord.getY() < ymin) {
                    //System.out.println("min y at " + iter.getIndex());
                    ymin = coord.getY();
                    yminvox[0] = iter.getXIndex();
                    yminvox[1] = iter.getYIndex();
                    yminvox[2] = iter.getZIndex();
                }
                if (coord.getZ() < zmin) {
                    //System.out.println("min z at " + iter.getIndex());
                    zmin = coord.getZ();
                    zminvox[0] = iter.getXIndex();
                    zminvox[1] = iter.getYIndex();
                    zminvox[2] = iter.getZIndex();
                }

                if (coord.getX() > xmax) {
                    //System.out.println("max x at " + iter.getIndex());
                    xmax = coord.getX();
                    xmaxvox[0] = iter.getXIndex();
                    xmaxvox[1] = iter.getYIndex();
                    xmaxvox[2] = iter.getZIndex();
                }
                if (coord.getY() > ymax) {
                    //System.out.println("max y at " + iter.getIndex());
                    ymax = coord.getY();
                    ymaxvox[0] = iter.getXIndex();
                    ymaxvox[1] = iter.getYIndex();
                    ymaxvox[2] = iter.getZIndex();
                }
                if (coord.getZ() > zmax) {
                    //System.out.println("max z at " + iter.getIndex());
                    zmax = coord.getZ();
                    zmaxvox[0] = iter.getXIndex();
                    zmaxvox[1] = iter.getYIndex();
                    zmaxvox[2] = iter.getZIndex();
                }
            }

            System.out.println("x min : " + xmin);
            System.out.println("y min : " + ymin);
            System.out.println("z min : " + zmin);


            System.out.println("x max : " + xmax);
            System.out.println("y max : " + ymax);
            System.out.println("z max : " + zmax);

            System.out.println("x min vox : " + Arrays.toString(xminvox));
            System.out.println("y min vox : " + Arrays.toString(yminvox));
            System.out.println("z min vox : " + Arrays.toString(zminvox));

            System.out.println("x max vox : " + Arrays.toString(xmaxvox));
            System.out.println("y max vox : " + Arrays.toString(ymaxvox));
            System.out.println("z max vox : " + Arrays.toString(zmaxvox));


        } catch (BrainFlowException e) {
            e.printStackTrace();
        }
    }
}
