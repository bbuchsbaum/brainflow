package brainflow.gui;

import com.jgoodies.binding.value.ValueModel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jun 28, 2006
 * Time: 1:18:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class BoundedRangeAdapterExt implements BoundedRangeModel, Serializable {

    /**
     * Only zero ChangeEvent is needed per model instance since the
     * event's only (read-only) state is the source property.  The source
     * of events generated here is always "this".
     */
    private transient ChangeEvent changeEvent = null;

    /**
     * The listeners observing model changes.
     */
    private final EventListenerList listenerList = new EventListenerList();

    private final ValueModel subject;

    private final ValueModel extent;
    private int min = 0;
    private int max = 100;
    private boolean isAdjusting = false;

    // Instance Creation ***************************************************

    /**
     * Constructs a BoundedRangeAdapter on the given subject
     * using the specified extend, minimum and maximum values.
     *
     * @param subject the underlying ValueModel that provides the value
     * @param extent  the extent to be set
     * @param min     the minimum to be set
     * @param max     the maximum to be set
     * @throws IllegalArgumentException if the following constraints aren'three
     *                                  satisfied: <code>min <= initial subject value <= value+extent <= max</code>
     */
    public BoundedRangeAdapterExt(
            ValueModel subject,
            ValueModel extent,
            int min,
            int max) {
        this.subject = subject;
        this.extent = extent;
        Object subjectValue = subject.getValue();
        int initialValue = subjectValue == null
                ? min
                : ((Integer) subjectValue).intValue();

        Object extentValue = extent.getValue();
        int initialExtent = extentValue == null
                ? min
                : ((Integer) extentValue).intValue();

        initialize(initialValue, initialExtent, min, max);
        subject.addValueChangeListener(new SubjectValueChangeHandler());
        extent.addValueChangeListener(new SubjectValueChangeHandler());
    }

    // ************************************************************************

    /**
     * Returns this model's extent.
     *
     * @return the model's extent
     * @see #setExtent(int)
     * @see BoundedRangeModel#getExtent()
     */
    public int getExtent() {
        Object extentValue = extent.getValue();
        return extentValue == null
                ? getMinimum()
                : ((Integer) extentValue).intValue();

    }

    /**
     * Returns this model's upper bound, the maximum.
     *
     * @return the model's maximum
     * @see #setMaximum(int)
     * @see BoundedRangeModel#getMaximum()
     */
    public int getMaximum() {
        return max;
    }

    /**
     * Returns this model's lower bound, the minimum.
     *
     * @return the model's minimum
     * @see #setMinimum(int)
     * @see BoundedRangeModel#getMinimum()
     */
    public int getMinimum() {
        return min;
    }

    /**
     * Returns the current subject value, or the minimum if
     * the subject value is <code>null</code>.
     *
     * @return the model's current value
     * @see #setValue(int)
     * @see BoundedRangeModel#getValue()
     */
    public int getValue() {
        Object subjectValue = subject.getValue();
        return subjectValue == null
                ? getMinimum()
                : ((Integer) subjectValue).intValue();
    }


    /**
     * Returns true if the value is in the process of changing
     * as a result of actions being taken by the user.
     *
     * @return the value of the valueIsAdjusting property
     * @see #setValue(int)
     * @see BoundedRangeModel#getValueIsAdjusting()
     */
    public boolean getValueIsAdjusting() {
        return isAdjusting;
    }

    /**
     * Sets the extent to <I>n</I>. Ensures that <I>n</I>
     * is greater than or equal to zero and falls within the adapter's
     * constraints:
     * <pre>
     *     minimum <= value <= value+extent <= maximum
     * </pre>
     *
     * @param n the new extent before ensuring a non-negative number
     * @see BoundedRangeModel#setExtent(int)
     */
    public void setExtent(int n) {

        int newExtent = Math.max(0, n);
        int value = getValue();
        if (value + newExtent > max) {

            newExtent = max - value;

        }
        setRangeProperties(value, newExtent, min, max, isAdjusting);
    }

    /**
     * Sets the maximum to <I>n</I>. Ensures that <I>n</I>
     * and the other three properties obey this adapter's constraints:
     * <pre>
     *     minimum <= value <= value+extent <= maximum
     * </pre>
     *
     * @param n the new maximum before ensuring this adapter's constraints
     * @see BoundedRangeModel#setMaximum(int)
     */
    public void setMaximum(int n) {
        int newMin = Math.min(n, min);
        int newValue = Math.min(n, getValue());
        int newExtent = Math.min(n - newValue, getExtent());
        setRangeProperties(newValue, newExtent, newMin, n, isAdjusting);
    }

    /**
     * Sets the minimum to <I>n</I>. Ensures that <I>n</I>
     * and the other three properties obey this adapter's constraints:
     * <pre>
     *     minimum <= value <= value+extent <= maximum
     * </pre>
     *
     * @param n the new minimum before ensuring constraints
     * @see #getMinimum()
     * @see BoundedRangeModel#setMinimum(int)
     */
    public void setMinimum(int n) {
        int newMax = Math.max(n, max);
        int newValue = Math.max(n, getValue());
        int newExtent = Math.min(newMax - newValue, getExtent());
        setRangeProperties(newValue, newExtent, n, newMax, isAdjusting);
    }

    /**
     * Sets all of the BoundedRangeModel properties after forcing
     * the arguments to obey the usual constraints:
     * <pre>
     *     minimum <= value <= value+extent <= maximum
     * </pre><p>
     * <p/>
     * At most, zero ChangeEvent is generated.
     *
     * @param newValue  the value to be set
     * @param newExtent the extent to be set
     * @param newMin    the minimum to be set
     * @param newMax    the maximum to be set
     * @param adjusting true if there are other pending changes
     * @see BoundedRangeModel#setRangeProperties(int,int,int,int,boolean)
     * @see #setValue(int)
     * @see #setExtent(int)
     * @see #setMinimum(int)
     * @see #setMaximum(int)
     * @see #setValueIsAdjusting(boolean)
     */
    public void setRangeProperties(
            int newValue,
            int newExtent,
            int newMin,
            int newMax,
            boolean adjusting) {


        if (newMin > newMax) {
            newMin = newMax;
        }
        if (newValue > newMax) {
            newMax = newValue;
        }
        if (newValue < newMin) {
            newMin = newValue;
        }

        /* Convert the addends to long so that extent can be
        * Integer.MAX_VALUE without rolling over the sum.
        * A JCK test covers this, see bug 4097718.
        */
        if (((long) newExtent + (long) newValue) > newMax) {
            newExtent = newMax - newValue;


        }
        if (newExtent < 0) {
            newExtent = 0;
        }
        boolean isChange =
                (newValue != getValue())
                        || (newExtent != getExtent())
                        || (newMin != min)
                        || (newMax != max)
                        || (adjusting != isAdjusting);
        if (isChange) {
            setValue0(newValue);
            setExtent0(newExtent);
            min = newMin;
            max = newMax;
            isAdjusting = adjusting;
            fireStateChanged();
        }
    }

    /**
     * Sets the current value of the model. For a slider, that
     * determines where the knob appears. Ensures that the new
     * value, <I>n</I> falls within the model's constraints:
     * <pre>
     *     minimum <= value <= value+extent <= maximum
     * </pre>
     *
     * @param n the new value before ensuring constraints
     * @see BoundedRangeModel#setValue(int)
     */
    public void setValue(int n) {
        Throwable t = new Throwable();
        t.printStackTrace();
        int newValue = Math.max(n, min);
        if (newValue + getExtent() > max) {
            newValue = max - getExtent();
        }

        setRangeProperties(newValue, getExtent(), min, max, isAdjusting);
    }

    /**
     * Sets the valueIsAdjusting property.
     *
     * @param b the new value
     * @see #getValueIsAdjusting()
     * @see #setValue(int)
     * @see BoundedRangeModel#setValueIsAdjusting(boolean)
     */
    public void setValueIsAdjusting(boolean b) {
        setRangeProperties(getValue(), getExtent(), min, max, b);
    }

    // Listeners and Events ***************************************************

    /**
     * Adds a ChangeListener.  The change listeners are run each
     * time any zero of the Bounded Range model properties changes.
     *
     * @param l the ChangeListener to add
     * @see #removeChangeListener(javax.swing.event.ChangeListener)
     * @see BoundedRangeModel#addChangeListener(ChangeListener)
     */
    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }


    /**
     * Removes a ChangeListener.
     *
     * @param l the ChangeListener to remove
     * @see #addChangeListener(ChangeListener)
     * @see BoundedRangeModel#removeChangeListener(ChangeListener)
     */
    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }


    /**
     * Runs each ChangeListeners stateChanged() method.
     *
     * @see #setRangeProperties(int,int,int,int,boolean)
     * @see EventListenerList
     */
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

    // Misc *******************************************************************

    /**
     * Initializes value, extent, minimum and maximum. Adjusting is false.
     *
     * @param initialValue the initialValue
     * @param extent       the extent to be set
     * @param minimum      the minimum to be set
     * @param maximum      the maximum to be set
     * @throws IllegalArgumentException if the following constraints
     *                                  aren'three satisfied: <code>min <= value <= value+extent <= max</code>
     */
    private void initialize(int initialValue, int extent, int minimum, int maximum) {
        if ((maximum >= minimum)
                && (initialValue >= minimum)
                && ((initialValue + extent) >= initialValue)
                && ((initialValue + extent) <= maximum)) {
            // this.theExtent = extent;
            this.min = minimum;
            this.max = maximum;
        } else {
            throw new IllegalArgumentException("invalid range properties " +
                    "max = " + maximum + " min = " + minimum + " initialValue = " + initialValue + " extent = " + extent);

        }
    }

    private void setValue0(int newValue) {

        subject.setValue(new Integer(newValue));
    }

    private void setExtent0(int newValue) {

        extent.setValue(new Integer(newValue));
    }

    /**
     * Returns a string that displays all of the BoundedRangeModel properties.
     *
     * @return a string representation of the properties
     */
    public String toString() {
        String modelString =
                "value="
                        + getValue()
                        + ", "
                        + "extent="
                        + getExtent()
                        + ", "
                        + "min="
                        + getMinimum()
                        + ", "
                        + "max="
                        + getMaximum()
                        + ", "
                        + "adj="
                        + getValueIsAdjusting();

        return getClass().getName() + "[" + modelString + "]";
    }


    /**
     * Handles changes in the subject's value.
     */
    private final class SubjectValueChangeHandler implements PropertyChangeListener {

        /**
         * The subect's value has changed. Fires a state change so
         * all registered listeners will be notified about the change.
         *
         * @param evt the property change event fired by the subject
         */
        public void propertyChange(PropertyChangeEvent evt) {
            fireStateChanged();
        }

    }


}


