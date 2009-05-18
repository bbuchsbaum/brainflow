package brainflow.core;

import brainflow.display.InterpolationType;
import brainflow.image.anatomy.BrainPoint3D;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.anatomy.GridPoint3D;
import brainflow.image.axis.AxisRange;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 25, 2007
 * Time: 4:18:03 PM
 * To change this template use File | Settings | File Templates.
 */


public interface IImageProducer {

    public void setPlot(IImagePlot plot);

    public IImagePlot getPlot();

    public ImageViewModel getModel();

    public Anatomy3D getDisplayAnatomy();

    public void setSlice(GridPoint3D slice);

    public GridPoint3D getSlice();

    public void setScreenSize(Rectangle rect);

    public void setScreenInterpolation(InterpolationType type);

    public InterpolationType getScreenInterpolation();
 
    public void reset();

    public AxisRange getXAxis();

    public AxisRange getYAxis();

    public Rectangle getScreenSize();

    public BufferedImage getImage();


}
