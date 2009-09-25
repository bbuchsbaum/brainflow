/*
 * BSplineInterpolator.java
 *
 * Created on April 25, 2003, 9:46 AM
 */

package brainflow.image.interpolation;

import cern.colt.list.DoubleArrayList;
import brainflow.image.data.IImageData3D;
import org.boxwood.array.IDataGrid3D;
import brainflow.image.space.Axis;
import brainflow.image.space.IImageSpace;

/**
 * @author Bradley
 */
public class BSplineInterpolator implements InterpolationFunction3D {

    IImageData3D srcImage;

    IImageData3D coefficientImage;

    int degree = 1;
    double epsilon = 2.2204460492503131E-16;
    int DBGvaluect = 1;

    int width, height, depth;

    /**
     * Creates a new instance of BSplineInterpolator
     */
    public BSplineInterpolator(IImageData3D _srcImage, int _degree) {
        srcImage = _srcImage;
        degree = _degree;
        initialize();
    }

    public void setDegree(int _degree) {
        degree = _degree;
    }

    public void initialize() {

        //FormatImageFilter filter = new FormatImageFilter();
        //filter.setInput(0, srcImage);
        //filter.setOutputDataType(DataType.DOUBLE);
        //coefficientImage = (IImageData3D) filter.getOutput();
        IImageSpace space = coefficientImage.getImageSpace();
        width = space.getDimension(Axis.X_AXIS);
        height = space.getDimension(Axis.Y_AXIS);
        depth = space.getDimension(Axis.Z_AXIS);

        DoubleArrayList poles = new DoubleArrayList();

        /* recover the poles from a lookup table */
        switch (degree) {
            case 2:
                poles.add(Math.sqrt(8.0) - 3.0);
                break;
            case 3:
                poles.add(Math.sqrt(3.0) - 2.0);
                break;
            case 4:
                poles.add(Math.sqrt(664.0 - Math.sqrt(438976.0)) + Math.sqrt(304.0) - 19.0);
                poles.add(Math.sqrt(664.0 + Math.sqrt(438976.0)) - Math.sqrt(304.0) - 19.0);
                break;
            case 5:
                poles.add(Math.sqrt(135.0 / 2.0 - Math.sqrt(17745.0 / 4.0)) + Math.sqrt(105.0 / 4.0)
                        - 13.0 / 2.0);
                poles.add(Math.sqrt(135.0 / 2.0 + Math.sqrt(17745.0 / 4.0)) - Math.sqrt(105.0 / 4.0)
                        - 13.0 / 2.0);
                break;
            default:
                throw new RuntimeException("Invalid spline degree: " + degree);
        }

        double[] row1D = new double[width];
        for (int z = 0; z < depth; z++) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    row1D[x] = coefficientImage.value(x, y, z);
                }
                convertToInterpolationCoefficients(row1D, poles, epsilon);
                for (int x = 0; x < width; x++) {
                    //todo fixme
                    //coefficientImage.setValue(x, y, z, row1D[x]);
                }
            }
        }

        row1D = new double[height];

        for (int z = 0; z < depth; z++) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    row1D[y] = coefficientImage.value(x, y, z);
                }
                convertToInterpolationCoefficients(row1D, poles, epsilon);
                for (int y = 0; y < height; y++) {
                    //todo fixme
                    //coefficientImage.setValue(x, y, z, row1D[y]);
                }
            }
        }
        row1D = new double[depth];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < depth; z++) {
                    row1D[z] = coefficientImage.value(x, y, z);
                }

                convertToInterpolationCoefficients(row1D, poles, epsilon);
                for (int z = 0; z < depth; z++) {
                    //todo fixme
                    //coefficientImage.setValue(x, y, z, row1D[z]);
                }
            }
        }

    }

    protected void convertToInterpolationCoefficients(double[] c, DoubleArrayList z, double tolerance) {
        int dataLength = c.length;
        int nbPoles = z.size();
        double lambda = 1.0;

        /* compute the overall gain */
        for (int k = 0; k < nbPoles; k++) {
            lambda = lambda * (1.0 - z.get(k)) * (1.0 - 1.0 / z.get(k));
        }
        /* apply the gain */
        for (int n = 0; n < dataLength; n++) {
            c[n] *= lambda;
        }
        /* loop over all poles */
        for (int k = 0; k < nbPoles; k++) {
            double zk = z.get(k);
            /* causal initialization */
            c[0] = initialCausalCoefficient(c, z.get(k), tolerance);
            /* causal recursion */
            for (int n = 1; n < dataLength; n++) {
                c[n] += zk * c[n - 1];
            }
            /* anticausal initialization */
            c[dataLength - 1] = initialAntiCausalCoefficient(c, z.get(k));
            /* anticausal recursion */
            for (int n = dataLength - 2; 0 <= n; n--) {
                c[n] = zk * (c[n + 1] - c[n]);
            }
        }
    }

    public double initialCausalCoefficient(double[] c, double z, double tolerance) {
        int dataLength = c.length;
        double sum, zn, z2n, iz;
        int horizon;

        /* this initialization corresponds to mirror boundaries */
        horizon = dataLength;
        if (tolerance > 0.0) {
            horizon = (int) Math.ceil(Math.log(tolerance) / Math.log(Math.abs(z)));
        }
        if (horizon < dataLength) {
            zn = z;
            sum = c[0];
            for (int n = 1; n < horizon; n++) {
                sum += zn * c[n];
                zn *= z;
            }
            return sum;
        } else {
            /* full loop */
            zn = z;
            iz = 1.0 / z;
            z2n = Math.pow(z, dataLength - 1);
            sum = c[0] + z2n * c[dataLength - 1];
            z2n *= z2n * iz;
            for (int n = 1; n <= dataLength - 2; n++) {
                sum += (zn + z2n) * c[n];
                zn *= z;
                z2n *= iz;
            }
            return sum / (1.0 - zn * zn);
        }
    } /* end InitialCausalCoefficient */

    /*--------------------------------------------------------------------------*/
    public double initialAntiCausalCoefficient(double[] c, double z) {
        //int dataLength = c.length;
        /* this initialization corresponds to mirror boundaries */
        return (z / (z * z - 1.0)) * (z * c[c.length - 2] + c[c.length - 1]);
    } /* end InitialAntiCausalCoefficient */


    public double interpolate(double x, double y, double z, IDataGrid3D data) {
        DBGvaluect++;
        double[] xWeight = new double[6];
        double[] yWeight = new double[6];
        double[] zWeight = new double[6];
        double interpolated = 0.0;
        double w, w2, w4, t, t0, t1;
        int[] xIndex = new int[6];
        int[] yIndex = new int[6];
        int[] zIndex = new int[6];
        int width2 = 2 * width - 2;
        int height2 = 2 * height - 2;
        int depth2 = 2 * depth - 2;
        int i, j, q, k;
        if ((degree & 1) > 0) {
            i = (int) Math.floor(x) - degree / 2;
            j = (int) Math.floor(y) - degree / 2;
            q = (int) Math.floor(z) - degree / 2;
        } else {
            i = (int) Math.floor(x + 0.5) - degree / 2;
            j = (int) Math.floor(y + 0.5) - degree / 2;
            q = (int) Math.floor(z + 0.5) - degree / 2;
        }

        for (k = 0; k <= degree; k++) {
            xIndex[k] = i++;
            yIndex[k] = j++;
            zIndex[k] = q++;
        }
        /* compute the interpolation weights */
        switch (degree) {
            case 2:
                /* zero */
                w = x - xIndex[1];
                xWeight[1] = 3.0 / 4.0 - w * w;
                xWeight[2] = (1.0 / 2.0) * (w - xWeight[1] + 1.0);
                xWeight[0] = 1.0 - xWeight[1] - xWeight[2];
                /* zero */
                w = y - yIndex[1];
                yWeight[1] = 3.0 / 4.0 - w * w;
                yWeight[2] = (1.0 / 2.0) * (w - yWeight[1] + 1.0);
                yWeight[0] = 1.0 - yWeight[1] - yWeight[2];
                /* one */
                w = z - zIndex[1];
                zWeight[1] = 3.0 / 4.0 - w * w;
                zWeight[2] = (1.0 / 2.0) * (w - zWeight[1] + 1.0);
                zWeight[0] = 1.0 - zWeight[1] - zWeight[2];
                break;
            case 3:
                /* zero */
                w = x - xIndex[1];
                xWeight[3] = (1.0 / 6.0) * w * w * w;
                xWeight[0] = (1.0 / 6.0) + (1.0 / 2.0) * w * (w - 1.0) - xWeight[3];
                xWeight[2] = w + xWeight[0] - 2.0 * xWeight[3];
                xWeight[1] = 1.0 - xWeight[0] - xWeight[2] - xWeight[3];
                /* zero */
                w = y - yIndex[1];
                yWeight[3] = (1.0 / 6.0) * w * w * w;
                yWeight[0] = (1.0 / 6.0) + (1.0 / 2.0) * w * (w - 1.0) - yWeight[3];
                yWeight[2] = w + yWeight[0] - 2.0 * yWeight[3];
                yWeight[1] = 1.0 - yWeight[0] - yWeight[2] - yWeight[3];
                /* one */
                w = z - zIndex[1];
                zWeight[3] = (1.0 / 6.0) * w * w * w;
                zWeight[0] = (1.0 / 6.0) + (1.0 / 2.0) * w * (w - 1.0) - zWeight[3];
                zWeight[2] = w + zWeight[0] - 2.0 * zWeight[3];
                zWeight[1] = 1.0 - zWeight[0] - zWeight[2] - zWeight[3];
                break;
            case 4:
                /* zero */
                w = x - xIndex[2];
                w2 = w * w;
                t = (1.0 / 6.0) * w2;
                xWeight[0] = 1.0 / 2.0 - w;
                xWeight[0] *= xWeight[0];
                xWeight[0] *= (1.0 / 24.0) * xWeight[0];
                t0 = w * (t - 11.0 / 24.0);
                t1 = 19.0 / 96.0 + w2 * (1.0 / 4.0 - t);
                xWeight[1] = t1 + t0;
                xWeight[3] = t1 - t0;
                xWeight[4] = xWeight[0] + t0 + (1.0 / 2.0) * w;
                xWeight[2] = 1.0 - xWeight[0] - xWeight[1] - xWeight[3] - xWeight[4];
                /* zero */
                w = y - yIndex[2];
                w2 = w * w;
                t = (1.0 / 6.0) * w2;
                yWeight[0] = 1.0 / 2.0 - w;
                yWeight[0] *= yWeight[0];
                yWeight[0] *= (1.0 / 24.0) * yWeight[0];
                t0 = w * (t - 11.0 / 24.0);
                t1 = 19.0 / 96.0 + w2 * (1.0 / 4.0 - t);
                yWeight[1] = t1 + t0;
                yWeight[3] = t1 - t0;
                yWeight[4] = yWeight[0] + t0 + (1.0 / 2.0) * w;
                yWeight[2] = 1.0 - yWeight[0] - yWeight[1] - yWeight[3] - yWeight[4];
                /* one */
                w = z - zIndex[2];
                w2 = w * w;
                t = (1.0 / 6.0) * w2;
                zWeight[0] = 1.0 / 2.0 - w;
                zWeight[0] *= zWeight[0];
                zWeight[0] *= (1.0 / 24.0) * zWeight[0];
                t0 = w * (t - 11.0 / 24.0);
                t1 = 19.0 / 96.0 + w2 * (1.0 / 4.0 - t);
                zWeight[1] = t1 + t0;
                zWeight[3] = t1 - t0;
                zWeight[4] = zWeight[0] + t0 + (1.0 / 2.0) * w;
                zWeight[2] = 1.0 - zWeight[0] - zWeight[1] - zWeight[3] - zWeight[4];
                break;
            case 5:
                /* zero */
                w = x - xIndex[2];
                w2 = w * w;
                xWeight[5] = (1.0 / 120.0) * w * w2 * w2;
                w2 -= w;
                w4 = w2 * w2;
                w -= 1.0 / 2.0;
                t = w2 * (w2 - 3.0);
                xWeight[0] = (1.0 / 24.0) * (1.0 / 5.0 + w2 + w4) - xWeight[5];
                t0 = (1.0 / 24.0) * (w2 * (w2 - 5.0) + 46.0 / 5.0);
                t1 = (-1.0 / 12.0) * w * (t + 4.0);
                xWeight[2] = t0 + t1;
                xWeight[3] = t0 - t1;
                t0 = (1.0 / 16.0) * (9.0 / 5.0 - t);
                t1 = (1.0 / 24.0) * w * (w4 - w2 - 5.0);
                xWeight[1] = t0 + t1;
                xWeight[4] = t0 - t1;
                /* zero */
                w = y - yIndex[2];
                w2 = w * w;
                yWeight[5] = (1.0 / 120.0) * w * w2 * w2;
                w2 -= w;
                w4 = w2 * w2;
                w -= 1.0 / 2.0;
                t = w2 * (w2 - 3.0);
                yWeight[0] = (1.0 / 24.0) * (1.0 / 5.0 + w2 + w4) - yWeight[5];
                t0 = (1.0 / 24.0) * (w2 * (w2 - 5.0) + 46.0 / 5.0);
                t1 = (-1.0 / 12.0) * w * (t + 4.0);
                yWeight[2] = t0 + t1;
                yWeight[3] = t0 - t1;
                t0 = (1.0 / 16.0) * (9.0 / 5.0 - t);
                t1 = (1.0 / 24.0) * w * (w4 - w2 - 5.0);
                yWeight[1] = t0 + t1;
                yWeight[4] = t0 - t1;
                /* one */
                w = z - zIndex[2];
                w2 = w * w;
                zWeight[5] = (1.0 / 120.0) * w * w2 * w2;
                w2 -= w;
                w4 = w2 * w2;
                w -= 1.0 / 2.0;
                t = w2 * (w2 - 3.0);
                zWeight[0] = (1.0 / 24.0) * (1.0 / 5.0 + w2 + w4) - zWeight[5];
                t0 = (1.0 / 24.0) * (w2 * (w2 - 5.0) + 46.0 / 5.0);
                t1 = (-1.0 / 12.0) * w * (t + 4.0);
                zWeight[2] = t0 + t1;
                zWeight[3] = t0 - t1;
                t0 = (1.0 / 16.0) * (9.0 / 5.0 - t);
                t1 = (1.0 / 24.0) * w * (w4 - w2 - 5.0);
                zWeight[1] = t0 + t1;
                zWeight[4] = t0 - t1;
                break;
            default:
                throw new RuntimeException("Illegal Spline degree: " + degree);
        }


        if (width == 1) {
            for (k = 0; k <= degree; k++) {
                xIndex[k] = 0;
            }
        }
        if (height == 1) {
            for (k = 0; k <= degree; k++) {
                yIndex[k] = 0;
            }
        }
        if (depth == 1) {
            for (k = 0; k <= degree; k++) {
                zIndex[k] = 0;
            }
        }

        for (k = 0; k <= degree; k++) {
            if (xIndex[k] < 0) {
                xIndex[k] = 0;
                xWeight[k] = 0;
            }
            if (xIndex[k] >= width) {
                xIndex[k] = width - 1;
                xWeight[k] = 0;
            }
            if (yIndex[k] < 0) {
                yIndex[k] = 0;
                yWeight[k] = 0;
            }
            if (yIndex[k] >= height) {
                yIndex[k] = height - 1;
                yWeight[k] = 0;
            }
            if (zIndex[k] < 0) {
                zIndex[k] = 0;
                zWeight[k] = 0;
            }
            if (zIndex[k] >= depth) {
                zIndex[k] = depth - 1;
                zWeight[k] = 0;
            }
        }

        double sumxyz = 0;
        double sumxy = 0;

        for (q = 0; q <= degree; q++) {

            sumxy = 0;
            for (j = 0; j <= degree; j++) {
                double sumx = 0;


                for (i = 0; i <= degree; i++) {
                    sumx += xWeight[i] * coefficientImage.value(xIndex[i], yIndex[j], zIndex[q]);
                }

                sumxy += yWeight[j] * sumx;
            }
            sumxyz += zWeight[q] * sumxy;
        }

        return sumxyz;
    }
}
