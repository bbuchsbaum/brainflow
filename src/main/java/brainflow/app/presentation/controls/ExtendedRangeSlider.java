package brainflow.app.presentation.controls;

import com.jidesoft.swing.RangeSlider;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 6, 2006
 * Time: 11:06:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExtendedRangeSlider extends RangeSlider {


    public void setLowValue(int i) {

        int curValue = getModel().getValue();
        int diff = i - curValue;
        int newExtent = Math.max(getModel().getExtent() - diff, 0);
        this.getModel().setRangeProperties(i, newExtent, getModel().getMinimum(), getModel().getMaximum(), getValueIsAdjusting());
    }

    public void setHighValue(int i) {
        super.setHighValue(i);
    }
}
