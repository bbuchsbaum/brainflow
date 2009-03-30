package brainflow.image.operations;

import brainflow.app.BrainFlowException;
import brainflow.image.io.IImageDataSource;
import brainflow.app.MemoryImageDataSource;
import brainflow.image.data.BasicImageData;
import brainflow.image.data.BasicImageData3D;
import brainflow.image.data.IImageData;
import brainflow.image.data.IImageData3D;
import brainflow.image.io.BrainIO;
import brainflow.image.space.Axis;
import brainflow.image.space.IImageSpace;
import brainflow.image.space.ImageSpace3D;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jan 18, 2007
 * Time: 10:52:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class ConnectedComponentsFilter2 extends AbstractImageFilter {


    private IntegerStack stack;

    private ISearchKernel3D searchKernel;

    public IImageData getOutput() {
        List<IImageData> sources = getSources();

        IImageData3D data = (IImageData3D) sources.get(0);
        IImageSpace space = data.getImageSpace();

        int xdim = space.getDimension(Axis.X_AXIS);
        int ydim = space.getDimension(Axis.Y_AXIS);
        int zdim = space.getDimension(Axis.Z_AXIS);

        return connect(data, xdim, ydim, zdim);

    }


    private int[] firstPass(int xdim, int ydim, int zdim, IImageData3D data) {
        int[] labels = new int[data.numElements()];


        int labelCounter = 1;

        for (int z = 0; z < (zdim - 1); z++) {
            for (int y = 0; y < (ydim - 1); y++) {
                for (int x = 0; x < xdim - 1; x++) {
                    if (data.value(x, y, z) > 0) {
                        int index = data.indexOf(x, y, z);
                        labels[index] = labelCounter;
                        //System.out.println("label counter " + labelCounter);
                        labelCounter++;
                    }
                }
            }
        }

        return labels;


    }


    private int searchStack(int[] labels, int i, IntegerStack stack, int[] loopvec, int loopnum) {
        int nswaps = 0;
        int curlab = labels[i];

        while (!stack.isEmpty()) {
            int idx = stack.pop();

            loopvec[idx] = loopnum;

            if (labels[idx] > curlab) {
                labels[idx] = curlab;
                searchKernel.pushKernel(idx, labels, stack);
                nswaps++;

            } else if (labels[idx] < curlab) {
                labels[i] = labels[idx];
                curlab = labels[idx];
                nswaps++;

            }


        }

        return nswaps;

    }


    private IImageData3D connect(IImageData3D data, int xdim, int ydim, int zdim) {
        ImageSpace3D space = (ImageSpace3D) data.getImageSpace();
        searchKernel = new LargeSearchKernel3D(space, 1, 1, 1);

        int[] labels = firstPass(xdim, ydim, zdim, data);

        //todo dangerous, need to fix
        stack = new IntegerStack(5000);


        int[] loopvec = new int[labels.length];

        int loopnum = 1;

        int nswaps;

        do {

            nswaps = 0;

            for (int i = 0; i < labels.length; i++) {
                if (labels[i] == 0 || loopvec[i] == loopnum) {
                    continue;
                }

                loopvec[i] = loopnum;
                searchKernel.pushKernel(i, labels, stack);
                nswaps += searchStack(labels, i, stack, loopvec, loopnum);
            }


            loopnum++;
            System.out.println("nswaps " + nswaps);

        } while (nswaps > 0);

        IImageData3D labelData = new BasicImageData3D(space, labels);

        return labelData;
    }


    public static void main(String[] args) {
        try {
            IImageDataSource img = new MemoryImageDataSource(BrainIO.readAnalyzeImage("C:/DTI/slopes/bAge.Norm.hdr"));
            IImageData3D data = (IImageData3D) img.getData();
            ConnectedComponentsFilter2 filter = null;

            long btime = System.currentTimeMillis();
            for (int i = 0; i < 1; i++) {
                System.out.println("" + i);
                filter = new ConnectedComponentsFilter2();
                filter.addInput(data);
                filter.getOutput();
            }

            long etime = System.currentTimeMillis();
            System.out.println("avg time " + (etime - btime) / 200.00);

            IImageData idata = filter.getOutput();
            BrainIO.writeAnalyzeImage("c:/DTI/slopes/bAge.Norm_index_largekernel.hdr", (BasicImageData) idata);


        } catch (BrainFlowException e) {
            e.printStackTrace();
        }

    }
}
