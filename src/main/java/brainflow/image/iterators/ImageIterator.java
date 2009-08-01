package brainflow.image.iterators;

import brainflow.image.space.IImageSpace;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public interface ImageIterator extends ValueIterator {

  public IImageSpace getImageSpace();

}