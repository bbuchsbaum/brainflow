package brainflow.image.data;

import cern.colt.bitvector.BitVector;
import brainflow.image.iterators.ImageIterator;
import brainflow.image.space.IImageSpace;

/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: May 2, 2005
 * Time: 1:06:03 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class BinaryImageData extends AbstractImageData {


    private BitVector bits;


    public BinaryImageData(IImageSpace space) {
        super(space);
        allocateBits();
    }

    public BinaryImageData(IImageSpace space, BitVector _bits) {
        super(space);

        if (_bits.size() != getImageSpace().getNumSamples()) {
            throw new IllegalArgumentException("BitVector has wrong number of samples: " + bits.size());
        }

        bits = _bits;

    }

    public BinaryImageData(IImageSpace space, boolean[] elements) {
        super(space);
        allocateBits(elements);

    }

    private void allocateBits() {
        bits = new BitVector(space.getNumSamples());
    }

    protected void allocateBits(boolean[] elements) {
        assert space.getNumSamples() == elements.length : "array size does not match ImageSpace dimensions";

        bits = new BitVector(elements.length);
        for (int i = 0; i < elements.length; i++) {
            bits.putQuick(i, elements[i]);
        }


    }

    protected BitVector getBitVector() {
        return bits;
    }

    public abstract BinaryImageData OR(BinaryImageData data);

    public abstract BinaryImageData AND(BinaryImageData data);



    public double maxValue() {
        if (bits.cardinality() > 0) return 1;
        else return 0;
    }

    public double minValue() {
        if (bits.cardinality() == bits.size()) return 1;
        else return 0;

    }

    public int cardinality() {
        return bits.cardinality();
    }

    public int length() {
        return bits.size();
    }


    public byte[] toBytes() {
        byte[] b = new byte[bits.size()];
        for (int i = 0; i < b.length; i++) {
            b[i] = bits.get(i) ? (byte) 1 : (byte) 0;
        }

        return b;

    }


    


}
