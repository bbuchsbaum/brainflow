package brainflow.image.data;

import brainflow.image.interpolation.InterpolationFunction3D;
import brainflow.image.io.ImageInfo;
import brainflow.image.iterators.Iterator3D;
import brainflow.image.iterators.ValueIterator;
import brainflow.image.space.IImageSpace3D;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.math.ArrayUtils;
import brainflow.math.Index3D;
import brainflow.utils.DataType;
import brainflow.utils.Dimension3D;
import org.boxwood.array.IDataGrid3D;


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

    @Deprecated
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
        return dataSupport.getData().getElemDouble(index);
    }


    public final double value(int x, int y, int z) {
        return dataSupport.getData().getElemDouble(indexOf(x, y, z));
    }


    void setValue(int idx, double val) {
        dataSupport.getData().setElemDouble(idx, val);
    }

    void setValue(int x, int y, int z, double val) {
        dataSupport.getData().setElemDouble(indexOf(x, y, z), val);
    }

    @Override
    public IDataGrid3D subGrid(int x0, int x1, int y0, int y1, int z0, int z1) {
        if (x1 < x0) throw new IllegalArgumentException("x1 cannot be < x0 ");
        if (y1 < y0) throw new IllegalArgumentException("y1 cannot be < y0 ");
        if (z1 < z0) throw new IllegalArgumentException("z1 cannot be < z0 ");

        return new DataSubGrid3D(this, x0, x1-x0+1, y0, y1-y0+1, z0, z1-z0+1);
    }

    public byte[] toBytes() {
        return ArrayUtils.scaleToBytes(dataSupport.getStorage(), minValue(), maxValue(), 255);       
    }

    public ImageBuffer3D createWriter(final boolean clear) {
        final IImageSpace3D space = BasicImageData3D.this.getImageSpace();
        final BasicImageData3D delegate;

        if (clear) {
            delegate = new BasicImageData3D(space, BasicImageData3D.this.getDataType());           
        } else {
            delegate = new BasicImageData3D(space, dataSupport.copyArray());
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

            public int length() {
                return delegate.length();
            }

            @Override
            public int indexOf(int i, int j, int k) {
                return delegate.indexOf(i,j,k);
            }

            @Override
            public double value(float x, float y, float z, InterpolationFunction3D interp) {
                return delegate.value(x,y,z,interp);
            }

            @Override
            public double value(int x, int y, int z) {
                return delegate.value(x,y,z);
            }

            @Override
            public ValueIterator iterator() {
                return delegate.iterator();
            }

            @Override
            public IDataGrid3D subGrid(int x0, int x1, int y0, int y1, int z0, int z1) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Dimension3D<Integer> dim() {
                return delegate.dim();
            }

            @Override
            public IImageSpace3D getImageSpace() {
                return space;

            }
        };
    }

    public ValueIterator iterator() {
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
