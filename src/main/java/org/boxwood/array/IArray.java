package org.boxwood.array;

import brainflow.utils.IDimension;

import java.util.Arrays;
import java.lang.reflect.Array;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Sep 4, 2009
 * Time: 12:21:17 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IArray {

    public double value(int i);

    public void set(int i, double val);

    public void set(int i, float val);

    public void set(int i, short val);

    public void set(int i, int val);

    public void set(int i, long val);

    public void set(int i, byte val);

    public Class getType();

    public int length();

    public IDimension<Integer> dim();


}
