package brainflow.gui;

import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.event.ChangeListener;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 12, 2008
 * Time: 7:56:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class NumberRangeModel {


    protected transient ChangeEvent changeEvent = null;


    protected EventListenerList listenerList = new EventListenerList();


    private double min = 0;

    private double max = 100;

    private double highValue = max;

    private double lowValue = min;


    private boolean isAdjusting = false;


    public NumberRangeModel(double lowValue, double highValue, double min, double max) {
        if ((max >= min) &&
                (lowValue >= min) &&
                (highValue <= max) &&
                (highValue >= lowValue)) {
            this.lowValue = lowValue;
            this.highValue = highValue;
            this.min = min;
            this.max = max;
        } else {
            throw new IllegalArgumentException("invalid range properties");
        }
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getHighValue() {
        return highValue;
    }

    public boolean inBounds(double value) {
        if (value >= min & value <= max) return true;
        return false;
    }



    public void setHighValue(double n) {
        double newLow = getLowValue();
        if (n < newLow) {
            newLow = n;
        }
        setRangeProperties(newLow, n, getMin(), getMax(), isAdjusting);
    }

    public void setLowValue(double n) {
        double newHigh = getHighValue();
        if (n > newHigh) {
            newHigh = n;
        }
        setRangeProperties(n, newHigh, getMin(), getMax(), isAdjusting);
    }

   
    public double getLowValue() {
        return lowValue;
    }

    public boolean isAdjusting() {
        return isAdjusting;
    }

    public void setRangeProperties(double newLow, double newHigh, double newMin, double newMax, boolean adjusting) {
        if (newMin > newMax) {
            newMin = newMax;
        }
        if (newHigh > newMax) {
            newHigh = newMax;
        }
        if (newLow < newMin) {
            newLow = newMin;
        }



        boolean isChange =
                (newHigh != highValue) ||
                        (newLow != lowValue) ||
                        (newMin != min) ||
                        (newMax != max) ||
                        (adjusting != isAdjusting);

        if (isChange) {
            highValue = newHigh;
            lowValue = newLow;
            min = newMin;
            max = newMax;
            isAdjusting = adjusting;

            
            fireStateChanged();
        }
    }


    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }


    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }


    public ChangeListener[] getChangeListeners() {
        return listenerList.getListeners(
                ChangeListener.class);
    }


    protected void fireStateChanged() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }

}
