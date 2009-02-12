package brainflow.colormap;

import brainflow.image.data.IImageData2D;
import brainflow.image.data.RGBAImage;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.util.ListIterator;

/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: Mar 10, 2005
 * Time: 12:45:36 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IColorMap {

    public static final String HIGH_CLIP_PROPERTY = "highClip";

    public static final String LOW_CLIP_PROPERTY = "lowClip";

    public static final String MAP_SIZE_PROPERTY = "mapSize";

    public static final String MAXIMUM_VALUE_PROPERTY = "maximumValue";

    public static final String MINIMUM_VALUE_PROPERTY = "minimumValue";

    public static final int MAXIMUM_INTERVALS = 1000;

    public int getMapSize();

    public ColorInterval getInterval(int index);

    public Color getColor(double value);

    public double getHighClip();

    public double getLowClip();

    public IColorMap newClipRange(double lowClip, double highClip, double min, double max);

    //public void setHighClip(double clip);

    //public void setLowClip(double clip);

    public double getMaximumValue();

    public double getMinimumValue();

    public ListIterator<ColorInterval> iterator();

    public RGBAImage getRGBAImage(IImageData2D data);

    public AbstractColorBar createColorBar();

    public void addPropertyChangeListener(PropertyChangeListener listener);

    public void removePropertyChangeListener(PropertyChangeListener listener);

}
