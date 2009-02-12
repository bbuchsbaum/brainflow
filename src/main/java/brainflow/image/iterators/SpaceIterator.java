package brainflow.image.iterators;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public interface SpaceIterator {

  public int nextIndex();

  public int[] nextCoordinate(int[] coord);

  public int[] nextCoordinate();

  public int remaining();

  public int previousIndex();

  public boolean hasNext();

  public boolean hasPrevious();
}