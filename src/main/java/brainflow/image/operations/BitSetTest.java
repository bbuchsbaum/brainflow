package brainflow.image.operations;

import cern.colt.bitvector.BitVector;

import java.util.BitSet;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Dec 14, 2006
 * Time: 1:55:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class BitSetTest {


    public static BitSet generateRandomBitSet(int len) {
        BitSet set = new BitSet(len);
        for (int i = 0; i < len; i++) {
            set.set(i, Math.random() > .5);
        }

        return set;

    }

    public static BitVector generateRandomBitVector(int len) {
        BitVector set = new BitVector(len);
        for (int i = 0; i < len; i++) {

            set.putQuick(i, Math.random() > .5);
        }

        return set;

    }

    public static void main(String[] args) {
        double time = System.currentTimeMillis();

        for (int i = 0; i < 100; i++) {
            System.out.println(i);
            BitSet bset1 = generateRandomBitSet(64 * 64 * 64);
            BitSet bset2 = generateRandomBitSet(64 * 64 * 64);
            bset1.and(bset2);
        }

        double endtime = System.currentTimeMillis();
        System.out.println("average time = " + ((endtime - time) / 100));
    }


}
