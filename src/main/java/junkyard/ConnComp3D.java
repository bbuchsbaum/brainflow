package junkyard;

import brainflow.image.data.*;
import brainflow.image.io.MemoryImageSource;
import brainflow.image.space.Axis;
import brainflow.image.io.BrainIO;
import brainflow.utils.Range;
import brainflow.utils.StopWatch;
import brainflow.core.*;
import brainflow.core.layer.ImageLayer3D;
import brainflow.core.layer.LayerProps;
import brainflow.core.BrainFlowException;
import brainflow.image.operations.UnionFindArray;
import brainflow.image.operations.ImageSlicer;
import brainflow.app.toplevel.ImageViewFactory;
import brainflow.colormap.ColorTable;
import brainflow.display.InterpolationType;
import cern.colt.list.IntArrayList;

import javax.swing.*;
import java.awt.*;
import java.awt.image.IndexColorModel;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Mar 16, 2009
 * Time: 6:29:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConnComp3D {


    private IMaskedData3D data;

    private ImageBuffer3D labels;

    private UnionFindArray unionArray;

    private final int[][] mask = new int[][]{
            {-1, -1, -1}, {0, -1, -1},{1, -1, -1},
            {-1, 0, -1},  {0, 0, -1}, {1, 0, -1},
            {-1, 1, -1},  {0, 1, -1}, {1, 1, -1},
            {-1, -1, 0},  {0, -1, 0}, {1, -1, 0}, {-1, 0, 0}};


    private IntArrayList conntable = new IntArrayList();



    private int label;

    public ConnComp3D(IMaskedData3D data) {
        this.data = data;
        labels = new BasicImageData3D.Int(data.getImageSpace()).createBuffer(false);
        unionArray = new UnionFindArray(50000);
    }


    private boolean check(int x, int y, int z, int width, int height, int length) {
        return inBounds(x, y, z, width, height, length) && (data.isTrue(x, y, z));
       
    }


    private boolean inBounds(int x, int y, int z, int width, int height, int length) {
        return ( (x >= 0 && x < width) && (y >= 0 && y < height) && (z >= 0 && z < length));
    }


    private void labelObjectFast(int x, int y, int z, int width, int height, int length, ImageBuffer3D labels, UnionFindArray ufa, boolean[] book) {

        int lab = Integer.MAX_VALUE;
        for (int index = 0; index < mask.length; index++) {

            int x1 = mask[index][0] + x;
            int y1 = mask[index][1] + y;
            int z1 = mask[index][2] + z;

            if (check(x1, y1, z1, width, height, length)) {
                int root = ufa.findRoot((int) labels.value(x1, y1, z1));
                book[index] = true;
                lab = Math.min(lab, root);
            } else {
                book[index] = false;
            }

        }

        if (lab == Integer.MAX_VALUE) {
            labels.setValue(x, y, z, label);
            ufa.set(label, label);
            label = label + 1;
        
        } else {
            labels.setValue(x, y, z, lab);

            for (int count = 0; count < mask.length; count++) {
                if (book[count]) {
                    ufa.setRoot((int) labels.value(mask[count][0] + x, mask[count][1] + y, mask[count][2] + z), lab);
                }

            }

        }
    }





    public void labelImage2() {


        int width = data.getDimension(Axis.X_AXIS);
        int height = data.getDimension(Axis.Y_AXIS);
        int length = data.getDimension(Axis.Z_AXIS);
        label = 1;

        boolean[] book = new boolean[mask.length];
        for (int z = 0; z < length; z++) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {

                    if (data.value(x,y,z) > 0) {
                        //labelObject2(j, i, width, height, labels, conntable, 1);
                        labelObjectFast(x, y, z, width, height, length, labels, unionArray, book);
                        // System.out.println("ufa : " + unionArray)
                    }

                }
            }
        }

        for (int z = 0; z < length; z++) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {

                    if (data.value(x,y,z) > 0) {
                        //labelObject2(j, i, width, height, labels, conntable, 1);
                        labelObjectFast(x, y, z, width, height, length, labels, unionArray, book);
                        // System.out.println("ufa : " + unionArray)
                    }

                }
            }
        }


        unionArray.flattenL();
        for (int z = 0; z < length; z++) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (data.value(x, y, z) > 0) {
                        labels.setValue(x, y, z, unionArray.findRoot((int) labels.value(x, y, z)));
                    }

                }
            }
        }


    }


    public static IMaskedData2D getSlice(IImageData3D dat, int i) {
        ImageSlicer slicer = ImageSlicer.createSlicer(dat.getImageSpace(), dat);
        IImageData2D dat2d = slicer.getSlice(dat.getAnatomy(), i);
        MaskedData2D mdat = new MaskedData2D(dat2d, new MaskPredicate() {
            public final boolean mask(double value) {
                return value > 2500;
            }
        });
        return mdat;


    }

    
    public static void main(String[] args) throws BrainFlowException {

        IImageData3D dat = (IImageData3D) BrainIO.readNiftiImage(BF.getDataURL("cohtrend_GLT#0_Tstat.nii"));
        MaskedData3D mdat = new MaskedData3D(dat, new MaskPredicate() {
            public final boolean mask(double value) {
                return value > 8;
            }
        });

        ConnComp3D comp = new ConnComp3D(mdat);

        StopWatch watch = new StopWatch();
        watch.start("conncomp");
        comp.labelImage2();

        watch.stopAndReport("conncomp");


        IImageData3D dat3d = comp.labels;
        System.out.println("dat3d max " + dat3d.maxValue());
        //IImageData3D dat3d = ImageData.asImageData3D(mdat, new BrainPoint1D(AnatomicalAxis.INFERIOR_SUPERIOR, 0),1);
        ImageViewModel model = new ImageViewModel("test", new ImageLayer3D(dat3d));

         IndexColorModel icm = ColorTable.createIndexColorModel(ColorTable.createColorGradient(Color.RED, Color.GREEN, (int)dat3d.maxValue()));
        LayerProps props = new LayerProps(icm, new Range(0, dat3d.maxValue()));
        props.interpolationType.set(InterpolationType.NEAREST_NEIGHBOR);

        model.add(new ImageLayer3D(new MemoryImageSource(dat3d), props));

        ImageView view = ImageViewFactory.createMontageView(model, 4, 4, 6);
        view.setScreenInterpolation(InterpolationType.NEAREST_NEIGHBOR);
        view.getSliceController().pageBack();
        JFrame frame = new JFrame();
        frame.add(view, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        //ConnComp comp2= new ConnComp(mdat);
        //comp2.labelImage();


        //System.out.println("");


        //System.out.println("" + comp.unionArray);
    }


}