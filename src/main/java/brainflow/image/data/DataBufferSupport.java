package brainflow.image.data;

import brainflow.image.space.IImageSpace;
import brainflow.utils.DataType;

import java.awt.image.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Mar 28, 2009
 * Time: 10:47:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataBufferSupport {

    public DataBuffer data;

    private Object storage;

    private IImageSpace space;

    private DataType datatype;

    public DataBufferSupport(IImageSpace space, Object storage) {
        this.space = space;
        this.storage = storage;       
        datatype = establishDataType(storage);
        fillBuffer(storage, space.getNumSamples());

    }

    protected DataBufferSupport(IImageSpace space, DataType datatype) {
        this.space = space;
        this.datatype = datatype;
        data = allocateBuffer(space.getNumSamples());
    }


    public Object getStorage() {
        return storage;
    }

    public DataBuffer getData() {
        return data;
    }

    public IImageSpace getSpace() {
        return space;
    }

    public DataType getDatatype() {
        return datatype;
    }

    public DataBuffer getDataBuffer() {
        return data;
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

    private void checkLength(int l1, int l2) {
        if (l1 != l2) {
            throw new IllegalArgumentException("length of data array not compatible with image size");
        }
    }

    public void fillBuffer(Object storage, int size) {
        if (datatype == DataType.BYTE) {
            checkLength(((byte[])storage).length, size);
            data = new DataBufferByte((byte[]) storage, size);
        } else if (datatype == DataType.SHORT) {
            checkLength(((short[])storage).length, size);
            data = new DataBufferShort((short[]) storage, size);
        } else if (datatype == DataType.INTEGER) {
            checkLength(((int[])storage).length, size);
            data = new DataBufferInt((int[]) storage, size);
        } else if (datatype == DataType.FLOAT) {
            checkLength(((float[])storage).length, size);
            data = new DataBufferFloat((float[]) storage, size);
        } else if (datatype == DataType.DOUBLE) {
            checkLength(((double[])storage).length, size);
            data = new DataBufferDouble((double[]) storage, size);
        } else {
            throw new IllegalArgumentException("DataBufferSupport: cannot allocate data of type " + datatype.toString());
        }



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

}
