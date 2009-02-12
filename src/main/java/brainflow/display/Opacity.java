package brainflow.display;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import test.Testable;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 17, 2007
 * Time: 3:55:41 PM
 * To change this template use File | Settings | File Templates.
 */


@XStreamAlias("opacity")
public class Opacity extends LayerProperty {

    public static final String OPACITY_PROPERTY = "opacity";

    @XStreamAlias("value")
    @XStreamAsAttribute
    private double opacity = 1f;


    public Opacity() {
    }

    public Opacity(float opacity) {
        this.opacity = opacity;
    }

    @Testable
    public double getOpacity() {
        return opacity;
    }

    public String getName() {
        return OPACITY_PROPERTY;
    }

    public Object getValue() {
        return opacity;
    }

    public void setOpacity(double opacity) {
        double old = getOpacity();
        this.opacity = opacity;

       firePropertyChange(OPACITY_PROPERTY, old, getOpacity());
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Opacity opacity1 = (Opacity) o;

        if (Double.compare(opacity1.opacity, opacity) != 0) return false;

        return true;
    }

    public int hashCode() {
        long temp = opacity != +0.0d ? Double.doubleToLongBits(opacity) : 0L;
        return (int) (temp ^ (temp >>> 32));
    }
}
