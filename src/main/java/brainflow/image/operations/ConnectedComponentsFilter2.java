package brainflow.image.operations;

import brainflow.core.BrainFlowException;
import brainflow.image.io.MemoryImageDataSource;
import brainflow.image.data.*;
import brainflow.image.io.BrainIO;
import brainflow.image.space.Axis;
import brainflow.image.space.IImageSpace;
import brainflow.image.space.ImageSpace3D;
import brainflow.image.iterators.ImageIterator;
import brainflow.core.BF;
import brainflow.core.ImageViewModel;
import brainflow.core.ImageView;
import brainflow.core.layer.ImageLayer3D;
import brainflow.core.layer.ImageLayerProperties;
import brainflow.colormap.ColorTable;
import brainflow.utils.Range;
import brainflow.display.InterpolationType;

import javax.swing.*;
import java.util.List;
import java.awt.image.IndexColorModel;
import java.awt.*;

import cern.colt.map.OpenIntIntHashMap;

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

    private IImageData3D relabel(IImageData3D data) {
        ImageBuffer3D writer = data.createWriter(true);
        OpenIntIntHashMap map = new OpenIntIntHashMap();
        ImageIterator iter = data.iterator();
        int lab= 1;
        while (iter.hasNext()) {
            int idx = iter.index();
            int curlab = (int)iter.next();
            if (curlab > 0) {

                if (map.containsKey(curlab)) {
                    writer.setValue(idx, map.get(curlab));
                } else {
                    map.put(curlab, lab);
                    writer.setValue(idx, lab);
                    lab++;
                }
            }
        }

        return writer.asImageData();
    }


    private IImageData3D connect(IImageData3D data, int xdim, int ydim, int zdim) {
        ImageSpace3D space = (ImageSpace3D) data.getImageSpace();
        searchKernel = new LargeSearchKernel3D(space);

        int[] labels = firstPass(xdim, ydim, zdim, data);

        //todo dangerous, need to fix
        stack = new IntegerStack(15000);


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

        return relabel(labelData);
    }


    public static void main(String[] args) {
        try {
            IImageData3D dat = (IImageData3D) BrainIO.readNiftiImage(BF.getDataURL("cohtrend_GLT#0_Tstat.nii"));
            
            MaskedData3D mask = new MaskedData3D(dat, new MaskPredicate() {
                public final boolean mask(double value) {
                    return value > 9;
                }
            });

            ConnectedComponentsFilter2 filter = new ConnectedComponentsFilter2();
            filter.addInput(mask);
            IImageData3D dat3d = (IImageData3D) filter.getOutput();
            System.out.println("image label " + dat3d.getImageLabel());

            IndexColorModel icm = ColorTable.createIndexColorModel(ColorTable.createColorGradient(Color.RED, Color.GREEN, (int) dat3d.maxValue()));
            ImageLayerProperties props = new ImageLayerProperties(icm, new Range(0, dat3d.maxValue()));
            props.interpolationType.set(InterpolationType.NEAREST_NEIGHBOR);
            //todo but this doesn't really enforce nearest neighbor due to the MappedDataAccessor method
            IImageData3D background = BrainIO.loadVolume(BF.getDataURL("anat_alepi.nii"));
            ImageLayer3D backLayer = new ImageLayer3D(background);
            ImageLayer3D layer = new ImageLayer3D(new MemoryImageDataSource(dat3d), props);

            ImageViewModel model = new ImageViewModel("test", backLayer, layer);

            System.out.println("max " + dat3d.maxValue());


            model.add(layer);

            //final ImageView view = ImageViewFactory.createAxialView(model);
            //view.setScreenInterpolation(InterpolationType.NEAREST_NEIGHBOR);
            //view.getSliceController().pageBack();
            /*try {
                SwingUtilities.invokeLater( new Runnable() {
                    @Override
                    public void run() {
                        try {
                            BrainFlow.get().launch();
                            BrainFlow.get().displayView(view);
                        } catch(Throwable t) {
                            t.printStackTrace();
                        }
                    }
                });


            } catch(Throwable e) {
                e.printStackTrace();
            }   */


        } catch (BrainFlowException e) {
            e.printStackTrace();
        }

    }
}
