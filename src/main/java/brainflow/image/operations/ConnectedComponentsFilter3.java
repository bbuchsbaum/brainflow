package brainflow.image.operations;

import brainflow.image.data.*;
import brainflow.image.space.IImageSpace;
import brainflow.image.space.Axis;
import brainflow.image.io.BrainIO;
import brainflow.image.io.IImageDataSource;
import brainflow.app.MemoryImageDataSource;
import brainflow.app.BrainFlowException;
//import brainflow.display.ThresholdRange;

import java.util.List;

import cern.colt.list.IntArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 27, 2007
 * Time: 1:27:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class ConnectedComponentsFilter3 extends AbstractImageFilter {


    public ConnectedComponentsFilter3() {
    }

    public IImageData getOutput() {
        List<IImageData> sources = getSources();

        IImageData3D data = (IImageData3D) sources.get(0);
        IImageSpace space = data.getImageSpace();

        int xdim = space.getDimension(Axis.X_AXIS);
        int ydim = space.getDimension(Axis.Y_AXIS);
        int zdim = space.getDimension(Axis.Z_AXIS);

        return connect(data, xdim, ydim, zdim);


    }

    public void addInput(IMaskedData3D data) {
        super.addInput(data);    //To change body of overridden methods use File | Settings | File Templates.
    }

    private int index(int i, int j, int k, int[] dim) {
        return ((k) * dim[0] * dim[1] + (j) * dim[0] + (i));
    }


    private IImageData3D connect(IImageData3D data, int xdim, int ydim, int zdim) {
        IntArrayList labels = doInitialLabelling(data);
        return null;
    }


    private IntArrayList doInitialLabelling(IImageData3D data) {

        int[] dim = new int[3];
        int[] nabo = new int[8];
        int label = 1;
        int nr_set = 0;

        int ttn = 100000;

        dim[0] = data.getDimension(Axis.X_AXIS);
        dim[1] = data.getDimension(Axis.Y_AXIS);
        dim[2] = data.getDimension(Axis.Z_AXIS);


        IntArrayList connTable = new IntArrayList(ttn);

        IntArrayList labels = new IntArrayList(new int[data.numElements()]);

        int lab;

        for (int z = 0; z < dim[2]; z++) {
            for (int y = 0; y < dim[1]; y++) {
                for (int x = 0; x < dim[0]; x++) {
                    nr_set = 0;

                    if (data.value(x, y, z) > 0) {
                       
                        nabo[0] = checkPreviousSlice(labels, x, y, z, dim, connTable);
                        if (nabo[0] > 0) nr_set += 1;

                        if (x > 0) {
                            lab = labels.getQuick(index(x - 1, y, z, dim));
                            if (lab > 0) nabo[nr_set++] = lab;
                        }
                        if (y > 0) {
                            lab = labels.getQuick(index(x, y - 1, z, dim));
                            if (lab > 0) nabo[nr_set++] = lab;
                        }

                        /*
                          For 18(edge)-connectivity
                          N.B. In current slice no difference to 26.
                        */

                        if ((y > 0) && (x > 0)) {
                            lab = labels.getQuick(index(x - 1, y - 1, z, dim));
                            if (lab > 0) nabo[nr_set++] = lab;
                        }
                        if ((y > 0) && (x < dim[0] - 1)) {
                            lab = labels.getQuick(index(x + 1, y - 1, z, dim));
                            if (lab > 0) nabo[nr_set++] = lab;
                        }

                        if (nr_set > 0) {
                            labels.setQuick(index(x, y, z, dim), nabo[0]);
                            fillConnTable(connTable, nabo, nr_set);
                        } else {
                            labels.setQuick(index(x, y, z, dim), label);
                            if (label >= connTable.size()) {
                                //System.out.println("conn table size : " + connTable.size());
                                connTable.ensureCapacity((int)(connTable.size()*1.5));
                            }

                            //System.out.println("label : " + label);
                            connTable.add(label);
                            //connTable.setQuick(label-1, label);
                            label++;
                        }
                    }
                }
            }
        }

        /*
           Finalise translation table
        */

        int j = 0;
        for (int i = 0; i < (label - 1); i++) {
            j = i;
            while (connTable.getQuick(j) != j + 1) {
                j = connTable.getQuick(j) - 1;
            }
            connTable.setQuick(i, j + 1);
        }


        return labels;
    }


    private int checkPreviousSlice(IntArrayList labels, int row, int col, int slice, int[] dim,
                                   IntArrayList connTable) {
        int lab = 0;
        int[] nabo = new int[9];
        int nr_set = 0;

        if (slice == 0) return 0;

        lab = labels.getQuick(index(row, col, slice - 1, dim));
        if (lab > 0) nabo[nr_set++] = lab;


        if (row > 0) {
            lab = labels.getQuick(index(row - 1, col, slice - 1, dim));
            if (lab > 0) nabo[nr_set++] = lab;
        }
        if (col > 0) {
            lab = labels.getQuick(index(row, col - 1, slice - 1, dim));
            if (lab > 0) nabo[nr_set++] = lab;
        }
        if (row < dim[0] - 1) {
            lab = labels.getQuick(index(row + 1, col, slice - 1, dim));
            if (lab > 0) nabo[nr_set++] = lab;
        }
        if (col < dim[1] - 1) {
            lab = labels.getQuick(index(row, col + 1, slice - 1, dim));
            if (lab > 0) nabo[nr_set++] = lab;
        }
        if ((row > 0) && (col > 0)) {
            lab = labels.getQuick(index(row - 1, col - 1, slice - 1, dim));
            if (lab > 0) nabo[nr_set++] = lab;
        }
        if ((row < dim[0] - 1) && (col > 0)) {
            lab = labels.getQuick(index(row + 1, col - 1, slice - 1, dim));
            if (lab > 0) nabo[nr_set++] = lab;
        }
        if ((row > 0) && (col < dim[1] - 1)) {
            lab = labels.getQuick(index(row - 1, col + 1, slice - 1, dim));
            if (lab > 0) nabo[nr_set++] = lab;
        }
        if ((row < dim[0] - 1) && (col < dim[1] - 1)) {
            lab = labels.getQuick(index(row + 1, col + 1, slice - 1, dim));
            if (lab > 0) nabo[nr_set++] = lab;

        }

        if (nr_set > 0) {
            fillConnTable(connTable, nabo,nr_set);
            return (nabo[0]);
        } else {
            return 0;
        }
    }


    private IntArrayList fillConnTable(IntArrayList connTable, int[] nabo, int nr_set) {

        int[] tn = new int[9];
        int ltn = Integer.MAX_VALUE;

        /*
            Find smallest terminal number in neighbourhood
        */

        int j;
        int cntr;
        for (int i = 0; i < nr_set; i++) {
            j = nabo[i];
            cntr = 0;
            while (connTable.getQuick(j - 1) != j) {
                j = connTable.getQuick(j - 1);
                cntr++;
                if (cntr > 100) throw new AssertionError("What up, Ashburner?");
            }

            tn[i] = j;
            ltn = Math.min(ltn, j);

        }

        /*
        Replace all terminal numbers in neighbourhood by the smallest one
        */
        for (int i = 0; i < nr_set; i++) {
            connTable.setQuick(tn[i] - 1, ltn);
        }

        return connTable;
    }

    private IntArrayList translateLabels(IntArrayList labels,
                                         int[] dim, IntArrayList connTable) {

        int ml = 0;
        int n = dim[0] * dim[1] * dim[2];

        int ttn = connTable.size();

        IntArrayList finalLabels = new IntArrayList(labels.size());

        for (int i = 0; i < ttn; i++) {
            ml = Math.max(ml, connTable.getQuick(i));
        }

        int[] fl = new int[ml];
        int cl = 0;

        for (int i = 0; i < n; i++) {
            if (labels.getQuick(i) > 0) {
                //if (!fl[tt[il[i]-1]-1])
                if (fl[connTable.getQuick(labels.getQuick(i) - 1) - 1] == 0) {
                    cl += 1;
                    fl[connTable.getQuick(labels.getQuick(i) - 1) - 1] = cl;
                }

                finalLabels.setQuick(i, fl[connTable.getQuick(labels.getQuick(i) - 1) - 1]);
            }

        }

        return finalLabels;
    }


    public static void main(String[] args) {
        try {
            IImageDataSource img = new MemoryImageDataSource(BrainIO.readNiftiImage("F:/data/anyback/tRepeat-stat.nii"));
            IImageData3D data = (IImageData3D) img.getData();

            MaskedData3D mask = new MaskedData3D(data, new MaskPredicate() {
                public boolean mask(double value) {
                    if (value > 1000) return true;
                    return false;
                }
            });
            System.out.println("cardinality " + mask.cardinality());
            BinaryImageData3D binData = new BinaryImageData3D(mask);
            System.out.println("cardinality " + binData.cardinality());

            ConnectedComponentsFilter3 filter = null;

            long btime = System.currentTimeMillis();
            for (int i = 0; i < 200; i++) {
                System.out.println("" + i);
                filter = new ConnectedComponentsFilter3();
                filter.addInput(mask);
                filter.getOutput();
            }

            long etime = System.currentTimeMillis();
            System.out.println("avg time " + (etime - btime) / 200.00);

            //IImageData idata = filter.getOutput();
            //BrainIO.writeAnalyzeImage("\"F:/data/anyback/tRepeat-stat-clustered-meth3", (DataBufferSupport) idata);


        } catch (BrainFlowException e) {
            e.printStackTrace();
        }

    }
}




