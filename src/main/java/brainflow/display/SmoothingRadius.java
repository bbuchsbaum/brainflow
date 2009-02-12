package brainflow.display;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Aug 28, 2007
 * Time: 9:29:51 PM
 */

@XStreamAlias("smoothing-radius")
public class SmoothingRadius extends LayerProperty {

    public static final String RADIUS_PROPERTY = "smoothingRadius";

    @XStreamAlias("value")
    @XStreamAsAttribute
    private int smoothingRadius = 0;


    public SmoothingRadius() {
    }

    public SmoothingRadius(int radius) {
        this.smoothingRadius = radius;
    }

    public String getName() {
        return "smoothingRadius";
    }

    public Object getValue() {
        return smoothingRadius;
    }

    public int getSmoothingRadius() {
        return smoothingRadius;
    }

    public void setSmoothingRadius(int smoothingRadius) {
        double old = getSmoothingRadius();
        this.smoothingRadius = smoothingRadius;

        firePropertyChange(RADIUS_PROPERTY, old, getSmoothingRadius());
    }
}
