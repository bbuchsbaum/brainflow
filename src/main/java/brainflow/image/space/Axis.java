package brainflow.image.space;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public final class Axis {

  public static final Axis X_AXIS = new Axis("X Axis", 0);
  public static final Axis Y_AXIS = new Axis("Y Axis", 1);
  public static final Axis Z_AXIS = new Axis("Z Axis", 2);
  public static final Axis T_AXIS = new Axis("Time", 3);
    
  private String label;
  private int id;

  private Axis(String _label, int _id) {
    label = _label;
    id = _id;
  }


  public static Axis getAxis(int id) {
      if (id == 0) return Axis.X_AXIS;
      if (id == 1) return Axis.Y_AXIS;
      if (id == 2) return Axis.Z_AXIS;
      if (id == 3) return Axis.T_AXIS;

      assert false : "id corresponds to no know Axis, will throw IllegalArgumentException...";

      throw new IllegalArgumentException("invalid id argument for Axis lookup");


  }

  public final int getId() {
    return id;
  }

  public String getLabel() {
    return label;
  }

  public boolean equals(Object other) {
    if ( !(other instanceof Axis) )
      return false;
    if ( ((Axis)other).id == id )
      return true;
    return false;
  }
  
  public String toString() {
      return getLabel();
  }


}