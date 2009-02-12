package brainflow.math;
import cern.colt.list.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class CatmullRomSpline extends BSpline {

  public CatmullRomSpline(DoubleArrayList xpoints, DoubleArrayList ypoints) {
    super(xpoints, ypoints);
  }
  
 
  public double b(int i, double t) {
     
    switch (i) {
    case -2:
      return ((-t+2)*t-1)*t/2;
    case -1:
      return (((3*t-5)*t)*t+2)/2;
    case 0:
      return ((-3*t+4)*t+1)*t/2;
    case 1:
      return ((t-1)*t*t)/2;
    }
    return 0; //we only get here if an invalid i is specified
  }

    

}