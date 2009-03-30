package brainflow.image.data;

import brainflow.image.iterators.ImageIterator;
import brainflow.image.space.IImageSpace;
import brainflow.image.space.ImageSpace2D;
import brainflow.image.space.ImageSpace3D;
import brainflow.utils.DataType;

import java.awt.image.*;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public abstract class BasicImageData extends AbstractImageData {


    protected DataBuffer data;

    protected Object storage;

   

    public BasicImageData(IImageSpace space) {
        super(space);

    }

    protected BasicImageData(IImageSpace space, DataType dtype) {
        super(space, dtype);
    }

    protected BasicImageData(IImageSpace space, DataType dtype, String imageLabel) {
        super(space, dtype, imageLabel);
    }

    public Object getStorage() {
        return storage;
    }



    private long computeHash() {
        ImageIterator iter = this.iterator();
        double sum = 0;
        double code = 0;

        while (iter.hasNext()) {
            double val = iter.next();
            sum += val;
            code += iter.index() * val;
        }

        return Double.doubleToLongBits(sum + code);

    }



    public static DataType establishDataType(Object array) {
        if (array instanceof byte[]) return DataType.BYTE;
        else if (array instanceof short[]) return DataType.SHORT;
        else if (array instanceof float[]) return DataType.FLOAT;
        else if (array instanceof int[]) return DataType.INTEGER;
        else if (array instanceof double[]) return DataType.DOUBLE;
        else {
            throw new IllegalArgumentException("DataBufferSupport: illegal array type: " + array);
        }

    }

    protected void fillBuffer(Object storage, int size) {
        if (datatype == DataType.BYTE) {
            data = new DataBufferByte((byte[]) storage, size);
        } else if (datatype == DataType.SHORT) {
            data = new DataBufferShort((short[]) storage, size);
        } else if (datatype == DataType.INTEGER) {
            data = new DataBufferInt((int[]) storage, size);
        } else if (datatype == DataType.FLOAT) {
            data = new DataBufferFloat((float[]) storage, size);
        } else if (datatype == DataType.DOUBLE) {
            data = new DataBufferDouble((double[]) storage, size);
        } else {
            throw new IllegalArgumentException("DataBufferSupport: cannot allocate data of type " + datatype.toString());
        }



    }

    public DataBuffer getDataBuffer() {
        return data;
    }

    protected DataBuffer copyBuffer() {
        if (datatype == DataType.BYTE) {
            return new DataBufferByte((byte[]) storage, ((byte[])storage).length);
        } else if (datatype == DataType.SHORT) {
            return new DataBufferShort((short[]) storage, ((short[])storage).length);
        } else if (datatype == DataType.INTEGER) {
            return new DataBufferInt((int[]) storage, ((int[])storage).length);
        } else if (datatype == DataType.FLOAT) {
            return new DataBufferFloat((float[]) storage, ((float[])storage).length);
        } else if (datatype == DataType.DOUBLE) {
            return new DataBufferDouble((double[]) storage, ((double[])storage).length);
        } else {
            throw new IllegalArgumentException("DataBufferSupport: cannot allocate data of type " + datatype.toString());
        }
        
    }
    protected DataBuffer allocateBuffer(int size) {

        if (datatype == DataType.BYTE) {
            if (storage == null)
                storage = new byte[size];
            data = new DataBufferByte((byte[]) storage, size);
        } else if (datatype == DataType.SHORT) {
            if (storage == null)
                storage = new short[size];
            data = new DataBufferShort((short[]) storage, size);
        } else if (datatype == DataType.INTEGER) {
            if (storage == null)
                storage = new int[size];
            data = new DataBufferInt((int[]) storage, size);
        } else if (datatype == DataType.FLOAT) {
            if (storage == null)
                storage = new float[size];
            data = new DataBufferFloat((float[]) storage, size);
        } else if (datatype == DataType.DOUBLE) {
            if (storage == null)
                storage = new double[size];
            data = new DataBufferDouble((double[]) storage, size);
        } else {
            throw new IllegalArgumentException("DataBufferSupport: cannot allocate data of type " + datatype.toString());
        }


        return data;
    }




    public abstract ImageIterator iterator();


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasicImageData that = (BasicImageData) o;

        if (!that.space.equals(o)) return false;
        //if (!(that.hashid() == hashid())) return false;
        if (!that.getImageLabel().equals(getImageLabel())) return false;

        return true;
    }



    public static AbstractImageData create(IImageSpace space, DataType type) {
        if (space.getNumDimensions() == 2) {
            return new BasicImageData2D((ImageSpace2D) space, type);
        }
        if (space.getNumDimensions() == 3) {
            return new BasicImageData3D((ImageSpace3D) space, type);
        } else
            throw new IllegalArgumentException("Cannot create DataBufferSupport with dimensionality " + space.getNumDimensions());
    }


}
