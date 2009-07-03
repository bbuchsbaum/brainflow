package brainflow.image.data;

import brainflow.image.interpolation.InterpolationFunction3D;
import brainflow.image.io.ImageInfo;
import brainflow.image.iterators.ImageIterator;
import brainflow.image.iterators.Iterator3D;
import brainflow.image.space.Axis;
import brainflow.image.space.IImageSpace3D;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.math.ArrayUtils;
import brainflow.math.Index3D;
import brainflow.utils.DataType;


import java.awt.image.DataBuffer;
import java.nio.ByteBuffer;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class BasicImageData3D extends AbstractImageData3D  {



    private DataBufferSupport dataSupport;


    public BasicImageData3D(BasicImageData3D src) {

        super(src.getImageSpace(), src.getDataType());
        //todo this does not actually create a deep copy -- should it?
        dataSupport = new DataBufferSupport(space, src.dataSupport.getStorage());

    }


    public BasicImageData3D(IImageSpace3D space, DataType type) {
        super(space, type);
        dataSupport = new DataBufferSupport(space, type);

    }

    public BasicImageData3D(IImageSpace3D space, DataType type, String imageLabel) {
        super(space, type, imageLabel);
        dataSupport = new DataBufferSupport(space, type);

    }

    public BasicImageData3D(IImageSpace3D space, Object array) {
        super(space,DataBufferSupport.establishDataType(array));
        dataSupport = new DataBufferSupport(space, array);


    }

    public BasicImageData3D(IImageSpace3D space, Object array, String imageLabel) {
        super(space,DataBufferSupport.establishDataType(array), imageLabel);
        dataSupport = new DataBufferSupport(space, array);
    }

    @Override
    public Anatomy3D getAnatomy() {
        return (Anatomy3D)space.getAnatomy();
    }

    public ImageInfo getImageInfo() {
        // todo is this  what we really want to do?
        return new ImageInfo(this);
    }


    public final Index3D indexToGrid(int idx) {
        return getImageSpace().indexToGrid(idx);
    }

    public final int indexOf(int x, int y, int z) {
        return (z * planeSize()) + dim0() * y + x;
    }

   

    public final double value(float x, float y, float z, InterpolationFunction3D interp) {
        return interp.interpolate(x, y, z, this);
    }




    public final double worldValue(float realx, float realy, float realz, InterpolationFunction3D interp) {
        double x = space3d.worldToGridX(realx, realy, realz);
        double y = space3d.worldToGridY(realx, realy, realz);
        double z = space3d.worldToGridZ(realx, realy, realz);


        return interp.interpolate(x, y, z, this);
    }

    public final double value(int index) {
        return dataSupport.data.getElemDouble(index);
    }


    public final double value(int x, int y, int z) {
        return dataSupport.data.getElemDouble(indexOf(x, y, z));
    }


    private void setValue(int idx, double val) {
        dataSupport.data.setElemDouble(idx, val);
    }

    private void setValue(int x, int y, int z, double val) {
        dataSupport.data.setElemDouble(indexOf(x, y, z), val);
    }


    public byte[] toBytes() {
        return ArrayUtils.scaleToBytes(dataSupport.getStorage(), minValue(), maxValue(), 255);       
    }

    public ImageBuffer3D createWriter(final boolean clear) {
        final IImageSpace3D space = BasicImageData3D.this.getImageSpace();
        final Object storage = BasicImageData3D.this.dataSupport.getStorage();
        final BasicImageData3D delegate;

        if (clear) {
            delegate = new BasicImageData3D(space, storage);
        } else {
            delegate = new BasicImageData3D(space, BasicImageData3D.this.getDataType());
        }

        return new ImageBuffer3D() {

            public void setValue(int x, int y, int z, double val) {
                delegate.setValue(x,y,z,val);
            }

            public IImageData3D asImageData() {
                return delegate;
            }

            public void setValue(int index, double value) {
                delegate.setValue(index,value);
            }

            public double value(int index) {
                return delegate.value(index);
            }

            public int numElements() {
                return delegate.numElements();
            }

            public double value(float x, float y, float z, InterpolationFunction3D interp) {
                return delegate.value(x,y,z,interp);
            }

            public double worldValue(float realx, float realy, float realz, InterpolationFunction3D interp) {
                return delegate.worldValue(realx,realy,realz,interp);
            }

            public double value(int x, int y, int z) {
                return delegate.value(x,y,z);
            }

            public ImageIterator iterator() {
                return delegate.iterator();
            }

            public IImageSpace3D getImageSpace() {
                return space;

            }
        };
    }

    public ImageIterator iterator() {
        return new Iterator3D(this);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasicImageData3D that = (BasicImageData3D) o;

        if (!space3d.equals(that.space3d)) return false;
        if (!getImageLabel().equals(that.getImageLabel())) return false;


        return true;
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + space3d.hashCode();
        //check super.hashcode to make sure this is necessary
        result = 31 * result + getImageLabel().hashCode();
        return result;
    }

    public String toString() {
        return this.getImageLabel();
    }






    public static void main(String[] args) {
        try {
            IImageData data = null;//TestUtils.quickDataSource("mean-BRB-EPI-001.nii").load();
            Index3D vox = new Index3D(0,0,0);

         

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
