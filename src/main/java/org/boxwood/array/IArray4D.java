package org.boxwood.array;

import brainflow.utils.Dimension4D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Sep 4, 2009
 * Time: 12:26:33 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IArray4D extends IArray {

    public double value(int i, int j, int k, int m);

    public int indexOf(int i, int j, int k, int m);

    public void set(int i, int j, int k, int m, double val);

    public void set(int i, int j, int k, int m, int val);

    public void set(int i, int j, int k, int m, short val);

    public void set(int i, int j, int k, int m, float val);

    public void set(int i, int j, int k, int m, long val);

    public void set(int i, int j, int k, int m, byte val);

    
    @Override
    Dimension4D<Integer> dim();
}
