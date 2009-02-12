package brainflow.chart;

import brainflow.graphics.GraphicalPointer;

import javax.swing.*;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class AxisPointer extends GraphicalPointer {

    private double axisValue;
    JComponent owner;

    boolean dragging;

    public AxisPointer(JComponent _owner, float base, float height) {
        super(base, height);
        owner = _owner;
    }


    public double getAxisValue() {
        return axisValue;
    }

    public void setAxisValue(double axisValue) {
        this.axisValue = axisValue;
    }

    public boolean isDragging() {
        return dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }
}