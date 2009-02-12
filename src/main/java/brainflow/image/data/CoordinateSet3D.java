package brainflow.image.data;

import cern.colt.list.DoubleArrayList;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import brainflow.image.anatomy.AnatomicalAxis;
import brainflow.image.anatomy.AnatomicalPoint1D;
import brainflow.image.anatomy.AnatomicalPoint3D;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.space.Axis;
import brainflow.image.space.ICoordinateSpace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 14, 2007
 * Time: 5:22:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class CoordinateSet3D {

    private DoubleMatrix2D points;

    private DoubleArrayList values;

    private DoubleArrayList pointRadius;

    private ICoordinateSpace space;


    public CoordinateSet3D(double[][] points) {
        if (points[0].length != 3) {
            throw new IllegalArgumentException("points array must have 3 columns (zero, zero, one)");
        }

    }


    public CoordinateSet3D(ICoordinateSpace _space, double[][] _points) {
        space = _space;

        if (_points[0].length != 3) {
            throw new IllegalArgumentException("points array must have 3 columns (zero, zero, one)");
        }

        init(_points);
    }

    public CoordinateSet3D(ICoordinateSpace _space, double[][] _points, double fillValue, double fillSize) {
        space = _space;

        if (_points[0].length != 3) {
            throw new IllegalArgumentException("points array must have 3 columns (zero, zero, one)");
        }

        init(_points, fillValue, fillSize);
    }

    public CoordinateSet3D(ICoordinateSpace _space, double[][] _points, double[] _values, double[] _sizes) {
        space = _space;

        if (_points[0].length != 3) {
            throw new IllegalArgumentException("points array must have 3 columns (zero, zero, one)");
        }

        if ((_sizes.length != _points.length) || (_values.length != _points.length)) {
            throw new IllegalArgumentException("values and size arrays must have same number of rows as points array");

        }


        init(_points, _values, _sizes);
    }

    private void init(double[][] _points, double fillValue, double fillSize) {
        points = new DenseDoubleMatrix2D(_points);
        double[] v = new double[_points.length];
        double[] s = new double[_points.length];
        Arrays.fill(v, fillValue);
        Arrays.fill(s, fillSize);
        values = new DoubleArrayList(v);
        pointRadius = new DoubleArrayList(s);
    }

    private void init(double[][] _points) {
        points = new DenseDoubleMatrix2D(_points);
        double[] v = new double[_points.length];
        double[] s = new double[_points.length];
        Arrays.fill(v, 1);
        Arrays.fill(s, 1);
        values = new DoubleArrayList(v);
        pointRadius = new DoubleArrayList(s);
    }

    private void init(double[][] _points, double[] _values, double[] _sizes) {
        points = new DenseDoubleMatrix2D(_points);
        values = new DoubleArrayList(_values);
        pointRadius = new DoubleArrayList(_sizes);
    }


    public int getRows() {
        return points.rows();
    }

    public int getColumns() {
        return 5;
    }

    public double getMinValue() {
        return cern.jet.stat.Descriptive.min(values);
    }

    public double getMaxValue() {
        return cern.jet.stat.Descriptive.max(values);
    }


    public void setCoordinate(int index, double x, double y, double z) {
        points.set(index, 0, x);
        points.set(index, 1, y);
        points.set(index, 2, z);

    }

    public void setXCoordinate(int index, double x) {
        points.set(index, 0, x);

    }

    public void setYCoordinate(int index, double y) {
        points.set(index, 1, y);
    }

    public void setZCoordinate(int index, double z) {
        points.set(index, 2, z);

    }

    public void setValue(int index, double value) {
        values.set(index, value);
    }

    public void setRadius(int index, double value) {
        pointRadius.set(index, value);
    }

    public double getValue(int index) {
        return values.get(index);
    }

    public double getRadius(int index) {
        return pointRadius.get(index);
    }

    public double getXCoordinate(int index) {
        return points.get(index, 0);
    }

    public double getYCoordinate(int index) {
        return points.get(index, 1);
    }

    public double getZCoordinate(int index) {
        return points.get(index, 2);

    }

    public ICoordinateSpace getSpace() {
        return space;
    }

    public double[] getCoordinates(int index) {
        if (index < 0 || index >= getRows()) {
            throw new IndexOutOfBoundsException("illegal index " + index);
        }

        double[] ret = new double[3];
        ret[0] = points.get(index, 0);
        ret[1] = points.get(index, 1);
        ret[2] = points.get(index, 2);

        return ret;

    }

    public double getCoordinate(int index, AnatomicalAxis axis) {
        if (getSpace().getImageAxis(Axis.Z_AXIS).getAnatomicalAxis() == axis) {
            return points.get(index, 2);
        } else if (getSpace().getImageAxis(Axis.Y_AXIS).getAnatomicalAxis() == axis) {
            return points.get(index, 1);
        } else if (getSpace().getImageAxis(Axis.X_AXIS).getAnatomicalAxis() == axis) {
            return points.get(index, 0);
        } else {
            throw new IllegalArgumentException("axis does not match coordinate space");
        }

    }

    public List<Integer> indicesWithinPlane(AnatomicalPoint1D slice) {
        List<Integer> idx = new ArrayList<Integer>();

        for (int i = 0; i < getRows(); i++) {
            double coord = getCoordinate(i, slice.getAnatomy());
            double radius = getRadius(i);
            if (Math.abs(slice.getValue() - coord) < radius) {
                idx.add(i);
            }

        }

        return idx;
    }

    public List<AnatomicalPoint3D> pointsWithinPlane(AnatomicalPoint1D slice) {
        List<AnatomicalPoint3D> pts = new ArrayList<AnatomicalPoint3D>();
        for (int i = 0; i < getRows(); i++) {
            double coord = getCoordinate(i, slice.getAnatomy());
            double radius = getRadius(i);
            if (Math.abs(slice.getValue() - coord) < radius) {
                pts.add(getAnatomicalPoint(i));
            }

        }

        return pts;

    }

    public AnatomicalPoint3D getAnatomicalPoint(int index) {
        if (index < 0 || index >= getRows()) {
            throw new IndexOutOfBoundsException("illegal index " + index);
        }

        return new AnatomicalPoint3D((Anatomy3D) space.getAnatomy(), points.get(index, 0), points.get(index, 1), points.get(index, 2));

    }


}
