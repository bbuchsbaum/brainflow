package brainflow.image.anatomy;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class AnatomicalDirection implements Anatomy {

  public static final AnatomicalDirection ANTERIOR = new AnatomicalDirection("Anterior", 0);
  public static final AnatomicalDirection POSTERIOR = new AnatomicalDirection("Posterior", 1);
  public static final AnatomicalDirection SUPERIOR = new AnatomicalDirection("Superior", 2);
  public static final AnatomicalDirection INFERIOR = new AnatomicalDirection("Inferior", 3);
  public static final AnatomicalDirection RIGHT = new AnatomicalDirection("Right", 4);
  public static final AnatomicalDirection LEFT = new AnatomicalDirection("Left", 5);


  String label;
  int id;

  private AnatomicalDirection(String _label, int _id) {
    label = _label;
    id = _id;
  }

  public AnatomicalDirection getReverse() {
    if (id == 0)
      return POSTERIOR;
    else if (id == 1)
      return ANTERIOR;
    else if (id == 2)
      return INFERIOR;
    else if (id == 3)
      return SUPERIOR;
    else if (id == 4)
      return LEFT;
    else if (id == 5)
      return RIGHT;
    else
      throw new RuntimeException("AnatomicalDirection.getReverse(): This should never happen!");
  }

  public String getLabel() {
    return label;
  }

  public int getId() {
    return id;
  }

  public String toString() {
      return label;
  }

  public boolean equals(Object other) {
    if ( !(other instanceof AnatomicalDirection) )
      return false;
    if ( ((AnatomicalDirection)other).id == id )
      return true;
    return false;
  }





}