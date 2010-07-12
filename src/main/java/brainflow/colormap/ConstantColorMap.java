package brainflow.colormap;

import javax.swing.*;
import java.util.ListIterator;
import java.util.ArrayList;
import java.awt.*;


/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jun 24, 2007
 * Time: 3:56:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConstantColorMap extends AbstractColorMap {

    private ColorInterval interval;


    public ConstantColorMap(double min, double max, Color clr) {
        setMinimumValue(min);
        setMaximumValue(max);
        
        highClip = getMaximumValue();
        lowClip = getMinimumValue();

        interval = new ColorInterval(new OpenInterval(min, max), clr);
    }

    public AbstractColorBar createColorBar() {
        return new LinearColorBar(this, SwingConstants.HORIZONTAL);

    }


    public ListIterator<ColorInterval> iterator() {
       java.util.List<ColorInterval> clist = new ArrayList<ColorInterval>();
       clist.add(interval);
       return clist.listIterator();
    }

    public int getMapSize() {
        return 1;
    }

    public ColorInterval getInterval(int index) {
        return interval;
    }

    public Color getColor(double value) {
        // todo check if file is in bounds?
        return interval.getColor();
    }

    public IColorMap newClipRange(double lowClip, double highClip, double min, double max) {
        return this;
    }
}
