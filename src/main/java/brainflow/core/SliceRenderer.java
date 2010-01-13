package brainflow.core;

import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.anatomy.VoxelLoc3D;
import brainflow.image.space.ICoordinateSpace;
import brainflow.core.layer.AbstractLayer;

import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 21, 2007
 * Time: 7:06:45 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SliceRenderer {

    public ICoordinateSpace getImageSpace();

    public Anatomy3D getDisplayAnatomy();

    public void setSlice(VoxelLoc3D slice);

    public VoxelLoc3D getSlice();

    public BufferedImage render();

    public void renderUnto(Rectangle2D frame, Graphics2D g2);

    public void flush();

    public boolean isVisible();

    public AbstractLayer getLayer();
}
