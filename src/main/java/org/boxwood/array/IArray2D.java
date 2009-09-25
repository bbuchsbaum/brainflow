package org.boxwood.array;

import brainflow.utils.Dimension2D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Sep 4, 2009
 * Time: 12:24:14 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IArray2D extends IArray {


    public double value(int i, int j);

    public int indexOf(int i, int j);

    public void set(int i, int j, double val);

    public void set(int i, int j, int val);

    public void set(int i, int j, short val);

    public void set(int i, int j, float val);

    public void set(int i, int j, long val);

    public void set(int i, int j, byte val);


    @Override
    Dimension2D<Integer> dim();


}
