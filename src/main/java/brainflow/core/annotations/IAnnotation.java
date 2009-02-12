package brainflow.core.annotations;

import brainflow.core.IImagePlot;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeListener;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 15, 2006
 * Time: 2:02:35 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IAnnotation extends Cloneable {


    public boolean isVisible();
    
    public IAnnotation safeCopy();

    public void draw(Graphics2D g2d, Rectangle2D plotArea, IImagePlot plot);

    public void addPropertyChangeListener(PropertyChangeListener listener);

    public void removePropertyChangeListener(PropertyChangeListener listener);

    public String getIdentifier();

}
