package brainflow.image.anatomy;

import brainflow.image.IndexConverter1D;
import brainflow.image.axis.ImageAxis;
import brainflow.math.Vector3f;

/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: Jun 30, 2003
 * Time: 12:35:21 PM
 * To change this template use Options | File Templates.
 */
public class AnatomicalAxis implements Anatomy {

    private static final AnatomicalAxis[] instances = new AnatomicalAxis[6];

    public static final AnatomicalAxis ANTERIOR_POSTERIOR = new AnatomicalAxis(AnatomicalDirection.ANTERIOR, AnatomicalDirection.POSTERIOR, new Vector3f(0, -1, 0));
    public static final AnatomicalAxis POSTERIOR_ANTERIOR = new AnatomicalAxis(AnatomicalDirection.POSTERIOR, AnatomicalDirection.ANTERIOR, new Vector3f(0,  1, 0));

    public static final AnatomicalAxis INFERIOR_SUPERIOR = new AnatomicalAxis(AnatomicalDirection.INFERIOR, AnatomicalDirection.SUPERIOR, new Vector3f(0,  0,  1));
    public static final AnatomicalAxis SUPERIOR_INFERIOR = new AnatomicalAxis(AnatomicalDirection.SUPERIOR, AnatomicalDirection.INFERIOR, new Vector3f(0,  0, -1));

    public static final AnatomicalAxis LEFT_RIGHT = new AnatomicalAxis(AnatomicalDirection.LEFT, AnatomicalDirection.RIGHT, new Vector3f(1, 0, 0));
    public static final AnatomicalAxis RIGHT_LEFT = new AnatomicalAxis(AnatomicalDirection.RIGHT, AnatomicalDirection.LEFT, new Vector3f(-1,0, 0));


    public final AnatomicalDirection min;
    public final AnatomicalDirection max;


    private static int count = 0;

    private final Vector3f directionVector;

    private AnatomicalAxis(AnatomicalDirection min, AnatomicalDirection max, Vector3f dvec) {
        this.min = min;
        this.max = max;

        directionVector = dvec;
        instances[count] = this;
        count++;
    }

    private AnatomicalAxis lookupAxis(AnatomicalDirection min, AnatomicalDirection max) {
        for (int i = 0; i < instances.length; i++) {
            if ((instances[i].min == min) && (instances[i].max == max)) {
                return instances[i];
            }
        }

        throw new IllegalArgumentException("Illegal axis parameters: " + min + ", " + max);
    }

    public static AnatomicalAxis matchAnatomy(AnatomicalDirection minDirection) {
        if (minDirection == AnatomicalDirection.LEFT) {
            return LEFT_RIGHT;
        } else if (minDirection == AnatomicalDirection.RIGHT) {
            return RIGHT_LEFT;
        } else if (minDirection == AnatomicalDirection.ANTERIOR) {
            return ANTERIOR_POSTERIOR;
        } else if (minDirection == AnatomicalDirection.POSTERIOR) {
            return POSTERIOR_ANTERIOR;
        } else if (minDirection == AnatomicalDirection.INFERIOR) {
            return INFERIOR_SUPERIOR;
        } else if (minDirection == AnatomicalDirection.SUPERIOR) {
            return SUPERIOR_INFERIOR;
        } else {
            throw new AssertionError();
        }
    }

    public Vector3f getDirectionVector() {
        return directionVector;
    }

    public AnatomicalDirection getMinDirection() {
        return min;
    }

    public AnatomicalDirection getMaxDirection() {
        return max;
    }

    public boolean sameAxis(AnatomicalAxis other) {
        if (other == this) return true;
        if (other == getFlippedAxis()) return true;

        return false;
    }

    public boolean sameDirection(AnatomicalAxis other) {
        if (other == this) return true;
        return false;
    }

    public double convertValue(AnatomicalAxis other, double min, double max, double value) {
        if (!other.sameAxis(this)) {
            throw new ImageAxis.IncompatibleAxisException("axis: " + other + " is incompatible with this axis " + this);
        }

        if (other == this) {
            return value;
        } else if (other == getFlippedAxis()) {
            return (max - value) + min;
        }

        throw new AssertionError("Shouldn't ever get here.");
    }

    public AnatomicalAxis getFlippedAxis() {
        return lookupAxis(max, min);
    }

    public String toString() {
        return min + "-" + max;
    }

    public static class IndexDoNothing implements IndexConverter1D {
        public final int convert(int in) {
            return in;
        }
    }

    public static class IndexFlipper implements IndexConverter1D {

        int maxIdx;

        public IndexFlipper(int dimLen) {
            maxIdx = dimLen - 1;
        }

        public int convert(int in) {
            return maxIdx - in;
        }
    }


}
