package brainflow.math;

import brainflow.utils.NumberUtils;

/**
 * Title:        LCBR Home Project
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 *
 * @author Brad Buchsbaum
 * @version 1.0
 */

public class ArrayUtils {

    public ArrayUtils() {
    }

    public static double max(int[] array) {
        double max = -Double.MAX_VALUE;
        for (int i = 0; i < array.length; i++) {
            max = Math.max(array[i], max);
        }

        return max;
    }

    public static double max(short[] array) {
        double max = -Double.MAX_VALUE;
        for (int i = 0; i < array.length; i++) {
            max = Math.max(array[i], max);
        }

        return max;
    }

    public static double max(double[] array) {
        double max = -Double.MAX_VALUE;
        for (int i = 0; i < array.length; i++) {
            max = Math.max(array[i], max);
        }

        return max;
    }

    public static double max(float[] array) {
        double max = -Double.MAX_VALUE;
        for (int i = 0; i < array.length; i++) {
            max = Math.max(array[i], max);
        }

        return max;
    }

    public static double max(long[] array) {
        double max = -Double.MAX_VALUE;
        for (int i = 0; i < array.length; i++) {
            max = Math.max(array[i], max);
        }

        return max;
    }


    public static double min(int[] array) {
        double min = Double.MAX_VALUE;
        for (int i = 0; i < array.length; i++) {
            min = Math.min(array[i], min);
        }

        return min;
    }

    public static double min(short[] array) {
        double min = Double.MAX_VALUE;
        for (int i = 0; i < array.length; i++) {
            min = Math.min(array[i], min);
        }

        return min;
    }

    public static double min(double[] array) {
        double min = Double.MAX_VALUE;
        for (int i = 0; i < array.length; i++) {
            min = Math.min(array[i], min);
        }

        return min;
    }

    public static double min(float[] array) {
        double min = Double.MAX_VALUE;
        for (int i = 0; i < array.length; i++) {
            min = Math.min(array[i], min);
        }

        return min;
    }

    public static double min(long[] array) {
        double min = Double.MAX_VALUE;
        for (int i = 0; i < array.length; i++) {
            min = Math.min(array[i], min);
        }

        return min;
    }


    public static Object scale(Object array, float sf) {
        if (array instanceof float[])
            return scaleFloats((float[]) array, sf);
        else if (array instanceof double[])
            return scaleDoubles((double[]) array, sf);
        else if (array instanceof int[])
            return scaleInts((int[]) array, sf);
        else if (array instanceof short[])
            return scaleShorts((short[]) array, sf);
        else if (array instanceof byte[])
            return scaleBytes((byte[]) array, sf);
        else throw new IllegalArgumentException("ArrayUtils.sclaeToBytes: array argument is invalid array type!");
    }


    public static byte[] scaleBytes(byte[] data, float sf) {
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) (data[i] * sf);
        }

        return data;
    }

    public static short[] scaleShorts(short[] data, float sf) {
        for (int i = 0; i < data.length; i++) {
            data[i] = (short) (data[i] * sf);
        }

        return data;
    }


    public static int[] scaleInts(int[] data, float sf) {
        for (int i = 0; i < data.length; i++) {
            data[i] = (int) (data[i] * sf);
        }

        return data;
    }

    public static float[] scaleFloats(float[] data, float sf) {
        for (int i = 0; i < data.length; i++) {
            data[i] = (float) (data[i] * sf);
        }

        return data;
    }

    public static double[] scaleDoubles(double[] data, float sf) {
        for (int i = 0; i < data.length; i++) {
            data[i] = (double) (data[i] * sf);
        }

        return data;
    }

    public static double[] multiply(byte[] data, double sf) {
        double[] array = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            array[i] = (double) (data[i] * sf);
        }
        return array;
    }

    public static double[] multiply(short[] data, double sf) {
        double[] array = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            array[i] = (double) (data[i] * sf);
        }
        return array;
    }

    public static double[] multiply(int[] data, double sf) {
        double[] array = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            array[i] = (double) (data[i] * sf);
        }
        return array;
    }

    public static double[] multiply(float[] data, double sf) {
        double[] array = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            array[i] = (double) (data[i] * sf);
        }
        return array;
    }

    public static double[] multiply(double[] data, double sf) {
        double[] array = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            array[i] = (double) (data[i] * sf);
        }
        return array;
    }

    public static double[] multiply(long[] data, double sf) {
        double[] array = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            array[i] = (double) (data[i] * sf);
        }
        return array;
    }


    public static byte[] scaleToBytes(Object array, double min, double max, int range) {
        if (array instanceof float[])
            return scaleToBytes((float[]) array, min, max, range);
        else if (array instanceof double[])
            return scaleToBytes((double[]) array, min, max, range);
        else if (array instanceof int[])
            return scaleToBytes((int[]) array, min, max, range);
        else if (array instanceof short[])
            return scaleToBytes((short[]) array, min, max, range);
        else if (array instanceof byte[])
            return scaleToBytes((byte[]) array, min, max, range);
        else throw new IllegalArgumentException("ArrayUtils.sclaeToBytes: array argument is invalid array type!");
    }

    public static byte[] scaleToBytes(float[] array, double min, double max, int range) {

        byte[] bs = new byte[array.length];
        double scaleFactor = range / (max - min);
        for (int i = 0; i < array.length; i++) {
            int tmp = (int) Math.round((((double) array[i] - min) * scaleFactor));
            if (tmp < 0)
                tmp = 0;
            else if (tmp > 255)
                tmp = 255;
            bs[i] = (byte) tmp;

        }

        return bs;
    }

    public static byte[] scaleToBytes(double[] array, double min, double max, int range) {

        byte[] bs = new byte[array.length];
        double scaleFactor = range / (max - min);
        for (int i = 0; i < array.length; i++) {
            int tmp = (int) Math.round((((double) array[i] - min) * scaleFactor));
            if (tmp < 0)
                tmp = 0;
            else if (tmp > 255)
                tmp = 255;
            bs[i] = (byte) tmp;

        }

        return bs;
    }

    public static byte[] scaleToBytes(int[] array, double min, double max, int range) {
        byte[] bs = new byte[array.length];
        double scaleFactor = range / (max - min);
        for (int i = 0; i < array.length; i++) {
            int tmp = (int) Math.round((((double) array[i] - min) * scaleFactor));
            if (tmp < 0)
                tmp = 0;
            else if (tmp > 255)
                tmp = 255;
            bs[i] = (byte) tmp;

        }

        return bs;
    }

    public static byte[] scaleToBytes(short[] array, double min, double max, int range) {

        byte[] bs = new byte[array.length];
        double scaleFactor = range / (max - min);
        for (int i = 0; i < array.length; i++) {
            int tmp = (int) Math.round((((double) array[i] - min) * scaleFactor));
            if (tmp < 0)
                tmp = 0;
            else if (tmp > 255)
                tmp = 255;
            bs[i] = (byte) tmp;

        }

        return bs;
    }

    public static void main(String[] args) {
        byte b = (byte) 145;

    }


    public static byte[] scaleToBytes(byte[] array, double min, double max, int range) {
        byte[] bs = new byte[array.length];
        double scaleFactor = range / (max - min);
        for (int i = 0; i < array.length; i++) {
            int val = NumberUtils.ubyte(array[i]);

            int tmp = (int) Math.round((((double) val - min) * scaleFactor));
            if (tmp < 0)
                tmp = 0;
            else if (tmp > 255)
                tmp = 255;
            bs[i] = (byte) tmp;

        }

        return bs;
    }

    public static byte[] scaleToBytes(boolean[] array, double min, double max, int range) {
        if (range > 255)
            range = 255;

        byte[] bs = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            if (array[i])
                bs[i] = (byte) range;
        }

        return bs;
    }

    public static byte[] castToBytes(double[] array) {
        byte[] op = new byte[array.length];
        for (int i = 0; i < op.length; i++)
            op[i] = (byte) array[i];
        return op;
    }

    public static byte[] castToUnsignedBytes(double[] array) {
        byte[] op = new byte[array.length];
        for (int i = 0; i < op.length; i++) {
            if (array[i] > 255) {
                op[i] = (byte) 255;
            } else if (array[i] < 0) {
                op[i] = 0;
            } else {
                op[i] = (byte) array[i];
            }

        }
        return op;
    }

    public static byte[] castToBytes(float[] array) {
        byte[] op = new byte[array.length];
        for (int i = 0; i < op.length; i++)
            op[i] = (byte) array[i];
        return op;
    }

    public static byte[] castToBytes(short[] array) {
        byte[] op = new byte[array.length];
        for (int i = 0; i < op.length; i++)
            op[i] = (byte) array[i];
        return op;
    }

    public static byte[] castToBytes(int[] array) {
        byte[] op = new byte[array.length];
        for (int i = 0; i < op.length; i++)
            op[i] = (byte) array[i];
        return op;
    }


    public static short[] castToShorts(double[] array) {
        short[] op = new short[array.length];
        for (int i = 0; i < op.length; i++)
            op[i] = (short) array[i];
        return op;
    }

    public static short[] castToShorts(float[] array) {
        short[] op = new short[array.length];
        for (int i = 0; i < op.length; i++)
            op[i] = (short) array[i];
        return op;
    }

    public static short[] castToShorts(int[] array) {
        short[] op = new short[array.length];
        for (int i = 0; i < op.length; i++)
            op[i] = (short) array[i];
        return op;
    }


    public static short[] castToShorts(byte[] array) {
        short[] op = new short[array.length];
        for (int i = 0; i < op.length; i++)
            op[i] = (short) array[i];
        return op;
    }


    public static float[] castToFloats(double[] array) {
        float[] op = new float[array.length];
        for (int i = 0; i < op.length; i++)
            op[i] = (float) array[i];
        return op;
    }


    public static float[] castToFloats(int[] array) {
        float[] op = new float[array.length];
        for (int i = 0; i < op.length; i++)
            op[i] = (float) array[i];
        return op;
    }

    public static float[] castToFloats(byte[] array) {
        float[] op = new float[array.length];
        for (int i = 0; i < op.length; i++)
            op[i] = (float) array[i];
        return op;
    }


    public static float[] castToFloats(short[] array) {
        float[] op = new float[array.length];
        for (int i = 0; i < op.length; i++)
            op[i] = (float) array[i];
        return op;
    }


    public static int[] castToInts(double[] array) {
        int[] op = new int[array.length];
        for (int i = 0; i < op.length; i++)
            op[i] = (int) array[i];
        return op;
    }


    public static int[] castToInts(float[] array) {
        int[] op = new int[array.length];
        for (int i = 0; i < op.length; i++)
            op[i] = (int) array[i];
        return op;
    }

    public static int[] castToInts(byte[] array) {
        int[] op = new int[array.length];
        for (int i = 0; i < op.length; i++)
            op[i] = (int) array[i];
        return op;
    }


    public static int[] castToInts(short[] array) {
        int[] op = new int[array.length];
        for (int i = 0; i < op.length; i++)
            op[i] = (int) array[i];
        return op;
    }

    public static double[] unsignedBytesToDoubles(byte[] array) {
        double[] op = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            op[i] = NumberUtils.ubyte(array[i]);
        }

        return op;
    }


    public static double[] castToDoubles(byte[] array) {
        double[] op = new double[array.length];
        for (int i = 0; i < op.length; i++)
            op[i] = (double) array[i];
        return op;
    }

    public static double[] castToDoubles(short[] array) {
        double[] op = new double[array.length];
        for (int i = 0; i < op.length; i++)
            op[i] = (double) array[i];
        return op;
    }

    public static double[] castToDoubles(float[] array) {
        double[] op = new double[array.length];
        for (int i = 0; i < op.length; i++)
            op[i] = (double) array[i];
        return op;
    }

    public static double[] castToDoubles(int[] array) {
        double[] op = new double[array.length];
        for (int i = 0; i < op.length; i++)
            op[i] = (double) array[i];
        return op;
    }


}