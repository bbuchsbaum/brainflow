package brainflow.core;

import brainflow.core.annotations.IAnnotation;
import brainflow.display.InterpolationType;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.anatomy.GridPoint3D;
import brainflow.image.anatomy.SpatialLoc2D;
import brainflow.image.axis.AxisRange;

import javax.swing.*;
import java.awt.*;
import java.util.Map;


/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Dec 3, 2004
 * Time: 12:14:51 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IImagePlot {

    public InterpolationType getScreenInterpolation();

    public void setScreenInterpolation(InterpolationType type);

    public void setPreserveAspectRatio(boolean b);

    public boolean isPreserveAspectRatio();

    public void setName(String name);

    public Insets getPlotInsets();

    public Insets getPlotMargins();

    public void setPlotInsets(Insets insets);

    public Anatomy3D getDisplayAnatomy();

    public ImageViewModel getModel();

    public void setViewBounds(ViewBounds vbounds);

    public ViewBounds getViewBounds();

    public void addViewBoundsChangedListener(ViewBoundsChangedListener listener);

    public void removeViewBoundsChangedListener(ViewBoundsChangedListener listener);

    public double getScaleX();

    public double getScaleY();

    public void setSlice(GridPoint3D slice);

    public GridPoint3D getSlice();

    public Point translateAnatToScreen(SpatialLoc2D pt);

    public SpatialLoc2D translateScreenToAnat(Point pt);

    public Rectangle getPlotArea();

    public double getXExtent();

    public double getYExtent();

    public AxisRange getXAxisRange();

    public AxisRange getYAxisRange();

    public JComponent getComponent();

    public Dimension getPreferredSize();

    public IImageProducer getImageProducer();

    public void setImageProducer(IImageProducer producer);

    public Map<String, IAnnotation> getAnnotations();

    public void setAnnotation(String name, IAnnotation annotation);

    public IAnnotation getAnnotation(String name);

    public void removeAnnotation(String name);

    public void clearAnnotations();


}
