package brainflow.image;

import brainflow.image.anatomy.AnatomicalAxis;
import brainflow.image.anatomy.Anatomy2D;
import brainflow.image.axis.ImageAxis;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class PlaneArray {

    Anatomy2D anatPlane;

    ImageAxis ixaxis;
    ImageAxis iyaxis;


    public PlaneArray(ImageAxis _xArray, ImageAxis _yArray) {
        anatPlane = Anatomy2D.matchAnatomy(_xArray.getAnatomicalAxis(), _yArray.getAnatomicalAxis());
        ixaxis = _xArray;
        iyaxis = _yArray;

    }



    public PlaneArray(PlaneArray src) {
        anatPlane = src.anatPlane;
        ixaxis = src.ixaxis;
        iyaxis = src.iyaxis;
    }

    public ImageAxis getXImageAxis() {
        return ixaxis;
    }

    public ImageAxis getYImageAxis() {
        return iyaxis;
    }


    public PlaneArray matchPlane(Anatomy2D other) {


        if (other.getOrientation() != anatPlane.getOrientation()) {
            throw new IncompatiblePlaneException("cannot match planes with different orientations");
        }


        ImageAxis xmatch;
        ImageAxis ymatch;

        // replace by findAxis?

        if (other.XAXIS == getXAxis()) {
            xmatch = new ImageAxis(ixaxis);
        }
        else if (other.XAXIS == getXAxis().getFlippedAxis()) {
            xmatch = ixaxis.flip();
        }
        else if (other.XAXIS == getYAxis()) {
            xmatch = new ImageAxis(iyaxis);
        }
        else if (other.XAXIS == getYAxis().getFlippedAxis()) {
            xmatch = iyaxis.flip();

        }
        else throw new IncompatiblePlaneException("failed to match planes");

        if (other.YAXIS == getYAxis()) {
            ymatch = new ImageAxis(iyaxis);
        }
        else if (other.YAXIS == getYAxis().getFlippedAxis()) {
           ymatch = iyaxis.flip();
        }
        else if (other.YAXIS == getXAxis()) {
            ymatch = new ImageAxis(ixaxis);
        }
        else if (other.YAXIS == getXAxis().getFlippedAxis()) {
            ymatch = ixaxis.flip();
        }
        else throw new IncompatiblePlaneException("failed to match planes");

        //AxisArray nx = new AxisArray(other.XAXIS, xRange, new IntegerSet1D(xsamples[0], xsamples[xsamples.length-1]), Axis.X_AXIS);
        //AxisArray ny = new AxisArray(other.YAXIS, yRange, new IntegerSet1D(ysamples[0], ysamples[ysamples.length-1]), Axis.Y_AXIS);
        // real range not set for matched plane
        return new PlaneArray(xmatch, ymatch);

    }

    public AnatomicalAxis getXAxis() {
        return anatPlane.XAXIS;
    }

    public AnatomicalAxis getYAxis() {
        return anatPlane.YAXIS;
    }


    public int[] getXSamples() {
        return ixaxis.getSampleArray();
    }

    public int[] getYSamples() {
        return iyaxis.getSampleArray();
    }

    public Anatomy2D getAnatomicalPlane() {
        return anatPlane;
    }

    public static class IncompatiblePlaneException extends IllegalArgumentException {
        public IncompatiblePlaneException() {
            super();
        }

        public IncompatiblePlaneException(String message) {
            super(message);
        }

    }


    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Anatomical Plane: " + anatPlane + "\n");
        buffer.append("Axis 1 samples: " + "\n");
        buffer.append(ixaxis.getSampleArray());
        buffer.append("\n");
        buffer.append("Axis 2 samples: " + "\n");
        buffer.append(iyaxis.getSampleArray());

        return buffer.toString();


    }





}
