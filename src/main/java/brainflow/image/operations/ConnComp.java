package brainflow.image.operations;

import brainflow.image.data.*;
import brainflow.image.space.Axis;
import brainflow.image.io.BrainIO;
import brainflow.image.anatomy.AnatomicalPoint3D;
import brainflow.image.anatomy.AnatomicalPoint1D;
import brainflow.image.anatomy.AnatomicalAxis;
import brainflow.utils.DataType;
import brainflow.utils.Range;
import brainflow.utils.StopWatch;
import brainflow.core.*;
import brainflow.core.layer.ImageLayer3D;
import brainflow.core.layer.ImageLayerProperties;
import brainflow.app.BrainFlowException;
import brainflow.app.MemoryImageDataSource;
import brainflow.app.toplevel.ImageViewFactory;
import brainflow.app.toplevel.ImageLayerFactory;
import brainflow.colormap.ColorTable;
import cern.colt.list.IntArrayList;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Mar 16, 2009
 * Time: 6:29:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConnComp {


    private IMaskedData2D data;

    private ImageBuffer2D labels;

    private UnionFindArray unionArray;

    private final int[][] mask = new int[][]{{-1, -1}, {0, -1}, {1, -1}, {-1, 0}};

    private IntArrayList conntable = new IntArrayList();


    private int label;

    public ConnComp(IMaskedData2D data) {
        this.data = data;
        labels = new BasicImageData2D(data.getImageSpace(), DataType.INTEGER).createWriter(false);
        unionArray = new UnionFindArray(50000);
    }


    private void copy_one(int x, int y, int bx, int by, ImageBuffer2D labels, IntArrayList conntable) {
        System.out.println("copy_one");
        labels.setValue(x, y, conntable.get((int) labels.value(bx, by)));
    }

    private void copy_two(int x, int y, int bx, int by, int cx, int cy, ImageBuffer2D labels, IntArrayList conntable) {
        System.out.println("copy_two");
        int lab = Math.min(conntable.getQuick((int) labels.value(bx, by)), conntable.getQuick((int) labels.value(cx, cy)));
        labels.setValue(x, y, lab);
        System.out.println("setting conn " + (int) labels.value(bx, by) + " to " + lab);
        System.out.println("setting conn " + (int) labels.value(cx, cy) + " to " + lab);
        conntable.setQuick((int) labels.value(bx, by), lab);
        conntable.setQuick((int) labels.value(cx, cy), lab);
        //labels.setValue(bx,by, lab);
        //labels.setValue(cx,cy, lab);

    }

    private boolean check(int x, int y, int width, int height) {
        if (inBounds(x, y, width, height) && (data.isTrue(x, y))) {
            return true;
        }

        return false;
    }

    private void label1(int x, int y, int width, int height, ImageBuffer2D labels, IntArrayList conntable) {
        if (!label2(x, y, width, height, labels, conntable)) {
            labels.setValue(x, y, label);
            conntable.add(label);
            System.out.println("new label " + label);
            label++;
        }

    }


    private boolean label2(int x, int y, int width, int height, ImageBuffer2D labels, IntArrayList conntable) {
        int bx = x;
        int by = y - 1;

        if (check(bx, by, width, height)) {
            System.out.println("b checked");
            copy_one(x, y, bx, by, labels, conntable);
        }

        int cx = x + 1;
        int cy = y - 1;
        int ax = x - 1;
        int ay = y - 1;
        int dx = x - 1;
        int dy = y;
        if (check(cx, cy, width, height)) {
            System.out.println("c checked");
            if (check(ax, ay, width, height)) {
                System.out.println("a checked");
                copy_two(x, y, cx, cy, ax, ay, labels, conntable);
                return true;
            } else if (check(dx, dy, width, height)) {
                System.out.println("d checked");
                copy_two(x, y, cx, cy, dx, dy, labels, conntable);
                return true;
            } else {
                copy_one(x, y, cx, cy, labels, conntable);
                return true;
            }
        }

        if (check(ax, ay, width, height)) {
            System.out.println("a checked");
            copy_one(x, y, ax, ay, labels, conntable);
            return true;
        }
        if (check(dx, dy, width, height)) {
            System.out.println("d checked");
            copy_one(x, y, dx, dy, labels, conntable);
            return true;
        }

        return false;

    }

    private boolean inBounds(int x, int y, int width, int height) {
        return (x >= 0 && x < width && y >= 0 && y < height);
    }


    private void labelObject2(int i, int j, int width, int height, ImageBuffer2D labels, IntArrayList conntable, int pass) {
        int lab = Integer.MAX_VALUE;
        System.out.println("object pixel j = " + j + " i = " + i);

        for (int k = 0; k < mask.length; k++) {
            int x1 = mask[k][0] + j;
            int y1 = mask[k][1] + i;

            //System.out.println("subject pixel x = " + x1 + " y = " + y1);

            if ((x1 < 0) || (x1 >= width) || (y1 < 0) || (y1 >= height)) {
                continue;
            }

            if (data.value(x1, y1) > 0) {
                System.out.println("mask pixel x1 = " + x1 + " y1 = " + y1);
                System.out.println("label of " + labels.value(x1, y1));
                lab = Math.min(lab, conntable.get((int) labels.value(x1, y1)));
            }
        }


        if ((lab == Integer.MAX_VALUE) && (pass == 1)) {
            System.out.println("new label " + label);
            labels.setValue(j, i, label);
            conntable.add(label);
            label = label + 1;

        } else if (lab != Integer.MAX_VALUE) {
            System.out.println("setting " + j + " " + i + " to " + lab);
            conntable.setQuick((int) labels.value(j, i), lab);
            labels.setValue(j, i, lab);
            for (int k = 0; k < mask.length; k++) {
                int x1 = mask[k][0] + j;
                int y1 = mask[k][1] + i;

                if (x1 < 0 || x1 >= width || y1 < 0 || y1 >= height) {
                    continue;
                }

                if (data.value(x1, y1) > 0) {
                    System.out.println("setting " + x1 + " " + y1 + " to " + lab);
                    conntable.setQuick((int) labels.value(x1, y1), lab);
                    labels.setValue(x1, y1, lab);
                }
            }


        }


    }

    private void labelObjectFast(int j, int i, int width, int height, ImageBuffer2D labels, UnionFindArray ufa, boolean[] book) {

        int lab = Integer.MAX_VALUE;

        for (int k = 0; k < mask.length; k++) {

            int x1 = mask[k][0] + j;
            int y1 = mask[k][1] + i;

            if ((x1 >= 0) && (x1 < width) && (y1 >= 0) && (y1 < height)) {
                if (data.value(x1, y1) > 0) {
                    int root = ufa.findRoot((int) labels.value(x1, y1));
                    book[k] = true;
                    lab = Math.min(lab, root);
                }
            } else {
                book[k] = false;
            }

        }

        if (lab == Integer.MAX_VALUE) {
            labels.setValue(j, i, label);
            ufa.set(label, label);

            label = label + 1;

        } else {
            labels.setValue(j, i, lab);

            for (int k = 0; k < mask.length; k++) {
                if (book[k]) {
                    ufa.setRoot((int) labels.value(mask[k][0] + j, mask[k][1] + i), lab);
                }

            }

        }
    }


    private void labelObject(int j, int i, int width, int height, ImageBuffer2D labels, UnionFindArray ufa) {

        int lab = Integer.MAX_VALUE;

        for (int k = 0; k < mask.length; k++) {
            int x1 = mask[k][0] + j;
            int y1 = mask[k][1] + i;

            if ((x1 >= 0) && (x1 < width) && (y1 >= 0) && (y1 < height)) {
                if (data.value(x1, y1) > 0) {
                    int root = ufa.findRoot((int) labels.value(x1, y1));


                    lab = Math.min(lab, root);
                }
            }

        }

        if (lab == Integer.MAX_VALUE) {
            labels.setValue(j, i, label);
            ufa.set(label, label);

            label = label + 1;

        } else {
            labels.setValue(j, i, lab);

            for (int k = 0; k < mask.length; k++) {
                int x1 = mask[k][0] + j;
                int y1 = mask[k][1] + i;

                if (x1 < 0 || x1 >= width || y1 < 0 || y1 >= height) {
                    continue;
                }

                if (data.value(x1, y1) > 0) {
                    ufa.setRoot((int) labels.value(x1, y1), lab);
                }
            }

        }


    }

    public void labelImage3() {


        int width = data.getDimension(Axis.X_AXIS);
        int height = data.getDimension(Axis.Y_AXIS);
        label = 1;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                if (data.value(j, i) > 0) {
                    //labelObject2(j, i, width, height, labels, conntable, 1);
                    labelObject(j, i, width, height, labels, unionArray);
                    // System.out.println("ufa : " + unionArray)
                }

            }
        }


        unionArray.flattenL();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                if (data.value(j, i) > 0) {
                    labels.setValue(j, i, unionArray.findRoot((int) labels.value(j, i)));
                }

            }
        }


    }


    public void labelImage2() {


        int width = data.getDimension(Axis.X_AXIS);
        int height = data.getDimension(Axis.Y_AXIS);
        label = 1;

        boolean[] book = new boolean[mask.length];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                if (data.value(j, i) > 0) {
                    //labelObject2(j, i, width, height, labels, conntable, 1);
                    labelObjectFast(j, i, width, height, labels, unionArray, book);
                    // System.out.println("ufa : " + unionArray)
                }

            }
        }


        unionArray.flattenL();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                if (data.value(j, i) > 0) {
                    labels.setValue(j, i, unionArray.findRoot((int) labels.value(j, i)));
                }

            }
        }


    }


    public void labelImage() {

        System.out.println("table start : " + unionArray);

        int width = data.getDimension(Axis.X_AXIS);
        int height = data.getDimension(Axis.Y_AXIS);
        conntable.add(0);
        label = 1;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                if (data.value(j, i) > 0) {
                    //labelObject2(i, j, width, height, labels, conntable, 1);
                    label1(j, i, width, height, labels, conntable);
                    // System.out.println("ufa : " + unionArray)
                }

            }
        }

        System.out.println("conntable 1 : " + conntable);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                if (data.value(j, i) > 0) {
                    //labelObject2(i, j, width, height, labels, conntable, 2);
                    label2(j, i, width, height, labels, conntable);
                    // System.out.println("ufa : " + unionArray)
                }

            }
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                if (data.value(j, i) > 0) {
                    //labelObject2(i, j, width, height, labels, conntable, 2);
                    label2(j, i, width, height, labels, conntable);
                    // System.out.println("ufa : " + unionArray)
                }

            }
        }


        System.out.println("conntable 2 : " + conntable);

    }

    public static IMaskedData2D getSlice(IImageData3D dat, int i) {
        ImageSlicer slicer = ImageSlicer.createSlicer(dat.getImageSpace(), dat);
        IImageData2D dat2d = slicer.getSlice(dat.getAnatomy(), i);
        MaskedData2D mdat = new MaskedData2D(dat2d, new MaskPredicate() {
            public final boolean mask(double value) {
                return value > 3500;
            }
        });
        return mdat;


    }

    public static ConnComp go(IImageData3D dat) {
        ConnComp comp = null;
        for (int i = 1; i < 120; i++) {
            IMaskedData2D dat2d = getSlice(dat, i);
            comp = new ConnComp(dat2d);
            comp.labelImage2();
        }

        return comp;


    }

    public static void main(String[] args) throws BrainFlowException {

        IImageData3D dat = (IImageData3D) BrainIO.readNiftiImage(BF.getDataURL("anat_alepi.nii"));
        ConnComp comp = null;
        StopWatch watch = new StopWatch();
        watch.start("conncomp");
        for (int i = 0; i < 100; i++) {
            System.out.println("i " + i);
            comp = go(dat);
        }

        watch.stopAndReport("conncomp");


        IImageData3D dat3d = ImageData.asImageData3D(comp.labels.asImageData(), new AnatomicalPoint1D(AnatomicalAxis.INFERIOR_SUPERIOR, 0), 1);
        //IImageData3D dat3d = ImageData.asImageData3D(mdat, new AnatomicalPoint1D(AnatomicalAxis.INFERIOR_SUPERIOR, 0),1);
        IImageDisplayModel model = new ImageDisplayModel("test");
        model.addLayer(new ImageLayer3D(new MemoryImageDataSource(dat3d), new ImageLayerProperties(ColorTable.SPECTRUM, new Range(0, dat3d.maxValue()))));

        ImageView view = ImageViewFactory.createAxialView(model);
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
