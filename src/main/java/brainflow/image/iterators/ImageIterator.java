package brainflow.image.iterators;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public interface ImageIterator {

  public double next();

  public void advance();

  public double previous();

  public boolean hasNext();
    
  public double jump(int number);

  //public boolean canJump(int number);

  public double nextRow();

  public double nextPlane();

  public boolean hasNextRow();

  public boolean hasNextPlane();

  public boolean hasPreviousRow();

  public boolean hasPreviousPlane();

  public double previousRow();

  public double previousPlane();

  public void set(double val);
    
  public int index();






}