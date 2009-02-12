package brainflow.colormap.operations;
import java.awt.image.*;

/**
 * Title:        Parvenu
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:
 * @author Brad Buchsbaum
 * @version 1.0
 */

public interface ColorMapTransformOp {

  public IndexColorModel transformMap(IndexColorModel icm);
  public RenderedImage transformImage(RenderedImage img);


}