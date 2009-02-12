package brainflow.display;

import com.jgoodies.binding.beans.Model;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 3, 2006
 * Time: 9:51:33 PM
 * To change this template use File | Settings | File Templates.
 */


public class InterpolationMethod extends Model {


    public static final String INTERPOLATION_PROPERTY = "interpolation";


    public InterpolationMethod(InterpolationType interpolation) {
        this.interpolation = interpolation;
    }


    private InterpolationType interpolation = InterpolationType.CUBIC;

    public static InterpolationType[] getInterpolationHints() {
        return InterpolationType.values();
    }

    public InterpolationType getInterpolation() {
        return interpolation;
    }

    public void setInterpolation(InterpolationType interpolation) {
        InterpolationType old = this.interpolation;
        this.interpolation = interpolation;
        
        firePropertyChange(INTERPOLATION_PROPERTY, old, interpolation);

    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InterpolationMethod that = (InterpolationMethod) o;

        if (interpolation != that.interpolation) return false;

        return true;
    }

    public int hashCode() {
        return (interpolation != null ? interpolation.hashCode() : 0);
    }
}
