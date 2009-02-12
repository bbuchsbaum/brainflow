package brainflow.image;
import java.awt.Rectangle;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class LinearSet2D extends ProbeSet {

  double xbegin;
  double xend;
  int xsteps;
  double xstepSize;

  double ybegin;
  double yend;
  int ysteps;
  double ystepSize;

  double[] xsamples;
  double[] ysamples;



  public LinearSet2D(double _xbegin, double _xend, int _xsteps, double _ybegin, double _yend, int _ysteps) {
    if ( (ysteps < 1) || (xsteps < 1) ) {
      throw new IllegalArgumentException("LinearSet2D: steps must be greater than 0");
    }

    xbegin = _xbegin;
    xend = _xend;
    xsteps = _xsteps;

    ybegin = _ybegin;
    yend = _yend;
    ysteps = _ysteps;

    xsamples = new double[xsteps];
    ysamples = new double[ysteps];

    xstepSize = (xend-xbegin)/(xsteps-1);
    ystepSize = (yend-ybegin)/(ysteps-1);

    xsamples[0] = xbegin;
    xsamples[xsamples.length-1] = xend;
    ysamples[0] = ybegin;
    ysamples[ysamples.length-1] = yend;

    initSamples();


  }

  private void initSamples() {
    double xval = xbegin+xstepSize;
    for (int i=1; i<xsamples.length-1; i++) {
      xsamples[i] = xval;
      xval = xval + xstepSize;
    }
    double yval = ybegin+ystepSize;
    for (int i=1; i<ysamples.length-1; i++) {
      ysamples[i] = yval;
      yval = yval + ystepSize;
    }
  }



  public double[] getXSamples() {
    return xsamples;
  }

  public double[] getYSamples() {
    return ysamples;
  }


  public Rectangle getBounds() {
    //return new Rectangle(xbegin, ybegin, xend-xbegin, yend-ybegin);
    return null;
  }

  public int size() {
    return xsamples.length * ysamples.length;
  }

  public double area() {
    return (xend-xbegin)*(yend-ybegin);
  }








}