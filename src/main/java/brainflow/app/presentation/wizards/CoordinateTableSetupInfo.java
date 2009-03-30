package brainflow.app.presentation.wizards;

import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.space.ICoordinateSpace;

import java.beans.PropertyChangeSupport;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 15, 2007
 * Time: 4:34:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class CoordinateTableSetupInfo {

    public static final String DEFAULT_OPACITY_PROPERTY = "defaultOpacity";

    public static final String DEFAULT_VALUE_PROPERTY = "defaultValue";

    public static final String DEFAULT_COLOR_PROPERTY = "defaultColor";

    public static final String DEFAULT_RADIUS_PROPERTY = "defaultRadius";

    public static final String TABLE_SIZE_PROPERTY = "tableSize";

    public static final String ANATOMY_PROPERTY = "anatomy";

    public static final String COORDINATE_SPACE_PROPERTY = "coordinateSpace";


    private double defaultOpacity = 1;

    private double defaultValue = 1;

    private double defaultRadius = 2;

    private int tableSize = 10;

    private Color defaultColor = Color.ORANGE;

    private ICoordinateSpace coordinateSpace;

    private PropertyChangeSupport support = new PropertyChangeSupport(this);


    public double getDefaultOpacity() {
        return defaultOpacity;
    }

    public void setDefaultOpacity(double defaultOpacity) {
        double old = getDefaultOpacity();
        this.defaultOpacity = defaultOpacity;
        support.firePropertyChange(DEFAULT_OPACITY_PROPERTY, old, getDefaultOpacity());
    }

    public int getTableSize() {
        return tableSize;
    }

    public void setTableSize(int tableSize) {
        int old = getTableSize();
        this.tableSize = tableSize;
        support.firePropertyChange(TABLE_SIZE_PROPERTY, old, getTableSize());
    }

    public double getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(double defaultValue) {
        double old = getDefaultValue();
        this.defaultValue = defaultValue;
        support.firePropertyChange(DEFAULT_VALUE_PROPERTY, old, getDefaultValue());
    }

    public Color getDefaultColor() {
        return defaultColor;
    }

    public ICoordinateSpace getCoordinateSpace() {
        return coordinateSpace;
    }

    public void setCoordinateSpace(ICoordinateSpace coordinateSpace) {
        ICoordinateSpace old = getCoordinateSpace();
        this.coordinateSpace = coordinateSpace;
        support.firePropertyChange(COORDINATE_SPACE_PROPERTY, old, getCoordinateSpace());
    }

    public double getDefaultRadius() {
        return defaultRadius;
    }

    public void setDefaultRadius(double defaultRadius) {
        double old = getDefaultRadius();
        this.defaultRadius = defaultRadius;
        support.firePropertyChange(DEFAULT_RADIUS_PROPERTY, old, getDefaultRadius());

    }

    public void setDefaultColor(Color defaultColor) {
        Color old = getDefaultColor();
        this.defaultColor = defaultColor;
        support.firePropertyChange(DEFAULT_COLOR_PROPERTY, old, getDefaultColor());

    }

    public Anatomy3D getAnatomy() {
        return (Anatomy3D)coordinateSpace.getAnatomy();
    }


}
