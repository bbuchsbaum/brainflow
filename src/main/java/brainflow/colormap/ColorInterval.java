package brainflow.colormap;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 23, 2005
 * Time: 10:59:30 PM
 * To change this template use File | Settings | File Templates.
 */
public final class ColorInterval implements Interval {

    private Color clr = Color.BLACK;
    private Interval interval;


    public ColorInterval(Interval _interval, Color c) {
        interval = _interval;
        clr = c;
    }

    public void setColor(Color c) {
        clr = c;
    }
    public Color getColor() {
        return clr;
    }

    public int getRed() {
        return clr.getRed();
    }

    public int getGreen() {
        return clr.getGreen();
    }

    public int getBlue() {
        return clr.getBlue();
    }

    public int getAlpha() {
        return clr.getAlpha();
    }


    public boolean overlapsWith(Interval other) {
        return interval.overlapsWith(other);
    }

    public boolean containsInterval(Interval other) {
        return interval.containsInterval(other);
    }

    public boolean containsValue(double v) {
        return interval.containsValue(v);
    }

    public int compareTo(Object o) {
        return interval.compareTo(o);
    }

    public double getMinimum() {
        return interval.getMinimum();
    }

    public double getMaximum() {
        return interval.getMaximum();
    }

    public double getSize() {
        return interval.getSize();
    }

  

    public boolean equals(Object other) {

        if (!(other instanceof ColorInterval)) return false;

        ColorInterval o1 = (ColorInterval) other;

        if (!o1.interval.equals(interval)) {
            return false;
        }

        if (!o1.getColor().equals(getColor())) {
            return false;
        }

        return true;


    }


    public ColorInterval clone() {
        //todo interval classes should be declared final
        return new ColorInterval(interval, clr);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("range(" + this.getMinimum() + ", " + this.getMaximum() + ") RGBA: " + clr);
        return sb.toString();


    }

}
