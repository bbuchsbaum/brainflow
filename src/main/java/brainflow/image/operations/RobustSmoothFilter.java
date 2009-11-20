package brainflow.image.operations;

import brainflow.image.data.IImageData3D;
import brainflow.image.data.ImageBuffer3D;
import brainflow.image.space.IImageSpace3D;
import brainflow.image.space.Axis;
import brainflow.image.io.BrainIO;
import brainflow.image.io.NiftiImageWriter;
import brainflow.image.io.NiftiImageInfo;
import brainflow.image.io.NiftiInfoReader;
import brainflow.core.BrainFlowException;
import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.FileSystemException;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 29, 2009
 * Time: 12:56:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class RobustSmoothFilter implements ImageFunctor<IImageData3D> {


    private KernelArray3D kernel;

    private float decay = .2f;

    @Override
    public IImageData3D process(IImageData3D input) {
        ImageBuffer3D outBuffer = input.createBuffer(true);
        initializeKernel(outBuffer.getImageSpace());

        int xlen = input.getDimension(Axis.X_AXIS);
        int ylen = input.getDimension(Axis.Y_AXIS);
        int zlen = input.getDimension(Axis.Z_AXIS);

        for (int z = 0; z < zlen; z++) {
            for (int y = 0; y < ylen; y++) {
                for (int x = 0; x < xlen; x++) {
                    outBuffer.setValue(x, y, z, applyKernel(x, y, z, input));

                }
            }
        }


        return outBuffer;
    }

    private void initializeKernel(IImageSpace3D input) {
        double xs = input.getSpacing(Axis.X_AXIS);
        double ys = input.getSpacing(Axis.Y_AXIS);
        double zs = input.getSpacing(Axis.Z_AXIS);


        double[][][] kernelWeights = new double[3][3][3];

        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int k = -1; k < 2; k++) {
                    double d = Math.sqrt((Math.pow(xs * Math.abs(i), 2) + Math.pow(ys * Math.abs(j), 2) + Math.pow(zs * Math.abs(k), 2)));
                    kernelWeights[i + 1][j + 1][k + 1] = (float) (1.0 / Math.pow(Math.E, decay * d));
                }
            }
        }


        kernel = new KernelArray3D(kernelWeights);
        kernel.normalize();


    }

    private boolean inBounds(int x, int y, int z, IImageData3D input) {
        if ((x >= 0) && (x < input.getDimension(Axis.X_AXIS)) &&
                (y >= 0) && (y < input.getDimension(Axis.Y_AXIS)) &&
                (z >= 0) && (z < input.getDimension(Axis.Z_AXIS))) {
            return true;
        }

        return false;

    }

    private KernelArray3D localVariance(int x0, int y0, int z0, IImageData3D input) {

        double[] variance = new double[kernel.length()];
        double val = input.value(x0, y0, z0);
        int count = 0;

        Math.pow(Math.E,  4*4);

        for (int k = -1; k < 2; k++) {
            for (int j = -1; j < 2; j++) {
                for (int i = -1; i < 2; i++) {
                    if (inBounds(x0 + i, y0 + j, z0 + k, input)) {
                        double dval = Math.abs(val - input.value(x0 + i, y0 + j, z0 + k));
                        variance[count] = (1.0 / Math.pow(Math.E, Math.sqrt(dval)));

                    }

                    count++;
                }
            }
        }


        return new KernelArray3D(variance, 3, 3, 3);

    }

    private double applyKernel(int x0, int y0, int z0, IImageData3D input) {
        //System.out.println("kernel sum " + kernel.sum());

        
        KernelArray3D varKern = localVariance(x0, y0, z0, input);
        //System.out.println("varkern sum " + varKern.sum());
        KernelArray3D localKern = varKern.multiply(kernel);
        //System.out.println("lokern sum " + localKern.sum());
        localKern.normalize();

        //System.out.println("norm lokern sum " + localKern.sum());
        return localKern.apply(input, x0, y0, z0);


    }


    public static void main(String[] args) {
        try {

            NiftiInfoReader reader = new NiftiInfoReader("c:/javacode/googlecode/brainflow/src/main/resources/data/cohtrend_GLT#0_Tstat.nii");
            NiftiImageInfo info = (NiftiImageInfo) reader.readInfo().get(0);

            IImageData3D data = (IImageData3D) BrainIO.readNiftiImage("c:/javacode/googlecode/brainflow/src/main/resources/data/cohtrend_GLT#0_Tstat.nii");
            RobustSmoothFilter filter = new RobustSmoothFilter();
            IImageData3D odat = filter.process(data);
            System.out.println("max val before " + data.maxValue());
            System.out.println("max val " + odat.maxValue());
            NiftiImageWriter writer = new NiftiImageWriter();
            writer.writeImage(info.copy(
                    VFS.getManager().resolveFile("c:/javacode/googlecode/brainflow/src/main/resources/data/filt7_cohtrend_GLT#0_Tstat.nii"),
                    VFS.getManager().resolveFile("c:/javacode/googlecode/brainflow/src/main/resources/data/filt7_cohtrend_GLT#0_Tstat.nii")), odat);


        } catch (BrainFlowException e) {
            e.printStackTrace();
        } catch (FileSystemException e2) {
            e2.printStackTrace();
        }
    }
}
