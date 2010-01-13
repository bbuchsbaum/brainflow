package brainflow.image.space;

import brainflow.image.anatomy.*;
import brainflow.image.axis.ImageAxis;
import brainflow.utils.Dimension2D;
import brainflow.math.Index2D;
import brainflow.math.Vector2f;
import brainflow.utils.IDimension;

/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: Feb 18, 2004
 * Time: 9:49:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageSpace2D extends AbstractImageSpace implements IImageSpace2D {


    private ImageMapping2D mapping;

    private int dim0;

    private Dimension2D<Float> origin;


    public ImageSpace2D(IImageSpace2D space) {
        this(space.getImageAxis(Axis.X_AXIS), space.getImageAxis(Axis.Y_AXIS), space.getMapping());
    }

    public ImageSpace2D(ImageAxis xaxis, ImageAxis yaxis) {
        this(xaxis, yaxis, null);
    }

    public ImageSpace2D(ImageAxis xaxis, ImageAxis yaxis, ImageMapping2D _mapping) {
        initAnatomy(xaxis, yaxis);
        initAxes(xaxis, yaxis);


        if (_mapping == null) {
            mapping = createMapping(xaxis, yaxis, getAnatomy());
        } else {
            mapping = _mapping;
        }


        Vector2f og = mapping.getOrigin();
        origin = new Dimension2D<Float>(og.x, og.y);
        dim0 = getDimension(Axis.X_AXIS);

    }


    private void initAnatomy(ImageAxis xaxis, ImageAxis yaxis) {
        Anatomy2D check = Anatomy2D.matchAnatomy(xaxis.getAnatomicalAxis(), yaxis.getAnatomicalAxis());

        if (check == null) {
            throw new IllegalArgumentException("could not initialize axes from supplied ImageAxes : " + xaxis + " : " + yaxis);
        }

        setAnatomy(check);


    }

    private void initAxes(ImageAxis xaxis, ImageAxis yaxis) {
        double xinterval = xaxis.getSpacing() * xaxis.getNumSamples();
        double yinterval = yaxis.getSpacing() * yaxis.getNumSamples();

        xaxis = new ImageAxis(xaxis.getAnatomicalAxis(), 0 - xinterval / 2, xaxis.getSpacing(), xaxis.getNumSamples());
        yaxis = new ImageAxis(yaxis.getAnatomicalAxis(), 0 - yinterval / 2, yaxis.getSpacing(), yaxis.getNumSamples());

        createImageAxes(2);

        initAxis(xaxis, Axis.X_AXIS);
        initAxis(yaxis, Axis.Y_AXIS);

    }

    private ImageMapping2D createMapping(ImageAxis xaxis, ImageAxis yaxis, Anatomy2D anatomy) {
        return new AffineMapping2D(new Vector2f((float) xaxis.getRange().getBeginning().getValue(), (float) yaxis.getRange().getBeginning().getValue()),
                new Vector2f((float) xaxis.getSpacing(), (float) yaxis.getSpacing()),
                anatomy);


    }

    public ImageMapping2D getMapping() {
        return mapping;
    }

    @Override
    public Anatomy2D getAnatomy() {
        return (Anatomy2D) super.getAnatomy();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public IDimension<Float> getSpacing() {
        return new Dimension2D<Float>((float)getSpacing(Axis.X_AXIS), (float)getSpacing(Axis.Y_AXIS));
    }

    public float gridToWorldY(float x, float y, float z) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public float gridToWorldX(float x, float y, float z) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public float worldToGridY(float x, float y, float z) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public float worldToGridX(float x, float y, float z) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int indexToGridY(int idx) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int indexToGridX(int idx) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public float[] indexToWorld(int x, int y, int z) {
        return new float[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Dimension2D<Integer> getDimension() {
        return new Dimension2D<Integer>(getDimension(Axis.X_AXIS), getDimension(Axis.Y_AXIS));

    }

    public Dimension2D<Float> getOrigin() {
        return origin;
    }


    public IImageSpace union(IImageSpace other) {
        throw new UnsupportedOperationException();
    }

    public SpatialLoc2D getCentroid() {
        ImageAxis a1 = getImageAxis(Axis.X_AXIS);
        ImageAxis a2 = getImageAxis(Axis.Y_AXIS);
        return new SpatialLoc2D(getAnatomy(), a1.getCenter().getValue(), a2.getCenter().getValue());
    }

    public float[] worldToGrid(float[] coord) {
        throw new UnsupportedOperationException();
    }

    public float[] indexToWorld(int[] index) {
        throw new UnsupportedOperationException();
    }

    public float[] gridToWorld(float[] gridpos) {
        throw new UnsupportedOperationException();
    }

    public Index2D indexToGrid(int idx) {
        throw new UnsupportedOperationException();
    }
}
