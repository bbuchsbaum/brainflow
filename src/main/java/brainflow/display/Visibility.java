package brainflow.display;

import brainflow.core.layer.ImageLayerProperties;
import com.jgoodies.binding.beans.Model;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Created by IntelliJ IDEA.
 * User: buchs
 * Date: Aug 31, 2006
 * Time: 1:39:40 PM
 * To change this template use File | Settings | File Templates.
 */

@XStreamAlias("visible")
public class Visibility extends Model {

    @XStreamAsAttribute()
    @XStreamAlias("value")
    private boolean visible = true;

    @XStreamOmitField
    private ImageLayerProperties layer;

    public static final String VISIBLE_PROPERTY = "visible";


    public Visibility(ImageLayerProperties _layer, boolean visible) {
        layer = _layer;
        this.visible = visible;
    }


    public ImageLayerProperties getLayerParameters() {
        return layer;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        boolean old = isVisible();
        this.visible = visible;

        firePropertyChange(VISIBLE_PROPERTY, old, this.visible);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Visibility that = (Visibility) o;

        if (visible != that.visible) return false;

        return true;
    }

    public int hashCode() {
        return (visible ? 1 : 0);
    }
}
