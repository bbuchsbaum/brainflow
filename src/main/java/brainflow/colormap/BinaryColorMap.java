package brainflow.colormap;

import javax.swing.*;

import java.util.ListIterator;
import java.util.List;
import java.util.Collections;
import java.util.Arrays;
import java.awt.Color;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Oct 14, 2007
 * Time: 5:21:41 PM
 * To change this template use File | Settings | File Templates.
 */
public final class BinaryColorMap extends AbstractColorMap {


    private final Color backgroundColor = new Color(0,0,0,0);

    private final Color foregroundColor;

    private List<ColorInterval> intervals;


    

    public BinaryColorMap(Color color) {
        this.foregroundColor = color;

        intervals = Collections.unmodifiableList(Arrays.asList(
                new ColorInterval(new OpenInterval(Double.NEGATIVE_INFINITY,0), backgroundColor),
                new ColorInterval(new ClosedOpenInterval(0,Double.POSITIVE_INFINITY), foregroundColor)));

    }

    public AbstractColorBar createColorBar() {

        return new DiscreteColorBar(this, SwingUtilities.HORIZONTAL);

    }

    public Color getColor(double value) {
        if (value > 0) {
            return foregroundColor;
        } else {
            return backgroundColor;
        }
    }

    public ColorInterval getInterval(int index) {
        return intervals.get(index);
    }

    public int getMapSize() {
        return 2;
    }

    public ListIterator<ColorInterval> iterator() {
        return intervals.listIterator();

    }

    public IColorMap newClipRange(double lowClip, double highClip, double min, double max) {
        return new BinaryColorMap(foregroundColor);
    }

    public static void main(String[] args) {
        BinaryColorMap map = new BinaryColorMap(Color.RED);
        System.out.println(map.getColor(0));
        System.out.println(map.getColor(.5));
        System.out.println(map.getColor(.9999));
        System.out.println(map.getColor(1));
        System.out.println(map.getColor(1000));

        System.out.println(map.getInterval(0).containsValue(0));
        System.out.println(map.getInterval(0).containsValue(.1));

        System.out.println(map.getInterval(1).containsValue(0));
        System.out.println(map.getInterval(1).containsValue(.1));



    }
}
