package brainflow.image.operations;

import brainflow.image.data.IImageData3D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 29, 2009
 * Time: 1:24:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class KernelArray3D {


    private double[] kernelVector;

    private int xlen, ylen, zlen;

    private int endx, endy, endz;

    private int[] dim;

    private double kernelSum;

    private boolean normalized = false;



    public KernelArray3D(double[][][] weights) {

        zlen = weights.length;
        ylen = weights[0].length;
        xlen = weights[1].length;

        dim = new int[] { xlen, ylen, zlen };

        kernelVector = new double[xlen * ylen * zlen];
        int i = 0;
        for (int z = 0; z < zlen; z++) {
            for (int y = 0; y < ylen; y++) {
                for (int x = 0; x < xlen; x++) {
                    kernelVector[i] = weights[z][y][x];
                    kernelSum += kernelVector[i];
                    i++;
                }
            }
        }

        endx = (xlen-1)/2;
        endy = (ylen-1)/2;
        endz = (zlen-1)/2;
    }

    private int indexOf(int x, int y, int z) {
        return (z * (dim[0]*dim[1])) + dim[0] * y + x;

    }

    public KernelArray3D(double[] weights, int dimx, int dimy, int dimz) {
        if (weights.length != (dimx*dimy*dimz)) {
            throw new IllegalArgumentException("length of weights array incompatible with supplied dimensions");
        }

        xlen = dimx;
        ylen = dimy;
        zlen = dimz;

        endx = (xlen-1)/2;
        endy = (ylen-1)/2;
        endz = (zlen-1)/2;


        dim = new int[] { xlen, ylen, zlen };

        for (int i=0; i<weights.length; i++) {
            kernelSum += weights[i];
        }

        kernelVector = weights;


    }



    public void normalize() {
        if (!normalized) {
            for (int i = 0; i < kernelVector.length; i++) {
                kernelVector[i] /= kernelSum;
            }
            normalized = true;
        }

        kernelSum = 1;
    }

    public int[] dim() {
        return dim;
    }

    public int length() {
        return kernelVector.length;
    }

    public double sum() {
        return kernelSum;
    }

    public double convolve(int val) {
        double out = 0;
        for (int i=0; i<kernelVector.length; i++) {
            out += kernelVector[i]*val;
        }

        return out;
    }
    private boolean inBounds(int x, int y, int z, IImageData3D input) {
        if ( (x >=0) && (x < input.dim().getDim(0)) &&
             (y >=0) && (y < input.dim().getDim(1)) &&
             (z >=0) && (z < input.dim().getDim(2)) ) {
            return true;
        }

        return false;
    }


    public double apply(IImageData3D gridArray, int x0, int y0, int z0) {
        double result = 0;
        int count = 0;
        for (int z=-endz; z<=endz; z++) {
            for (int y=-endy; y<=endy; y++) {
                for (int x=-endx; x<=endx; x++) {
                    if (inBounds(x+x0,y+y0,z+z0,gridArray)) {
                        result += gridArray.value(x+x0,y+y0,z+z0) * kernelVector[count++];

                    }

                }
            }
        }

        return result;

    }

    public KernelArray3D multiply(KernelArray3D other) {
        if (other.length() != length()) {
            throw new IllegalArgumentException("kernel multiplication requirtes kernels of equal length");
        }

        double[] out = new double[kernelVector.length];
        for (int i=0; i<out.length; i++) {
            out[i] = other.kernelVector[i] * kernelVector[i];
        }

        return new KernelArray3D(out, xlen,ylen,zlen);

    }


}
