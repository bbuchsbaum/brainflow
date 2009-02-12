package brainflow.functional;

import java.util.*;

/**
 * Title:        LCBR Home Project
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 *
 * @author Brad Buchsbaum
 * @version 1.0
 */

public class Factor {

    private String name;

    private Map<String, List<Integer>> levelMap = new TreeMap<String, List<Integer>>();

    private List<String> values = new ArrayList<String>();

    private int length = 0;

    public Factor(String _name) {
        name = _name;
    }

    public Factor(String _name, String[] values) {
        name = _name;
        for (int i = 0; i < values.length; i++) {
            addValue(values[i]);
        }

    }

    public String getName() {
        return name;
    }

    public void addLevel(String level) {
        if (!levelMap.containsKey(level)) {
            levelMap.put(level, new ArrayList<Integer>());
        }
    }

    public void addValue(String obj, int reps) {
        if (!levelMap.containsKey(obj)) {
            List<Integer> indexList = new ArrayList<Integer>();

            for (int i = length; i < length + reps; i++) {
                indexList.add(new Integer(i));
                values.add(obj);
            }

            length = length + reps;
            levelMap.put(obj, indexList);

        } else {
            List<Integer> indexList = levelMap.get(obj);
            for (int i = length; i < length + reps; i++) {
                indexList.add(i);
                values.add(obj);
            }

            length = length + reps;
        }

    }


    public void addValue(String obj) {
        if (!levelMap.containsKey(obj)) {
            List<Integer> indexList = new ArrayList<Integer>();
            indexList.add(new Integer(length));
            levelMap.put(obj, indexList);
            length++;

        } else {
            List<Integer> indexList = levelMap.get(obj);
            indexList.add(new Integer(length));
            length++;
        }

        values.add(obj);
    }

    public String[] getValues() {
        String[] ret = new String[values.size()];
        values.toArray(ret);
        return ret;
    }

    public String[] getLabels() {
        String[] levels = new String[levelMap.size()];
        int i = 0;
        for (Iterator iter = levelMap.keySet().iterator(); iter.hasNext(); i++) {
            levels[i] = iter.next().toString();
        }

        return levels;
    }


    public Object[] getLevelSet() {
        Object[] levels = new Object[levelMap.size()];
        int i = 0;
        for (Iterator iter = levelMap.keySet().iterator(); iter.hasNext(); i++) {
            levels[i] = iter.next();
        }

        return levels;
    }


    public int[] getIndices(String value) {
        ArrayList indexList = (ArrayList) levelMap.get(value);
        int[] array = new int[indexList.size()];
        for (int i = 0; i < array.length; i++) {
            Integer index = (Integer) indexList.get(i);
            array[i] = index.intValue();
        }

        return array;
    }

    public Factor multiply(Factor other) {
        if (other.length != length)
            throw new IllegalArgumentException("Factor.multiply() requires that the factors have same length! " + other.length + " != " + length);

        Factor multf = new Factor(name + ":" + other.getName());
        Object[] vals = getValues();
        Object[] ovals = other.getValues();

        for (int i = 0; i < vals.length; i++) {
            multf.addValue(vals[i].toString() + ":" + ovals[i].toString());
        }

        return multf;

    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Factor: ").append(getName()).append("\n");
        Object[] vals = getValues();

        for (int i = 0; i < vals.length; i++) {
            buffer.append(vals[i]).append(", ");
        }

        buffer.append("\n\n");

        buffer.append("Levels: " + "\n");
        buffer.append("\t");

        Object[] levs = getLevelSet();
        for (int i = 0; i < levs.length; i++) {
            buffer.append(levs[i]);
            buffer.append("\t");
        }

        buffer.append("\n");
        return buffer.toString();
    }

    /* public boolean equals(Object other) {
      if ( ((Factor)other).getName() == getName() )
        return true;
      return false;
    }*/


    public int length() {
        return length;
    }


}