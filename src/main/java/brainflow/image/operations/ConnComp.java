package brainflow.image.operations;

import brainflow.image.data.*;
import brainflow.image.space.Axis;
import brainflow.image.space.ImageSpace2D;
import brainflow.image.axis.ImageAxis;
import brainflow.image.anatomy.AnatomicalAxis;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.io.BrainIO;
import brainflow.utils.DataType;
import brainflow.core.*;
import brainflow.application.BrainFlowException;
import cern.colt.list.IntArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Mar 16, 2009
 * Time: 6:29:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConnComp {


    private IMaskedData2D data;

    private DataWriter2D labels;

    private UnionFindArray unionArray;

    private final int[][] mask = new int[][]{{-1, -1}, {0, -1}, {1, -1}, {-1, 0}};

    private IntArrayList conntable = new IntArrayList();


    private int label;

    public ConnComp(IMaskedData2D data) {
        this.data = data;
        labels = new BasicImageData2D(data.getImageSpace(), DataType.INTEGER).createWriter(false);
        unionArray = new UnionFindArray(100);
    }




    private void copy_one(int x, int y, int bx, int by, DataWriter2D labels, IntArrayList conntable) {
        System.out.println("copy_one");
        labels.setValue(x,y, conntable.get((int)labels.value(bx,by)));
    }

    private void copy_two(int x, int y, int bx, int by, int cx, int cy, DataWriter2D labels, IntArrayList conntable) {
        System.out.println("copy_two");
        int lab = Math.min(conntable.getQuick((int)labels.value(bx,by)),conntable.getQuick((int)labels.value(cx,cy)));
        labels.setValue(x,y, lab);
        System.out.println("setting conn " +(int)labels.value(bx,by) + " to " +lab );
        System.out.println("setting conn " +(int)labels.value(cx,cy) + " to " +lab );
        conntable.setQuick((int)labels.value(bx,by), lab);
        conntable.setQuick((int)labels.value(cx,cy), lab);
        //labels.setValue(bx,by, lab);
        //labels.setValue(cx,cy, lab);

    }

    private boolean check(int x, int y, int width, int height) {
        if (inBounds(x,y,width, height) && (data.isTrue(x,y))) {
            return true;
        }

        return false;
    }

    private void label1(int x, int y, int width, int height, DataWriter2D labels, IntArrayList conntable) {
        if (!label2(x,y,width,height, labels, conntable)) {
            labels.setValue(x,y,label);
            conntable.add(label);
            System.out.println("new label " + label);
            label++;
        }
        
    }


    private boolean label2(int x, int y, int width, int height, DataWriter2D labels, IntArrayList conntable) {
        int bx = x;
        int by = y-1;

        if (check(bx,by,width, height)) {
            System.out.println("b checked");
            copy_one(x,y,bx,by, labels,conntable);
        }

        int cx = x+1;
        int cy=y-1;
        int ax = x-1;
        int ay = y-1;
        int dx = x-1;
        int dy = y;
        if (check(cx,cy,width, height)) {
            System.out.println("c checked");
            if (check(ax,ay, width, height)) {
                System.out.println("a checked");
                copy_two(x,y, cx, cy, ax,ay, labels,conntable);
                return true;
            } else if (check(dx,dy, width, height)) {
                System.out.println("d checked");
                copy_two(x,y, cx, cy, dx,dy, labels,conntable);
                return true;
            } else {
                copy_one(x,y,cx,cy, labels,conntable);
                return true;
            }
        }

        if (check(ax, ay, width, height)) {
            System.out.println("a checked");
            copy_one(x,y,ax,ay, labels,conntable);
            return true;
        }
        if (check(dx,dy,width, height)) {
            System.out.println("d checked");
            copy_one(x,y,dx,dy, labels,conntable);
            return true;
        }

        return false;

    }

    private boolean inBounds(int x, int y, int width, int height) {
        return (x >= 0 && x < width && y >=0 && y < height);
    }


    private void labelObject2(int i, int j, int width, int height, DataWriter2D labels, IntArrayList conntable, int pass) {
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
                System.out.println("label of " + labels.value(x1, y1) );
               lab = Math.min(lab, conntable.get((int) labels.value(x1, y1)));
            }
        }


        if ((lab == Integer.MAX_VALUE) && (pass ==1)) {
            System.out.println("new label " + label);
            labels.setValue(j, i, label);
            conntable.add(label);
            label = label + 1;

        } else if (lab != Integer.MAX_VALUE){
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
                    System.out.println("setting " + x1 + " " +y1 + " to " + lab);
                   conntable.setQuick((int) labels.value(x1, y1), lab);
                   labels.setValue(x1, y1, lab);
                }
            }


        }


    }

    private void labelObject(int i, int j, int width, int height, DataWriter2D labels, UnionFindArray ufa) {

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
                //System.out.println("(once again) object pixel j = " + j + " i = " + i);
                //System.out.println("subject pixel survived x = " + x1 + " y = " + y1 + " label value " + labels.value(x1, y1));


                int root = ufa.findRoot((int) labels.value(x1, y1));
                //System.out.println("found root : " + root + " for " + labels.value(x1, y1));
                lab = Math.min(lab, root);
                //lab = Math.min(lab, (int) labels.value(x1, y1));
            }
        }

        if (lab == Integer.MAX_VALUE) {
            labels.setValue(j, i, label);
            ufa.add(label);

            label = label + 1;

        } else {
            //System.out.println("with neighbor : " + lab);
            //ufa.add(lab);
            assert lab != 0;
            labels.setValue(j, i, lab);

            //System.out.println("setting root to " + lab);
            for (int k = 0; k < mask.length; k++) {
                int x1 = mask[k][0] + j;
                int y1 = mask[k][1] + i;

                if (x1 < 0 || x1 >= width || y1 < 0 || y1 >= height) {
                    continue;
                }

                if (data.value(x1, y1) > 0) {

                    //labels.setValue(x1, y1, lab);
                    ufa.setRoot((int) labels.value(x1, y1), lab);
                }
            }


            //ufa.add(lab);

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

        

        System.out.println("conntable 2 : " + conntable);

    }

    public static void main(String[] args) throws BrainFlowException {

        IImageData3D dat = (IImageData3D)BrainIO.readNiftiImage(BF.getDataURL("anat_alepi.nii"));
        ImageSlicer slicer = ImageSlicer.createSlicer(dat.getImageSpace(), dat);
        IImageData2D dat2d = slicer.getSlice(dat.getAnatomy(), 56);


        int[][] mask = new int[][]{
                {1, 1, 1, 1, 0},
                {1, 0, 1, 0, 0},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 1, 1}};

        /*BasicImageData2D dat = new BasicImageData2D(new ImageSpace2D(
                new ImageAxis(0, 1, AnatomicalAxis.LEFT_RIGHT, mask[0].length),
                new ImageAxis(0, 1, AnatomicalAxis.POSTERIOR_ANTERIOR, mask.length)),
                DataType.INTEGER); */

        DataWriter2D dw = dat2d.createWriter(false);
        /*for (int i = 0; i < mask.length; i++) {
            for (int j = 0; j < mask[0].length; j++) {
                dw.setValue(j, i, mask[i][j]);
            }
        } */


        MaskedData2D mdat = new MaskedData2D(dat2d, new MaskPredicate() {
            public boolean mask(double value) {
                return value > 2500;
            }
        });



        //System.out.println("mdat[0][0] " + mdat.value(0, 0));
        System.out.println(mdat.cardinality());

        ConnComp comp = new ConnComp(mdat);
        comp.labelImage();

        





        //System.out.println("" + comp.unionArray);
    }


}
