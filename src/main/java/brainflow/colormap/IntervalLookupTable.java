package brainflow.colormap;


import java.util.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 23, 2005
 * Time: 7:43:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class IntervalLookupTable<T extends MutableInterval> {


    private List<T> intervalList = new LinkedList<T>();

    private final Comparator wi_comparator = new AbstractInterval.WithinIntervalComparator();

    private final Comparator i_comparator = new AbstractInterval.IntervalComparator();

    public IntervalLookupTable() {
        intervalList = new ArrayList<T>();
    }


    public void addInterval(T ival) {
        if (intervalList.contains(ival)) {
            throw new IllegalArgumentException("entry already exists for interval: " + ival);
        }

        if (intervalList.size() == 0) {
            intervalList.add(ival);
            return;
        }

        // Search for the non-existent item
        int index = Collections.binarySearch(intervalList, ival, i_comparator);
        // Add the non-existent item to the list
        if (index < 0) {
            Interval nearest = intervalList.get(intervalList.size() - 1);
            if (nearest.overlapsWith(ival)) {
                throw new IllegalArgumentException("interval overlaps with existing interval in table: " +
                        nearest + " overlaps with " + ival);

            }

            intervalList.add(-index - 1, ival);

        } else {

            intervalList.add(index, ival);
        }
    }


    public void addIntervalToEnd(T ival) {
        if (intervalList.size() == 0) {
            intervalList.add(ival);
            return;
        }

        if (!(ival.getMinimum() >= getLastInterval().getMaximum())) {
            throw new IllegalArgumentException("interval cannot be added to end, minimum less than current maximum");
        }

        if (getLastInterval().overlapsWith(ival)) {
            throw new IllegalArgumentException("interval cannot be added to end, overlaps with current max interval" +
                    " interval: " + ival + " max interval: " + getLastInterval());

        }

        intervalList.add(ival);

    }

    public void addIntervals(T[] intervals) {
        for (T i : intervals) {
            addInterval(i);
        }
    }

    public T getFirstInterval() {
        if (intervalList.size() > 0) {
            return intervalList.get(0);
        }

        return null;
    }

    public T getLastInterval() {
        if (intervalList.size() > 0) {
            return intervalList.get(intervalList.size() - 1);
        }

        return null;
    }

    public void removeFirstInterval() {
        if (!intervalList.isEmpty()) {
            intervalList.remove(0);
        }
    }

    public void removeLastInterval() {
        if (!intervalList.isEmpty()) {
            intervalList.remove(intervalList.size() - 1);
        }
    }

    public int getNumIntervals() {
        return intervalList.size();
    }

    public void insertInterval(int index, T interval) {
        intervalList.add(index, interval);

    }

    public void setInterval(int index, T interval) {
        T oldval = intervalList.get(index);
        if (oldval == null) {
            throw new IllegalArgumentException("Interval is null for supplied index");
        }

        if ((oldval.getMinimum() == interval.getMinimum()) &&
                (oldval.getMaximum() == interval.getMaximum())) {
            intervalList.set(index, interval);
        } else if (oldval.overlapsWith(interval)) {
            placeInterval(index, interval);
        } else {
            throw new IllegalArgumentException("Supplied interval must overlap with prior interval at same index " +
                    "old interval: " + oldval + " new interval: " + interval);
        }
    }


    private List<T> suture(int index, List<T> ilist) {
        if (index < 0 || index > (ilist.size() - 1)) {
            return ilist;
        }

        if (ilist.size() == 1) {
            // no suturing required or possible
            return ilist;
        } else if (index == 0) {
            T next = ilist.get(index + 1);
            T replace = ilist.get(index);
            if (Double.compare(replace.getMaximum(), next.getMinimum()) != 0) {

                next.setRange(replace.getMaximum(), next.getMinimum());
            }

        } else if (index == (ilist.size() - 1)) {

            T prev = ilist.get(index - 1);
            T replace = ilist.get(index);

            if (Double.compare(prev.getMaximum(), replace.getMinimum()) != 0) {
                prev.setRange(prev.getMinimum(), replace.getMinimum());
            }


        } else {
            T prev = ilist.get(index - 1);
            T replace = ilist.get(index);
            T next = ilist.get(index + 1);

            if (Double.compare(prev.getMaximum(), replace.getMinimum()) != 0) {
                prev.setRange(prev.getMinimum(), replace.getMinimum());
            }

            if (Double.compare(replace.getMaximum(), next.getMinimum()) != 0) {
                next.setRange(replace.getMaximum(), next.getMinimum());
            }

        }

        return suture(index + 1, ilist);

    }

    private void placeInterval(int index, T interval) {

        T oldInterval = getInterval(index);

        /*if (!(interval.overlapsWith(oldInterval) || interval.leftAdjacent(oldInterval) || interval.rightAdjacent(oldInterval))) {
            throw new IllegalArgumentException("Supplied interval must overlap with prior interval at same index");
        } */

        double difference = interval.getMinimum() - oldInterval.getMinimum();
        double makeup = difference / (double) index;

        /*double min = getFirstInterval().getMinimum();
        double max = getLastInterval().getMaximum();
        double nboundary = interval.getMinimum() - min;
        double oboundary = getInterval(index).getMinimum() - min;
        double difference = nboundary - oboundary;
        double fraction = difference / (index - 1);     */

        double prevMax = getFirstInterval().getMinimum();

        for (int i = 0; i < index; i++) {
            T t = getInterval(i);
            t.setRange(prevMax, prevMax + t.getSize() + makeup);
            prevMax = t.getMaximum();
        }

        if (index > 0) {
            T t = getInterval(index - 1);
            t.setRange(t.getMinimum(), interval.getMinimum());
        }

        difference = oldInterval.getMaximum() - interval.getMaximum();
        makeup = difference / (double) (getNumIntervals() - index - 1);

        intervalList.set(index, interval);


        prevMax = interval.getMaximum();
        for (int i = (index + 1); i < getNumIntervals(); i++) {
            T t = getInterval(i);
            t.setRange(prevMax, prevMax + t.getSize() + makeup);
            prevMax = t.getMaximum();
        }


    }

    /*private int placeInterval(T interval) {
        List<T> newList = new ArrayList<T>();

        Iterator<T> iter = iterator();
        boolean placed = false;
        int replaceIndex = -1;

        while (iter.hasNext()) {
            T ival = iter.next();
            if (!interval.overlapsWith(ival)) {
                newList.add(ival);
            } else if (ival.equals(interval)) {
                replaceIndex = newList.size();
                newList.add(interval);
                placed = true;
            } else if (interval.containsInterval(ival) && !placed) {
                replaceIndex = newList.size();
                newList.add(interval);
                placed = true;
            } else if (interval.overlapsWith(ival) && !placed) {
                if (interval.getMinimum() == ival.getMinimum()) {
                    // intervals share a lower bound, just add the interval
                    replaceIndex = newList.size();
                    newList.add(interval);

                } else {
                    T truncated = (T) ival.truncate(interval.getMinimum());
                    newList.add(truncated);
                    replaceIndex = newList.size();
                    newList.add(interval);
                }

                placed = true;
            } else {

            }

        }

        newList = suture(replaceIndex, newList);
        intervalList = newList;

        assert replaceIndex != -1;
        return replaceIndex;


    }*/

    public T getInterval(int index) {
        return intervalList.get(index);
    }

    public ListIterator<T> iterator() {
        return intervalList.listIterator();
    }

    public T lookup(double val) {
        int index = 0;
        try {

            index = Collections.binarySearch(intervalList, val, wi_comparator);

            return intervalList.get(index);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw e;

        }
    }

    public double getMaximumValue() {
        if (intervalList.isEmpty()) return 0;
        Interval ival = intervalList.get(intervalList.size() - 1);
        if (ival != null) {
            return ival.getMaximum();
        }

        return 0;

    }

    public double getMinimumValue() {
        if (intervalList.isEmpty()) return 0;
        T ival = intervalList.get(0);
        if (ival != null) {
            return ival.getMinimum();
        }

        return 0;

    }

    public String toString() {
        Iterator<T> iter = intervalList.iterator();
        StringBuffer sb = new StringBuffer();

        while (iter.hasNext()) {
            T ival = iter.next();
            sb.append(ival);
            sb.append("  ");
        }

        return sb.toString();
    }


    public static void main(String[] args) {
        IntervalLookupTable itl = new IntervalLookupTable();


    }


}
