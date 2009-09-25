package org.boxwood.array;

import brainflow.utils.Dimension3D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Sep 4, 2009
 * Time: 12:25:11 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IArray3D extends IArray {


    public double value(int i, int j, int k);

    public int indexOf(int i, int j, int k);

    public void set(int i, int j, int k, double val);

    public void set(int i, int j, int k, int val);

    public void set(int i, int j, int k, short val);

    public void set(int i, int j, int k, float val);

    public void set(int i, int j, int k, long val);

    public void set(int i, int j, int k, byte val);

    @Override
    Dimension3D<Integer> dim();
}
